package rounding;

import java.text.DecimalFormat;

public class RoundValues {
    /** use public String roundValue(int checkBox, float value) to get rounded value*/
    private final int checkBox;
    public RoundValues(int checkBox) {
        this.checkBox = checkBox;
    }

    public String roundValue(Double value){
        return switch (checkBox) {
            case (1) -> formatOutputData(value, "#.#");
            case (2) -> formatOutputData(value, "#.##");
            case (3) -> formatOutputData(value, "#.###");
            case (4) -> formatOutputData(value, "#.####");
            default -> "Invalid command";
        };
    }

    private String formatOutputData(Double value, String pattern){
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(value);
    }
}