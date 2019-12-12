package com.wsy.testuiapplication.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.bean.ForcastBean
import kotlinx.android.synthetic.main.activity_kotlin_api.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class KotlinAPIActivity : AppCompatActivity() {

    companion object {
        private val TAG = "KotlinAPIActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_api)

//        getDataFromAPI()
        rv.layoutManager = LinearLayoutManager(this)
        //todo
//        rv.adapter = ForecastListAdapter(this,)
        getDataFromAPItoGson()
    }


    fun getDataFromAPI(): String? {
        var forecastJsonStr: String? = ""

        //荒废的老方法
//        async() {
//
//        }
        doAsync {
            val url = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22"
//            Request(url).run()
//            uiThread { longToast("Request performed") }
            forecastJsonStr = URL(url).readText()
            Log.d(javaClass.simpleName, forecastJsonStr)
            uiThread {
                toast(forecastJsonStr!!)
            }
        }

        return forecastJsonStr
    }

    fun getDataFromAPItoGson() {
        doAsync {

            val cityCode = "London,uk"
            var result: ForcastBean = ForcastRequest(cityCode).run()
            Log.d(TAG, result.name + "\n" + result.cod)

            uiThread {

            }

        }
    }

}