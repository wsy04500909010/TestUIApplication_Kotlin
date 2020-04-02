package com.wsy.testuiapplication.launcher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.view.View
import android.widget.ProgressBar
import com.wsy.testuiapplication.DFRoundTextView
import com.wsy.testuiapplication.kotlin.ChangeHeadIconActivity
import com.wsy.testuiapplication.net.ImageUploadManager
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.graphics.drawable.Drawable
import android.os.Build
import com.wsy.testuiapplication.R


/**
 * Created by WSY on 2020/2/17.
 */
class LauncherActivity : Activity() {

    var mBtn1: DFRoundTextView? = null
    var mBtn2: DFRoundTextView? = null
    var mProgressGroup: Group? = null

    private var mProgressBar: ProgressBar? = null

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
        mProgressBar = findViewById(R.id.pb_upload)

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//
//            val drawableProgress = DrawableCompat.wrap(mProgressBar!!.getIndeterminateDrawable())
//            DrawableCompat.setTint(drawableProgress,
//                    ContextCompat.getColor(this@LauncherActivity, android.R.color.holo_green_light))
//            mProgressBar!!.setIndeterminateDrawable(DrawableCompat.unwrap<Drawable>(drawableProgress))
//
//        } else {
//            mProgressBar!!.getIndeterminateDrawable()
//                    .setColorFilter(ContextCompat.getColor(this@LauncherActivity, R.color.colorPrimary),
//                            PorterDuff.Mode.SRC_IN)
//        }

//        实际上测试 这个颜色不处理 每个sdk也都是xml设置的白色



    }

    override fun onBackPressed() {
        if (mProgressGroup?.visibility == View.VISIBLE) {
            mProgressGroup?.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }


}