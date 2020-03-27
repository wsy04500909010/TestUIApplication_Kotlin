package com.wsy.testuiapplication.launcher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.view.View
import com.wsy.testuiapplication.DFRoundTextView
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.ChangeHeadIconActivity
import com.wsy.testuiapplication.net.ImageUploadManager

/**
 * Created by WSY on 2020/2/17.
 */
class LauncherActivity : Activity() {

    var mBtn1: DFRoundTextView? = null
    var mBtn2: DFRoundTextView? = null
    var mProgressGroup: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)

        mBtn1 = findViewById(R.id.tv_watch_now)
        mBtn2 = findViewById(R.id.tv_signin_activity)

        mBtn1?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@LauncherActivity, ChangeHeadIconActivity::class.java)
                startActivity(intent)
            }
        })
        mBtn2?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ImageUploadManager.uploadAvatarByMultipart(this@LauncherActivity)
                mProgressGroup?.visibility = View.VISIBLE
            }

        })
        mProgressGroup = findViewById(R.id.group_progress)
    }

    override fun onBackPressed() {
        if (mProgressGroup?.visibility == View.VISIBLE) {
            mProgressGroup?.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }


}