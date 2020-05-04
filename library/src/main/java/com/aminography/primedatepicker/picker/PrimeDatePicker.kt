package com.aminography.primedatepicker.picker

import android.content.DialogInterface
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.OnDayPickedListener
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.builder.RequestBuilder
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.dialog.PrimeDatePickerBottomSheet
import com.aminography.primedatepicker.picker.dialog.PrimeDatePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * `PrimeDatePicker` is an interface which defines common functionality for picker classes.
 *
 * @author aminography
 */
@Suppress("unused")
interface PrimeDatePicker {

    /**
     * A property to get current value of [PickType].
     */
    val pickType: PickType

    /**
     * Shows the picker which is a fragment too.
     *
     * @param manager The [FragmentManager] this fragment will be added to.
     * @param tag The tag for this fragment
     */
    fun show(manager: FragmentManager, tag: String?)

    /**
     * Set a listener to be invoked when the dialog is canceled.
     *
     * @param listener The [DialogInterface.OnCancelListener] to use.
     */
    fun setOnCancelListener(listener: DialogInterface.OnCancelListener?)

    /**
     * Set a listener to be invoked when the dialog is dismissed.
     *
     * @param listener The [DialogInterface.OnDismissListener] to use.
     */
    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?)

    /**
     * Set a callback to be invoked when the select button is clicked and the picked day(s) is a
     * valid day to pick.
     *
     * @param callback The [BaseDayPickCallback] to use.
     */
    fun setDayPickCallback(callback: BaseDayPickCallback?)

    /**
     * Set a listener to be invoked when a day is clicked which is a valid day to pick.
     *
     * @param listener The [OnDayPickedListener] to use.
     */
    fun setOnEachDayPickedListener(listener: OnDayPickedListener?)

    companion object {

        /**
         * The first step in builder mechanism to create an instance of `PrimeDatePicker` as a
         * [DialogFragment] is to call this function.
         *
         * @param initialDate The [PrimeCalendar] to use as the starting date. Also, some other
         * configurations to be reflected to the date picker are retrieved from it. Such as:
         * language locale, view direction, and firstDayOfWeek.
         */
        @Suppress("UNCHECKED_CAST")
        fun dialogWith(initialDate: PrimeCalendar): RequestBuilder<PrimeDatePicker> =
            RequestBuilder(
                PrimeDatePickerDialog::class.java,
                initialDate
            ) as RequestBuilder<PrimeDatePicker>

        /**
         * The first step in builder mechanism to create an instance of `PrimeDatePicker` as a
         * [BottomSheetDialogFragment] is to call this function.
         *
         * @param initialDate The [PrimeCalendar] to use as the starting date. Also, some other
         * configurations to be reflected to the date picker are retrieved from it. Such as:
         * language locale, view direction, and firstDayOfWeek.
         */
        @Suppress("UNCHECKED_CAST")
        fun bottomSheetWith(initialDate: PrimeCalendar): RequestBuilder<PrimeDatePicker> =
            RequestBuilder(
                PrimeDatePickerBottomSheet::class.java,
                initialDate
            ) as RequestBuilder<PrimeDatePicker>
    }

}