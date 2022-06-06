package bykuharev.taskforalfa.exchangeratesingif.service;

import java.util.List;

public interface ExchangeRatesService {

    List<String> getCodes();

    int getKeyFromTag(String code);

    void refreshDataRates();
}
