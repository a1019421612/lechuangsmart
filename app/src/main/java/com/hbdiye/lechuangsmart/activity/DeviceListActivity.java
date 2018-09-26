package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.BrandListAdapter;
import com.hbdiye.lechuangsmart.adapter.DeviceListAdapter;
import com.hbdiye.lechuangsmart.adapter.ModeListAdapter;
import com.hbdiye.lechuangsmart.bean.BrandListBean;
import com.hbdiye.lechuangsmart.bean.ModeListBean;
import com.hbdiye.lechuangsmart.bean.RemoteDeviceBean;
import com.hbdiye.lechuangsmart.util.TipsUtil;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class DeviceListActivity extends BaseActivity {

    @BindView(R.id.rv_device_one)
    RecyclerView rvDeviceOne;
    @BindView(R.id.rv_device_two)
    RecyclerView rvDeviceTwo;
    @BindView(R.id.rv_device_three)
    RecyclerView rvDeviceThree;

    private DeviceListAdapter adapter;
    private List<RemoteDeviceBean.Data> mList = new ArrayList<>();

    private BrandListAdapter adapter_brand;
    private List<BrandListBean.Data> mList_brand = new ArrayList<>();

    private ModeListAdapter adapter_mode;
    private List<ModeListBean.Data> mList_mode = new ArrayList<>();

    private int flag = 0;
    private int device_id;
    private int brand_id;
    private String uuid;

    private SceneDialog sceneDialog;
    private String mode_id;

    @Override
    protected void initData() {
        uuid = getIntent().getStringExtra("uuid");
        getDeviceList();
    }

    private void getDeviceList() {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.GETDEVICELIST))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        RemoteDeviceBean remoteDeviceBean = new Gson().fromJson(response, RemoteDeviceBean.class);
                        if (remoteDeviceBean.success) {
                            if (mList.size() > 0) {
                                mList.clear();
                            }
                            List<RemoteDeviceBean.Data> data = remoteDeviceBean.data;
                            mList.addAll(data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected String getTitleName() {
        return "添加设备";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceOne.setLayoutManager(manager);
        adapter = new DeviceListAdapter(mList);
        rvDeviceOne.setAdapter(adapter);

        LinearLayoutManager manager_two = new LinearLayoutManager(this);
        manager_two.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceTwo.setLayoutManager(manager_two);
        adapter_brand = new BrandListAdapter(mList_brand);
        rvDeviceTwo.setAdapter(adapter_brand);

        LinearLayoutManager manager_three = new LinearLayoutManager(this);
        manager_three.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceThree.setLayoutManager(manager_three);
        adapter_mode = new ModeListAdapter(mList_mode);
        rvDeviceThree.setAdapter(adapter_mode);

        handlerClicker();
    }

    private void handlerClicker() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 2) {
                    flag = 1;
                    rvDeviceOne.setVisibility(View.GONE);
                    rvDeviceThree.setVisibility(View.GONE);
                    rvDeviceTwo.setVisibility(View.VISIBLE);
                } else if (flag == 1) {
                    flag = 0;
                    rvDeviceThree.setVisibility(View.GONE);
                    rvDeviceTwo.setVisibility(View.GONE);
                    rvDeviceOne.setVisibility(View.VISIBLE);
                } else if (flag == 0) {
                    finish();
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                device_id = mList.get(position).id;
                getBrandList(device_id + "");
            }
        });
        adapter_brand.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                brand_id = mList_brand.get(position).id;
                getModeList(device_id + "", brand_id + "");
            }
        });
        adapter_mode.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int key_squency = mList_mode.get(position).key_squency;
                String bn = mList_mode.get(position).bn;
                mode_id = mList_mode.get(position).id;
                checkIrDevice(device_id,brand_id, mode_id,uuid);
            }
        });
    }

    private void checkIrDevice(int device_id, int brand_id, String mode_id, String uuid) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.CHECKIRDEVICE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("type",device_id+"")
                .addParams("brand_id",brand_id+"")
                .addParams("modelid",mode_id+"")
                .addParams("uuid",uuid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String info = jsonObject.getString("info");
                            SmartToast.show(info);
                            if (success){
                                AlertDialog.Builder builder=new AlertDialog.Builder(DeviceListActivity.this);
                                builder.setMessage("设备有响应吗？");
                                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sceneDialog = new SceneDialog(DeviceListActivity.this, R.style.MyDialogStyle, dailogClicer, "设备名称");
                                        sceneDialog.show();
                                    }
                                });
                                builder.setNegativeButton("否",null);
                                builder.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    public View.OnClickListener dailogClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName().trim();
                    if (TextUtils.isEmpty(sceneName)) {
                        SmartToast.show("设备名称不能为空");
                    } else {
                        addIrDevice(uuid,device_id,mode_id,sceneName);
                    }
                    break;
            }
        }
    };

    private void addIrDevice(String uuid, int device_id, String mode_id, String sceneName) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.ADDIRDEVICE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("type",device_id+"")
                .addParams("modelid",mode_id+"")
                .addParams("uuid",uuid)
                .addParams("name",sceneName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SmartToast.show(response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String info = jsonObject.getString("info");
                            if (success){
                                if (sceneDialog!=null){
                                    sceneDialog.dismiss();
                                }
                                SmartToast.show(info);
                                setResult(101);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getModeList(String s, String s1) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.GETMODELIST))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("device_id", s)
                .addParams("brand_id", s1)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        ModeListBean modeListBean = new Gson().fromJson(response, ModeListBean.class);
                        if (modeListBean.success) {
                            if (mList_mode.size() > 0) {
                                mList_mode.clear();
                            }
                            flag = 2;
                            rvDeviceOne.setVisibility(View.GONE);
                            rvDeviceTwo.setVisibility(View.GONE);
                            rvDeviceThree.setVisibility(View.VISIBLE);
                            mList_mode.addAll(modeListBean.data);
                            adapter_mode.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getBrandList(String s) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.GETBRANDLIST))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("device_id", s)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        BrandListBean brandListBean = new Gson().fromJson(response, BrandListBean.class);
                        if (brandListBean.success) {
                            if (mList_brand.size() > 0) {
                                mList_brand.clear();
                            }
                            flag = 1;
                            rvDeviceOne.setVisibility(View.GONE);
                            rvDeviceTwo.setVisibility(View.VISIBLE);
                            mList_brand.addAll(brandListBean.data);
                            adapter_brand.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device_list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            //调用方法
            if (flag == 2) {
                flag = 1;
                rvDeviceOne.setVisibility(View.GONE);
                rvDeviceThree.setVisibility(View.GONE);
                rvDeviceTwo.setVisibility(View.VISIBLE);
            }else if (flag == 1) {
                flag = 0;
                rvDeviceThree.setVisibility(View.GONE);
                rvDeviceTwo.setVisibility(View.GONE);
                rvDeviceOne.setVisibility(View.VISIBLE);
            } else if (flag == 0) {
                finish();
            }
        }
        return false;
    }

}
