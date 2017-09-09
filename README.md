# Android Color Palette Picker
<b>Developed by: Pirashanth Suri (DEVPIRA)</b>

This is a simple to use color picker that can be included into any android project with ease. It has a simple component that allows less experienced users to select from an array of colors based off of the Android's Material design color palette. 

For more experienced users who wish to generate there own unique color, they can use the advance function that allows them to describe each value of a color.

<img src="https://github.com/devpira/Android-Color-Picker/blob/master/ColorPalettePicker_SS.png?raw=true" alt="basiccolorpicker" width="300">
<img src="https://github.com/devpira/Android-Color-Picker/blob/master/AdvanceColorPicker_SS.png?raw=true" alt="basiccolorpicker" width="300">

<h3>How to install</h3>
Add the following to your 'build.gradle' file in AndroidStudios:

``` 
gradle
dependencies {
    compile 'com.devpira.colorlibrary:AndroidColorPicker:1.0.0'
} 
```
<h3>How to use</h3>
To open up the Color Picker, simply add the following lines to your code:

 ```java        
ColorPalettePicker.open(context, parentLayout, new ColorPalettePicker.OnColorPalettePickerListener() {
       @Override
       public void onColorSelected(int color) {
                            //Do something with color
       }
 });
```

<h2>Limitations</h2>
<b>Verison 1.0.0:</b>
- Does not support landscape view
- Child views of the color picker cannot be customized or changed.

<h2>License</h2>

```
Copyright 2016 DEVPIRA
         
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
