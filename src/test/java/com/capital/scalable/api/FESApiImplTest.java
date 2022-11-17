package com.capital.scalable.api;

import com.capital.scalable.infra.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FESApiImplTest {

    private static final String EUR = "EUR";
    private static final String INR = "INR";
    private static final String AUD = "AUD";
    private static final String JPY = "JPY";
    private static final String KKK = "KKK";
    private static final Double EUR_RATE = 100.0;
    private static final Double INR_RATE = 200.0;
    private static final Double AUD_RATE = 300.0;
    private static final Double JPY_RATE = 400.0;

    private static final Double AMOUNT = 3.0;

    @Mock
    private RestClient client;
    @InjectMocks
    private FESApiImpl api;

    private Map<String, Double> currencyMap = new HashMap<>();

    @Before
    public void setup() {
        currencyMap.put(EUR, EUR_RATE);
        currencyMap.put(INR, INR_RATE);
        currencyMap.put(AUD, AUD_RATE);
        currencyMap.put(JPY, JPY_RATE);
        when(client.getSourceData()).thenReturn(currencyMap);
    }

    @Test
    public void testEuroRate_ExistingCurrency() {
        Double from = 1.0;
        Double to = 2.0;
        ResponseEntity<CurrencyPair> responseEntity = api.getECBRateAsComparedToEuro(INR);
        assertTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()));
        assertTrue(EUR.equals(responseEntity.getBody().getFromCurrency()));
        assertTrue(INR.equals(responseEntity.getBody().getToCurrency()));
        assertTrue(from.equals(responseEntity.getBody().getFromAmount()));
        assertTrue(to.equals(responseEntity.getBody().getToAmount()));
    }

    @Test
    public void testEuroRate_NONExistingCurrency() {
        ResponseEntity<CurrencyPair> responseEntity = api.getECBRateAsComparedToEuro(KKK);
        assertTrue(HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode()));
        assertTrue(responseEntity.getBody()==null);
    }

    @Test
    public void getExchangeRate() {
        Double from = 1.0;
        Double to = 1.5;
        ResponseEntity<CurrencyPair> responseEntity = api.getExchangeRate(INR, AUD);
        assertTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()));
        assertTrue(responseEntity.getBody() != null);
        assertTrue(INR.equals(responseEntity.getBody().getFromCurrency()));
        assertTrue(AUD.equals(responseEntity.getBody().getToCurrency()));
        assertTrue(from.equals(responseEntity.getBody().getFromAmount()));
        assertTrue(to.equals(responseEntity.getBody().getToAmount()));
    }

    @Test
    public void getListOfCurrencies() {
        ResponseEntity<List<CurrencyFrequency>> responseEntity = api.getListOfCurrencies();
        assertTrue(responseEntity.getBody() != null);
        assertTrue(responseEntity.getBody().size() == currencyMap.size());
    }

    @Test
    public void convertBetweenTwoCurrency() {
        Double expected = 2 * AMOUNT;
        ResponseEntity<CurrencyPair> responseEntity = api.convertBetweenTwoCurrency(INR, JPY, AMOUNT);
        assertTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()));
        assertTrue(responseEntity.getBody() != null);
        assertTrue(INR.equals(responseEntity.getBody().getFromCurrency()));
        assertTrue(JPY.equals(responseEntity.getBody().getToCurrency()));
        assertTrue(AMOUNT.equals(responseEntity.getBody().getFromAmount()));
        assertTrue(expected.equals(responseEntity.getBody().getToAmount()));

    }

    @Test
    public void convertBetweenTwoCurrencyWIthNonExistingCurrency() {
        ResponseEntity<CurrencyPair> responseEntity = api.convertBetweenTwoCurrency(KKK, JPY, AMOUNT);
        assertTrue(HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode()));
        assertTrue(responseEntity.getBody() == null);
    }

    @Test
    public void openBrowserWithWrongCurrency() {
        URI responseEntity = api.openCurrencyTrendInBrowser(KKK);
        assertTrue(responseEntity == null);
    }

}