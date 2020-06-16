package com.aminography.primedatepicker.monthview

/**
 * @author aminography
 */
enum class DayState {
    PICKED_SINGLE,
    START_OF_RANGE_SINGLE,
    START_OF_RANGE,
    IN_RANGE,
    END_OF_RANGE,
    NORMAL,
    DISABLED,
    OUT_OF_VALID_RANGE,
    BESIDE_MONTH
}