package com.wsy.testuiapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wsy.testuiapplication.constant.AppConstants
import com.wsy.testuiapplication.util.PreferencesUtil
import com.wsy.testuiapplication.util.ToastUtil
import org.jetbrains.anko.find

/**
 * Create by WSY on 2021/2/1
 */
class ChangeLanguageActivity : AppCompatActivity(), View.OnClickListener {

    var mTextViewZH: View? = null
    var mTextViewEN: View? = null
    var mTextViewDefault: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_language)


        mTextViewZH = find(R.id.v_zh)
        mTextViewEN = find(R.id.v_en)
        mTextViewDefault = find(R.id.v_default)

        setListener()
    }

    fun setListener() {
        mTextViewZH?.setOnClickListener(this)
        mTextViewEN?.setOnClickListener(this)
        mTextViewDefault?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.v_zh -> {
                saveLastLanguage(AppConstants.LANGUAGE_ZH)
                ToastUtil.showShortToast("语言改为中文,重启app生效")
                restartApp()
            }
            R.id.v_en -> {
                saveLastLanguage(AppConstants.LANGUAGE_EN)
                ToastUtil.showShortToast("语言改为英文,重启app生效")
                restartApp()
            }
            R.id.v_default -> {
                saveLastLanguage(AppConstants.LANGUAGE_DEFAULT)
                ToastUtil.showShortToast("语言改为跟随系统,重启app生效")
                restartApp()
            }
        }
    }

    fun saveLastLanguage(lastLanguage: Int) {
        PreferencesUtil.putInt(this, AppConstants.LANGUAGE, lastLanguage)
    }

    fun restartApp() {


        Handler().postDelayed(Runnable {
            var intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            var restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            var mgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent);
//            System.exit(0) //这种方式也可以
            android.os.Process.killProcess(android.os.Process.myPid());
        }, 2000)

    }

}