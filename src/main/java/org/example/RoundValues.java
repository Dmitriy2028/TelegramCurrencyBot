package org.example;

import java.text.DecimalFormat;

public class RoundValues {
/*  use public String roundValue(int checkBox, float value) to get rounded value**/

    public RoundValues() {
    }

    public String roundValue(int checkBox, float value){
         switch (checkBox){
             case (1):
                 return  formatOutputData(value, "#.#" );
             case (2) :
                 return   formatOutputData(value, "#.##");
             case (3):
                 return   formatOutputData(value, "#.###");
             case (4):
                 return   formatOutputData(value, "#.####");
         }
        return "Invalid command";
     }

     private String formatOutputData(float value, String pattern){
         DecimalFormat decimalFormat = new DecimalFormat(pattern);
         return decimalFormat.format(value);
     }
}
