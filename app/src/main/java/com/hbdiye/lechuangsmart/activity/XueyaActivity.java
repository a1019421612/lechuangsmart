package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.TiZhiAdapter;
import com.hbdiye.lechuangsmart.bean.TiZhiBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.yiwent.viewlib.ShiftyTextview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;

public class XueyaActivity extends BaseActivity {

    @BindView(R.id.iv_xueya)
    ImageView ivXueya;
    @BindView(R.id.stv_gaoya)
    ShiftyTextview stvGaoya;
    @BindView(R.id.stv_diya)
    ShiftyTextview stvDiya;
    @BindView(R.id.tv_rcjy)
    TextView tvRcjy;
    @BindView(R.id.tv_ydjy)
    TextView tvYdjy;
    @BindView(R.id.tv_ysjy)
    TextView tvYsjy;
    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.tv_pulse)
    TextView tvPulse;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private String phone;
    private List<TiZhiBean.Data.Lists> mList = new ArrayList<>();
    private TiZhiAdapter adapter;
    private int flag = 0;

    @Override
    protected void initData() {
        AnimationDrawable animationDrawable = (AnimationDrawable) ivXueya.getDrawable();
        animationDrawable.start();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BTLTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        phone = (String) SPUtils.get(this, "mobilephone", "");
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        mConnection.sendTextMessage("{\"pn\":\"BTLTP\",\"page\":\"1\",\"pageSize\":\"6\",\"phone\":\"15932670382\",\"type\":\"7\"}");
    }

    @Override
    protected String getTitleName() {
        return "血压";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTime.setLayoutManager(manager);
        adapter = new TiZhiAdapter(mList);
        rvTime.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (flag == position) {
                    return;
                }
                flag = position;
                TiZhiBean.Data.Lists lists = mList.get(position);
                String common = lists.jsonStr.data.suggestion.common;
                String sport = lists.jsonStr.data.suggestion.sport;
                String foot = lists.jsonStr.data.suggestion.foot;
                tvRcjy.setText(common);
                tvYdjy.setText(sport);
                tvYsjy.setText(foot);
                int diastolic = lists.jsonStr.data.diastolic;
                stvDiya.setDuration(3000);
                stvDiya.setNumberString(diastolic + "");
                int systolic = lists.jsonStr.data.systolic;
                stvGaoya.setDuration(3000);
                stvGaoya.setNumberString(systolic + "");
                int pulse =lists.jsonStr.data.pulse;
                tvPulse.setText("丨心率："+pulse+"次/分");
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_xueya;
    }


    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("BTLTP")) {
                parseData(payload);
            }
        }
    }

    private void parseData(String payload) {
        TiZhiBean tiZhiBean = new Gson().fromJson(payload, TiZhiBean.class);
        if (tiZhiBean.data.list == null || tiZhiBean.data.list.size() == 0) {
            return;
        }
        String common = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.common;
        String sport = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.sport;
        String foot = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.foot;
        tvRcjy.setText(common);
        tvYdjy.setText(sport);
        tvYsjy.setText(foot);
        int diastolic = tiZhiBean.data.list.get(0).jsonStr.data.diastolic;
        stvDiya.setDuration(3000);
        stvDiya.setNumberString(diastolic + "");
        int systolic = tiZhiBean.data.list.get(0).jsonStr.data.systolic;
        stvGaoya.setDuration(3000);
        stvGaoya.setNumberString(systolic + "");
        int pulse = tiZhiBean.data.list.get(0).jsonStr.data.pulse;
        tvPulse.setText("丨心率："+pulse+"次/分");
        List<TiZhiBean.Data.Lists> list = tiZhiBean.data.list;
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}
