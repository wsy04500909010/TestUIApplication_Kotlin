package com.wsy.testuiapplication.kotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.wsy.testuiapplication.util.FileUtil
import kotlinx.android.synthetic.main.activity_change_head_icon.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by WSY on 2020/3/3.
 */
class ChangeHeadIconActivity : AppCompatActivity() {

    val RC_TAKE_PHOTO = 1
    val RC_CHOOSE_PHOTO = 2

    var mImage: ImageView? = null
    var mButton: Button? = null

    var mTempPhotoPath: String? = null
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wsy.testuiapplication.R.layout.activity_change_head_icon)

        mImage = findViewById(com.wsy.testuiapplication.R.id.iv_head_icon)
        mButton = findViewById(com.wsy.testuiapplication.R.id.btn_change)

        initView()

    }

    fun initView() {
        mButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (hasPermission()) {
                    //                    choosePhoto()
                    takePhoto()
                } else {
                    requestPermission()
                }
            }

        })
    }


    /*
    * 判断是否有权限
    * */
    fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            return true;
        }
    }

    /**
     * 请求权限
     */
    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) != true) {
                Toast.makeText(this, "请在设置中配置授权", Toast.LENGTH_SHORT).show();
            }
            var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            requestPermissions(permissions, 1);
        }
    }


    /**
     * 权限申请结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RC_TAKE_PHOTO   //拍照权限申请返回
            -> if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            }
            RC_CHOOSE_PHOTO   //相册选择照片权限申请返回
            -> choosePhoto()
        }
    }


    /**
     * 从相册选择照片
     */
    fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
    }

    /**
     *
     */
    private fun takePhoto() {

        //        File imageFile = null;
        //        String storagePath;
        //        File storageDir;
        //        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date ());
        //        try {
        //            storagePath = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        //            storageDir = new File (storagePath);
        //            storageDir.mkdirs();
        //            imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        //创建Uri
        //        Uri photoURI = FileProvider . getUriForFile (this, "com.hm.camerademo.fileprovider", imageFile);


        val intentToTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //        val fileDir = File(getExternalStorageDirectory().toString() + separator + "photoTest" + separator)
        //        if (!fileDir.exists()) {
        //            fileDir.mkdirs()
        //        }
        //        val photoFile = File(fileDir, "photo.jpeg")
        //        mTempPhotoPath = photoFile.getAbsolutePath()
        //        imageUri = FileProvider.getUriForFile(this, "com.wsy.testuiapplication.provider", photoFile)


        var imageFile: File? = null
        var storagePath: String? = null
        var storageDir: File? = null
        var imageUri: Uri? = null

        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        storagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath
        storageDir = File(storagePath)
        storageDir.mkdirs()
        imageFile = File.createTempFile(timeStamp, ".jpg", storageDir)

        mTempPhotoPath = imageFile.absolutePath

        imageUri = FileProvider.getUriForFile(this, "com.wsy.testuiapplication.provider", imageFile)


        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_CHOOSE_PHOTO -> {
                val uri = data!!.data
                val filePath = FileUtil.getFilePathByUri(this, uri)

                if (!TextUtils.isEmpty(filePath)) {
                    //                    val requestOptions1 = RequestOptions().skipMemoryCache(true)
                    //                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //将照片显示在 ivImage上
                    Glide.with(this).load(filePath).into(iv_head_icon)
                }
            }
            RC_TAKE_PHOTO -> {

                //                val requestOptions = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                //将图片显示在ivImage上
                Glide.with(this).load(mTempPhotoPath).into(iv_head_icon)
            }
        }
    }

}