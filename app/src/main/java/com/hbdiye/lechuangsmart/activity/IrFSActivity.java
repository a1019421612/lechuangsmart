package com.hbdiye.lechuangsmart.activity;

import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class IrFSActivity extends BaseActivity{
    @BindView(R.id.tv_fs_power)
    TextView tvFsPower;
    @BindView(R.id.tv_fs_time)
    TextView tvFsTime;
    @BindView(R.id.tv_fs_speed)
    TextView tvFsSpeed;
    @BindView(R.id.tv_fs_type)
    TextView tvFsType;
    @BindView(R.id.tv_fs_baifeng)
    TextView tvFsBaifeng;
    @BindView(R.id.tv_fs_novoice)
    TextView tvFsNovoice;

    private String uuid;
    private InfraredBean.Remote_list.Ir_list data;
    private String type;
    private String modelid;
    private String key;

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
        return "电风扇";
    }

    @Override
    protected void initView() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fs;
    }
    @OnClick({R.id.tv_fs_power, R.id.tv_fs_time, R.id.tv_fs_speed, R.id.tv_fs_type, R.id.tv_fs_baifeng, R.id.tv_fs_novoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fs_power:
                //电源
                sendCode("电源");
                break;
            case R.id.tv_fs_time:
                sendCode("定时");
                break;
            case R.id.tv_fs_speed:
                sendCode("风速");
                break;
            case R.id.tv_fs_type:
                sendCode("风类");
                break;
            case R.id.tv_fs_baifeng:
                sendCode("摆风");
                break;
            case R.id.tv_fs_novoice:
                break;
        }
    }
    private void sendCode(String handle) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.SENDCODE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("type",type)
                .addParams("uuid",uuid)
                .addParams("modelid",modelid)
                .addParams("handle",handle)
                .addParams("key",key)
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
}
