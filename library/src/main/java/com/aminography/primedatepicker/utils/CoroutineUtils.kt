@file:Suppress("unused")

package com.aminography.primedatepicker.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author aminography
 */

internal val IO: CoroutineDispatcher = Dispatchers.IO
internal val DEFAULT: CoroutineDispatcher = Dispatchers.Default
internal val MAIN: CoroutineDispatcher = Dispatchers.Main