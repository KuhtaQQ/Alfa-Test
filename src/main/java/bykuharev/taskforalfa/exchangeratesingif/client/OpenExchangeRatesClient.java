package bykuharev.taskforalfa.exchangeratesingif.client;

import bykuharev.taskforalfa.exchangeratesingif.model.ExchangeRates;

public interface OpenExchangeRatesClient {

    ExchangeRates getLatestRates(String appId);

    ExchangeRates getHistoricalRates(String date, String appId);
}
