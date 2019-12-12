package com.wsy.testuiapplication.kotlin

import android.util.Log
import com.google.gson.Gson
import com.wsy.testuiapplication.kotlin.bean.ForcastBean
import java.net.URL

public class ForcastRequest(val cityCode: String) {

    companion object {
        private val APP_ID = "b6907d289e10d714a6e88b30761fae22"
        private val URL = "https://samples.openweathermap.org/data/2.5/weather"
        private val COMPLETE_URL = "$URL?APPID=$APP_ID&q="
    }

    public fun run(): ForcastBean {
        val url = COMPLETE_URL + cityCode
        val forecastJsonStr = URL(url).readText()
        Log.d(javaClass.simpleName, forecastJsonStr)

        return Gson().fromJson(forecastJsonStr, ForcastBean::class.java)

    }
}