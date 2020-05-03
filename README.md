# `PrimeDatePicker` :zap:
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PrimeDatePicker-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7743)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23367-red.svg)](http://androidweekly.net/issues/issue-367)
[![Download](https://api.bintray.com/packages/aminography/maven/PrimeDatePicker/images/download.svg) ](https://bintray.com/aminography/maven/PrimeDatePicker/_latestVersion)
  
First, **`PrimeDatePicker`** is a tool which provides picking a single day, multiple days, and a range of days. Second, you can use its `MonthView` and `CalendarView` as stand-alone views in your projects.
![](static/prime_logo.png)

<table>

  <tr>
    <td><b>Picking Multiple Days</b><br/><i>Example for Civil</i></td>
    <td><b>Picking a Range of Days</b><br/><i>Example for Persian</i></td>
    <td><b>Picking a Single Day</b><br/><i>Example for Japanese</i></td>
    <td><b>Goto View</b><br/><i>Example for Civil</i></td>
  </tr>

  <tr>
    <td><img src="static/pick_multiple.gif"/></td>
    <td><img src="static/pick_range.gif"/></td>
    <td><img src="static/pick_single.gif"/></td>
    <td><img src="static/goto.gif"/></td>
  </tr>

</table>

<br/>

Core Logic
----------
The ❤️ of this library is provided by [**PrimeCalendar**](https://github.com/aminography/PrimeCalendar).

<br/>

Main Features
-------------
- Endless scrolling
- Customizable views & theme
- Fluent UI
- Various calendar types
- Various pick strategies
- Showing Dialog & BottomSheet

<br/>

Download
--------
**`PrimeDatePicker`** is available on [bintray](https://bintray.com/aminography/maven/PrimeDatePicker) to download using build tools systems. Add the following lines to your `build.gradle` file:

```gradle
repositories {
    jcenter()
}
  
dependencies {
    implementation 'com.aminography:primedatepicker:3.0.0'
    implementation 'com.aminography:primecalendar:1.2.18'
}
```

<br/>

Usage
-----------------
To enjoy `PrimeDatePicker`, create an instance of it using builder pattern, like the following snippets:

> Kotlin
```kotlin

val multipleDaysPickCallback = MultipleDaysPickCallback { multipleDays ->
    // TODO
}

val themeFactory = DarkThemeFactory()

val today = CivilCalendar()  // Causes a Civil date picker, also today as the starting date

val datePicker = PrimeDatePicker.bottomSheetWith(today) // or dialogWith(today)
        .pickMultipleDays(multipleDaysPickCallback)  // Passing callback is optional, can be set later using setDayPickCallback()
        .minPossibleDate(minDateCalendar)            // Optional
        .maxPossibleDate(maxDateCalendar)            // Optional
        .disabledDays(disabledDaysList)              // Optional
        .firstDayOfWeek(Calendar.MONDAY)             // Optional
        .applyTheme(themeFactory)                    // Optional
        .build()

datePicker.show(supportFragmentManager, "SOME_TAG")
```

<br/>

> Java
```java
SingleDayPickCallback singleDayPickCallback = new SingleDayPickCallback() {
    @Override
    public void onSingleDayPicked(PrimeCalendar singleDay) {
        // TODO
    }
};

BaseThemeFactory themeFactory = new LightThemeFactory();

PrimeCalendar today = new JapaneseCalendar();  // Causes a Japanese date picker, also today as the starting date

PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today) // or bottomSheetWith(today)
    .pickSingleDay(singleDayPickCallback)  // Passing callback is optional, can be set later using setDayPickCallback()
    .minPossibleDate(minDateCalendar)      // Optional
    .maxPossibleDate(maxDateCalendar)      // Optional
    .disabledDays(disabledDaysList)        // Optional
    .firstDayOfWeek(Calendar.MONDAY)       // Optional
    .applyTheme(themeFactory)              // Optional
    .build();

datePicker.show(getSupportFragmentManager(), "SOME_TAG");
```

<br/>

### Configurations Based on Input Calendar

`PrimeDatePicker` reads some configurations from the input calendar, so they are reflected to the date picker. For example:

```kotlin
// shows a Persian calendar, but in English language which leads to LTR direction
val calendar = PersianCalendar(Locale.ENGLISH).also {
    it.year = 1398                       // customizes starting year
    it.month = 7                         // customizes starting month
    it.firstDayOfWeek = Calendar.MONDAY  // sets first day of week to Monday
}

val datePicker = PrimeDatePicker.bottomSheetWith(calendar)
                    .pickSingleDay(singleDayPickCallback)
                     ...
                    .build()
```

<br/>

### Customizing Texts in Date Picker Bottom Sheet

If you want to change the texts of the date picker bottom sheet, define some strings in your project's `strings.xml` with equal name defined in the library's [`strings.xml`](https://github.com/aminography/PrimeDatePicker/blob/master/library/src/main/res/values/strings.xml), to override them.

<br/>
<hr/>

Stand-Alone Views
-----------------
In addition to use **`PrimeDatePicker`** as a date picker tool, it is possible to employ stand-alone views in your project.
They are **`PrimeMonthView`** & **`PrimeCalendarView`** which can be used in layout `xml` files or instantiated programmatically.
For example:

> xml
```xml
<com.aminography.primedatepicker.monthview.PrimeMonthView
    android:id="@+id/monthView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp" />
```

> Programmatically
```kotlin
val monthView = PrimeMonthView(context)
```

<br/>

### 1. XML Attributes
Applying customization has been provided for `PrimeMonthView` and `PrimeCalendarView` by using `xml` attributes as well as setting them programmatically.
For example:

> xml
```xml
<com.aminography.primedatepicker.calendarview.PrimeCalendarView
    android:id="@+id/calendarView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:pickedDayCircleColor="#0000FF" />
```

> Programmatically
```kotlin
calendarView.pickedDayCircleColor = Color.BLUE
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
    <td colspan="2"><i>Specifies the calendar type which is shown by this view. Its possible values are: <b>civil</b>, <b>persian</b>, <b>hijri</b>, and <b>japanese</b>.</i></td>
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
    <td colspan="2"><i>When it sets <b>true</b>, month view shows two weeks (14 days) in each row for landscape screen orientation.</i></td>
  </tr>
  
  <tr>
    <td><b>• animateSelection</b></td>
  <td>boolean</td>
  </tr>
  <tr>
    <td colspan="2"><i>When it sets <b>true</b>, selected day/days will appear with animation.</i></td>
  </tr>
  
  <tr>
    <td><b>• animationDuration</b></td>
  <td>integer</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the duration of day selection animation.</i></td>
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
    <td colspan="2"><i>Specifies the number of month to be load in pagination (endless scrolling).</i></td>
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

### 2. Other Variables (Common for `PrimeMonthView` & `PrimeCalendarView`)
These variables are only accessible programmatically to get or set. (Available by getter and setter methods in java)

<table>

  <tr>
    <td><b>Variable</b></td>
    <td><b>Type</b></td>
  </tr>
  
  <tr>
    <td><b>• typeface</b></td>
    <td>Typeface</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the typeface of showing texts.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedSingleDayCalendar</b></td>
    <td>PrimeCalendar</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the single picked date.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedRangeStartCalendar</b></td>
    <td>PrimeCalendar</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the start date of the picked range.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickedRangeEndCalendar</b></td>
    <td>PrimeCalendar</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the end date of the picked range.</i></td>
  </tr>

  <tr>
    <td><b>• pickedMultipleDaysList</b></td>
    <td>List&lt;PrimeCalendar&gt;</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the list of multiple picked dates.</i></td>
  </tr>

  <tr>
    <td><b>• minDateCalendar</b></td>
    <td>PrimeCalendar</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the minimum feasible date of the view.</i></td>
  </tr>
  
  <tr>
    <td><b>• maxDateCalendar</b></td>
    <td>PrimeCalendar</td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the maximum feasible date of the view.</i></td>
  </tr>
  
  <tr>
    <td><b>• pickType</b></td>
    <td><a href="https://github.com/aminography/PrimeDatePicker/blob/master/library/src/main/java/com/aminography/primedatepicker/common/PickType.kt">PickType</a></td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the date picking type of the view. Its possible values are: <b>SINGLE</b>, <b>RANGE_START</b>, <b>RANGE_END</b>, <b>MULTIPLE</b>, <b>NOTHING</b>.</i></td>
  </tr>
  
  <tr>
    <td><b>• animationInterpolator</b></td>
  <td><a href="https://developer.android.com/reference/android/view/animation/Interpolator">Interpolator</a></td>
  </tr>
  <tr>
    <td colspan="2"><i>Specifies the interpolator of day selection animation.</i></td>
  </tr>

</table>

<br/>

### 3. Listener (Common for `PrimeMonthView` & `PrimeCalendarView`)
You can listen to day picking actions by setting an instance of [OnDayPickedListener](https:///aminography/PrimeDatePicker/blob/master/library/src/main/java/com/aminography/primedatepicker/common/OnDayPickedListener.kt) to the views.
For example:

```kotlin
monthView.onDayPickedListener = object : OnDayPickedListener {

    override fun onDayPicked(pickType: PickType, 
                             singleDay: PrimeCalendar?, 
                             startDay: PrimeCalendar?, 
                             endDay: PrimeCalendar?,
                             multipleDays: List<PrimeCalendar>?) {
        // TODO
    }
}
```

<br/>

Locale
-------------------------------------------
`PrimeMonthView` and `PrimeCalendarView` (consequently `PrimeDatePicker`) have been implemented with localization capabilities.
By choosing locale for the `PrimeCalendar` instance which is passed to `goto` method, or by setting it directly to the views, you can  localize names, digits, and layout direction.

#### Localization example for `PrimeMonthView` using `PersianCalendar`:

<br/>

<table>

  <tr>
    <td><b>monthView.goto(PersianCalendar())</b><br/><i>// or</i><br/><b>monthView.locale = Locale("fa")</b></td>
    <td><b>monthView.goto(PersianCalendar(Locale.ENGLISH))</b><br/><i>// or</i><br/><b>monthView.locale = Locale.ENGLISH</b></td>
  </tr>

  <tr>
    <td><img src="static/month_persian_fa.jpg"/></td>
    <td><img src="static/month_persian_en.jpg"/></td>
  </tr>

</table>

<br/>

#### Localization example for `PrimeCalendarView` using `HijriCalendar`:

<br/>

<table>

  <tr>
    <td><b>calendarView.goto(HijriCalendar())</b><br/><i>// or</i><br/><b>calendarView.locale = Locale("ar")</b></td>
    <td><b>calendarView.goto(HijriCalendar(Locale.ENGLISH))</b><br/><i>// or</i><br/><b>calendarView.locale = Locale.ENGLISH</b></td>
  </tr>

  <tr>
    <td><img src="static/calendar_hijri_ar.jpg"/></td>
    <td><img src="static/calendar_hijri_en.jpg"/></td>
  </tr>

</table>

<br/>

Change Log
----------
### Version 3.0.0
- Builder mechanism has changed a bit.
- Adding full customization ability using a user configurable theme factory.
- Ability to show the date picker as a dialog using `PrimeDatePicker.dialogWith` method as well as `PrimeDatePicker.bottomSheetWith` to show a bottom sheet.
- Ability to specify a list of disabled days.

### Version 2.0.0
- Builder mechanism has changed.
- Picking multiple days has better UX.
- Adding Goto by tapping on Month-Year.
- Picking animation has improved.
- Possibility to change start day of week.
- Adding RTL support in bottom sheet.

### Version 1.1.0
- Migrated to AndroidX

### Version 1.0.16
- Ability to pick multiple days using `MULTIPLE` <a href="https://github.com/aminography/PrimeDatePicker/blob/master/library/src/main/java/com/aminography/primedatepicker/PickType.kt">PickType</a>.

### Version 1.0.15
- A minor bug is fixed.

### Version 1.0.14
- Animation for selected days using `animateSelection`, `animationDuration`, and `animationInterpolator` attributes.

<br/>

Third-Party Libraries
---------------------
**• PrimeCalendar** (<https://github.com/aminography/PrimeCalendar>)

**• PrimeAdapter** (<https://github.com/aminography/PrimeAdapter>)

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
