package com.devpira.androidcolorpicker;


/**
 * Created by chamber on 7/26/16.
 */
public class HexConverter {

    public static long toDecimal(String hex){
        hex = hex.toLowerCase();
        long decimal = 0;
        for (long i= 0; i<hex.length(); i++){
            long power = hex.length() - 1 - i;
            String c = String.valueOf(hex.charAt((int) i));
            switch (c){
                case "a":
                    c = "10";
                    break;
                case "b":
                    c ="11";
                    break;
                case "c":
                    c= "12";
                    break;
                case "d":
                    c="13";
                    break;
                case "e":
                    c="14";
                    break;
                case "f":
                    c = "15";
                    break;
            }
            long hexBase = Long.valueOf(c);
            long powerBase = 1;
            while( power > 0){
                powerBase = powerBase * 16;
                power = power - 1;
                if(powerBase > Long.MAX_VALUE){
                    powerBase = Long.MAX_VALUE;
                    break;
                }
            }

            decimal = decimal + hexBase*powerBase;

        }
        return decimal;
    }

    public static String toHexadecimal (long decimal){
        String hex = "";
        long R = 0;
        while (decimal >0){
           R = (int) (decimal%16);
            decimal = (decimal-R)/16;
            if(R == 10){
                hex = "A"+hex;
            }else if(R == 11){
                hex = "B"+hex;
            }else if(R == 12){
                hex = "C"+hex;
            }else if(R == 13){
                hex = "D"+hex;
            }else if(R == 14){
                hex = "E"+hex;
            }else if(R == 15){
                hex = "F"+hex;
            }else{
                hex = String.valueOf(R)+hex;
            }
        }
        if(decimal> 0 ){
            if(decimal == 10){
                hex = "A"+hex;
            }else if(decimal == 11){
                hex = "B"+hex;
            }else if(decimal == 12){
                hex = "C"+hex;
            }else if(decimal == 13){
                hex = "D"+hex;
            }else if(decimal == 14){
                hex = "E"+hex;
            }else if(decimal == 15){
                hex = "F"+hex;
            }else{
                hex = String.valueOf(R)+hex;
            }
        }
        hex.trim();
        return hex;
    }

}
