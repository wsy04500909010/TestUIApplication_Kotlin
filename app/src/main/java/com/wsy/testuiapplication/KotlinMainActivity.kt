package com.wsy.testuiapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.wsy.testuiapplication.kotlin.KotlinAPIActivity
import com.wsy.testuiapplication.kotlin.adapter.MainListAdapter
import com.wsy.testuiapplication.kotlin.bean.Animal
import com.wsy.testuiapplication.kotlin.bean.Person
import com.wsy.testuiapplication.util.Slog
import kotlinx.android.synthetic.main.activity_kotlin_main.*

class KotlinMainActivity : AppCompatActivity() {

    //    private val mRecyclerView: RecyclerView? = null;
//    init {
//
//    }
    companion object {
        private val TAG: String = "KotlinMainActivity"
    }

    private val items = listOf(
            "Mon 6/23 - Sunny - 31/17",
            "Tue 6/24 - Foggy - 21/8",
            "Wed 6/25 - Cloudy - 22/17",
            "Thurs 6/26 - Rainy - 18/11",
            "Fri 6/27 - Foggy - 21/10",
            "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
            "Sun 6/29 - Sunny - 20/7"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        toast("ceshi")
//        val tb = TestBean("aaa")
//        toast(tb.name)
        val person = Person("名字", "姓")
        toast(person.name + "\n" + person.surname)
        setContentView(R.layout.activity_kotlin_main)
        initViews()

    }

    private fun initViews() {

        val mRecyclerView = findViewById(R.id.rv) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mRecyclerView.adapter = MainListAdapter(this, items)

        btn_request?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                startActivity(Intent(this@KotlinMainActivity, KotlinAPIActivity::class.java))
            }

        })
        //这里使用对Animal类的扩展参数
        Animal("喵喵").newFunction("newFunctionText")
    }


    fun toast(content: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, "写死的内容===$content", length).show()
        Slog.e("KotlinMainActivity", "$content")
    }

//    fun niceToast(message:String,
//                  tag:String  = javaClass<KotlinMainActivity>().getSimpleName,
//                  length: Int =Toast.LENGTH_SHORT){
//
//    }

    fun Animal.newFunction(text: String) {
        Log.d(TAG, text + "====" + this.getRandomNumber() + "name=$name")
    }


}