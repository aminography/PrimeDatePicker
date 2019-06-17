package com.aminography.primedatepicker.tools

/**
 * @author aminography
 */
object PersianUtils {

    fun convertLatinDigitsToPersian(input: String): String {
        var str = input
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
    }

}
