/*
 * Copyright (C) 2016 DEVPIRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */

package com.devpira.androidcolorpicker;



/**
 * Created by DEVPIRA on 7/28/16.
 */
public class MyColorUtils {

    public static int R = 0;
    public static int G = 1;
    public static int B = 2;


    /**
     * Converts Hexadecimal value of a color  into Red, green, blue values of that color
     * @param Hex String
     * @return  RGB as int array. int[0] = R , int[1] = G, int[2] = B
     */
    public static int[] hexToRGB(String Hex) {
        if (Hex.contains("#")) {
            if (String.valueOf(Hex.charAt(0)).equals("#")) {
                Hex = Hex.substring(1, Hex.length());
            } else {
                return null;
            }
        }
        if (Hex.length() != 6) return null;

        if(!Hex.matches("^[a-fA-F0-9]*$")) return  null;

        int[] RGB = new int[3];
        for (int i = 0; i < 3; i++) {
            int end = (i + 1) * 2;
            RGB[i] = (int) HexConverter.toDecimal(Hex.substring(i * 2, end));
        }
        return RGB;
    }

    /**
     * Converts red. green, blue values that make up a color into its hexadecimal value of that color
     * @param rgb int array containing {red value,green value, blue value}
     * @return hexadecimal string. Ex.#ffffff
     */
    public static String RGBtoHexColor(int[] rgb){
        String hex ="#";
        String h;
        for(int i = 0; i<rgb.length;i++){
            hex = hex+(((h = HexConverter.toHexadecimal(Long.valueOf(rgb[i]))).length() == 2) ? h: "00");
        }
        return hex;
    }


}
