# `PrimeDatePicker` :zap: 
  
**`PrimeDatePicker`** is a date picker tool.

![](static/prime_logo.png)
  
Download
--------
**`PrimeDatePicker`** is available on bintray to download using build tools systems. Add the following lines to your `build.gradle` file:

```gradle
repositories {
    jcenter()
}
  
dependencies {
    implementation 'com.aminography:primedatepicker:1.0.3'
}
```

Attributes
----------
#### PrimeMonthView

<table>

  <tr>
    <td><b>Attribute</b></td>
    <td><b>Type</b></td>
  </tr>
  
  <tr>
    <td><b>• calendarType</b></td>
    <td>enum</td>
  </tr>
  <tr>
    <td colspan="2"><i>civil, persian, hijri</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• todayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedDayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedDayCircleColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• disabledDayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTopPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelBottomPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTopPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelBottomPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelVerticalPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• showTwoWeeksInLandscape</b></td>
    <td>boolean</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
</table>

#### PrimeCalendarView

The `PrimeMonthView` attributes are accessible for `PrimeCalendarView` in addition to below ones.

<table>

  <tr>
    <td><b>Attribute</b></td>
    <td><b>Type</b></td>
  </tr>
  
  <tr>
    <td><b>• flingOrientation</b></td>
    <td>enum</td>
  </tr>
  <tr>
    <td colspan="2"><i>vertical, horizontal</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerThickness</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetLeft</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetRight</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetTop</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetBottom</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• loadFactor</b></td>
    <td>integer</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• maxTransitionLength</b></td>
    <td>integer</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
  <tr>
    <td><b>• transitionSpeedFactor</b></td>
    <td>float</td>
  </tr>
  <tr>
    <td colspan="2"><i>Descriptions</i></td>
  </tr>
  
</table>

License
--------
```
Copyright 2019 Mohammad Amin Hassani.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
