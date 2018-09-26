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

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import okhttp3.Call;

public class IrDianShiActivity extends BaseActivity{
    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;
    @BindView(R.id.tv_ds_power)
    TextView tvDsPower;
    @BindView(R.id.tv_ds_inputsource)
    TextView tvDsInputsource;
    @BindView(R.id.tv_ds_number)
    TextView tvDsNumber;
    @BindView(R.id.tv_ds_menu)
    TextView tvDsMenu;
    @BindView(R.id.tv_ds_novoice)
    TextView tvDsNovoice;
    @BindView(R.id.tv_ds_exp)
    TextView tvDsExp;
    @BindView(R.id.tv_ds_channel_up)
    TextView tvDsChannelUp;
    @BindView(R.id.tv_ds_channel_down)
    TextView tvDsChannelDown;
    @BindView(R.id.tv_ds_back)
    TextView tvDsRefresh;
    @BindView(R.id.tv_ds_voice_up)
    TextView tvDsVoiceUp;
    @BindView(R.id.tv_ds_voice_down)
    TextView tvDsVoiceDown;
    @BindView(R.id.tv_ds_home)
    TextView tvDsHome;

    private TVNumDialog dialog;
    private String rid = "";

    private String mac;
    private String power;
    private String ok;
    private String channel_up;
    private String channel_down;
    private String navigate_up;
    private String navigate_down;
    private String navigate_left;
    private String navigate_right;
    private String volume_up;
    private String volume_down;
    private String back;
    private String homepage;
    private String menu;
    private String zero;
    private String nine;
    private String eight;
    private String seven;
    private String six;
    private String five;
    private String four;
    private String three;
    private String two;
    private String one;
    private String rcode;
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
        return "电视";
    }

    @Override
    protected void initView() {
        initRoundMenu();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_dian_shi;
    }
    private void initRoundMenu() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrDianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrDianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //左
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrDianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                上
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(IrDianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                右
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(IrDianShiActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       ok
                    }
                });
    }
    @OnClick({R.id.tv_ds_power, R.id.tv_ds_inputsource, R.id.tv_ds_number, R.id.tv_ds_menu, R.id.tv_ds_novoice,
            R.id.tv_ds_exp, R.id.tv_ds_channel_up, R.id.tv_ds_channel_down, R.id.tv_ds_home, R.id.tv_ds_back, R.id.tv_ds_voice_up, R.id.tv_ds_voice_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ds_power:
                //开关
                sendCode();
                break;
            case R.id.tv_ds_inputsource:
                //输入源
                break;
            case R.id.tv_ds_number:
                //数字键盘
                dialog = new TVNumDialog(this, R.style.MyDialogStyle, NumOnClick);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.tv_ds_menu:
                //菜单
                break;
            case R.id.tv_ds_novoice:
                //静音
                break;
            case R.id.tv_ds_exp:
//                扩展键
                break;
            case R.id.tv_ds_channel_up:
//                频道+
                break;
            case R.id.tv_ds_channel_down:
//                频道-
                break;
            case R.id.tv_ds_home:
                //主页
                break;
            case R.id.tv_ds_back:
//                返回
                break;
            case R.id.tv_ds_voice_up:
//                声音+
                break;
            case R.id.tv_ds_voice_down:
//                声音-
                break;
        }
    }

    private void sendCode() {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.SENDCODE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("type",type)
                .addParams("uuid",uuid)
                .addParams("modelid",modelid)
                .addParams("handle","")
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

    public View.OnClickListener NumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_num_one:
                    break;
                case R.id.tv_num_two:
                    break;
                case R.id.tv_num_three:
                    break;
                case R.id.tv_num_four:
                    break;
                case R.id.tv_num_five:
                    break;
                case R.id.tv_num_six:
                    break;
                case R.id.tv_num_seven:
                    break;
                case R.id.tv_num_eight:
                    break;
                case R.id.tv_num_nine:
                    break;
                case R.id.tv_num_back:
                    SmartToast.show("back");
                    break;
                case R.id.tv_num_zero:
                    break;
                case R.id.tv_num_pick:
                    break;
            }
        }
    };
}
