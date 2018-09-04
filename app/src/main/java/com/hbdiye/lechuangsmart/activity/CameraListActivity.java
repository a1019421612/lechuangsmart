package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.CameraAdapter;
import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    }

    private void cameraListData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    List<EZDeviceInfo> deviceList = EZOpenSDK.getInstance().getDeviceList(0, 10);
                    if (deviceList != null && deviceList.size() > 0) {
                        //设备存在
                        if (mList.size()>0){
                            mList.clear();
                        }
                        mList.addAll(deviceList);
                        CameraListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }else {
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
                startActivity(new Intent(CameraListActivity.this,AddCameraActivity.class));
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EZDeviceInfo deviceInfo = mList.get(position);
                EZCameraInfo cameraInfo = getCameraInfoFromDevice(deviceInfo, 0);
//                            intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
//                            intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
                if (mList.get(position).getStatus()==1){
                    startActivity(new Intent(CameraListActivity.this, EZRealPlayActivity.class)
                            .putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo)
                            .putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo));
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
                            if (aBoolean){
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
    public  EZCameraInfo getCameraInfoFromDevice(EZDeviceInfo deviceInfo, int camera_index) {
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
