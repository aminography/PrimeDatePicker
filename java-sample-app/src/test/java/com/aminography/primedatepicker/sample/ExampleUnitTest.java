package com.aminography.primedatepicker.sample;

import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primecalendar.hijri.HijriCalendar;
import com.aminography.primecalendar.persian.PersianCalendar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void persianToCivilConversion() {
        PersianCalendar persian = new PersianCalendar();
        persian.setDate(1370, 3, 15);
        System.out.println("Persian Date: " + persian.getLongDateString());

        CivilCalendar civil = persian.toCivil();
        System.out.println("Civil Date: " + civil.getLongDateString());

        assertEquals(civil.getYear(), 1991);
        assertEquals(civil.getMonth(), 6);
        assertEquals(civil.getDayOfMonth(), 6);
    }

    @Test
    public void persianToHijriConversion() {
        PersianCalendar persian = new PersianCalendar();
        persian.setDate(1370, 3, 15);
        System.out.println("Persian Date: " + persian.getLongDateString());

        HijriCalendar hijri = persian.toHijri();
        System.out.println("Hijri Date: " + hijri.getLongDateString());

        assertEquals(hijri.getYear(), 1411);
        assertEquals(hijri.getMonth(), 11);
        assertEquals(hijri.getDayOfMonth(), 23);
    }

}