# `PrimeDatePicker` :zap:
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PrimeDatePicker-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7743)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23367-red.svg)](http://androidweekly.net/issues/issue-367)
[![Bintray](https://img.shields.io/bintray/v/aminography/maven/PrimeDatePicker?label=Bintray)](https://bintray.com/aminography/maven/PrimeDatePicker/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c1c44ee8a3a14b0e8c963c36c8e586d8)](https://app.codacy.com/manual/aminography/PrimeDatePicker?utm_source=github.com&utm_medium=referral&utm_content=aminography/PrimeDatePicker&utm_campaign=Badge_Grade_Dashboard)
[![API](https://img.shields.io/badge/minSdkVersion-13-important.svg)](https://android-arsenal.com/api?level=13)

Firstly, **`PrimeDatePicker`** is a tool that provides picking a single day, multiple days, and a range of days. Secondly, you can use internal elements like `MonthView` and `CalendarView` as stand-alone views in your projects.
![](static/prime_logo.png)

<table>

  <tr>
    <td><b>Ex: Multiple Days</b><br/><i>Civil | BottomSheet | Dark</i></td>
    <td><b>Ex: Range of Days</b><br/><i>Persian | BottomSheet | Light</i></td>
    <td><b>Ex: Single Day</b><br/><i>Hijri | Dialog | Light</i></td>
    <td><b>Ex: Goto View</b><br/><i>Japanese | Dialog | Dark</i></td>
  </tr>

  <tr>
    <td><img src="static/MCDB.gif"/></td>
    <td><img src="static/RPLB.gif"/></td>
    <td><img src="static/SHLD.gif"/></td>
    <td><img src="static/GJDD2.gif"/></td>
  </tr>

</table>

<br/>

Table of Contents
-----------------

- [Core Logic](#core-logic)
- [Main Characteristics](#main-characteristics)
- [Download](#download)
- [Usage](#usage)
   - [Configurations Based on Input Calendar](#configurations-based-on-input-calendar)
   - [Customizing Theme](#customizing-theme)
   - [Customizing Texts](#customizing-texts)
- [Stand-Alone Views](#stand-alone-views)
- [Change Log](#change-log)

<br/>

Core Logic
----------
The ❤️ of this library is provided by [**PrimeCalendar**](https://github.com/aminography/PrimeCalendar).

<br/>

Main Characteristics
--------------------
- Endless Scrolling
- Fully Customizable Views & Themes
- Align With Material Design
- Fluent UI
- RTL Support
- Landscape Support
- Various Calendar Types
- Various Date Picking Strategies
- Dialog & BottomSheet Presentations
- Fast Goto

#### :dart: Download [SampleApp.apk](https://github.com/aminography/PrimeDatePicker/releases/download/v3.1.1/sample-app-release.apk)

<br/>

Download
--------
**`PrimeDatePicker`** is available on [bintray](https://bintray.com/aminography/maven/PrimeDatePicker) to download using build tools systems. Add the following lines to your `build.gradle` file:

```gradle
repositories {
    jcenter()
}
  
dependencies {
    implementation 'com.aminography:primedatepicker:3.3.1'
    implementation 'com.aminography:primecalendar:1.3.2'
}
```

<br/>

Usage
-----------------
To enjoy `PrimeDatePicker`, create an instance using a builder pattern, like the following snippets:

> Kotlin
```kotlin

val callback = MultipleDaysPickCallback { multipleDays ->
    // TODO
}

val themeFactory = DarkThemeFactory()

val today = CivilCalendar()  // To show a date picker with Civil dates, also today as the starting date

val datePicker = PrimeDatePicker.bottomSheetWith(today)  // or dialogWith(today)
        .pickMultipleDays(callback)        // Passing callback is optional, can be set later using setDayPickCallback()
        .minPossibleDate(minDateCalendar)  // Optional
        .maxPossibleDate(maxDateCalendar)  // Optional
        .disabledDays(disabledDaysList)    // Optional
        .firstDayOfWeek(Calendar.MONDAY)   // Optional
        .applyTheme(themeFactory)          // Optional
        .build()

datePicker.show(supportFragmentManager, "SOME_TAG")
```

<br/>

> Java
```java
SingleDayPickCallback callback = new SingleDayPickCallback() {
    @Override
    public void onSingleDayPicked(PrimeCalendar singleDay) {
        // TODO
    }
};

BaseThemeFactory themeFactory = new LightThemeFactory();

PrimeCalendar today = new JapaneseCalendar();  // To show a date picker with Japanese dates, also today as the starting date

PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)  // or bottomSheetWith(today)
    .pickSingleDay(callback)           // Passing callback is optional, can be set later using setDayPickCallback()
    .minPossibleDate(minDateCalendar)  // Optional
    .maxPossibleDate(maxDateCalendar)  // Optional
    .disabledDays(disabledDaysList)    // Optional
    .firstDayOfWeek(Calendar.MONDAY)   // Optional
    .applyTheme(themeFactory)          // Optional
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
                    .pickSingleDay(callback)
                     ...
                    .build()
```

<br/>

### Customizing Theme

`PrimeDatePicker` is fully customizable and you can tailor it to what you desire.
Almost everything is customizable. For example:
- text sizes & colors
- background & element colors
- padding & distances
- font typeface
- string formatter
- calendar animations & transition parameters
- *etc*

In this way, a set of theme factory class are provided to specify theme parameters.
By default, there are two concrete subclasses for the them factory:

- [`DarkThemeFactory`](library/src/main/java/com/aminography/primedatepicker/picker/theme/DarkThemeFactory.kt)
- [`LightThemeFactory`](library/src/main/java/com/aminography/primedatepicker/picker/theme/LightThemeFactory.kt)

You can override their parameters, or inherit a class from, or make your own theme factory.

Here is an example of how to override theme parameters in `Kotlin` as well as `Java`:

> Kotlin
```kotlin
val themeFactory = object : LightThemeFactory() {

    override val typefacePath: String?
        get() = "fonts/Roboto-Regular.ttf"
        
    override val calendarViewPickedDayInRangeBackgroundColor: Int
        get() = getColor(R.color.red100)

    override val calendarViewPickedDayInRangeLabelTextColor: Int
        get() = getColor(R.color.gray900)

    override val calendarViewWeekLabelFormatter: LabelFormatter
        get() = { primeCalendar ->
            when (primeCalendar[Calendar.DAY_OF_WEEK]) {
                Calendar.SATURDAY,
                Calendar.SUNDAY -> String.format("%s😍", primeCalendar.weekDayNameShort)
                else -> String.format("%s😁", primeCalendar.weekDayNameShort)
            }
        }

    override val calendarViewWeekLabelTextColors: SparseIntArray
        get() = SparseIntArray(7).apply {
            val red = getColor(R.color.red300)
            val green = getColor(R.color.green400)
            put(Calendar.SATURDAY, red)
            put(Calendar.SUNDAY, red)
            put(Calendar.MONDAY, green)
            put(Calendar.TUESDAY, green)
            put(Calendar.WEDNESDAY, green)
            put(Calendar.THURSDAY, green)
            put(Calendar.FRIDAY, green)
        }
        
     // Other customizations...
}
```

<table>

  <tr>
    <td><b>Result:</b></td>
  </tr>

  <tr>
    <td><img src="static/theming_result.png" width="400"/></td>
  </tr>

</table>

> Java
```java
BaseThemeFactory themeFactory = new LightThemeFactory() {
    
    @NotNull
    @Override
    public PrimeCalendarView.FlingOrientation getCalendarViewFlingOrientation() {
        return PrimeCalendarView.FlingOrientation.HORIZONTAL;
    }
    
    @Override
    public int getSelectionBarBackgroundColor() {
        return super.getColor(R.color.green300);
    }
    
    // Other customizations...
};
```

<br/>

### Customizing Texts

If you want to change some texts in `PrimeDatePicker`, such as a button text, the current solution is to
define some strings in your project's `strings.xml` with equal name defined in the library's `strings.xml`, to override them.

<br/>

Stand-Alone Views
-----------------

To see how to use **`PrimeMonthView`** & **`PrimeCalendarView`**, [refer to wiki page](https://github.com/aminography/PrimeDatePicker/wiki#stand-alone-views) .

<br/>

Change Log
----------

The change log is available [here](https://github.com/aminography/PrimeDatePicker/wiki/Change-Log).

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
