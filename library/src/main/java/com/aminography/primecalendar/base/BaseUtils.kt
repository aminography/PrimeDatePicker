package com.aminography.primecalendar.base

import java.util.*

/**
 * @author aminography
 */

const val delimiter = "/"

fun normalize(i: Int): String = String.format(Locale.getDefault(), if (i < 9) "0%d" else "%d", i)
