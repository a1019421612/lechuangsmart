package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

    @Override
    protected void initData() {
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
                int brand_id = mList_brand.get(position).id;
                getModeList(device_id + "", brand_id + "");
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
