package com.aminography.primedatepicker.picker

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.picker.builder.RequestBuilder
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.dialog.PrimeDatePickerBottomSheet
import com.aminography.primedatepicker.picker.dialog.PrimeDatePickerDialog

/**
 * @author aminography
 */
@Suppress("unused")
interface PrimeDatePicker {

    val pickType: PickType

    fun show(manager: FragmentManager, tag: String?)

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener?)

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?)

    fun setDayPickCallback(callback: BaseDayPickCallback?)

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun dialogWith(initialDate: PrimeCalendar): RequestBuilder<PrimeDatePicker> =
            RequestBuilder(
                PrimeDatePickerDialog::class.java,
                initialDate
            ) as RequestBuilder<PrimeDatePicker>

        @Suppress("UNCHECKED_CAST")
        fun bottomSheetWith(initialDate: PrimeCalendar): RequestBuilder<PrimeDatePicker> =
            RequestBuilder(
                PrimeDatePickerBottomSheet::class.java,
                initialDate
            ) as RequestBuilder<PrimeDatePicker>
    }

}