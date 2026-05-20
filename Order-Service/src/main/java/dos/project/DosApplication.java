package dos.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DosApplication {
    public static void main(String[] args) {
        System.setProperty("server.port", "8082");
        SpringApplication.run(DosApplication.class, forcePort8082(args));
    }

    /** IDE run configs sometimes pass --server.port=8087; project standard is 8082. */
    private static String[] forcePort8082(String[] args) {
        List<String> cleaned = new ArrayList<>();
        for (String arg : args) {
            if (!arg.startsWith("--server.port=") && !arg.equals("-Dserver.port=8087")) {
                cleaned.add(arg);
            }
        }
        cleaned.add("--server.port=8082");
        return cleaned.toArray(String[]::new);
    }
}
