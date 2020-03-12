package com.aminography.primedatepicker.tools

/**
 * @author aminography
 */
object PersianUtils {

    fun convertLatinDigitsToPersian(input: String): String =
            input.replace('0', '\u06f0')
                    .replace('1', '\u06f1')
                    .replace('2', '\u06f2')
                    .replace('3', '\u06f3')
                    .replace('4', '\u06f4')
                    .replace('5', '\u06f5')
                    .replace('6', '\u06f6')
                    .replace('7', '\u06f7')
                    .replace('8', '\u06f8')
                    .replace('9', '\u06f9')

}
