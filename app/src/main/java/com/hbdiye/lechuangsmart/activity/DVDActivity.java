package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
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

public class DVDActivity extends BaseActivity {

    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;
    @BindView(R.id.tv_dvd_power)
    TextView tvDvdPower;
    @BindView(R.id.tv_dvd_out)
    TextView tvDvdOut;
    @BindView(R.id.tv_dvd_last)
    ImageView tvDvdLast;
    @BindView(R.id.tv_dvd_quickback)
    ImageView tvDvdQuickback;
    @BindView(R.id.tv_dvd_quickgo)
    ImageView tvDvdQuickgo;
    @BindView(R.id.tv_dvd_next)
    ImageView tvDvdNext;
    @BindView(R.id.tv_dvd_end)
    ImageView tvDvdEnd;
    @BindView(R.id.tv_dvd_start)
    ImageView tvDvdStart;
    @BindView(R.id.tv_dvd_stop)
    ImageView tvDvdStop;
    @BindView(R.id.tv_dvd_voice_down)
    TextView tvDvdVoiceDown;
    @BindView(R.id.tv_dvd_voice_up)
    TextView tvDvdVoiceUp;

    private String rid = "";
    private int type;
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;
    private String rcode;
    private String power;
    private String out;
    private String last;
    private String quickback;
    private String quickgo;
    private String next;
    private String end;
    private String start;
    private String stop;
    private String volume_up;
    private String volume_down;
    private String navigate_up;
    private String navigate_down;
    private String navigate_left;
    private String navigate_right;
    private String ok;
    
    private String TAG = DVDActivity.class.getSimpleName();

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
                    }else if (irDatas.get(0).keys.get(i).fid == 50) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        volume_up = pulse.replace(" ", "").replace(",", "");
                    } else if (irDatas.get(0).keys.get(i).fid == 51) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        volume_down = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 42) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        ok = pulse.replace(" ", "").replace(",", "");
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
                    }else if (irDatas.get(0).keys.get(i).fid == 186) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        out = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 201) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        last = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 141) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        quickback = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 151) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        quickgo = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 206) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        next = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 166) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        stop = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 146) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        start = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 161) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        end = pulse.replace(" ", "").replace(",", "");
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
        return "DVD";
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
        roundMenu.icon = ImageUtil.drawable2Bitmap(DVDActivity.this, R.mipmap.right);
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
        roundMenu.icon = ImageUtil.drawable2Bitmap(DVDActivity.this, R.mipmap.right);
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
        roundMenu.icon = ImageUtil.drawable2Bitmap(DVDActivity.this, R.mipmap.right);
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
        roundMenu.icon = ImageUtil.drawable2Bitmap(DVDActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + navigate_right + "\"}");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(DVDActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + ok + "\"}");
                    }
                });
    }
    @Override
    protected int getLayoutID() {
        return R.layout.activity_dvd;
    }

    @OnClick({R.id.tv_dvd_power, R.id.tv_dvd_out, R.id.tv_dvd_last, R.id.tv_dvd_quickback, R.id.tv_dvd_quickgo, R.id.tv_dvd_next, R.id.tv_dvd_end, R.id.tv_dvd_start, R.id.tv_dvd_stop, R.id.tv_dvd_voice_down, R.id.tv_dvd_voice_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dvd_power:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + power + "\"}");
                break;
            case R.id.tv_dvd_out:
                //退出
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + out + "\"}");
                break;
            case R.id.tv_dvd_last:
                //上一曲
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + last + "\"}");
                break;
            case R.id.tv_dvd_quickback:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + quickback + "\"}");
                break;
            case R.id.tv_dvd_quickgo:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + quickgo + "\"}");
                break;
            case R.id.tv_dvd_next:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + next + "\"}");
                break;
            case R.id.tv_dvd_end:
                //暂停
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + end + "\"}");
                break;
            case R.id.tv_dvd_start:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + start + "\"}");
                break;
            case R.id.tv_dvd_stop:
                //暂停
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + stop + "\"}");
                break;
            case R.id.tv_dvd_voice_down:
                //                声音-
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_down + "\"}");

                break;
            case R.id.tv_dvd_voice_up:
                //                声音+
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + volume_up + "\"}");

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
