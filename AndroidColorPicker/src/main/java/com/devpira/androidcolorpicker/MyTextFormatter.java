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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by DEVPIRA on 8/20/16.
 */
public class MyTextFormatter {

    private int phoneScreenWidth,phoneScreenHeight;
    private float density, scaledDensity;


    public MyTextFormatter(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.phoneScreenHeight = displayMetrics.heightPixels;
        this.phoneScreenWidth = displayMetrics.widthPixels;
        this.density = displayMetrics.density;
        this.scaledDensity = displayMetrics.scaledDensity;

    }

    public void setTextProperties(View view, float normalTextSize, float maxTextSize, float minTextSize){
        this.setTextProperties(view, normalTextSize, maxTextSize, minTextSize, 0, 0, false, false);
    }
    public void setTextProperties(View view,float textSize, float maxTextSize, float minTextSize, float x, float y, boolean centerText, boolean scaleText){
        if(view instanceof TextView){
            //following sets text size:
            if(maxTextSize > 0 || minTextSize >0) {
                textSize = calculateTextSize(textSize, maxTextSize, minTextSize);
                scaleText = false;
            }
            if(scaleText) {
                //if scale text true then text view text size will also depend on user preference:
                if(textSize >0)
                    ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * (((textSize / 100) / density) * scaledDensity));
            }else{
                //if false then user preference not considered:
                if(textSize >0)
                    ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * ((textSize / 100)));
            }

            //following centers text if set to true:
            if(centerText)
                ((TextView)view).setGravity(Gravity.CENTER);
        }else if(view instanceof EditText){
            if(scaleText) {
                if(textSize >0)
                    ((EditText)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * (((textSize / 100) / density) * scaledDensity));
            }else{
                if(textSize >0)
                    ((EditText)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * ((textSize / 100)));
            }

            if(centerText)
                ((EditText)view).setGravity(Gravity.CENTER);
        }else if(view instanceof Button){
            if(scaleText) {
                if(textSize >0)
                    ((Button)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * (((textSize / 100) / density) * scaledDensity));
            }else{
                if(textSize >0)
                    ((Button)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * ((textSize / 100)));
            }

            if(centerText)
                ((Button)view).setGravity(Gravity.CENTER);
        }else if(view instanceof CheckBox){
            if(scaleText) {
                if(textSize >0)
                    ((CheckBox)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * (((textSize / 100) / density) * scaledDensity));
            }else{
                if(textSize >0)
                    ((CheckBox)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneScreenWidth * ((textSize / 100)));
            }

            if(centerText)
                ((CheckBox)view).setGravity(Gravity.CENTER);
        }

        //following positions text view on the x and y axis:
        if(y != 0)
            view.setY(phoneScreenHeight * y / 100);
        if(x != 0)
            view.setX(phoneScreenWidth * x / 100);
    }


    public float calculateTextSize(float normalSize, float maxSize, float minSize){
        if(normalSize <= 0)
            return 0;
        if(maxSize <= 0)
            maxSize = normalSize;
        if(minSize <=0)
            minSize = normalSize;

        float anchor = scaledDensity/density;
        float percentChange = (anchor - 1);
        float sizeChange = normalSize * percentChange;
        float newSize = normalSize + sizeChange;
        if(newSize >maxSize)
            newSize = maxSize;
        if(newSize < minSize)
            newSize = minSize;
        return newSize;
    }
}
