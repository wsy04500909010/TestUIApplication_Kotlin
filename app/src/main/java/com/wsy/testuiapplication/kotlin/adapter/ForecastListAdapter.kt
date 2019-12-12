package com.wsy.testuiapplication.kotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.bean2.ForcastBean
import org.jetbrains.anko.find

class ForecastListAdapter(val context: Context, val items: List<ForcastBean>, val listener: OnItemClickListener)
    : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        operator fun invoke(forecast: ForcastBean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        var rootView: View = LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(rootView, listener)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecastData(items[position])
    }


    class ViewHolder(view: View, itemClick: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        var date: TextView? = null
        var high: TextView? = null
        var low: TextView? = null
        var listener: OnItemClickListener? = null

        init {
            date = view.find(R.id.date)
            high = view.find(R.id.maxTemperature)
            low = view.find(R.id.minTemperature)
            listener = itemClick
        }

        fun bindForecastData(forecast: ForcastBean) {
            date?.text = forecast.cityName
            high?.text = forecast.high.toString()
            low?.text = forecast.low.toString()
            itemView.setOnClickListener {
                listener?.invoke(forecast)
            }
        }

    }
}