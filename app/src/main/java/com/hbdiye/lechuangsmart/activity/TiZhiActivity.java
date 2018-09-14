package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.bean.TiZhiBean;
import com.hbdiye.lechuangsmart.views.CircleProgressBar;
import com.yiwent.viewlib.ShiftyTextview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;

public class TiZhiActivity extends BaseActivity {

    @BindView(R.id.progress_bar)
    CircleProgressBar progressBar;
    @BindView(R.id.progress_bar1)
    CircleProgressBar progressBar1;
    @BindView(R.id.progress_bar2)
    CircleProgressBar progressBar2;
    @BindView(R.id.progress_bar3)
    CircleProgressBar progressBar3;
    @BindView(R.id.stv_tzl)
    ShiftyTextview stvTzl;
    @BindView(R.id.stv_sfl)
    ShiftyTextview stvSfl;
    @BindView(R.id.stv_jrl)
    ShiftyTextview stvJrl;
    @BindView(R.id.stv_nzzfdj)
    ShiftyTextview stvNzzfdj;
    @BindView(R.id.tv_rcjy)
    TextView tvRcjy;
    @BindView(R.id.tv_ydjy)
    TextView tvYdjy;
    @BindView(R.id.tv_ysjy)
    TextView tvYsjy;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;

    @Override
    protected void initData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BTLTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"BTLTP\",\"page\":\"1\",\"pageSize\":\"6\",\"phone\":\"15932670382\",\"type\":\"8\"}");
    }

    @Override
    protected String getTitleName() {
        return "体质检测";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_ti_zhi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
//        try {
        TiZhiBean tiZhiBean = new Gson().fromJson(payload, TiZhiBean.class);
        String datetime = tiZhiBean.data.list.get(0).datetime;
        TiZhiBean.Data.Lists.JsonStr jsonStr = tiZhiBean.data.list.get(0).jsonStr;
        double adiposerate1 = tiZhiBean.data.list.get(0).jsonStr.data.adiposerate;
        double muscle = tiZhiBean.data.list.get(0).jsonStr.data.muscle;
        double moisture = tiZhiBean.data.list.get(0).jsonStr.data.moisture;
        int visceralfat = tiZhiBean.data.list.get(0).jsonStr.data.visceralfat;
        String common = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.common;
        String sport = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.sport;
        String foot = tiZhiBean.data.list.get(0).jsonStr.data.suggestion.foot;
        Log.e("TTT", adiposerate1 + "===" + muscle + "===" + moisture + "===" + visceralfat);
        progressBar.setAnimProgress(adiposerate1);
        progressBar1.setAnimProgress(moisture);
        progressBar2.setAnimProgress(muscle);
        progressBar3.setAnimProgress(visceralfat);

        stvTzl.setPostfixString("%");
        stvTzl.setDuration(3000);
        stvTzl.setNumberString(adiposerate1 + "");

        stvSfl.setPostfixString("%");
        stvSfl.setDuration(3000);
        stvSfl.setNumberString(moisture + "");

        stvJrl.setPostfixString("%");
        stvJrl.setDuration(3000);
        stvJrl.setNumberString(muscle + "");

        stvNzzfdj.setDuration(3000);
        stvNzzfdj.setNumberString(visceralfat + "");

        tvRcjy.setText(common);
        tvYdjy.setText(sport);
        tvYsjy.setText(foot);
        Pattern p = Pattern.compile("\\s");
        Matcher m = p.matcher(datetime);
        datetime = m.replaceAll("\n");
        tvDate.setText(datetime);
//            JSONObject jsonObject=new JSONObject(payload);
//            JSONObject data = jsonObject.getJSONObject("data");
//            JSONArray list = data.getJSONArray("list");
//            for (int i = 0; i < list.length(); i++) {
//                JSONObject jsonObject1 = list.getJSONObject(i);
////                double moisture = jsonObject1.getDouble("moisture");
//                double adiposerate = jsonObject1.getDouble("adiposerate");
//                double muscle = jsonObject1.getDouble("muscle");
//                int visceralfat = jsonObject1.getInt("visceralfat");
//                System.out.println("   "+adiposerate+" "+muscle+"  "+visceralfat);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Double moisture = tiZhiBean.data.list.get(0).jsonStr.dataStr.moisture;//水分率
//        int adiposerate = tiZhiBean.data.list.get(0).jsonStr.dataStr.adiposerate;//脂肪率
//        Double muscle = tiZhiBean.data.list.get(0).jsonStr.dataStr.muscle;//肌肉量
//        int visceralfat = tiZhiBean.data.list.get(0).jsonStr.dataStr.visceralfat;//内脏脂肪等级
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}
