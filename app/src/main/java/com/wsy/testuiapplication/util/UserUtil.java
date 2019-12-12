package com.wsy.testuiapplication.util;

import com.wsy.testuiapplication.DFApplication;
import com.wsy.testuiapplication.constant.AppConstants;
import com.wsy.testuiapplication.operation.SettingInfo;

/**
 * Created by qinmin on 2017/8/30.
 */

public class UserUtil {

    private static final String TAG = UserUtil.class.getSimpleName();

    /**
     * avatar
     *
     * @return
     */
    public static String getAvatar() {
        return PreferencesUtil.getString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.USER_AVATAR);
    }

    public static void setAvatar(String avatar) {
        PreferencesUtil.putString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.USER_AVATAR, avatar);
    }

    /**
     * username
     *
     * @return
     */
    public static String getUserName() {
        return PreferencesUtil.getString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.PHONE);
    }

    public static void setUserName(String userName) {
        PreferencesUtil.putString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.PHONE, userName);
    }

    /**
     * nickname
     *
     * @return
     */
    public static String getNickName() {
        return PreferencesUtil.getString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.NICK_NAME);
    }

    public static void setNickName(String nikename) {
        PreferencesUtil.putString(DFApplication.getInstance().getApplicationContext(),
                AppConstants.NICK_NAME, nikename);
    }

    /**
     * get the user_id
     *
     * @return
     */
    public static int getUserId() {
        return SettingInfo.getInstance().mUserId;
    }

    /**
     * send push message token to server(发送消息推送token到服务端)
     */
    public static void sendPushMessageTokenToServer() {
        // String deviceToken = FirebaseInstanceId.getInstance().getToken();
        // Slog.d(TAG, "device_Token: " + deviceToken);
        // String deviceId = PackageUtil.getCombinaMAC();
        // // If you want to send messages to this application instance or
        // // manage this apps subscriptions on the server side, send the
        // // Instance ID token to your app server.
        // if (StringUtil.isEmpty(deviceToken) || StringUtil.isEmpty(deviceId)) {
        // return;
        // }
//         DFData.getInstance().registerNotification(deviceToken,deviceId,new DFData.Listener() {
        // @Override
        // public void onSuccess(Object response) {
        // Slog.i(TAG, "registerNotification onSuccess"
        // + (response != null ? response.toString() : ""));
        // }
        //
        // @Override
        // public void onFail(int errorCode) {
        // Slog.i(TAG, "registerNotification onFail");
        // }
        // });
    }

    /**
     * get user Infomation such as nickname 、 avatar
     */
//    public static void getUserInfo(final UserInfoCallBack callBack) {
//        DFData.getInstance().getUserInfo(UserInfoBean.class, new DFData.Listener() {
//            @Override
//            public void onSuccess(Object response) {
//                if (response instanceof DFResponse) {
//                    if (((DFResponse) response).getResult() instanceof UserInfoBean) {
//                        UserInfoBean userInfoBean = (UserInfoBean) ((DFResponse) response)
//                                .getResult();
//
//                        //存储用户信息
//                        setAvatar(userInfoBean.userAvatar);
//                        setNickName(userInfoBean.nickName);
//
//                        if (callBack != null) {
//                            callBack.onUserInfoBack();
//                        }
//
//                        Intent intent = new Intent();
//                        intent.setAction(AppConstants.ADD_PLAYHISTORY_SUCCESS);
//                        DFApplication.getInstance().getApplicationContext().sendBroadcast(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(int errorCode) {
//
//            }
//        });
//    }

}
