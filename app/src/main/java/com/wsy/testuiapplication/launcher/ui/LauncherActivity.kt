package com.wsy.testuiapplication.launcher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.wsy.testuiapplication.DFRoundTextView
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.ChangeHeadIconActivity

/**
 * Created by WSY on 2020/2/17.
 */
class LauncherActivity : Activity() {

    var mBtn1: DFRoundTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)

        mBtn1 = findViewById(R.id.tv_watch_now)

        mBtn1?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@LauncherActivity, ChangeHeadIconActivity::class.java)
                startActivity(intent)
            }

        })
    }

}