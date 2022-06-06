package bykuharev.taskforalfa.exchangeratesingif;

import bykuharev.taskforalfa.exchangeratesingif.service.ExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitData {

    private ExchangeRatesService exchangeRatesService;

    @Autowired
    public InitData(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @PostConstruct
    public void firstDataInit() {
        exchangeRatesService.refreshDataRates();
    }
}
