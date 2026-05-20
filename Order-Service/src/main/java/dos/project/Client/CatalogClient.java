package dos.project.Client;

import dos.project.Model.CatalogBookInfo;
import dos.project.Model.UpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class CatalogClient {
    private final RestClient restClient;

    public CatalogClient(@Value("${catalog.base-url}") String catalogBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(catalogBaseUrl)
                .build();
    }

    public CatalogBookInfo getInfo(int id) {
        try {
            // No onStatus throwing — just let RestClient throw RestClientResponseException
            // for any 4xx/5xx, then check the status code in the catch block below.
            return restClient.get()
                    .uri("/api/catalog/info/{id}", id)
                    .retrieve()
                    .body(CatalogBookInfo.class);

        } catch (RestClientResponseException ex) {
            // Now we safely check the status code here, no lambda tricks needed
            if (ex.getStatusCode().value() == 404) {
                throw new BookNotFoundException(id);
            }
            throw new CatalogServiceException("Catalog error: " + ex.getStatusCode(), ex);

        } catch (RestClientException ex) {
            throw new CatalogServiceException("Catalog service unavailable", ex);
        }
    }

    public CatalogBookInfo decrementStock(int id, int currentQuantity) {
        UpdateRequest request = new UpdateRequest();
        request.setQuantity(currentQuantity - 1);
        try {
            return restClient.put()
                    .uri("/api/catalog/update/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(CatalogBookInfo.class);

        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new BookNotFoundException(id);
            }
            throw new CatalogServiceException(
                    "Catalog rejected stock update for book " + id + ": " + ex.getStatusCode(), ex);

        } catch (RestClientException ex) {
            throw new CatalogServiceException("Catalog service unavailable", ex);
        }
    }
}
