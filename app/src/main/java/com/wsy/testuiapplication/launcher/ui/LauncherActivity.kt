package com.wsy.testuiapplication.launcher.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.constraint.Group
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.wsy.testuiapplication.DFRoundTextView
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.kotlin.ChangeHeadIconActivity
import com.wsy.testuiapplication.net.ImageUploadManager
import com.wsy.testuiapplication.util.Slog
import java.io.ByteArrayOutputStream


/**
 * Created by WSY on 2020/2/17.
 */
class LauncherActivity : Activity() {

    var mBtn1: DFRoundTextView? = null
    var mBtn2: DFRoundTextView? = null
    var mProgressGroup: Group? = null

    var mIvHeadIcon: ImageView? = null

    var mBtnGoogle: Button? = null

    private var mProgressBar: ProgressBar? = null

    // google
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        // google
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestIdToken(getString(R.string.server_client_id)).build()
        //
        //        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


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

        mIvHeadIcon = findViewById(R.id.iv_head_icon)


        Glide.with(this)
                .load("http://192.168.8.211:8082/v1/video/avatars?consumer_email=tvsc01@tvtest.com&auth_key=cbd1DilQ7OT_ZFDgzAiRZTSQiyo51a6J")
                .asBitmap().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().placeholder(0).into(
                        mIvHeadIcon)
        //        Glide.with(this)
        //                .load("http://192.168.8.211:8082/v1/video/avatars?consumer_email=tvsc01@tvtest.com&auth_key=cbd1DilQ7OT_ZFDgzAiRZTSQiyo51a6J")
        //                .asBitmap().into(mIvHeadIcon)
        mIvHeadIcon?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val src = mIvHeadIcon?.getDrawable()
                val bd = src as BitmapDrawable
                val bitmap = bd.bitmap

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()

                val intent1 = Intent(this@LauncherActivity, ChangeHeadIconActivity::class.java)
                intent1.putExtra("picture", b)
                startActivity(intent1)
            }
        })

        mBtnGoogle = findViewById(R.id.btn_google_signin)
        mBtnGoogle?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val signInIntent = mGoogleSignInClient?.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

        })

    }

    override fun onBackPressed() {
        if (mProgressGroup?.visibility == View.VISIBLE) {
            mProgressGroup?.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            Slog.d("handleSignInResult", "account")
            // Signed in successfully, show authenticated UI.
            //            updateUI(account);
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("handleSignInResult", "signInResult:failed code=" + e.statusCode)
            //            updateUI(null);
        }

    }

}