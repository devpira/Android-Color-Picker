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
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVPIRA on 7/29/16.
 *
 * Class creates an initial Color picker with an array of Material design colors.
 * Advance functions for creating unique colors is also available when the user
 * clicks the "Advance button". To use, simply call the static "open" function of the class
 * and provide the required parameters.
 *
 * IMPORTANT NOTE:
 * All child views are designed and measured relatively to the
 * view width and view height provided.If view width and view height change, so will the
 * dimensions of the child views. If no dimensions are provided the class will automatically
 * use the predetermined dimensions based on each devices screen width and height. THis ensures that
 * values for the child views are not fixed and change accordingly to each device screen resolution.
 *
 * If user of this code wishes to change the dimensions
 * of a specific child view then the user must do so directly in the source code.
 */
public class ColorPalettePicker extends RelativeLayout {

    /**
     * Simple static functions to open ColorPalettePicker
     */

    public static ColorPalettePicker open(Context context,ViewGroup parant, OnColorPalettePickerListener colorPalettePickerListener){
        return ColorPalettePicker.open(context,parant, 0,0,colorPalettePickerListener);
    }

    public static ColorPalettePicker open(Context context,ViewGroup parant,float viewWidth, float viewHeight, OnColorPalettePickerListener colorPalettePickerListener){
        //set base width and height of Color Picker to default dimensions if not provided or zero:
        viewWidth = (viewWidth > 0)? viewWidth: context.getResources().getDisplayMetrics().widthPixels * 0.89f;
        viewHeight = (viewHeight > 0)? viewHeight: context.getResources().getDisplayMetrics().heightPixels * 0.89f;

        ColorPalettePicker colorPalettePicker = new ColorPalettePicker(context, parant,viewWidth,viewHeight);     //Create new instance
        colorPalettePicker.setColorPalettePickerListener(colorPalettePickerListener);       // set listener provided
        parant.addView(colorPalettePicker);       // added created instance to View provided
        return colorPalettePicker;
    }

    /**
     * parent is the view on which this class will attach itself to
     */
    private ViewGroup parent;

    /**
     * Base Dimensions of the color picker.
     * All child views are measured based on these values.
     */
    private float baseWidth, baseHeight;

    private RelativeLayout mainHolder, defaultHolder;
    private TextView colorPickerTitle;
    private Button advanceButton, existButton;
    private SeekBar levelSeekBar, brigthnessSeekBar;

    /**
     * Theme color of the color picker.
     */
    private int colorPickerThemeColor;


    private List<ColorBox> colorBoxList = new ArrayList<>();
    private MyTextFormatter myTextFormatter;

    /**
     * A check to see if advance functions are displayed to user
     */
    private boolean isAdvanceSettingOpen = false;

    /**
     * Class containing the functions to create unique colors
     */
    private AdvancedColorPalettePicker advancedColorPicker;

    /**
     * Holds the advance color picker function
     */
    private RelativeLayout advanceHolder;


    private OnColorPalettePickerListener colorPalettePickerListener;


    private ColorPalettePicker(Context context, ViewGroup parent, float viewWidth, float viewHeight) {
        super(context);
        this.parent = parent;
        this.baseWidth = viewWidth;
        this.baseHeight = viewHeight;
        this.colorPickerThemeColor = Color.parseColor("#00BCD4"); //default theme color set to Cyan 500 from Material Color Palette
        this.myTextFormatter = new MyTextFormatter(getContext());
        setupBasicViews();
    }

    public boolean close(){
        try {
            if (parent != null && this != null) {
                parent.removeView(this);
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * XML layout file not created.
     * All views are programmatically build
     */
    private void setupBasicViews(){
        //Setup root view:
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.parseColor("#b3000000"));
        setClickable(true);
        setFocusable(true);

        //Setup Card View holder for main holder:
        CardView cardViewHolder = new CardView(getContext());
        cardViewHolder.setCardBackgroundColor(Color.parseColor("#ffffff"));
        RelativeLayout.LayoutParams params_cardViewHolder = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_cardViewHolder.addRule(CENTER_HORIZONTAL);
        params_cardViewHolder.addRule(CENTER_VERTICAL);
        cardViewHolder.setLayoutParams(params_cardViewHolder);
        addView(cardViewHolder);

        //Setup main holder for all other views:
        mainHolder  = new RelativeLayout(getContext());
        mainHolder.setLayoutParams(new ViewGroup.LayoutParams((int) baseWidth, (int) baseHeight));
        cardViewHolder.addView(mainHolder);
        //Setup default holder for default color chooser:
        defaultHolder = new RelativeLayout(getContext());
        defaultHolder.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainHolder.addView(defaultHolder);

        //Setup title:
        colorPickerTitle = new TextView(getContext());
        colorPickerTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        colorPickerTitle.setTextColor(colorPickerThemeColor);
        colorPickerTitle.setX(baseWidth * 0.07f);
        colorPickerTitle.setY(baseHeight * 0.015f);
        colorPickerTitle.setText("Select Color");
        myTextFormatter.setTextProperties(colorPickerTitle, 5.5f, 6.5f, 4.5f);
        mainHolder.addView(colorPickerTitle);

        //set up advance button which also serves as back button:
        advanceButton = new Button(getContext());
        RelativeLayout.LayoutParams param_advanceButton = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_advanceButton.addRule(ALIGN_PARENT_BOTTOM);
        param_advanceButton.addRule(ALIGN_PARENT_LEFT);
        param_advanceButton.setMargins((int) (baseWidth * 0.1), 0, 0, (int) (baseHeight * 0.01));
        advanceButton.setLayoutParams(param_advanceButton);
        advanceButton.setBackgroundColor(Color.parseColor("#00ffffff"));
        advanceButton.setText("ADVANCE");
        myTextFormatter.setTextProperties(advanceButton, 5, 6, 4);
        advanceButton.setTextColor(colorPickerThemeColor);
        mainHolder.addView(advanceButton);
        advanceButton.setOnClickListener(advanceButtonClickListener);

        // setup exit button:
        existButton = new Button(getContext());
        RelativeLayout.LayoutParams param_existButton = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_existButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param_existButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param_existButton.setMargins(0, 0, (int) (baseWidth * 0.1), (int) (baseHeight * 0.01));
        existButton.setLayoutParams(param_existButton);
        existButton.setBackgroundColor(Color.parseColor("#00ffffff"));
        existButton.setText("EXIT");
        myTextFormatter.setTextProperties(existButton, 5, 6, 4);
        existButton.setTextColor(colorPickerThemeColor);
        mainHolder.addView(existButton);
        existButton.setOnClickListener(exitButtonClickListener);

        //Sets up each individual boxes that displays each color
        setupColorBox();

        //Set up individual SeekBar:
        RelativeLayout.LayoutParams params_seekBar = new LayoutParams((int) (baseWidth*0.82), ViewGroup.LayoutParams.WRAP_CONTENT);
        params_seekBar.addRule(CENTER_HORIZONTAL);

        levelSeekBar = new SeekBar(getContext());  //Controls the level of color from 0 - 900
        levelSeekBar.setLayoutParams(params_seekBar);
        levelSeekBar.setY(baseHeight * 0.76f);
        levelSeekBar.setMax(9); // There are only 10 levels for each color according to current material design
        levelSeekBar.setProgress(5);
        levelSeekBar.setOnSeekBarChangeListener(levelSeekBarChangeListener);
        defaultHolder.addView(levelSeekBar);

        brigthnessSeekBar = new SeekBar(getContext()); //Controls the brightness of color
        brigthnessSeekBar.setLayoutParams(params_seekBar);
        brigthnessSeekBar.setY(baseHeight * 0.85f);
        brigthnessSeekBar.setMax(100);
        brigthnessSeekBar.setProgress(50);
        brigthnessSeekBar.setOnSeekBarChangeListener(brightnessSeekBarChangeListener);
        defaultHolder.addView(brigthnessSeekBar);


    }

    private void setupColorBox(){
        int [] basicColorRes = new int[]{R.array.material_red,R.array.material_pink,R.array.material_purple,R.array.material_deep_purple,
                R.array.material_cyan, R.array.material_light_blue,R.array.material_blue,R.array.material_indigo,
                R.array.material_lime,R.array.material_light_green,R.array.material_green,R.array.material_teal,
                R.array.material_yellow,R.array.material_amber,R.array.material_orange,R.array.material_deep_orange,
                R.array.material_brown,R.array.material_grey,R.array.material_blue_grey,R.array.material_black,};


        ViewGroup.LayoutParams params_colorBox = new ViewGroup.LayoutParams((int) (baseWidth*0.17f), (int) (baseWidth*0.17f));
        for(int i = 0; i < basicColorRes.length; i++){
            int columnBaseX = (i+4)%4;
            int rowBaseY = (i -(i%4))/4;
            ColorBox colorBox = createColorBox(params_colorBox,0.064f+(0.234f*columnBaseX),.09f+(0.134f*rowBaseY),basicColorRes[i]);
            colorBoxList.add(i,colorBox);
        }
    }
    private ColorBox createColorBox(ViewGroup.LayoutParams params, float x, float y,int resId){
        ColorBox colorBox = new ColorBox(getContext());
        colorBox.setLayoutParams(params);
        if(x > 0) colorBox.setX(x * baseWidth);
        if(y > 0) colorBox.setY(y  * baseHeight);
        colorBox.setColorArray(getResources().getStringArray(resId));
        colorBox.setCardBackgroundColor(Color.parseColor(colorBox.getHexColorFromArray(5)));
        defaultHolder.addView(colorBox);

        colorBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorPalettePickerListener != null)
                    colorPalettePickerListener.onColorSelected(Color.parseColor(((ColorBox) view).getHexColor()));
                close();
            }
        });
        return colorBox;
    }

    private int lastLevelSeekerProgress = 50;
    private final SeekBar.OnSeekBarChangeListener levelSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            for(int i = 0; i<colorBoxList.size();i++){
                ColorBox colorBox = colorBoxList.get(i);
                colorBox.setCardBackgroundColor(Color.parseColor(colorBox.getHexColorFromArray(progress)));
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private final SeekBar.OnSeekBarChangeListener brightnessSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int delta = progress - lastLevelSeekerProgress;
            lastLevelSeekerProgress = progress;
            for(int i = 0; i<colorBoxList.size();i++){
                ColorBox colorBox = colorBoxList.get(i);
                int [] RGB = colorBox.getRGB();
                int h;
                RGB[0] = ((((h = RGB[0]+delta) >=0)? h:0) <=255)? h:255 ;

                RGB[1] =((((h = RGB[1]+delta) >=0)? h:0) <=255)? h:255 ;
                RGB[2] = ((((h = RGB[2]+delta) >=0)? h:0) <=255)? h:255 ;

                colorBox.adjustRGB(delta,delta,delta);
                colorBox.setCardBackgroundColor(Color.parseColor(colorBox.getHexColor()));
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };


    /**
     * Allows coder to change theme color of the color picker
     * @param color
     */
    public void setColorPickerThemeColor(int color){
        colorPickerThemeColor = color;
        if(colorPickerTitle !=null)
            colorPickerTitle.setTextColor(colorPickerThemeColor);
        if(advanceButton != null)
            advanceButton.setTextColor(colorPickerThemeColor);
        if(existButton != null)
            existButton.setTextColor(colorPickerThemeColor);

    }

    final private OnClickListener advanceButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isAdvanceSettingOpen) {
                openAdvanceColorPicker();
            } else {
                removeAdvanceColorPicker();
            }
        }
    };

    final private OnClickListener exitButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!close()) {         //if closing fails then remove all views
                try {
                    removeAllViews();
                    setBackgroundColor(Color.parseColor("#1a000000"));
                    setClickable(false);
                    setFocusable(false);
                } catch (Exception e) {e.printStackTrace();}
            }
        }
    };

    /*****************************                        SET UP FOR ADVANCED COLOR PICKER FUNCTION:                                        */

    private void openAdvanceColorPicker(){
        //Check to see if advance color picker exists
        //If not create new one.
        //Remove and change normal color picker views to match advance
        if(advanceHolder != null && advancedColorPicker != null && mainHolder !=null){
            mainHolder.addView(advanceHolder);
        }else{
            advanceHolder = new RelativeLayout(getContext());
            advanceHolder.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            advanceHolder.setFocusable(true);
            advanceHolder.setFocusableInTouchMode(true);

            advancedColorPicker = new AdvancedColorPalettePicker(getContext(),baseWidth,baseHeight,new AdvancedColorPalettePicker.OnAdvancedColorChangedListener() {
                @Override
                public void onColorChanged(int color, float[] hsv, int Alpha) {
                    advanceHolder.requestFocus();

                    setColor(color);
                }
            });
            advancedColorPicker.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            advanceHolder.addView(advancedColorPicker);

            setupHexEditText();
            setupOuterCircleDetails();
            setupSelectButton();
            mainHolder.addView(advanceHolder);
            setColor(colorPickerThemeColor);
            advancedColorPicker.setSelectedColor(selectedColor);
        }

        defaultHolder.setVisibility(INVISIBLE);
        advanceButton.setText("BACK");
        colorPickerTitle.setText("Advanced Setting");

        isAdvanceSettingOpen = true;
    }

    private void removeAdvanceColorPicker(){
        if (advanceHolder != null)
            mainHolder.removeView(advanceHolder);
        defaultHolder.setVisibility(VISIBLE);
        advanceButton.setText("ADVANCE");
        colorPickerTitle.setText("Select Color");
        isAdvanceSettingOpen = false;
    }

    private int selectedColor;
    private float [] HSV = new float[3];
    private int ALPHA = 255;
    private int [] RGB = new int[3];
    private String hexColor;

    public void setColor(int color){
        selectedColor = color;
        Color.colorToHSV(color,HSV);
        ALPHA = Color.alpha(color);
        hexColor = Integer.toHexString(color);
        //append "0" to the front since "toHexString" does not append "0" in front of single digit hex
        if(hexColor.length() < 6){
            int loop = 6 - hexColor.length();
            while (loop >0){
                hexColor = "0"+hexColor;
                loop = loop -1;
            }
        }

        RGB = MyColorUtils.hexToRGB(hexColor.substring(hexColor.length() - 6));
        if(RGB == null)
            setColor(Color.parseColor("#000000"));

        updateColorDetailsViews();


    }
    private void updateColorDetailsViews(){
        if(hexET!=null && !hexET.hasFocus()) {
            hexET.removeTextChangedListener(textWatcher);
            hexET.setText(hexColor);
            hexET.setSelection(hexET.getText().toString().length());
            hexET.addTextChangedListener(textWatcher);
        }

        for (int i = 0; i <ETList.size() ; i++){
            if(ETList.get(i) == null)
                continue;

            ETList.get(i).removeTextChangedListener(textWatcher);

            switch (i){
                case AET:
                    ETList.get(i).setText(String.valueOf(ALPHA));
                    break;
                case RET:
                    ETList.get(i).setText(String.valueOf(RGB[0]));
                    break;
                case GET:
                    ETList.get(i).setText(String.valueOf(RGB[1]));
                    break;
                case BET:
                    ETList.get(i).setText(String.valueOf(RGB[2]));
                    break;
                case HET:
                    if(!ETList.get(i).hasFocus() || Float.valueOf(ETList.get(i).getText().toString()) > 359 || HSV [0] == 0)
                        ETList.get(i).setText(String.valueOf(Math.round(HSV[0])));
                    break;
                case SET:
                    if(!ETList.get(i).hasFocus() || Float.valueOf(ETList.get(i).getText().toString()) > 100 || HSV [1] == 0)
                        ETList.get(i).setText(String.valueOf((int) (HSV[1] * 100)));
                    break;
                case VET:
                    if(!ETList.get(i).hasFocus() || Float.valueOf(ETList.get(i).getText().toString()) > 100 || HSV [2] == 0)
                         ETList.get(i).setText(String.valueOf((int)(HSV[2] * 100)));
                    break;

            }
            ETList.get(i).setSelection(ETList.get(i).getText().toString().length());
            ETList.get(i).addTextChangedListener(textWatcher);
        }
    }



    private EditText hexET;
    private void setupHexEditText(){
        hexET = new EditText(getContext());
        hexET.setLayoutParams(new ViewGroup.LayoutParams((int) (baseWidth * 0.6), (int) (baseHeight * 0.065)));
        hexET.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.devpira_style1_edittext));
        hexET.setTextColor(Color.parseColor("#000000"));
        hexET.setHintTextColor(Color.parseColor("#9E9E9E"));
        hexET.setHint("Hex Color");
        hexET.setSingleLine(true);
        hexET.setGravity(Gravity.CENTER);
        hexET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        hexET.setX(baseWidth * 0.2f);
        hexET.setY(baseHeight * 0.56f);
        hexET.setPadding((int) (baseWidth * 0.04), 0, 0, 0);
        hexET.addTextChangedListener(textWatcher);
        hexET.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(advanceHolder != null)
            advanceHolder.addView(hexET);
    }

    private Button selectButton;
    private void setupSelectButton(){
        selectButton = new Button(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(0, 0, 0, (int) (baseHeight * 0.01));
        selectButton.setLayoutParams(params);
        selectButton.setBackgroundColor(Color.parseColor("#00ffffff"));
        selectButton.setText("SELECT");
        myTextFormatter.setTextProperties(selectButton, 5, 6, 4);
        selectButton.setTextColor(colorPickerThemeColor);
        advanceHolder.addView(selectButton);

        selectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorPalettePickerListener != null)
                    colorPalettePickerListener.onColorSelected(selectedColor);
                close();
            }
        });
    }

    private final int AET = 0;
    private final int RET = 1;
    private final int GET = 2;
    private final int BET = 3;
    private final int HET = 4;
    private final int SET = 5;
    private final int VET = 6;
    private List<EditText> ETList;

    private void setupOuterCircleDetails(){
        RelativeLayout.LayoutParams paramsET = new LayoutParams((int) (baseWidth*0.11), (int) (baseHeight*0.055));
        ETList = new ArrayList<>();

        ETList.add(AET, createColorDetailEditText(paramsET, 0.14f, 0.65f));
        createColorDetailTextView(0.07f, 0.656f, "A:");

        ETList.add(RET, createColorDetailEditText(paramsET, 0.14f, 0.72f));
        createColorDetailTextView(0.07f, 0.728f, "R:");

        ETList.add(GET, createColorDetailEditText(paramsET, 0.14f, 0.79f));
        createColorDetailTextView(0.07f, 0.795f, "G:");

        ETList.add(BET, createColorDetailEditText(paramsET, 0.14f, 0.86f));
        createColorDetailTextView(0.07f, 0.868f, "B:");

        ETList.add(HET, createColorDetailEditText(paramsET, 0.82f, 0.67f));
        createColorDetailTextView(0.75f, 0.677f, "H:");

        ETList.add(SET, createColorDetailEditText(paramsET, 0.82f, 0.76f));
        createColorDetailTextView(0.75f, 0.767f, "S:");

        ETList.add(VET, createColorDetailEditText(paramsET, 0.82f, 0.85f));
        createColorDetailTextView(0.75f, 0.858f, "V:");

    }

    private EditText createColorDetailEditText(ViewGroup.LayoutParams params, float x, float y){
        final EditText edittext = new EditText(getContext());
        edittext.setLayoutParams(params);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setMaxLines(1);
        edittext.setTextSize(TypedValue.COMPLEX_UNIT_PX, baseWidth * 0.04f);
        edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        edittext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.devpira_style1_edittext));
        edittext.setPadding(0,0,0,0);
        edittext.setX(baseWidth * x);
        edittext.setY(baseHeight * y);
        edittext.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edittext.setGravity(Gravity.CENTER);
        edittext.setTextColor(Color.BLACK);
        advanceHolder.addView(edittext);

        edittext.addTextChangedListener(textWatcher);
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (hasFocus() && i == EditorInfo.IME_ACTION_DONE) {
                    clearFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        return edittext;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int atPosition, int beforeLength, int addTextLength) {
            if(hexET.hasFocus() && !hexET.getText().toString().equals("")){
                String string = hexET.getText().toString();
                string.replace("#","");
                string = string.toLowerCase();
                if(!string.matches("^[a-fA-F0-9]*$"))
                    return;

                if(string.length() == 5 || string.length() == 7)
                    string = "0"+string;
                if(string.length() >8 || string.length() <6)
                    return;

                try {
                    setColor(Color.parseColor("#" + string));
                    advancedColorPicker.setSelectedColor(selectedColor);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }

            for(int position = 0; position <ETList.size();position++ ){
                if(!ETList.get(position).hasFocus() || ETList.get(position).getText().toString().equals(""))
                    continue;

                switch (position){
                    case AET:
                       ALPHA = Math.max(0,Math.min(255,Integer.valueOf(ETList.get(position).getText().toString())));
                        break;
                    case RET:
                        RGB[0] = Math.max(0,Math.min(255,Integer.valueOf(ETList.get(position).getText().toString())));
                        break;
                    case GET:
                        RGB[1] = Math.max(0,Math.min(255,Integer.valueOf(ETList.get(position).getText().toString())));
                        break;
                    case BET:
                        RGB [2] = Math.max(0,Math.min(255,Integer.valueOf(ETList.get(position).getText().toString())));
                        break;
                    case HET:
                        HSV[0] = Math.max(0f,Math.min(359.0f, Float.valueOf(ETList.get(position).getText().toString())));
                        break;
                    case SET:
                        HSV[1] = Math.max(0f,Math.min(1.0f, Float.valueOf(ETList.get(position).getText().toString())/100));
                        break;
                    case VET:
                        HSV[2] = Math.max(0f,Math.min(1.0f, Float.valueOf(ETList.get(position).getText().toString())/100));
                        break;
                }
                if(position <= 3 ){
                    setColor(Color.argb(ALPHA, RGB[0], RGB[1], RGB[2]));
                    advancedColorPicker.setSelectedColor(selectedColor);
                }else if(position > 3 && position <= 6) {
                    setColor(Color.HSVToColor(ALPHA,HSV));
                    advancedColorPicker.setSelectedColor(selectedColor);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private TextView createColorDetailTextView( float x, float y, String text) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, baseWidth * 0.05f);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setX(baseWidth * x);
        textView.setY(baseHeight * y);
        advanceHolder.addView(textView);
        return textView;
    }

    public void setColorPalettePickerListener(OnColorPalettePickerListener colorPalettePickerListener) {
        this.colorPalettePickerListener = colorPalettePickerListener;
    }

    public interface OnColorPalettePickerListener{

        void onColorSelected (int color);
    }
}
