package com.capital.scalable.api;

import com.capital.scalable.domain.dto.EventsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FESApiImpl implements FESApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(FESApiImpl.class);

    @Override
    public ResponseEntity<EventsDto> availableEvents(String starts_at, String ends_at) {
        return null;
    }

//    public ResponseEntity<CurrencyPair> getExchangeRate(String from, String to) {
//        client.callExternalEventsEndpoint();
//        Map<String, Double> currencyRates = client.getSourceData();
//        if(currencyRates.containsKey(from.toUpperCase()) && currencyRates.containsKey(to.toUpperCase())) {
//            Double c1 = currencyRates.get(from.toUpperCase());
//            Double c2 = currencyRates.get(to.toUpperCase());
//            double amountConverted = Math.round((c2/c1) * 100.0) / 100.0;
//            CurrencyPair pair = new CurrencyPair(from.toUpperCase(), to.toUpperCase(), 1.00, amountConverted);
//            LOGGER.info(new LogMessage()
//                    .with("uri", "/exchange-rate/{from}/{to}")
//                    .with("from", from.toUpperCase())
//                    .with("to", to.toUpperCase())
//                    .with("amount", amountConverted).toString());
//            updateFrequencyMap(Arrays.asList(from, to));
//            return new ResponseEntity<>(pair, HttpStatus.OK);
//        }
//        LOGGER.error(new LogMessage()
//                .with("uri", "/exchange-rate/{from}/{to}")
//                .with("from", from.toUpperCase())
//                .with("to", to.toUpperCase())
//                .with("error", "Currency Missing").toString());
//        return ResponseEntity.notFound().build();
//    }
}
