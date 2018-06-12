package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.HWDeviceAdapter;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.TipsUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.kookong.app.data.BrandList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HWDeviceListActivity extends BaseActivity {
    /**
     * 获取某设备的品牌列表
     * public int STB = 1; //机顶盒
     * public int TV  = 2; //电视
     * public int BOX = 3; //网络盒子
     * public int DVD = 4; //DVD
     * public int AC  = 5; //空调
     * public int PRO = 6; //投影仪
     * public int PA  = 7; //功放
     * public int FAN = 8; //风扇
     * public int SLR = 9; //单反相机
     * public int Light = 10; //开关灯泡
     * public int AIR_CLEANER = 11;// 空气净化器
     * public int WATER_HEATER = 12;// 热水器
     */
    @BindView(R.id.rv_hw_device)
    RecyclerView rvHwDevice;
    private int type;

    private List<BrandList.Brand> mList = new ArrayList<>();
    private HWDeviceAdapter adapter;
    private String mac;

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", -1);
        mac = getIntent().getStringExtra("mac");
        if (type == 1) {
            tvBaseTitle.setText("机顶盒");
        } else if (type == 2) {
            tvBaseTitle.setText("电视");
        } else if (type == 3) {
            tvBaseTitle.setText("网络盒子");
        } else if (type == 4) {
            tvBaseTitle.setText("DVD播放机");
        } else if (type == 5) {
            tvBaseTitle.setText("空调");
        } else if (type == 6) {
            tvBaseTitle.setText("投影仪");
        } else if (type == 7) {
            tvBaseTitle.setText("功放");
        } else if (type == 8) {
            tvBaseTitle.setText("风扇");
        } else if (type == 9) {
            tvBaseTitle.setText("相机");
        } else if (type == 11) {
            tvBaseTitle.setText("空气净化器");
        } else if (type == 12) {
            tvBaseTitle.setText("热水器");
        } else {
            tvBaseTitle.setText("---");
        }
        getBrandListFromNet(type);
    }

    private void getBrandListFromNet(int type) {
        KookongSDK.getBrandListFromNet(type, new IRequestResult<BrandList>() {
            @Override
            public void onSuccess(String msg, BrandList result) {
                List<BrandList.Brand> stbs = result.brandList;
                if (mList.size() > 0) {
                    mList.clear();
                }
                mList.addAll(stbs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Integer errorCode, String msg) {
                SmartToast.show(msg);

            }
        });
    }

    @Override
    protected String getTitleName() {
        return "";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHwDevice.setLayoutManager(manager);
        adapter = new HWDeviceAdapter(mList);
        rvHwDevice.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(HWDeviceListActivity.this, PicYaoKongActivity.class)
                        .putExtra("type", type)
                        .putExtra("mac",mac)
                        .putExtra("brandId", mList.get(position).brandId));
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_hwdevice_list;
    }

}
