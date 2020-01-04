package com.aminography.foursquareapp.presentation.ui.recommendations.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.aminography.foursquareapp.R
import com.aminography.foursquareapp.core.tools.splitVenueName
import com.aminography.foursquareapp.domain.model.VenueItemModel
import com.aminography.foursquareapp.presentation.ui.base.BaseAdapter
import com.aminography.foursquareapp.presentation.ui.loadImage
import kotlinx.android.synthetic.main.list_item_venue.view.*
import java.util.*

/**
 * @author aminography
 */
@SuppressLint("InflateParams")
class VenueItemViewHolder(context: Context) :
    BaseAdapter.BaseViewHolder(
        LayoutInflater.from(context).inflate(R.layout.list_item_venue, null)
    ) {

    override fun <DH> bindDataToView(dataHolder: DH) {
        if (dataHolder is VenueItemModel) {
            with(itemView) {
                val name = splitVenueName(dataHolder.name).first
                nameTextView.text = name

                distanceTextView.text = if (dataHolder.address.isNullOrEmpty()) {
                    dataHolder.distance
                } else {
                    String.format(Locale.getDefault(), "%s,", dataHolder.distance)
                }

                addressTextView.text = dataHolder.address
                addressTextView.visibility =
                    if (dataHolder.address.isNullOrEmpty()) View.GONE
                    else View.VISIBLE

                verifiedImageView.visibility =
                    if (dataHolder.verified) View.VISIBLE
                    else View.GONE

                imageView.loadImage(
                    imageUrl = dataHolder.categoryIcon,
                    crossFade = true
                )
            }
        }
    }

}