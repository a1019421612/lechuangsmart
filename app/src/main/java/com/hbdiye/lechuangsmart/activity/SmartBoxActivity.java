package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * 智能盒子遥控
 */
public class SmartBoxActivity extends BaseActivity {

    private String rid = "";
    private int type;
    private String rcode;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;

    private String TAG=SmartBoxActivity.class.getSimpleName();

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
        RoundMenuView.RoundMenu roundMenu=new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon= ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了1");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了2");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了3");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了4");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor( R.color.gray_f2f2),
                getResources().getColor( R.color.gray_9999), getResources().getColor( R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SmartToast.show("点击了中心圆圈");
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_smart_box;
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
