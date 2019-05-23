package com.aminography.primecalendar.common

import com.aminography.primecalendar.base.delimiter

/**
 * @author aminography
 */
internal data class DateHolder(
        var year: Int,
        var month: Int,
        var day: Int
) {

    override fun toString(): String = "$year$delimiter$month$delimiter$day"

}