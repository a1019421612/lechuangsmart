package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.util.ImageUtil;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.RoundMenuView;
import com.hbdiye.lechuangsmart.views.TVNumDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.AppConst;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class JDHActivity extends BaseActivity {

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

    private String TAG = JDHActivity.class.getSimpleName();

    private TVNumDialog dialog;
    private String rid = "";
    private int type;

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
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
    private String novoice;

    @Override
    protected void initData() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("IRTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();

        type = getIntent().getIntExtra("type", -1);
        rid = getIntent().getStringExtra("rid");
        mac = getIntent().getStringExtra("mac");
        if (!TextUtils.isEmpty(rid) && type != -1) {
            getIRDataById();
        }
    }

    private void getIRDataById() {
        KookongSDK.getIRDataById(rid, type, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                rcode = irDatas.get(0).exts.get(99999);
                ArrayList<IrData.IrKey> keys = irDatas.get(0).keys;
                for (int i = 0; i < keys.size(); i++) {
                    if (irDatas.get(0).keys.get(i).fid == 1) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        power = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 42) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        ok = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 43) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        channel_up = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 44) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        channel_down = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 45) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        menu = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 46) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        navigate_up = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 47) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        navigate_down = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 48) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        navigate_left = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 49) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        navigate_right = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 50) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        volume_up = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 51) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        volume_down = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 116) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        back = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 136) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        homepage = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 61) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        one = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 66) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        two = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 71) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        three = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 76) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        four = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 81) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        five = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 86) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        six = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 91) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        seven = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 96) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        eight = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 101) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        nine = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 56) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        zero = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 106){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        novoice = pulse.replace(" ", "").replace(",", "");
                    }
                }

            }

            @Override
            public void onFail(Integer errorCode, String msg) {
                //按红外设备授权的客户，才会用到这两个值
                if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
                    msg = "下载的遥控器超过了套数限制";
                } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
                    msg = "设备总数超过了授权的额度";
                }
                SmartToast.show(msg);

            }
        });
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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_jdh;
    }

    private void initRoundMenu() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(JDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_down + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(JDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_left + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(JDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_up + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(JDHActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_right + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(JDHActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + ok + "\"}");
                    }
                });
    }

    @OnClick({R.id.tv_jdh_power, R.id.tv_jdh_menu, R.id.tv_jdh_number, R.id.tv_jdh_novoice, R.id.tv_jdh_back, R.id.tv_jdh_channel_up, R.id.tv_jdh_channel_down, R.id.tv_jdh_voice_up, R.id.tv_jdh_voice_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_jdh_power:
                //开关
                String data = "{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + power + "\"}";
                mConnection.sendTextMessage(data);
                break;
            case R.id.tv_jdh_menu:
                //菜单
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + menu + "\"}");
                break;
            case R.id.tv_jdh_number:
                //数字键盘
                dialog = new TVNumDialog(this, R.style.MyDialogStyle, NumOnClick);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.tv_jdh_novoice:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + novoice + "\"}");
                break;
            case R.id.tv_jdh_back:
                break;
            case R.id.tv_jdh_channel_up:
//                频道+
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + channel_up + "\"}");
                break;
            case R.id.tv_jdh_channel_down:
                //                频道-
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + channel_down + "\"}");
                break;
            case R.id.tv_jdh_voice_up:
                //                声音+
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_up + "\"}");
                break;
            case R.id.tv_jdh_voice_down:
                //                声音-
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_down + "\"}");
                break;
        }
    }

    public View.OnClickListener NumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_num_one:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + one + "\"}");
                    break;
                case R.id.tv_num_two:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + two + "\"}");
                    break;
                case R.id.tv_num_three:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + three + "\"}");
                    break;
                case R.id.tv_num_four:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + four + "\"}");
                    break;
                case R.id.tv_num_five:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + five + "\"}");
                    break;
                case R.id.tv_num_six:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + six + "\"}");
                    break;
                case R.id.tv_num_seven:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + seven + "\"}");
                    break;
                case R.id.tv_num_eight:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + eight + "\"}");
                    break;
                case R.id.tv_num_nine:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + nine + "\"}");
                    break;
                case R.id.tv_num_back:
                    break;
                case R.id.tv_num_zero:
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + zero + "\"}");
                    break;
                case R.id.tv_num_pick:
//                    -/--
                    break;
            }
        }
    };
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");

        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE02\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"IRTP\"")) {

            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(JDHActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       unregisterReceiver(homeReceiver);
    }
}
