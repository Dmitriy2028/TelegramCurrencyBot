package bankUtilsBuilder;

import enums.BankNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankUtilBuilder {

    private static Bank BANK =  new Bank();
    private static final BankDirector DIRECTOR = new BankDirector();
    private static final BankBuilder BUILDER = new BankBuilder();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static void main(String[] args) {
        BankUtilBuilder bankUtilBuilder = new BankUtilBuilder();
        bankUtilBuilder.getCource(BankNames.NBU).entrySet().forEach(System.out::println);
        bankUtilBuilder.getCource(BankNames.PRIVAT).entrySet().forEach(System.out::println);
        bankUtilBuilder.getCource(BankNames.MONO).entrySet().forEach(System.out::println);

//        List<CurrenciesAndCodeEnum> listCurrencyEnum = bankNBU.getListCurrencyEnum();
//        listCurrencyEnum.stream()
//                        .forEach(currency -> {
//                            System.out.println(currency.name() + " : " + currency.getCurrencyCode());
//                        });

//        System.out.println("bank = " + bankNBU);

//        bankUtilBuilder.getCource(BankNames.NBU, new CurrenciesAndCodeEnum[]{CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR} );
//        System.out.println("CurrenciesAndCode.EUR = " + CurrenciesAndCodeEnum.EUR + " cc: " + CurrenciesAndCodeEnum.EUR.getCurrencyCode() );
    }

    public Map<String, Double> getCource(BankNames bankName) {
        BANK = buildBank(bankName);
        return getCurrency();
    }

    private Map<String, Double> getCurrency() {
        final Map<String, Double> mapCurrency = new LinkedHashMap<>();
        final HttpResponse<String> response = sendGET(URI.create(BANK.getUrl()));
        JSONArray jsonArray = new JSONArray(response.body());
        return selectCurrency(mapCurrency, jsonArray);
    }

    private Map<String, Double> selectCurrency(Map<String, Double> map, JSONArray jsonArr) {
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            if ((BANK.getCurrency_USD()).equals(String.valueOf(jsonObject.get(BANK.jsonCurrencyCode())))) {
                addCurrencyToMap(map, jsonObject, CurrenciesAndCodeEnum.USD.name());
            } else if ((BANK.getCurrency_EUR()).equals(String.valueOf(jsonObject.get(BANK.jsonCurrencyCode())))) {
                // only for MONO, if it's rate for EUR/USD then scip it
                if (BANK.isMono()) {
                    if ((BANK.getCurrency_USD()).equals(String.valueOf(jsonObject.get(BANK.getCurrencyCode_B()))))
                        continue;
                }
                addCurrencyToMap(map, jsonObject, CurrenciesAndCodeEnum.EUR.name());
            }
        }
        return map;
    }

    private void addCurrencyToMap(Map<String, Double> map, JSONObject jsonObject, String currencyAcronym) {
        map.put(currencyAcronym + "_buy", jsonObject.getDouble(BANK.getBuy()));
        map.put(currencyAcronym + "_sell", jsonObject.getDouble(BANK.getSell()));
    }

    private Bank buildBank(BankNames bankName) {
        Bank bank = BANK;
        switch (bankName) {
            case NBU: {
                DIRECTOR.buildNBU(BUILDER);
                bank = BUILDER.build();
                }break;
            case PRIVAT:{
                DIRECTOR.buildPRIVAT(BUILDER);
                bank = BUILDER.build();
                }break;
            case MONO:{
                DIRECTOR.buildMONO(BUILDER);
                bank = BUILDER.build();
            }break;

        }
        return bank;
    }

    private HttpResponse<String> sendGET(URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
