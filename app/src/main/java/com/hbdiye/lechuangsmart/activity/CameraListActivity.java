package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.CameraAdapter;
import com.hbdiye.lechuangsmart.google.zxing.activity.CaptureActivity;
import com.hbdiye.lechuangsmart.remoteplayback.list.PlayBackListActivity;
import com.hbdiye.lechuangsmart.remoteplayback.list.RemoteListContant;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.util.TimeUtil;
import com.hbdiye.lechuangsmart.utils.EZUtils;
import com.hbdiye.lechuangsmart.utils.TimeUtils;
import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CameraListActivity extends BaseActivity {

    @BindView(R.id.rv_camera)
    RecyclerView rvCamera;

    private String resultString = "ys7\n" +
            "801588939\n" +
            "GLGAUK\n" +
            "CS-C6H-3B1WFR 4mm\n";
    public String TAG = "TestActivity";
    String mSerialNoStr;
    String mSerialVeryCodeStr;
    String deviceType;

    private CameraAdapter adapter;
    private List<EZDeviceInfo> mList = new ArrayList<>();

    @Override
    protected void initData() {
        EZOpenSDK.getInstance().openLoginPage();
        handleString();

        cameraListData();

        String firstToken = (String) SPUtils.get(getApplicationContext(), "firstToken", "");
        if (TextUtils.isEmpty(firstToken)){
            SPUtils.put(getApplicationContext(),"firstToken", TimeUtils.getNowTimeNoWeek());
        }else {
            boolean b = TimeUtils.DateCompare(TimeUtils.getNowTimeNoWeek(), firstToken);
            if (b){
                Log.e("sss","大于7天");
            }else {
                Log.e("sss","小于7天");
            }
        }


    }

    private void cameraListData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<EZDeviceInfo> deviceList = EZOpenSDK.getInstance().getDeviceList(0, 10);
                    if (deviceList != null && deviceList.size() > 0) {
                        //设备存在
                        if (mList.size() > 0) {
                            mList.clear();
                        }
                        mList.addAll(deviceList);
                        CameraListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        //设备不存在，添加设备
                        mList.clear();
                        CameraListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected String getTitleName() {
        return "摄像头";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseEdit.setImageResource(R.drawable.my_add);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCamera.setLayoutManager(manager);
        adapter = new CameraAdapter(mList);
        rvCamera.setAdapter(adapter);

        handlerClick();

    }

    private void handlerClick() {
        ivBaseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CameraListActivity.this,AddCameraActivity.class));
                startActivity(new Intent(CameraListActivity.this, CaptureActivity.class)
                        .putExtra("camera", true));
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EZDeviceInfo deviceInfo = mList.get(position);
                EZCameraInfo cameraInfo = getCameraInfoFromDevice(deviceInfo, 0);
//                            intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                            intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
                if (mList.get(position).getStatus() == 1) {
                    startActivity(new Intent(CameraListActivity.this, EZRealPlayActivity.class)
                            .putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo)
                            .putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo));
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tab_remoteplayback_btn:
                        //回放
                        EZDeviceInfo deviceInfo = mList.get(position);
                        if (deviceInfo.getCameraNum() <= 0 || deviceInfo.getCameraInfoList() == null || deviceInfo.getCameraInfoList().size() <= 0) {
                            LogUtil.d(TAG, "cameralist is null or cameralist size is 0");
                            return;
                        }
                        if (deviceInfo.getCameraNum() == 1 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() == 1) {
                            LogUtil.d(TAG, "cameralist have one camera");
                            EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, 0);
                            if (cameraInfo == null) {
                                return;
                            }
                            Intent intent = new Intent(CameraListActivity.this, PlayBackListActivity.class);
                            intent.putExtra(RemoteListContant.QUERY_DATE_INTENT_KEY, DateTimeUtil.getNow());
                            intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
                            startActivity(intent);
                        }
                        break;
                    case R.id.tab_alarmlist_btn:
                        //消息列表
                        break;
                    case R.id.tab_setdevice_btn:
                        //设置
                        break;
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {

            private boolean aBoolean;

            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            aBoolean = EZOpenSDK.getInstance().deleteDevice(mSerialNoStr);
                            if (aBoolean) {
                                cameraListData();
                            }
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return false;
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_camera_list;
    }

    private void handleString() {
        // 初始化数据
        mSerialNoStr = "";
        mSerialVeryCodeStr = "";
        deviceType = "";
        LogUtil.errorLog(TAG, resultString);
        String[] newlineCharacterSet = {
                "\n\r", "\r\n", "\r", "\n"};
        String stringOrigin = resultString;
        // 寻找第一次出现的位置
        int a = -1;
        int firstLength = 1;
        for (String string : newlineCharacterSet) {
            if (a == -1) {
                a = resultString.indexOf(string);
                if (a > stringOrigin.length() - 3) {
                    a = -1;
                }
                if (a != -1) {
                    firstLength = string.length();
                }
            }
        }

        // 扣去第一次出现回车的字符串后，剩余的是第二行以及以后的
        if (a != -1) {
            resultString = resultString.substring(a + firstLength);
        }
        // 寻找最后一次出现的位置
        int b = -1;
        for (String string : newlineCharacterSet) {
            if (b == -1) {
                b = resultString.indexOf(string);
                if (b != -1) {
                    mSerialNoStr = resultString.substring(0, b);
                    firstLength = string.length();
                }
            }
        }

        // 寻找遗失的验证码阶段
        if (mSerialNoStr != null && b != -1 && (b + firstLength) <= resultString.length()) {
            resultString = resultString.substring(b + firstLength);
        }

        // 再次寻找回车键最后一次出现的位置
        int c = -1;
        for (String string : newlineCharacterSet) {
            if (c == -1) {
                c = resultString.indexOf(string);
                if (c != -1) {
                    mSerialVeryCodeStr = resultString.substring(0, c);
                }
            }
        }

        // 寻找CS-C2-21WPFR 判断是否支持wifi
        if (mSerialNoStr != null && c != -1 && (c + firstLength) <= resultString.length()) {
            resultString = resultString.substring(c + firstLength);
        }
        if (resultString != null && resultString.length() > 0) {
            deviceType = resultString;
        }

        if (b == -1) {
            mSerialNoStr = resultString;
        }

        if (mSerialNoStr == null) {
            mSerialNoStr = stringOrigin;
        }
        LogUtil.debugLog(TAG, "mSerialNoStr = " + mSerialNoStr + ",mSerialVeryCodeStr = " + mSerialVeryCodeStr
                + ",deviceType = " + deviceType);
    }

    public EZCameraInfo getCameraInfoFromDevice(EZDeviceInfo deviceInfo, int camera_index) {
        if (deviceInfo == null) {
            return null;
        }
        if (deviceInfo.getCameraNum() > 0 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() > camera_index) {
            return deviceInfo.getCameraInfoList().get(camera_index);
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraListData();
    }
}
