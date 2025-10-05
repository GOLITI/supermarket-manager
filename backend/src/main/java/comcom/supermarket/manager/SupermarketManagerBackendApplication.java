package comcom.supermarket.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SupermarketManagerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermarketManagerBackendApplication.class, args);
    }

}
