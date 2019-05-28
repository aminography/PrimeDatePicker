package com.aminography.primedatepicker.tools

/**
 * Created by Amin on 3/11/2018.
 */
object PersianUtils {

    private val PERSIAN_DIGITS = charArrayOf(
            ('0'.toInt() + 1728).toChar(),
            ('1'.toInt() + 1728).toChar(),
            ('2'.toInt() + 1728).toChar(),
            ('3'.toInt() + 1728).toChar(),
            ('4'.toInt() + 1728).toChar(),
            ('5'.toInt() + 1728).toChar(),
            ('6'.toInt() + 1728).toChar(),
            ('7'.toInt() + 1728).toChar(),
            ('8'.toInt() + 1728).toChar(),
            ('9'.toInt() + 1728).toChar()
    )

    fun convertDigitsToLatin(input: String?): String? {
        input?.let {
            var str = it
            str = str.replace('\u0660', '0')
            str = str.replace('\u0661', '1')
            str = str.replace('\u0662', '2')
            str = str.replace('\u0663', '3')
            str = str.replace('\u0664', '4')
            str = str.replace('\u0665', '5')
            str = str.replace('\u0666', '6')
            str = str.replace('\u0667', '7')
            str = str.replace('\u0668', '8')
            str = str.replace('\u0669', '9')

            str = str.replace('\u06f0', '0')
            str = str.replace('\u06f1', '1')
            str = str.replace('\u06f2', '2')
            str = str.replace('\u06f3', '3')
            str = str.replace('\u06f4', '4')
            str = str.replace('\u06f5', '5')
            str = str.replace('\u06f6', '6')
            str = str.replace('\u06f7', '7')
            str = str.replace('\u06f8', '8')
            str = str.replace('\u06f9', '9')
            return str
        } ?: return null
    }

    fun convertLatinDigitsToPersian(input: String?): String? {
        input?.let {
            var str = it
            str = str.replace('0', '\u06f0')
            str = str.replace('1', '\u06f1')
            str = str.replace('2', '\u06f2')
            str = str.replace('3', '\u06f3')
            str = str.replace('4', '\u06f4')
            str = str.replace('5', '\u06f5')
            str = str.replace('6', '\u06f6')
            str = str.replace('7', '\u06f7')
            str = str.replace('8', '\u06f8')
            str = str.replace('9', '\u06f9')
            return str
        } ?: return null
    }

    fun convertToPersianDigits(num: Int): String {
        val number = Integer.toString(num)
        val sb = StringBuilder()
        for (i in number.toCharArray()) {
            if (Character.isDigit(i)) {
                sb.append(PERSIAN_DIGITS[Integer.parseInt(i.toString())])
            } else {
                sb.append(i)
            }
        }
        return sb.toString()
    }

    fun convertTimeDigitsToPersianDigits(num: Int): String {
        val number = Integer.toString(num)
        val sb = StringBuilder()
        for (i in number.toCharArray()) {
            if (Character.isDigit(i)) {
                sb.append(PERSIAN_DIGITS[Integer.parseInt(i.toString())])
            } else {
                sb.append(i)
            }
        }
        return if (sb.length == 1) {
            convertLatinDigitsToPersian("0") + sb.toString()
        } else {
            sb.toString()
        }
    }

}
