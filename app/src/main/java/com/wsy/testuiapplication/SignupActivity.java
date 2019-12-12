package com.wsy.testuiapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsy.testuiapplication.constant.NetConstants;
import com.wsy.testuiapplication.util.DensityUtil;
import com.wsy.testuiapplication.util.LanguageUtil;
import com.wsy.testuiapplication.util.Slog;
import com.wsy.testuiapplication.util.StatusBarUtil;

import java.util.Locale;

/**
 * Created by WangSiYe on 2019/4/2.
 */
public class SignupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    private static final String PAGE_TYPE = "pageType";
    public static final int SIGNUP = 1;
    public static final int FORGET_PSD = 2;

//    private ImageView mIvClose;
    private ImageView mIvBottomLogo;
    private WebView mWebView;
    private ProgressBar mLoadingBar;
    private TextView mSignUpTitle;
//    private DFRoundTextView mGotoLogin;

    private ImageView mBackBtn;
    private TextView mTvLogin;
    private TextView mTvSkip;
    private TextView mTvLoginTip;

    private int mPageType;//1 注册页面  2 忘记密码页面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mPageType = getIntent().getIntExtra(PAGE_TYPE, 1);

//        mSignupTip = findViewById(R.id.tip_signup2);
//        mSignupTip.setOnClickListener(this);

        mBackBtn = findViewById(R.id.iv_back_signup);
        mBackBtn.setOnClickListener(this);
        mTvLogin = findViewById(R.id.title_logintext);
        mTvLogin.setOnClickListener(this);

        mIvBottomLogo = findViewById(R.id.logo_ad_signup);
//        mGotoLogin = findViewById(R.id.tv_goto_login);
//        mGotoLogin.setOnClickListener(this);
        mSignUpTitle = findViewById(R.id.title_signup);
        mLoadingBar = findViewById(R.id.signup_loading_pb);
//        mIvClose = findViewById(R.id.iv_close_signup);
//        mIvClose.setOnClickListener(this);


        mTvSkip = findViewById(R.id.tv_skip);
        mTvSkip.setOnClickListener(this);
        mTvLoginTip = findViewById(R.id.tv_login_tip);

        setStatusBarMargin();

        RelativeLayout mContainerLayout = findViewById(R.id.layout_webview_container);
        initWebView();
        mContainerLayout.addView(mWebView);

    }

    private void initWebView() {
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadingBar.setVisibility(View.GONE);
            }
        });

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.addJavascriptInterface(this, "Android");
    }

    private void initLayout() {
        if (SIGNUP == mPageType) {
            mIvBottomLogo.setVisibility(View.GONE);
//            mGotoLogin.setVisibility(View.VISIBLE);
            mSignUpTitle.setVisibility(View.VISIBLE);
        } else {
            mIvBottomLogo.setVisibility(View.VISIBLE);
//            mGotoLogin.setVisibility(View.GONE);
            mSignUpTitle.setVisibility(View.GONE);
        }
    }

    private void initData() {
        mLoadingBar.setVisibility(View.VISIBLE);
        int languageType = getLanguage();
        String linkUrl = generateUrl(languageType);

        mWebView.loadUrl(linkUrl);
    }

    /**
     * 重置密码成功回调
     */
    @JavascriptInterface
    public void getLoginMsg() {
        Slog.d(TAG, "getLoginMsg");
        finish();
    }

//    @JavascriptInterface
//    public void sendRegisteResultMsg(String params) {
//        Slog.d(TAG, "callfromJS: params=" + params);
//        if (!StringUtil.isEmpty(params)) {
//            AutoLoginBean autoLoginBean = new Gson().fromJson(params, AutoLoginBean.class);
//            if (autoLoginBean != null) {
//                if (autoLoginBean.getCode() == 0) {//注册成功
//                    UserController.getInstance().setLoginAsUser(true);
//                    UserController.getInstance().userLogin(this, autoLoginBean.getData().getEmail
//                                    (), autoLoginBean.getData().getPwd(),
//                            new UserController.AutoLoginErrorListener() {
//                                @Override
//                                public void onError() {
//                                    final DFDialog dfDialog = new DFDialog(SignupActivity.this,
//                                            true,
//                                            getString(R.string.auto_login_failed), getString(R
//                                            .string.sure),
//                                            getString(R.string.cancel));
//                                    dfDialog.setClicklistener(new DFDialog.ClickListenerInterface
//                                            () {
//                                        @Override
//                                        public void doConfirm() {
//
//                                            dfDialog.dismiss();
//                                            finish();
//                                        }
//
//                                        @Override
//                                        public void doCancel() {
//                                            dfDialog.dismiss();
//                                        }
//                                    });
//                                    dfDialog.show();
//                                }
//                            },
//                            null);
//                }
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_close_signup:
//                finish();
//                break;
//            case R.id.tv_goto_login:
//                startActivity(new Intent(this,MainActivity.class));
//                break;

            case R.id.title_logintext:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.iv_back_signup:
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                }else {
                    finish();
                }
                break;
            case R.id.tv_skip:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();

        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWebView.loadUrl("about:blank");
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
        initLayout();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mPageType = intent.getIntExtra(PAGE_TYPE, 1);
    }

    /**
     * fix special-shaped screen.
     */
    private void setStatusBarMargin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            if (statusBarHeight == 0) {
                return;
            }
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.height = DensityUtil.dip2px(this, 22);
//            params.height = DensityUtil.dip2px(this, 22);
//            params.addRule(RelativeLayout.ALIGN_PARENT_END);
//            params.setMargins(0, DensityUtil.dip2px(this, 10) + statusBarHeight,
//                    DensityUtil.dip2px(this, 30), 0);
//            mIvClose.setLayoutParams(params);
        }
    }

    private String generateUrl(int languageType) {
        if (SIGNUP == mPageType) {//注册
            if (LanguageUtil.LANGUAGE_ZHCN == languageType) {//简体中文
                return NetConstants.SIGNUP_URL_ZHCN;
            }
            if (LanguageUtil.LANGUAGE_ZHTW == languageType) {//繁体中文
                return NetConstants.SIGNUP_URL_ZHTW;
            }
            if (LanguageUtil.LANGUAGE_ENUS == languageType) {//英文
                return NetConstants.SIGNUP_URL_ENUS;
            }
            return NetConstants.SIGNUP_URL_ZHCN;//默认简体中文
        } else {//忘记密码
            if (LanguageUtil.LANGUAGE_ZHCN == languageType) {
                return NetConstants.FORGOT_PSD_URL_ZHCN;
            }
            if (LanguageUtil.LANGUAGE_ZHTW == languageType) {
                return NetConstants.FORGOT_PSD_URL_ZHTW;
            }
            if (LanguageUtil.LANGUAGE_ENUS == languageType) {
                return NetConstants.FORGOT_PSD_URL_ENUS;
            }
            return NetConstants.FORGOT_PSD_URL_ZHCN;
        }
    }

    private int getLanguage() {
        Locale locale = LanguageUtil.getLocale(this);
        String language = locale.getLanguage();
        if (language.equals(LanguageUtil.LANGUAGE_EN)) {
            return LanguageUtil.LANGUAGE_ENUS;
        }
        if (language.equals(LanguageUtil.LANGUAGE_ZH)) {
            String country = locale.getCountry();
            if (country.equals(LanguageUtil.COUNTRY_CN)) {
                return LanguageUtil.LANGUAGE_ZHCN;
            }
            if (country.equals(LanguageUtil.COUNTRY_TW)) {
                return LanguageUtil.LANGUAGE_ZHTW;
            }
        }
        return LanguageUtil.LANGUAGE_ZHCN;
    }

}

