package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.ImageUtil;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.RoundMenuView;
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

/**
 * 智能盒子遥控
 */
public class SmartBoxActivity extends BaseActivity {

    @BindView(R.id.tv_box_power)
    TextView tvBoxPower;
    @BindView(R.id.tv_box_setting)
    TextView tvBoxSetting;
    @BindView(R.id.tv_box_home)
    TextView tvBoxHome;
    @BindView(R.id.tv_box_menu)
    TextView tvBoxMenu;
    @BindView(R.id.tv_box_back)
    TextView tvBoxBack;
    @BindView(R.id.tv_box_ex)
    TextView tvBoxEx;
    @BindView(R.id.tv_box_voice_up)
    TextView tvBoxVoiceUp;
    @BindView(R.id.tv_box_voice_down)
    TextView tvBoxVoiceDown;
    private String rid = "";
    private int type;
    private String rcode;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;
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
    private String power;
    private String TAG = SmartBoxActivity.class.getSimpleName();

    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
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
        return "智能盒子";
    }

    @Override
    protected void initView() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(SmartBoxActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_down + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(SmartBoxActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //左
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_left + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(SmartBoxActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            //上
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_up + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(SmartBoxActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            //右
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_right + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(SmartBoxActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + ok + "\"}");
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_smart_box;
    }


    @OnClick({R.id.tv_box_power, R.id.tv_box_setting, R.id.tv_box_home, R.id.tv_box_menu, R.id.tv_box_back, R.id.tv_box_ex, R.id.tv_box_voice_up, R.id.tv_box_voice_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_box_power:
                //开关
//                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"AF471518004B1200\",\"rcode\":\"010C0ED8060004811C04801700150480170021048017002D0480170038048026001502801700\",\"fpulse\":\"00403303030335\"}");
                String data = "{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + power + "\"}";
                mConnection.sendTextMessage(data);
                break;
            case R.id.tv_box_setting:
                break;
            case R.id.tv_box_home:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + homepage + "\"}");
                break;
            case R.id.tv_box_menu:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + menu + "\"}");
                break;
            case R.id.tv_box_back:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + back + "\"}");
                break;
            case R.id.tv_box_ex:
                break;
            case R.id.tv_box_voice_up:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_up + "\"}");
                break;
            case R.id.tv_box_voice_down:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_down + "\"}");
                break;
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
                Intent intent = new Intent(SmartBoxActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mConnection.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mConnection != null) {
            socketConnect();
        }
    }

    private void socketConnect() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
    }
}
