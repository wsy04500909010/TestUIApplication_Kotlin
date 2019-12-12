package com.wsy.testuiapplication.kotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.bean2.ForcastBean
import org.jetbrains.anko.find

class ForecastListAdapter(val context: Context, val items: List<ForcastBean>, val itemClick: ForecastListAdapter.OnItemClickListener) : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {


    public interface OnItemClickListener {
        operator fun invoke(forecast: ForcastBean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {

//        return ViewHolder(TextView(context))

        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(view, itemClick)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textView.text = items[position]

        holder.bindForecast(items[position])
    }


    class ViewHolder(view: View, val itemClick: OnItemClickListener) :
            RecyclerView.ViewHolder(view) {
        private val iconView: ImageView
        private val dateView: TextView
        private val descriptionView: TextView
        private val maxTemperatureView: TextView
        private val minTemperatureView: TextView

        init {
            iconView = view.find(R.id.icon)
            dateView = view.find(R.id.date)
            descriptionView = view.find(R.id.description)
            maxTemperatureView = view.find(R.id.maxTemperature)
            minTemperatureView = view.find(R.id.minTemperature)
        }

        fun bindForecast(forecast: ForcastBean) {
            with(forecast) {
                //                Picasso.with(itemView.ctx).load(iconUrl).into(iconView)
                dateView.text = "$cityName"
                descriptionView.text = "这里先写死"
                maxTemperatureView.text = "${high.toString()}"
                minTemperatureView.text = "${low.toString()}"
                itemView.setOnClickListener { itemClick(forecast) }
            }
        }
    }
}