package com.wsy.testuiapplication.kotlin

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.adapter.ForecastListAdapter
import com.wsy.testuiapplication.kotlin.bean.Animal
import com.wsy.testuiapplication.kotlin.bean.ForcastBean
import kotlinx.android.synthetic.main.activity_kotlin_api.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL
import com.wsy.testuiapplication.kotlin.bean2.ForcastBean as ModelBean

class KotlinAPIActivity : AppCompatActivity() {

    companion object {
        private val TAG = "KotlinAPIActivity"
    }

    var forecastList: MutableList<ModelBean> = ArrayList()


    //内联函数  内联函数会把我们调用这个函数的地方替换掉,所以它不需要为此生成一个内部的对象,节省资源
    //这是一个高阶函数  所谓高阶函数 就是使用一个（子）函数（方法）当做另一个（主）函数的参数或者返回值
    //这里就是需要传进来一个带有一个Int类型参数的函数作为本函数的 参数，并起个别名为code，通过判断后，通过code（2）执行参数传过来的（子）函数

    inline fun supportsLollipop(code: (Int) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            code(2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_api)

//        getDataFromAPI()

        rv.layoutManager = LinearLayoutManager(this)
//        rv.adapter = ForecastListAdapter(this, forecastList, object : ForecastListAdapter.OnItemClickListener {
//            override fun invoke(forecast: ModelBean) {
//                Log.d(TAG, forecast.cityName + "===" + forecast.high + "===" + forecast.low)
//            }
//
//        })
        rv.adapter = ForecastListAdapter(this, forecastList) { it ->
            toast(it.cityName)
            Log.d(TAG, it.cityName + "===" + it.high + "===" + it.low)
            66//返回值放最后一行 测试用 无实际意义
        }
        getDataFromAPItoGson()

        supportsLollipop { number ->
            toast(number.toString())
        }
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
                var modelBean: ModelBean = DataMapper().filterData(result)
                forecastList.clear()
                forecastList.add(modelBean)
                rv.adapter?.notifyDataSetChanged()
            }

        }
    }


}