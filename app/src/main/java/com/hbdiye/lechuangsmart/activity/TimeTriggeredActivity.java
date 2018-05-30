package com.hbdiye.lechuangsmart.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.LinkageSettingBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private OptionsPickerView pickerBuilder;

    private String[] items={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private boolean[] defaultChoice={false,false,false,false,false,false,false};

    String finalWeek="";
    private LinkageSettingBean.Linkage linkage;
    @Override
    protected void initData() {
        linkage= (LinkageSettingBean.Linkage) getIntent().getSerializableExtra("LinkageData");
        String cronExpression = linkage.timingRecord.cronExpression;
        if (TextUtils.isEmpty(cronExpression)){
            tvTimeRepeat.setText("仅一次");
        }else {
            tvTimeRepeat.setText(cronExpression);
        }
    }

    @Override
    protected String getTitleName() {
        return "设置时间触发条件";
    }

    @Override
    protected void initView() {
        initCustomTimePicker();
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        tvTimeRepeat.setText(finalWeek);
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
                int i = options1 * 3600 + options2 * 60 + options3;
//                tvBaseTitle.setText(ContentConfig.secToTime(options1*3600+options2*60+options3));
//                mConnection.sendTextMessage("{\"pn\":\"LTUTP\",\"ltID\":\"" + mList.get(timeFlag).id + "\",\"proActID\":\"" + mList.get(timeFlag).proActID + "\",\"delaytime\":\"" + i + "\"}");
            }
        }).setLabels("时", "分", "秒")
                .isCenterLabel(true)
                .build();
        pickerBuilder.setNPicker(ContentConfig.getTimeHours(), ContentConfig.getTimeMin(), ContentConfig.getTimeSeco());
    }
}
