package dos.project.Client;

import dos.project.Model.CatalogBookInfo;
import dos.project.Model.UpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CatalogClient {
    private final RestClient restClient;

    //Build CLient with base URL
    public CatalogClient(@Value("${catalog.base-url}") String catalogBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(catalogBaseUrl)
                .build();
    }

    public CatalogBookInfo getInfo(int id) {
        try {
            return restClient.get()
                    .uri("/api/catalog/info/{id}", id)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        throw new BookNotFoundException(id);
                    })
                    .body(CatalogBookInfo.class);
        } catch (BookNotFoundException ex) {
            throw ex;
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
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new CatalogServiceException("Catalog rejected stock update for book " + id);
                    })
                    .body(CatalogBookInfo.class);
        } catch (RestClientException ex) {
            throw new CatalogServiceException("Catalog service unavailable", ex);
        }
    }
}
