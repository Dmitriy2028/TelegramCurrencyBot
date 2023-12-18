package org.example.privat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyItem {
    private Currency ccy;
    private Currency base_ccy;
    private double buy;
    private double sale;
}
