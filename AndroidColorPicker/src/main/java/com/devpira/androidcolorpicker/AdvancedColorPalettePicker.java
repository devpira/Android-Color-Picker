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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DEVPIRA on 8/13/16.
 */
public class AdvancedColorPalettePicker extends View {


    private float leftMargin = 0.1f;

    private int selectedColor;

    private Paint outerSelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerSelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float bWidth, bHeight;

    private float[] HSV = new float[]{0f,1f,1f};
    private float[] hueHSV = new float[]{210f,1f,1f};
    private int ALPHA = 255;

    //Following is for Hue bar:
    private Paint hueBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader hueBarShader;
    private RectF hueBar;
    private RectF huePointer;
    private float huePointerHeight = 0.01f;

    private Paint huePointerColor = new Paint(Paint.ANTI_ALIAS_FLAG);
    //following is for satval box:
    private Paint satValBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint valPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader satBoxShader;
    private RectF satValBox;
    private float xSatValPointer,ySatValPointer;
    private Paint outerPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //following is for Alpha bar:
    private Paint alphaBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader alphaBarShader;
    private RectF alphaBar, alphaBarPointer;
    private Paint alphaBarPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float alphaPointerWidth = 0.015f;

    private Paint barBorderPaint;

    private OnAdvancedColorChangedListener colorChangedListener;




    public AdvancedColorPalettePicker(Context context, float width, float height, OnAdvancedColorChangedListener listener) {
        super(context);
        this.bWidth = width;
        this.bHeight = height;
        this.colorChangedListener = listener;


        barBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBorderPaint.setStyle(Paint.Style.STROKE);
        barBorderPaint.setStrokeWidth(bWidth * 0.001f);
        barBorderPaint.setColor(Color.BLACK);

        setupOuterInnerCircles();
        setupHueBar();
        setupAlphaBar();
        setupSatValBox();

        //set default color:
        setSelectedColor(Color.parseColor("#000000"));

    }

    private void setupOuterInnerCircles(){
        outerSelPaint.setStyle(Paint.Style.STROKE);
        outerSelPaint.setStrokeWidth(bWidth * 0.05f);
        innerSelPaint.setColor(selectedColor);
    }

    private void setupHueBar(){
        hueBar = new RectF(bWidth *0.74f, bHeight *0.08f, bWidth *0.9f, bHeight *0.53f);
        huePointer = new RectF(bWidth *0.73f, bHeight *0.20f, bWidth *0.91f, bHeight *0.21f);

        huePointerColor.setStyle(Paint.Style.STROKE);
        huePointerColor.setStrokeWidth(bHeight * 0.007f);
        huePointerColor.setColor(Color.BLACK);

        int [] hueColors = new int[360];

        for (int i = 0; i <hueColors.length; i++){
            hueColors[i] = Color.HSVToColor( new float[]{i,1f,1f});
        }
        hueBarShader = new LinearGradient(hueBar.left, hueBar.top, hueBar.left, hueBar.bottom,hueColors,null, Shader.TileMode.CLAMP);
        hueBarPaint.setShader(hueBarShader);

    }
    private void setupAlphaBar(){
        alphaBar = new RectF(bWidth*leftMargin,hueBar.bottom - (hueBar.right - hueBar.left),huePointer.left - (bWidth*leftMargin/2),hueBar.bottom);

        alphaBarPointer = new RectF(alphaBar.left, alphaBar.top-bHeight*0.004f, alphaBar.left+bWidth*0.015f, alphaBar.bottom+bHeight*0.004f);
        alphaBarPointerPaint.setStyle(Paint.Style.STROKE);
        alphaBarPointerPaint.setStrokeWidth(bWidth * 0.008f);
        alphaBarPointerPaint.setColor(Color.BLACK);
    }

    private void setupSatValBox(){
        satValBox = new RectF(bWidth*leftMargin,hueBar.top,huePointer.left - (bWidth*leftMargin/2), alphaBar.top - (bHeight*0.03f));
        Shader valShader =  new LinearGradient(satValBox.left, satValBox.top, satValBox.left, satValBox.bottom,new int[]{0x00ffffff,Color.BLACK},null, Shader.TileMode.CLAMP);
        valPaint.setShader(valShader);



        outerPointerPaint.setStyle(Paint.Style.STROKE);
        outerPointerPaint.setStrokeWidth(bHeight * 0.007f);
        outerPointerPaint.setColor(Color.BLACK);

        innerPointerPaint.setStyle(Paint.Style.STROKE);
        innerPointerPaint.setStrokeWidth(bHeight * 0.005f);
        innerPointerPaint.setColor(Color.WHITE);

    }





    private float xTouch = 0;
    private float xLast = 0;
    private float yTouch = 0;
    private float yLast = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xTouch =  event.getX();
        yTouch = event.getY();
        invalidate();

        if(xTouch >= hueBar.left - bWidth *0.05f && xTouch <= hueBar.right + bWidth *0.05f && yTouch >= hueBar.top - bHeight *0.1f && yTouch <= hueBar.bottom + bHeight *0.05f){
          return true;
        }
        if(xTouch <= hueBar.left && yTouch >= satValBox.bottom && yTouch <= alphaBar.bottom + bHeight*0.1f){
            return true;
        }
        if(xTouch >= satValBox.left - leftMargin && xTouch <= hueBar.left && yTouch >= satValBox.top - bHeight*0.05f && yTouch <= alphaBar.top){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(hueBar, 10, 10, hueBarPaint);
        canvas.drawRoundRect(alphaBar, 10, 10, alphaBarPaint);
        canvas.drawRoundRect(alphaBar, 10, 10, barBorderPaint);
        canvas.drawRoundRect(satValBox, 10, 10, satValBarPaint);
        canvas.drawRoundRect(satValBox,10,10,valPaint);
        canvas.drawRoundRect(satValBox,10,10,barBorderPaint);

        if( xTouch != xLast && yTouch != yLast && xTouch >= hueBar.left && xTouch <= hueBar.right && yTouch >= hueBar.top- bHeight *0.05f && yTouch <= hueBar.bottom+ bHeight *0.05f){
            float y =  Math.max(hueBar.top, Math.min(yTouch, hueBar.bottom));
            huePointer.set(huePointer.left,y,huePointer.right,y+ bHeight *0.01f);
            onHueChanged(y);

        }
        canvas.drawRect(huePointer,huePointerColor);

        if(xTouch != xLast && yTouch != yLast && xTouch >= satValBox.left - leftMargin && xTouch <= hueBar.left && yTouch >= satValBox.top - bHeight*0.05f && yTouch <= alphaBar.top){
            xSatValPointer=  Math.max(satValBox.left,Math.min(satValBox.right,xTouch));
            ySatValPointer =  Math.max(satValBox.top,Math.min(satValBox.bottom,yTouch));
            onSatValChanged(xSatValPointer,ySatValPointer);
        }

        //draw satvalbox pointer:
        canvas.drawCircle(xSatValPointer,ySatValPointer,bWidth*0.03f,outerPointerPaint);
        canvas.drawCircle(xSatValPointer,ySatValPointer,bWidth*0.02f,innerPointerPaint);


        //following is for brightbar pointer
        if(xTouch != xLast && yTouch != yLast && xTouch <= hueBar.left && yTouch >= satValBox.bottom && yTouch <= alphaBar.bottom + bHeight*0.1f){
            float x =  Math.max(alphaBar.left,Math.min(alphaBar.right,xTouch));
            alphaBarPointer.set(x, alphaBar.top,x+ alphaBarPointer.width(), alphaBar.bottom);
            onAlphaChanged(x);
        }
        canvas.drawRect(alphaBarPointer, alphaBarPointerPaint);

        canvas.drawCircle(bWidth / 2, bHeight * 0.7f + (bWidth * 0.25f / 2), bWidth * 0.15f, outerSelPaint);
        canvas.drawCircle(bWidth / 2, bHeight * 0.7f + (bWidth *0.25f/2),bWidth*0.11f, innerSelPaint);

        xLast = xTouch;
        yLast = yTouch;

    }

    private void onHueChanged(float y){
        HSV[0] = ((y -hueBar.top)/hueBar.height())*360f;
        hueHSV[0] = HSV[0];
        updateSelectedColor();
    }

    /*  Takes hue value which ranges from 0 - 360
     *  and converts it to the y-coordinate where the pointer
     *  will be moved to.
     */
    private void setHuePointerTo(float hue){
        float top = ((hue/360f)*hueBar.height())+hueBar.top;
        huePointer.set(huePointer.left, top, huePointer.right, top + bHeight * huePointerHeight);
    }
    private void onSatValChanged (float x, float y){
        //saturation value:
        HSV[1] = ((x-satValBox.left)/satValBox.width())*1.0f;
        //Value:
        HSV[2] =  ((satValBox.bottom - y)/satValBox.height())*1.0f;
        updateAdjustedSelectedColor();
    }
    private void setxSatValPointerTo(float sat, float val){
        xSatValPointer = (sat*satValBox.width())+satValBox.left;
        ySatValPointer = satValBox.bottom -(val*satValBox.height());
    }
    private void onAlphaChanged(float x){
        ALPHA = (int) (((alphaBar.right - x)/ alphaBar.width())*255);
        updateAdjustedSelectedColor();

    }
    private void setAlphaBarPointerTo(float alpha){
        float left = alphaBar.right -((alpha/255f)*alphaBar.width());
        alphaBarPointer.set(left,alphaBarPointer.top,left+bWidth*alphaPointerWidth,alphaBarPointer.bottom);
    }
    private void updateSelectedColor(){
        selectedColor = Color.HSVToColor(ALPHA,HSV);
        outerSelPaint.setColor(selectedColor);
        innerSelPaint.setColor(selectedColor);

        alphaBarShader = new LinearGradient(alphaBar.left, alphaBar.top, alphaBar.right, alphaBar.top,new int[]{Color.HSVToColor(hueHSV),0},null, Shader.TileMode.CLAMP);
        alphaBarPaint.setShader(alphaBarShader);

        satBoxShader = new LinearGradient(satValBox.left, satValBox.top, satValBox.right, satValBox.bottom,new int[]{Color.WHITE,Color.HSVToColor(hueHSV)},null, Shader.TileMode.CLAMP);
        satValBarPaint.setShader(satBoxShader);

        if(colorChangedListener != null)
            colorChangedListener.onColorChanged(selectedColor,HSV,ALPHA);

        invalidate();
    }
    private void updateAdjustedSelectedColor(){
        selectedColor = Color.HSVToColor(ALPHA,HSV);
        innerSelPaint.setColor(selectedColor);

        if(colorChangedListener != null)
            colorChangedListener.onColorChanged(selectedColor,HSV,ALPHA);

        invalidate();
    }

    public void setSelectedColor(int color){
        selectedColor = color;
        Color.colorToHSV(color,HSV);                        //convert selected color to HSV:
        ALPHA = Color.alpha(color);                        //get alpha value of color:
        hueHSV[0] = HSV[0];                               //get hue value only to set satValBox and alphaBar

        outerSelPaint.setColor(selectedColor);          //Set selected color to outerCircle and
        innerSelPaint.setColor(selectedColor);         // inner circle

        //Set the hue of the selected color to alphaBar:
        alphaBarShader = new LinearGradient(alphaBar.left, alphaBar.top, alphaBar.right, alphaBar.top,new int[]{Color.HSVToColor(hueHSV),0},null, Shader.TileMode.CLAMP);
        alphaBarPaint.setShader(alphaBarShader);
        //set the hue of the selected color to satValBox:
        satBoxShader = new LinearGradient(satValBox.left, satValBox.top, satValBox.right, satValBox.bottom,new int[]{Color.WHITE,Color.HSVToColor(hueHSV)},null, Shader.TileMode.CLAMP);
        satValBarPaint.setShader(satBoxShader);

        //set each pointer to their respective color value on their bar:
        setHuePointerTo(HSV[0]);
        setxSatValPointerTo(HSV[1],HSV[2]);
        setAlphaBarPointerTo(ALPHA);

        invalidate();  //Have canvas re-draw to update color



    }

    public void setAdvancedColorChangedListener(OnAdvancedColorChangedListener colorChangedListener) {
        this.colorChangedListener = colorChangedListener;
    }

    public interface OnAdvancedColorChangedListener{

        void onColorChanged(int color, float HSV[], int Alpha);

    }
}
