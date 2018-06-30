package com.hbdiye.lechuangsmart.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.LinkageSettingBean;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * 设置时间触发条件
 */
public class TimeTriggeredActivity extends BaseActivity {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_time_dotime)
    LinearLayout llTimeDotime;
    @BindView(R.id.tv_time_repeat)
    TextView tvTimeRepeat;
    @BindView(R.id.ll_time_repeat)
    LinearLayout llTimeRepeat;

    private String TAG=TimeTriggeredActivity.class.getSimpleName();

    private OptionsPickerView pickerBuilder;

    private String[] items={"周一","周二","周三","周四","周五","周六","周日"};
    private boolean[] defaultChoice={false,false,false,false,false,false,false};

    String finalWeek="";
    private LinkageSettingBean.Linkage linkage;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    @Override
    protected void initData() {

        linkage= (LinkageSettingBean.Linkage) getIntent().getSerializableExtra("LinkageData");
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnection();
        String cronExpression = linkage.timingRecord.cronExpression;
        String timing = linkage.timingRecord.timing;
        tvTime.setText(timing);
        if (TextUtils.isEmpty(cronExpression)){
            tvTimeRepeat.setText("仅一次");
        }else {
            tvTimeRepeat.setText(cronExpression);
            String[] split = cronExpression.split(",");
            for (int i = 0; i < split.length; i++) {
                for (int j = 0; j < items.length; j++) {
                    String item = items[j];
                    if (item.equals(split[i])){
                        defaultChoice[j]=true;
                    }
                }
            }
        }
    }

    @Override
    protected String getTitleName() {
        return "设置时间触发条件";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseEdit.setImageResource(R.drawable.duigou);
        initCustomTimePicker();
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBaseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int timingType=-1;
                String repeatMode = tvTimeRepeat.getText().toString();
                if (repeatMode.equals("仅一次")){
                    timingType=2;
                }else {
                    timingType=3;
                }
                mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\""+linkage.id+"\",\"timing\":\""+tvTime.getText().toString()+"\",\"timingType\":"+timingType+",\"cronExpression\":\""+tvTimeRepeat.getText().toString()+"\"}");
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_time_triggered;
    }

    @OnClick({R.id.ll_time_dotime, R.id.ll_time_repeat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_time_dotime:
                if (pickerBuilder != null){
                    pickerBuilder.show();
                }
                break;
            case R.id.ll_time_repeat:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMultiChoiceItems(items, defaultChoice, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            defaultChoice[which]=isChecked;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < defaultChoice.length; i++) {
                            if (defaultChoice[i]){
                                if (TextUtils.isEmpty(finalWeek)){
                                    finalWeek=items[i];
                                }else {
                                    finalWeek=finalWeek+","+items[i];
                                }
                            }
                        }
                        if (TextUtils.isEmpty(finalWeek)){
                            tvTimeRepeat.setText("仅一次");
                        }else {
                            tvTimeRepeat.setText(finalWeek);
                        }
                    }
                });
                builder.show();
                break;
        }
    }
    private void initCustomTimePicker() {
        pickerBuilder = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                tvBaseTitle.setText(options1+"时"+options2+"分"+options3+"秒");
                Integer hour = ContentConfig.getTimeHours().get(options1);
                Integer minu = ContentConfig.getTimeMin().get(options2);
                Integer second = ContentConfig.getTimeSeco().get(options3);
                String finalTime_H=hour+"";
                String finalTime_M=minu+"";
                String finalTime_S=second+"";
                if (hour<10){
                    finalTime_H="0"+hour;
                }
                if (minu<10){
                    finalTime_M="0"+minu;
                }
                if (second<10){
                    finalTime_S="0"+second;
                }
                tvTime.setText(finalTime_H+":"+finalTime_M+":"+finalTime_S);
            }
        }).setLabels("时", "分", "秒")
                .isCenterLabel(true)
                .build();
        pickerBuilder.setNPicker(ContentConfig.getTimeHours(), ContentConfig.getTimeMin(), ContentConfig.getTimeSeco());
    }

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
//            mConnection.sendTextMessage("{\"pn\":\"LDLTP\",\"type\":1}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");

            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(TimeTriggeredActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"LUTP\"")) {
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    private void parseData(String payload) {
    }

    private void socketConnection() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onstop");
        mConnection.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onrestart");
        if (mConnection != null) {
            socketConnection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onstop");
        mConnection.disconnect();
    }
}
