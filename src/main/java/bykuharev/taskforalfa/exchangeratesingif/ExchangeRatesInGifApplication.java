package bykuharev.taskforalfa.exchangeratesingif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExchangeRatesInGifApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatesInGifApplication.class, args);
    }

}
