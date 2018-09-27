package com.hbdiye.lechuangsmart.activity;

import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.hbdiye.lechuangsmart.util.ImageUtil;
import com.hbdiye.lechuangsmart.views.RoundMenuView;
import com.hbdiye.lechuangsmart.views.TVNumDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class IrJDHActivity extends BaseActivity{
    @BindView(R.id.tv_jdh_power)
    TextView tvJdhPower;
    @BindView(R.id.tv_jdh_menu)
    TextView tvJdhMenu;
    @BindView(R.id.tv_jdh_number)
    TextView tvJdhNumber;
    @BindView(R.id.tv_jdh_novoice)
    TextView tvJdhNovoice;
    @BindView(R.id.tv_jdh_back)
    TextView tvJdhBack;
    @BindView(R.id.tv_jdh_channel_up)
    TextView tvJdhChannelUp;
    @BindView(R.id.tv_jdh_channel_down)
    TextView tvJdhChannelDown;
    @BindView(R.id.tv_jdh_voice_up)
    TextView tvJdhVoiceUp;
    @BindView(R.id.tv_jdh_voice_down)
    TextView tvJdhVoiceDown;
    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;
    
    private TVNumDialog dialog;

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
        return "机顶盒";
    }

    @Override
    protected void initView() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initRoundMenu();
    }
    private void initRoundMenu() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrJDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下
                sendCode("上");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrJDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //左
                sendCode("左");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrJDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                上
                sendCode("上");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrJDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                右
                sendCode("右");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(IrJDHActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       ok
                        sendCode("确认");
                    }
                });
    }
    @Override
    protected int getLayoutID() {
        return R.layout.activity_jdh;
    }
    @OnClick({R.id.tv_jdh_power, R.id.tv_jdh_menu, R.id.tv_jdh_number, R.id.tv_jdh_novoice, R.id.tv_jdh_back, R.id.tv_jdh_channel_up, R.id.tv_jdh_channel_down, R.id.tv_jdh_voice_up, R.id.tv_jdh_voice_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_jdh_power:
                //开关
                sendCode("电源");
                break;
            case R.id.tv_jdh_menu:
                //菜单
                sendCode("菜单");
                break;
            case R.id.tv_jdh_number:
                //数字键盘
                dialog = new TVNumDialog(this, R.style.MyDialogStyle, NumOnClick);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.tv_jdh_novoice:
                sendCode("静音");
                break;
            case R.id.tv_jdh_back:
                sendCode("返回");
                break;
            case R.id.tv_jdh_channel_up:
//                频道+
                sendCode("频道+");
                break;
            case R.id.tv_jdh_channel_down:
                //                频道-
                sendCode("频道-");
                break;
            case R.id.tv_jdh_voice_up:
                //                声音+
                sendCode("音量+");
                break;
            case R.id.tv_jdh_voice_down:
                //                声音-
                sendCode("音量-");
                break;
        }
    }
    public View.OnClickListener NumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_num_one:
                    sendCode("1");
                    break;
                case R.id.tv_num_two:
                    sendCode("2");
                    break;
                case R.id.tv_num_three:
                    sendCode("3");
                    break;
                case R.id.tv_num_four:
                    sendCode("4");
                    break;
                case R.id.tv_num_five:
                    sendCode("5");
                    break;
                case R.id.tv_num_six:
                    sendCode("6");
                    break;
                case R.id.tv_num_seven:
                    sendCode("7");
                    break;
                case R.id.tv_num_eight:
                    sendCode("8");
                    break;
                case R.id.tv_num_nine:
                    sendCode("9");
                    break;
                case R.id.tv_num_back:
                    sendCode("回看");
                    break;
                case R.id.tv_num_zero:
                    sendCode("0");
                    break;
                case R.id.tv_num_pick:
                    sendCode("-/--");
                    break;
            }
        }
    };

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
