package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.videogo.exception.ErrorCode;
import com.videogo.exception.ExtraException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCameraActivity extends BaseActivity {

    @BindView(R.id.et_SerialNoStr)
    EditText etSerialNoStr;
    @BindView(R.id.et_SerialVeryCodeStr)
    EditText etSerialVeryCodeStr;
    @BindView(R.id.tv_adddevice)
    TextView tvAdddevice;

    protected static final int MSG_QUERY_CAMERA_FAIL = 0;

    protected static final int MSG_QUERY_CAMERA_SUCCESS = 1;

    private static final int MSG_LOCAL_VALIDATE_SERIALNO_FAIL = 8;

    private static final int MSG_LOCAL_VALIDATE_CAMERA_PSW_FAIL = 9;

    private static final int MSG_ADD_CAMERA_SUCCESS = 10;

    private static final int MSG_ADD_CAMERA_FAIL = 12;

    // private static final int SHOW_DIALOG_ADD_FINISHED = 15;

    private static final int SHOW_DIALOG_SET_WIFI = 16;

    private String mSerialNoStr;
    private String mSerialVeryCodeStr;
    private String deviceType="";

    private String TAG = "AddDeviceActivity";
    private EZProbeDeviceInfoResult mEZProbeDeviceInfo = null;

    private MessageHandler mMsgHandler = null;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "添加摄像头";
    }

    @Override
    protected void initView() {
        mMsgHandler = new MessageHandler();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_camera;
    }

    @OnClick(R.id.tv_adddevice)
    public void onViewClicked() {
        mSerialNoStr = etSerialNoStr.getText().toString();
        mSerialVeryCodeStr = etSerialVeryCodeStr.getText().toString();

        if (TextUtils.isEmpty(mSerialNoStr)){
            SmartToast.show("设备编号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mSerialVeryCodeStr)){
            SmartToast.show("设备验证码不能为空");
            return;
        }

        probeDeviceInfo();
    }

    private void probeDeviceInfo() {
        new Thread() {
            public void run() {

                mEZProbeDeviceInfo = EZOpenSDK.getInstance().probeDeviceInfo(mSerialNoStr,deviceType);
                if (mEZProbeDeviceInfo.getBaseException() == null){
                    // TODO: 2018/6/25 添加设备
                    sendMessage(MSG_QUERY_CAMERA_SUCCESS);
                }else{
                    switch (mEZProbeDeviceInfo.getBaseException().getErrorCode()){
                        case 120023:
                            // TODO: 2018/6/25  设备不在线，未被用户添加 （这里需要网络配置）
                        case 120002:
                            // TODO: 2018/6/25  设备不存在，未被用户添加 （这里需要网络配置）
                        case 120029:
                            // TODO: 2018/6/25  设备不在线，已经被自己添加 (这里需要网络配置)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.infoLog(TAG, "probeDeviceInfo fail :" + mEZProbeDeviceInfo.getBaseException().getErrorCode());
                                    sendMessage(MSG_QUERY_CAMERA_FAIL, mEZProbeDeviceInfo.getBaseException().getErrorCode());
                                }
                            });
                            break;
                        case 120020:
                            // TODO: 2018/6/25 设备在线，已经被自己添加 (给出提示)
                        case 120022:
                            // TODO: 2018/6/25  设备在线，已经被别的用户添加 (给出提示)
                        case 120024:
                            // TODO: 2018/6/25  设备不在线，已经被别的用户添加 (给出提示)
                        default:
                            // TODO: 2018/6/25 请求异常
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SmartToast.show("Request failed = "
                                            + mEZProbeDeviceInfo.getBaseException().getErrorCode());
                                }
                            });

                            break;
                    }
                }
            }
        }.start();
    }
    /**
     * 在此对类做相应的描述
     *
     * @author Admin
     * @data 2012-9-24
     */
    class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCAL_VALIDATE_SERIALNO_FAIL:
                    handleLocalValidateSerialNoFail(msg.arg1);
                    break;
                case MSG_LOCAL_VALIDATE_CAMERA_PSW_FAIL:
                    handleLocalValidateCameraPswFail(msg.arg1);
                    break;
                case MSG_QUERY_CAMERA_SUCCESS:
                    handleQueryCameraSuccess();
                    break;
                case MSG_QUERY_CAMERA_FAIL:
                    handleQueryCameraFail(msg.arg1);
                    break;
                case MSG_ADD_CAMERA_SUCCESS:
                    handleAddCameraSuccess();
                    break;
                case MSG_ADD_CAMERA_FAIL:
                    handleAddCameraFail(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleAddCameraFail(int arg1) {
        SmartToast.show("添加摄像头失败");
    }

    private void handleAddCameraSuccess() {
        startActivity(new Intent(this,AutoWifiNetConfigActivityActivity.class));
    }

    private void handleQueryCameraFail(final int errCode) {
        switch (errCode) {
//            case ErrorCode.ERROR_WEB_PASSWORD_ERROR:
////                handleCmaeraPswError();
//                break;
//            case ErrorCode.ERROR_WEB_DEVICE_VERSION_UNSUPPORT:
//            case ErrorCode.ERROR_WEB_DEVICE_UNSUPPORT:
//                SmartToast.show(getResources().getString(R.string.seek_camera_fail_device_not_support_shipin7));
//                break;
//            case ErrorCode.ERROR_WEB_NET_EXCEPTION:
//                SmartToast.show(R.string.query_camera_fail_network_exception, 0);
//                break;
//            case ErrorCode.ERROR_WEB_SERVER_EXCEPTION:
//                SmartToast.show(R.string.query_camera_fail_server_exception, 0);
//                break;
//            case ErrorCode.ERROR_TRANSF_ACCESSTOKEN_ERROR:
////                ActivityUtils.handleSessionException(SeriesNumSearchActivity.this);
//                break;
//            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
//                SmartToast.show(R.string.check_feature_code_fail, errCode);
//                //ActivityUtils.handleHardwareError(SeriesNumSearchActivity.this, null);
////                ActivityUtils.handleSessionException(SeriesNumSearchActivity.this);
//                break;
//            case ErrorCode.ERROR_INNER_PARAM_ERROR:
//                SmartToast.show(R.string.query_camera_fail_network_exception_or_server_exception, 0);
//                break;

            case ErrorCode.ERROR_WEB_DEVICE_ADD_OWN_AGAIN:     // 设备已被自己添加
            case ErrorCode.ERROR_WEB_DEVICE_OFFLINE_ADDED:   //设备不在线，已被别人添加
                SmartToast.show(getResources().getString(R.string.query_camera_fail_repeat_error));
                showCameraList();
                break;

            case ErrorCode.ERROR_WEB_DEVICE_ONLINE_ADDED:// 已被其他用户添加
                showUnbind();
                break;

            case ErrorCode.ERROR_WEB_DEVICE_NOT_ONLINE:// 设备不在线, 走wifi配置流程
                showWifiConfig();
                break;

            case ErrorCode.ERROR_WEB_DEVICE_NOT_EXIT:// 设备未注册, 或者不在线未添加, 走wifi配置流程
            case ErrorCode.ERROR_WEB_DEVICE_OFFLINE_NOT_ADD:
                showWifiConfig();
                break;
            default:
                SmartToast.show(getResources().getString(R.string.query_camera_fail));
                LogUtil.errorLog(TAG, "handleQueryCameraFail-> unkown error, errCode:" + errCode);
                break;
        }
    }

    private void showWifiConfig() {
        startActivity(new Intent(this,AutoWifiPrepareStepOneActivity.class));
    }

    private void showUnbind() {
        SmartToast.show("设备被其他用户绑定");
    }

    private void showCameraList() {
        SmartToast.show("设备已被自己添加");
    }

    private void handleQueryCameraSuccess() {
        if (mEZProbeDeviceInfo != null) {
            LogUtil.infoLog(TAG, "handleQueryCameraSuccess, msg:" );

        }

        // 更新搜索摄像头的图片
//        showCameraList();
//        mDeviceName.setText(mEZProbeDeviceInfo.getSubSerial());
//        mDeviceIcon.setImageResource(getDeviceIcon(""));
    }
    private void sendMessage(int msgCode, int errorCode) {
        if (mMsgHandler != null) {
            Message msg = Message.obtain();
            msg.what = msgCode;
            msg.arg1 = errorCode;
            mMsgHandler.sendMessage(msg);
        } else {
            LogUtil.errorLog(TAG, "sendMessage-> mMsgHandler object is null");
        }
    };
    private void sendMessage(int msgCode) {
        if (mMsgHandler != null) {
            Message msg = Message.obtain();
            msg.what = msgCode;
            mMsgHandler.sendMessage(msg);
        } else {
            LogUtil.errorLog(TAG, "sendMessage-> mMsgHandler object is null");
        }
    }
    private void handleLocalValidateSerialNoFail(int errCode) {
        switch (errCode) {
            case ExtraException.SERIALNO_IS_NULL:
                SmartToast.show(getResources().getString(R.string.serial_number_is_null));
                break;
            case ExtraException.SERIALNO_IS_ILLEGAL:
                SmartToast.show(getResources().getString(R.string.serial_number_put_the_right_no));
                break;
            default:
                SmartToast.show(getResources().getString(R.string.serial_number_error));
                LogUtil.errorLog(TAG, "handleLocalValidateSerialNoFail-> unkown error, errCode:" + errCode);
                break;
        }
    }
    private void handleLocalValidateCameraPswFail(int errCode) {
        switch (errCode) {
            case ExtraException.CAMERA_PASSWORD_IS_NULL:
                SmartToast.show(getResources().getString(R.string.camera_password_is_null));
                break;
            default:
                SmartToast.show(getResources().getString(R.string.camera_password_error));
                LogUtil.errorLog(TAG, "handleLocalValidateCameraPswFail-> unkown error, errCode:" + errCode);
                break;
        }
    }
}
