package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.TiZhiAdapter;
import com.hbdiye.lechuangsmart.bean.TiZhiBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.DashboardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;

public class TizhongActivity extends BaseActivity {

    @BindView(R.id.dashboard_view)
    DashboardView dashboardView;
    @BindView(R.id.tv_rcjy)
    TextView tvRcjy;
    @BindView(R.id.tv_ydjy)
    TextView tvYdjy;
    @BindView(R.id.tv_ysjy)
    TextView tvYsjy;
    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private String phone;
    private List<TiZhiBean.Data.Lists> mList=new ArrayList<>();
    private TiZhiAdapter adapter;
    private int flag=0;
    //    "{pn\":\"BTLTP\",\"page\":\"1\",\"pageSize\":\"6\",\"phone\":\"%@\",\"type\":\"9\"}"
    @Override
    protected void initData() {
//        int i = new Random().nextInt(150);
//        dashboardView.setCreditValueWithAnim(currentKG(i));
//        dashboardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int i = new Random().nextInt(150);
//                dashboardView.setCreditValueWithAnim(currentKG(i));
//            }
//        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BTLTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        phone = (String) SPUtils.get(this, "mobilephone", "");
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        mConnection.sendTextMessage("{\"pn\":\"BTLTP\",\"page\":\"1\",\"pageSize\":\"6\",\"phone\":\"15932670382\",\"type\":\"9\"}");
    }

    private int currentKG(double i) {
        return (int) (i + 350);
    }

    @Override
    protected String getTitleName() {
        return "体重";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTime.setLayoutManager(manager);
        adapter=new TiZhiAdapter(mList);
        rvTime.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (flag==position){
                    return;
                }
                flag=position;
                TiZhiBean.Data.Lists lists = mList.get(position);
                String common = lists.jsonStr.data.suggestion.common;
                String sport = lists.jsonStr.data.suggestion.sport;
                String foot = lists.jsonStr.data.suggestion.foot;
                tvRcjy.setText(common);
                tvYdjy.setText(sport);
                tvYsjy.setText(foot);
                Double weight = lists.jsonStr.data.weight;
                dashboardView.setWeightValue(String.valueOf(weight));
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tizhong;
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
        if (tiZhiBean.data.list==null||tiZhiBean.data.list.size()==0){
            return;
        }
        String common = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.common;
        String sport = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.sport;
        String foot = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.foot;
        tvRcjy.setText(common);
        tvYdjy.setText(sport);
        tvYsjy.setText(foot);
        Double weight = tiZhiBean.data.list.get(0).jsonStr.data.weight;
        List<TiZhiBean.Data.Lists> list = tiZhiBean.data.list;
        dashboardView.setCreditValueWithAnim(currentKG(weight));
        dashboardView.setWeightValue(String.valueOf(weight));
        mList.addAll(list);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}
