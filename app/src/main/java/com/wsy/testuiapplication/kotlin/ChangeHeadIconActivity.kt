package com.wsy.testuiapplication.kotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.content.FileProvider
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.wsy.testuiapplication.R
import com.wsy.testuiapplication.net.ImageUploadManager
import com.wsy.testuiapplication.util.StatusBarUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by WSY on 2020/3/3.
 */
class ChangeHeadIconActivity : Activity() {

    private val RC_TAKE_PHOTO = 1
    private val RC_CHOOSE_PHOTO = 2
    private val REQUEST_CROP = 3

    private var mTvChange: TextView? = null
    private var mIvBack: ImageView? = null
    private var mRlTakePhoto: RelativeLayout? = null
    private var mRlGallery: RelativeLayout? = null
    private var mRlCancel: RelativeLayout? = null

    private var mPopupWindow: PopupWindow? = null


    private var mHeadImage: ImageView? = null

    private var mTempPhotoPath: String? = null

    private var mCutUri: Uri? = null //// 图片裁剪时返回的uri

    private var imageFile: File? = null
    private var imageUri: Uri? = null


    private fun getStatusBarHeight(): Int {

        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, Color.BLACK)
        //        StatusBarUtil.setStatusBarTranslucent(this, true)
        //        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //竖屏
        //        val view = View(this)
        //        view.setBackgroundColor(Color.WHITE)
        //        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //                getStatusBarHeight())
        //        view.layoutParams = layoutParams
        //        val rootView = LinearLayout(this)
        //        rootView.orientation = LinearLayout.VERTICAL
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            rootView.addView(view)
        //        }
        //        rootView.addView(View.inflate(this, R.layout.activity_change_head_icon, null))
        //
        //
        //        setContentView(rootView)
        setContentView(R.layout.activity_change_head_icon)

        initView()
        initData()
    }

    fun initView() {
        initPopupWindow()

        mHeadImage = findViewById(R.id.iv_head_icon)

        mTvChange = findViewById(R.id.tv_change_head_icon)

        mTvChange?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showPopupWindow()
                // 设置背景颜色变暗
                var lp: WindowManager.LayoutParams = window.attributes
                lp.alpha = 0.7f
                window.attributes = lp

            }
        })

    }

    fun initData() {
        if (intent != null) {
            var extras: Bundle = getIntent().getExtras()
            if (extras != null) {
                var b: ByteArray = extras.getByteArray("picture")
                if (b != null) {
                    var bmp: Bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                    mHeadImage?.setImageDrawable(BitmapDrawable(bmp))
                }
            }
        }
    }

    private fun initPopupWindow() {
        var contextView: View = LayoutInflater.from(this).inflate(R.layout.change_head_icon_selector, null)
        mPopupWindow = PopupWindow(contextView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true)

        mPopupWindow?.setOnDismissListener(object : PopupWindow.OnDismissListener {
            override fun onDismiss() {
                var lp: WindowManager.LayoutParams = window.attributes
                lp.alpha = 1f
                window.attributes = lp
            }
        })


        mRlTakePhoto = contextView.findViewById(R.id.rl_take_photo)
        mRlGallery = contextView.findViewById(R.id.rl_gallery)
        mRlCancel = contextView.findViewById(R.id.rl_cancel)

        mRlTakePhoto?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (hasPermission()) {
                    takePhoto()
                    mPopupWindow?.dismiss()
                } else {
                    requestPermission(RC_TAKE_PHOTO)
                }
            }
        })
        mRlGallery?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (hasPermission()) {
                    choosePhoto()
                    mPopupWindow?.dismiss()
                } else {
                    requestPermission(RC_CHOOSE_PHOTO)
                }
            }
        })
        mRlCancel?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mPopupWindow?.dismiss()
            }
        })

    }

    private fun showPopupWindow() {
        var rootview: View = LayoutInflater.from(this).inflate(R.layout.activity_change_head_icon, null)

        mPopupWindow?.setAnimationStyle(R.style.pop_animation);
        mPopupWindow?.showAtLocation(rootview, Gravity.BOTTOM, 0, 0)
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
    fun requestPermission(requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) != true) {
                Toast.makeText(this, "请在设置中配置授权", Toast.LENGTH_SHORT).show();
            }
            var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            requestPermissions(permissions, requestCode);
        }
    }


    /**
     * 权限申请结果回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                RC_TAKE_PHOTO   //拍照权限申请返回
                -> {
                    takePhoto()
                    mPopupWindow?.dismiss()
                }
                RC_CHOOSE_PHOTO   //相册选择照片权限申请返回
                -> {
                    choosePhoto()
                    mPopupWindow?.dismiss()
                }

            }
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


        var tempFile: File?
        var storagePath: String?
        var storageDir: File?


        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        storagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath
        storageDir = File(storagePath)
        storageDir.mkdirs()
        tempFile = File.createTempFile(timeStamp, ".jpg", storageDir)

        imageFile = tempFile

        mTempPhotoPath = tempFile.absolutePath

        //        imageUri = FileProvider.getUriForFile(this, "com.wsy.testuiapplication.provider", imageFile)

        imageUri = getUriForFile(tempFile)

        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO)
    }

    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    fun getUriForFile(file: File): Uri {
        var uri: Uri?
        if (file == null) {
            throw  NullPointerException();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.wsy.testuiapplication.provider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_CHOOSE_PHOTO -> {
                    //                val uri = data!!.data
                    //                val filePath = FileUtil.getFilePathByUri(this, uri)
                    //
                    //                if (!TextUtils.isEmpty(filePath)) {
                    //                    //                    val requestOptions1 = RequestOptions().skipMemoryCache(true)
                    //                    //                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //                    //将照片显示在 ivImage上
                    //                    Glide.with(this).load(filePath).into(iv_head_icon)
                    //                }
                    if (data != null) {
                        val uri = data!!.data
                        cropPhoto(uri, false)
                    } else {
                        cropPhoto(imageUri, false)
                    }

                }
                RC_TAKE_PHOTO -> {

                    if (data != null) {
                        val uri = data!!.data
                        cropPhoto(uri, true)
                    } else {
                        cropPhoto(imageUri, true)
                    }

                    //                val requestOptions = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    //将图片显示在ivImage上
                    //                Glide.with(this).load(mTempPhotoPath).into(iv_head_icon)
                }
                REQUEST_CROP -> {
                    //                if (data != null) {
                    //                    val uri = data.data
                    //                    val filePath = FileUtil.getFilePathByUri(this, uri)
                    //
                    //                    if (!TextUtils.isEmpty(filePath)) {
                    //                        //                    val requestOptions1 = RequestOptions().skipMemoryCache(true)
                    //                        //                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //                        //将照片显示在 ivImage上
                    //                        Glide.with(this).load(filePath).into(iv_head_icon)
                    //                    }
                    //                }

                    ImageUploadManager.uploadAvatarByMultipart(this@ChangeHeadIconActivity)


                    var bitmap: Bitmap

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mCutUri));
                    var baos = ByteArrayOutputStream()

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    var bytes: ByteArray = baos.toByteArray()
                    //                byte[] bytes = baos . toByteArray ();

                    Glide.with(this).load(bytes).into(mHeadImage);

                    //                Glide.with(this).load(bitmap).into(iv_head_icon)


                }
            }
        }

    }

    // 图片裁剪
    fun cropPhoto(uri: Uri?, fromCapture: Boolean) {
        var intent = Intent("com.android.camera.action.CROP") //打开系统自带的裁剪图片的intent

        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*")
        intent.putExtra("scale", true)

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);


        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(imageFile)
            //            mCutUri = Uri.fromFile(imgFile);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            var time = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(Date())
            var fileName = "photo_" + time;
            var mCutFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath, fileName + ".jpg")
            //            var mCutFile = File(Environment.getExternalStorageDirectory().absolutePath + "/take_photo", fileName + ".jpeg")
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        //        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();

        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        var intentBc = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, REQUEST_CROP);

    }


    //    private void cropPhoto(Uri uri, boolean fromCapture)
    //    {
    //        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
    //
    //
    //        // 注意一定要添加该项权限，否则会提示无法裁剪
    //        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    //
    //        intent.setDataAndType(uri, "image/*");
    //        intent.putExtra("scale", true);
    //
    //        // 设置裁剪区域的宽高比例
    //        intent.putExtra("aspectX", 1);
    //        intent.putExtra("aspectY", 1);
    //
    //        // 设置裁剪区域的宽度和高度
    //        intent.putExtra("outputX", 200);
    //        intent.putExtra("outputY", 200);
    //
    //        // 取消人脸识别
    //        intent.putExtra("noFaceDetection", true);
    //        // 图片输出格式
    //        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    //
    //        // 若为false则表示不返回数据
    //        intent.putExtra("return-data", false);
    //
    //        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
    //        if (fromCapture) {
    //            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
    //            mCutUri = Uri.fromFile(imgFile);
    //        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
    //            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date ());
    //            String fileName = "photo_"+time;
    //            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
    //            if (!mCutFile.getParentFile().exists()) {
    //                mCutFile.getParentFile().mkdirs();
    //            }
    //            mCutUri = Uri.fromFile(mCutFile);
    //        }
    //        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
    //        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
    //        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
    //        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    //        intentBc.setData(uri);
    //        this.sendBroadcast(intentBc);
    //
    //        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    //    }


    //将图片转换为数组
    fun getImageBytes(bmp: Bitmap): ByteArray {
        if (bmp == null) {
            return ByteArray(0)
        }
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes: ByteArray = baos.toByteArray()
        return imageBytes


    }


}