package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.zhouyou.view.seekbar.SignSeekBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class IrKongTiaoActivity extends BaseActivity {
    @BindView(R.id.tv_kt_power)
    TextView tvKtPower;
    @BindView(R.id.tv_kt_mode)
    TextView tvKtMode;
    @BindView(R.id.tv_kt_speed)
    TextView tvKtSpeed;
    @BindView(R.id.tv_kt_director)
    TextView tvKtDirector;
    @BindView(R.id.tv_kt_saof)
    TextView tvKtSaof;
    @BindView(R.id.tv_kt_tem_down)
    TextView tvKtTemDown;
    @BindView(R.id.tv_kt_tem_up)
    TextView tvKtTemUp;
    @BindView(R.id.tv_kt_cur_mode)
    TextView tvKtCurMode;
    @BindView(R.id.tv_kt_cur_speed)
    TextView tvKtCurSpeed;
    @BindView(R.id.tv_kt_cur_temp)
    TextView tvKtCurTemp;

    private SignSeekBar signSeekBar;

    private String uuid;
    private InfraredBean.Remote_list.Ir_list data;
    private String type;
    private String modelid;
    private String key;

    String[] array_mode= {"自动","制冷","制热","除湿","送风"};
    String[] mode_code={"auto","cold","heat","dehumidification","airsupply"};

    String[] array_speed={"自动","最小","中等","最大"};
    String[] speed_code={"auto","min","medium","max"};

    String[] array_direction={"自动","上下扫-开","上下扫-关","左右扫-开","左右扫-关"};
    String[] direction_code={"auto","updown_on","updown_off","letfright_on","leftright_off"};

    @Override
    protected void initData() {
        uuid = getIntent().getStringExtra("uuid");
        data = (InfraredBean.Remote_list.Ir_list) getIntent().getSerializableExtra("data");
        type = data.type;
        modelid = data.modelid;
        key = data.key_squency;
    }

    @Override
    protected String getTitleName() {
        return "空调";
    }

    @Override
    protected void initView() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signSeekBar = (SignSeekBar) findViewById(R.id.seek_bar);
        signSeekBar.getConfigBuilder()
                .min(16)
                .max(30)
                .progress(2)
                .sectionCount(14)
                .trackColor(ContextCompat.getColor(this, R.color.color_gray))
                .secondTrackColor(ContextCompat.getColor(this, R.color.color_blue))
                .thumbColor(ContextCompat.getColor(this, R.color.color_blue))
                .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .sectionTextSize(16)
                .thumbTextColor(ContextCompat.getColor(this, R.color.color_red))
                .thumbTextSize(18)
                .signColor(ContextCompat.getColor(this, R.color.color_green))
                .signTextSize(18)
                .autoAdjustSectionMark()
                .sectionTextPosition(SignSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();
        signSeekBar.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                //fromUser 表示是否是用户触发 是否是用户touch事件产生
                String s = String.format(Locale.CHINA, "onChanged int:%d, float:%.1f", progress, progressFloat);
//                progressText.setText(s);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {
                String s = String.format(Locale.CHINA, "onActionUp int:%d, float:%.1f", progress, progressFloat);
//                progressText.setText(s);
            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                String s = String.format(Locale.CHINA, "onFinally int:%d, float:%.1f", progress, progressFloat);
                Log.e("sss", progress + "℃");
                sendCode("temperature",progress+"");
//                temp=progress+"";
//                tvKtCurTemp.setText(temp+"℃");
//                makeFkey(mode,temp,speed);
//                getFpulse();
//                mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
//                progressText.setText(s + getContext().getResources().getStringArray(R.array.labels)[progress]);
            }
        });

    }

    private boolean flag=true;
    @OnClick({R.id.tv_kt_power, R.id.tv_kt_mode, R.id.tv_kt_speed, R.id.tv_kt_director, R.id.tv_kt_saof, R.id.tv_kt_tem_down, R.id.tv_kt_tem_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kt_power:
                //电源
                if (flag){
                    sendCode("powerstate","on");
                    flag=false;
                }else {
                    sendCode("powerstate","off");
                    flag=true;
                }
//                tvKtCurMode.setText(mode_array[Integer.parseInt(mode)]);
//                tvKtCurSpeed.setText(speed);
//                tvKtCurTemp.setText(temp+"℃");

                break;
            case R.id.tv_kt_mode:
                //模式

                showDialog(array_mode,mode_code,tvKtMode,"mode");

                break;
            case R.id.tv_kt_speed:
//                风速
                showDialog(array_speed,speed_code,tvKtSpeed,"windspeed");
                break;
            case R.id.tv_kt_director:
//                风向
                showDialog(array_direction,direction_code,tvKtDirector,"winddirection");
                break;
            case R.id.tv_kt_saof:
//                扫风
                break;
            case R.id.tv_kt_tem_down:
//                温度-
                break;
            case R.id.tv_kt_tem_up:
//                温度+
                break;
        }
    }

    private void showDialog(final String[] array, final String[] code, final TextView tv, final String hanlde) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv.setText(array[which]);
                sendCode(hanlde, code[which]);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void sendCode(String handle, String val) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(InterfaceManager.getInstance().getURL(InterfaceManager.SENDCODE));
        postFormBuilder.addParams("app_id", InterfaceManager.APPID);
        postFormBuilder.addParams("app_type", InterfaceManager.APPKEY);
        postFormBuilder.addParams("type", type);
        postFormBuilder.addParams("uuid", uuid);
        postFormBuilder.addParams("modelid", modelid);
        postFormBuilder.addParams("handle", handle);
        postFormBuilder.addParams("key", key);
        if (!TextUtils.isEmpty(val)) {
            postFormBuilder.addParams("val", val);
        }
        postFormBuilder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String info = jsonObject.getString("info");
                            SmartToast.show(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_kong_tiao;
    }
}
