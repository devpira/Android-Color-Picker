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

import android.content.Context;
import android.support.v7.widget.CardView;

/**
 * Created by DEVPIRA on 8/10/16.
 */
public class ColorBox extends CardView {

    private String hexColor;
    private int[] RGB = new int[3];
    private int[] RGBajustment = new int[]{0,0,0};
    private float [] HSV;
    private String [] colorArray;


    public ColorBox(Context context) {
        super(context);
    }
    public int[] getRGB() {
        int H;
        return new int[]{((((H = RGB[0] + RGBajustment[0]) >= 0)? H: 0) <= 255)? H :255,
                            ((((H = RGB[1] + RGBajustment[1]) >= 0)? H: 0) <= 255)? H :255,
                            ((((H = RGB[2] + RGBajustment[2]) >= 0)? H: 0) <= 255)? H :255
        };
    }

    public void setRGB(int[] RGB) {
        if(RGB == null)
            RGB = new int[]{0,0,0};
        this.RGB = RGB;
        this.hexColor = MyColorUtils.RGBtoHexColor(this.RGB);
    }

    public void adjustRGB(int Ra,int Ba,int Ga){
            RGBajustment[0] = RGBajustment [0] + Ra;
            RGBajustment[1] = RGBajustment [1] + Ba;
            RGBajustment[2] = RGBajustment [2] + Ga;
            setRGB(RGB);
    }

    public void removeRGBAdjustment(){
        this.RGBajustment = new int[]{0,0,0};
    }


    public String getHexColor() {
        return MyColorUtils.RGBtoHexColor(getRGB());
    }

    public void setHexColor(String hexColor) {
        setRGB(MyColorUtils.hexToRGB(hexColor));
    }


    public void setColorArray(String[] colorArray) {
        this.colorArray = colorArray;
    }

    public String getHexColorFromArray(int position){
        if(colorArray == null || position <0)
            return "#000000";
        setHexColor(colorArray[position]);
        return this.getHexColor();
    }


}