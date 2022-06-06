package bykuharev.taskforalfa.exchangeratesingif.service.ServiceImpl;

import bykuharev.taskforalfa.exchangeratesingif.client.OpenExchangeRatesClient;
import bykuharev.taskforalfa.exchangeratesingif.model.ExchangeRates;
import bykuharev.taskforalfa.exchangeratesingif.service.ExchangeRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private ExchangeRates prevRate;
    private ExchangeRates currentRate;
    private OpenExchangeRatesClient oerClient;
    @Qualifier("date_bean")
    private SimpleDateFormat dateFormat;
    @Qualifier("time_bean")
    private SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public ExchangeRatesServiceImpl(
            OpenExchangeRatesClient oerClient,
            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat
    ) {
        this.oerClient = oerClient;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    @Override
    public List<String> getCodes() {
        List<String> code = null;
        if(this.currentRate.getRates()!=null){
            code = new ArrayList<>(this.currentRate.getRates().keySet());
        }
        return code;
    }

    @Override
    public int getKeyFromTag(String code) {

        this.refreshDataRates();
        Double prevCoefficient = this.getCoefficient(this.prevRate, code);
        Double currentCoefficient = this.getCoefficient(this.currentRate, code);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }

    @Override
    public void refreshDataRates() {

        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }

    private void refreshCurrentRates(long time) {
        if (
                this.currentRate == null ||
                        !timeFormat.format(Long.valueOf(this.currentRate.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRate = oerClient.getLatestRates(this.appId);
        }
    }

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRate == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRate.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRate.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRate = oerClient.getHistoricalRates(newPrevDate, appId);
        }
    }

    private Double getCoefficient(ExchangeRates rates, String code) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map = null;
        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(code);
            appBaseRate = map.get(this.base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal(
                    (defaultBaseRate / appBaseRate) * targetRate
            )
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }
}
