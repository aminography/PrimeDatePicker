# `PrimeDatePicker` <:zap: 
  
First, **`PrimeDatePicker`** is a>> date picker tool. It provides picking a single day in addition to a range of days. Second, it is possible to use its `MonthView` and `CalendarView` as stand alone views in your project.

![](static/prime_logo.png)

<center>

| Pick a Single Day | Pick a Range of Days |
| --- | --- |
| ![](static/picking_single.gif) | ![](static/picking_range.gif) | 

</center>

<br/>

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

<br/>

Usage
-----------------
Descriptions

<br/>

Stand Alone Views
-----------------
In addition to use **`PrimeDatePicker`** as a date picker tool, it is possible to employ stand alone views in your project. 
They are `PrimeMonthView` & `PrimeCalendarView` which can be used in layout `xml` files or instantiated programmatically.

<br/>

### 1. XML & View Attributes
Descriptions

```xml
<com.aminography.primedatepicker.monthview.PrimeMonthView
    android:id="@+id/monthView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp" />
```

```xml
<com.aminography.primedatepicker.calendarview.PrimeCalendarView
    android:id="@+id/calendarView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

<br/>

### 1.1. Common attributes for `PrimeMonthView` & `PrimeCalendarView`:

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
    <td colspan="2"><i>Specifies the calendar type which is shown by this view. Its possible values are: <b>civil</b>, <b>persian</b>, <b>hijri</b>.</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of month name label.</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of week day name labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of day number labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• todayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of day number label which belongs to today.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedDayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of day number labels when they are picked.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedDayCircleColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the background color of day numbers when they are picked.</i></td>
  </tr>
  
  <tr>
    <td><b>• disabledDayLabelTextColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text color of day number labels when they are disabled.</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text size of month name label.</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text size of week day name labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelTextSize</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the text size of day number labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelTopPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the top padding of month name label.</i></td>
  </tr>
  
  <tr>
    <td><b>• monthLabelBottomPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the bottom padding of month name label.</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelTopPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the top padding of week day name labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• weekLabelBottomPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the bottom padding of week day name labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• dayLabelVerticalPadding</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the vertical padding (top and bottom) of day number labels.</i></td>
  </tr>
  
  <tr>
    <td><b>• showTwoWeeksInLandscape</b></td>
    <td>boolean</td>
  </tr>
  <tr>
    <td colspan="2"><i>When in sets <b>true</b>, month view shows two weeks (14 days) in each row for landscape screen orientation.</i></td>
  </tr>
  
</table>

<br/>

### 1.2. `PrimeCalendarView` Specific Attributes:

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
    <td colspan="2"><i>Specifies the fling orientation of calendar view. Its possible values are: <b>vertical</b>, <b>horizontal</b>.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerColor</b></td>
    <td>color</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the color of divider lines separating month views.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerThickness</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the thickness of divider lines separating month views.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetLeft</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the left margin of divider lines when the fling orientation is vertical.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetRight</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the right margin of divider lines when the fling orientation is vertical.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetTop</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the top margin of divider lines when the fling orientation is horizontal.</i></td>
  </tr>
  
  <tr>
    <td><b>• dividerInsetBottom</b></td>
    <td>dimension</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the bottom margin of divider lines when the fling orientation is horizontal.</i></td>
  </tr>
  
  <tr>
    <td><b>• loadFactor</b></td>
    <td>integer</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the number of month to be load in pagination.</i></td>
  </tr>
  
  <tr>
    <td><b>• maxTransitionLength</b></td>
    <td>integer</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the maximum number of month that are shown between current and target in transitions. It's used when <b>goto</b> method has called.</i></td>
  </tr>
  
  <tr>
    <td><b>• transitionSpeedFactor</b></td>
    <td>float</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the speed factor of scrolling in transitions. It's used when <b>goto</b> method has called.</i></td>
  </tr>
  
</table>

<br/>

### Customize Texts in Date Picker Bottom Sheet
If you want to change the texts of the date picker bottom sheet, define strings in your project's `strings.xml` with equal names defined in library's [`strings.xml`](https://github.com/aminography/PrimeDatePicker/blob/master/library/src/main/res/values/strings.xml), to override them.

<br/>

Third Party Libraries
---------------------
**• PrimeCalendar** (https://github.com/aminography/PrimeCalendar)

**• PrimeAdapter** (https://github.com/aminography/PrimeAdapter)

<br/>

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
