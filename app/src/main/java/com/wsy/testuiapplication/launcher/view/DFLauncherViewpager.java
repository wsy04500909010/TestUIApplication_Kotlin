package com.wsy.testuiapplication.launcher.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wsy.testuiapplication.R;
import com.wsy.testuiapplication.launcher.adapter.ViewPagerAdapter;
import com.wsy.testuiapplication.launcher.bean.Banner;
import com.wsy.testuiapplication.util.ImageUtil;
import com.wsy.testuiapplication.util.Slog;
import com.wsy.testuiapplication.view.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwl on 2017/9/9.
 */

public class DFLauncherViewpager extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "DFBannerView";

    /* banner view start scrolling */
    private static final byte MESSAGE_SCROLL_START = 0;
    /* banner view pause scroll */
    private static final byte MESSAGE_SCROLL_PAUSE = 1;
    /* auto scrolling timemills */
    private static final long AUTO_SCROLL_TIME_MILLS = 5000;

    private Context mContext;
    private ViewPager mViewPager;
    private ImageView mPlaceHolder;
    private CirclePageIndicator mIndicator;
    /* banner datalist */
    private List<Banner> mBannerList;
    private List<View> mImageViews;
    private ViewPagerAdapter mAdapter;

    /* record current banner position */
    private int mCurrentPosition;

    private Handler mHandler;

    /* support scrolling cycle true-yes false-no */
    private boolean mIsCycle = true;
    /* support auto scrolling true-yes false-no */
    private boolean mIsAuto;
    private boolean mShowing;

    public DFLauncherViewpager(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DFLauncherViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DFLauncherViewpager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initData();
        initView();
        initHandler();

        setData(mBannerList);
        setBannerShowing(true);
    }

    protected void initData() {
        mIsCycle = true;
        mCurrentPosition = 0;
        mBannerList = new ArrayList<>();
        mImageViews = new ArrayList<>();
        mAdapter = new ViewPagerAdapter();

        prepareData();
        mAdapter.setData(mImageViews);
    }

    private void prepareData() {

        Banner banner1 = new Banner();
        banner1.setImage(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581945793577&di=b97ad78290dcfa83f8bbbe2c1e6c382b&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1308%2F26%2Fc1%2F24886281_1377447873547.jpg");
        Banner banner2 = new Banner();
        banner2.setImage(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581945793575&di=f5f58b0f1a41dc369e1acbb233afc2f6&imgtype=0&src=http%3A%2F%2Fbbs-fd.zol-img.com.cn%2Ft_s800x5000%2Fg3%2FM03%2F04%2F04%2FCg-4V1CbWeSIdoXeAAESrv-wAzQAABQ1ABFDcEAARLG605.jpg");
        Banner banner3 = new Banner();
        banner3.setImage(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581945793575&di=f2dd8f77d064bd515b849f444dbcf82d&imgtype=0&src=http%3A%2F%2Fpic23.nipic.com%2F20120827%2F7567492_131413588183_2.jpg");

        mBannerList.add(banner1);
        mBannerList.add(banner2);
        mBannerList.add(banner3);

    }


    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_launcher_viewpager, this);
        mPlaceHolder = findViewById(R.id.banner_placeholder);
        mViewPager = findViewById(R.id.banner_viewpager);
        mIndicator = findViewById(R.id.banner_indicator);
        RelativeLayout.LayoutParams bannerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(bannerParams);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mIndicator.setViewPager(mViewPager);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Slog.d(TAG, "handleMessage msg :" + msg);
                switch (msg.what) {
                    case MESSAGE_SCROLL_START:
                        int max = mImageViews.size();
                        if (max > 1) {
                            int position = (mCurrentPosition + 1) % mImageViews.size();
                            mViewPager.setCurrentItem(position, true);
                            if (position == max) { // 最后一页时回到第一页
                                mViewPager.setCurrentItem(1, false);
                            }
                            Slog.d(TAG, "MESSAGE_SCROLL_START, position :" + position + ", mCurrentPosition :" +
                                        mCurrentPosition + ", size :" + max);
                        }
                        break;
                    case MESSAGE_SCROLL_PAUSE:
                        mHandler.removeMessages(MESSAGE_SCROLL_START);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void setData(List<Banner> bannerList) {
        if (bannerList.size() == 0) {
            mPlaceHolder.setVisibility(View.VISIBLE);
            ImageUtil.showImage(getContext(), R.drawable.placeholder_16to9_small, mPlaceHolder);
            mViewPager.setVisibility(View.GONE);
            mIndicator.setVisibility(View.GONE);
        } else {
            mPlaceHolder.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.VISIBLE);
            mBannerList = bannerList;
            initBannerView();
            setAuto(true);
        }
    }

    private void initBannerView() {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        } else {
            mImageViews.clear();
        }
        if (mBannerList == null || mBannerList.size() == 0) {
            Slog.e(TAG, "mBannerList is null");
            return;
        }
        int size = mBannerList.size();
        if (mIsCycle && size == 1) {// only one banner do not support cycle
            mIsCycle = false;
        }
        if (mIsCycle) {// add the last banner
            mImageViews.add(getBannerView(size - 1));
        }
        for (int i = 0; i < size; i++) {
            mImageViews.add(getBannerView(i));
        }
        if (mIsCycle) {// add the first banner
            mImageViews.add(getBannerView(0));
        }
        if (mIndicator != null) {
            mIndicator.setCycleMode(mIsCycle);
        }
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mIsCycle ? 1 : 0);
    }

    private View getBannerView(final int index) {
        final Banner banner = mBannerList.get(index);
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_banner_view_item, null);
        if (banner != null) {
//            view.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    PageManager.startTargetActivity(getContext(), banner);
//                    postData2Imetis(index, banner);
//                }
//            });
            ImageView imageView = view.findViewById(R.id.banner_image);
            ImageUtil.showImage(getContext(), banner.getImage(), R.drawable.placeholder_16to9_middle, imageView);
//            String summaries = StringUtil.isEmpty(banner.getSummaries()) ? banner.getTarget().getName() :
//                    banner.getSummaries();
//            if (StringUtil.isEmpty(summaries)) {
//            LinearLayout background = view.findViewById(R.id.banner_image_backgound);
//            background.setVisibility(View.GONE);
//            } else {
//                TextView textView = view.findViewById(R.id.banner_text);
//                textView.setText(summaries);
//                DFTextView rootNameView = view.findViewById(R.id.banner_textview_rootname);
//                if (mRootId == AppConstants.ROOT_ID_HOME) {
//                    String rootName = ZypeUtil.getRootNameById(banner.getTarget().getRootId());
//                    if (StringUtil.isEmpty(banner.getTarget().getUrl())) {
//                        if (StringUtil.isEmpty(rootName)) {
//                            rootNameView.setVisibility(GONE);
//                        } else {
//                            rootNameView.setVisibility(VISIBLE);
//                            rootNameView.setText(rootName);
//                        }
//                    }
////                    else {
////                        rootNameView.setVisibility(VISIBLE);
////                        rootNameView.setText(R.string.main_banner_category);
////                    }
//                }
//            }
        }

        return view;
    }

    /**
     * set cycle type
     * this method need to be invoked before setData
     *
     * @param isCycle true-support cycle false-not support
     */
    public void setCycle(boolean isCycle) {
        this.mIsCycle = isCycle;
    }

    /**
     * set auto scrolling or not
     * auto scrolling mode means it support cycle
     *
     * @param isAuto true-support auto scrolling false-not support
     */
    public void setAuto(boolean isAuto) {
        if (mBannerList != null && mBannerList.size() == 1) {
            return;
        }
        this.mIsAuto = isAuto;
        mIsCycle = true;
        startScroll();
    }

    private void startScroll() {
        Slog.d(TAG, "startScroll mIsAuto :" + mIsAuto + ", mShowing :" + mShowing);
        if (mIsAuto && mShowing) {
            mHandler.removeMessages(MESSAGE_SCROLL_START);
            mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_START, AUTO_SCROLL_TIME_MILLS);
        }
    }

    public void setBannerShowing(boolean showing) {
        mShowing = showing;
        if (mShowing) {
            mAdapter.notifyDataSetChanged();
            startScroll();
        } else {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int max = mImageViews.size();
        mCurrentPosition = position;
        if (mIsCycle) {
            if (position == 0) {
                mCurrentPosition = max - 2;
            } else {
                if (position == max - 1) {
                    mCurrentPosition = 1;
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mImageViews.size() < 2) {
            return;
        }
        Slog.d(TAG, "onPageScrollStateChanged state:" + state);
        if (state == 1) {// viewPager is scrolling
            mHandler.removeMessages(MESSAGE_SCROLL_START);
            return;
        } else {
            if (state == 0) { // viewPager scroll over
                mViewPager.setCurrentItem(mCurrentPosition, false);
                mHandler.removeMessages(MESSAGE_SCROLL_START);
                mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_START, AUTO_SCROLL_TIME_MILLS);
            }
        }
    }

//    private void postData2Imetis(int index, Banner banner) {
//
//        if (banner == null) {
//            Slog.e(TAG, "getTargetClass banner is null");
//            return;
//        }
//        if (banner.getTarget() == null) {
//            Slog.e(TAG, "getTargetClass target is null");
//            return;
//        }
//
//    }
}
