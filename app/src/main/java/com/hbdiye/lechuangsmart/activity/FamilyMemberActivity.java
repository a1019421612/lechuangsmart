package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.FamilyMemberAdapter;
import com.hbdiye.lechuangsmart.bean.FamilyNameBean;
import com.hbdiye.lechuangsmart.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class FamilyMemberActivity extends BaseActivity {
    @BindView(R.id.rv_member_list)
    RecyclerView rvMemberList;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private List<FamilyNameBean.FamilyUsers> mList=new ArrayList<>();
    private FamilyMemberAdapter adapter;

    private String TAG=FamilyMemberActivity.class.getSimpleName();

    @Override
    protected void initData() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("UITP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
//        mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
    }

    @Override
    protected String getTitleName() {
        return "家庭成员";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMemberList.setLayoutManager(manager);
        adapter=new FamilyMemberAdapter(mList);
        rvMemberList.setAdapter(adapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_family_member;
    }
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("UITP")) {
                parseData(payload);
            }
        }
    }
    class FamilyMemberWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(FamilyMemberActivity.this, LoginActivity.class);
                    startActivity(intent);
            }
            if (payload.contains("\"pn\":\"UITP\"")) {
                parseData(payload);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }
    private void parseData(String payload) {

        try {
            FamilyNameBean familyNameBean = new Gson().fromJson(payload, FamilyNameBean.class);
            if (mList.size()>0){
                mList.clear();
            }
            List<FamilyNameBean.FamilyUsers> familyUsers = familyNameBean.familyUsers;
            mList.addAll(familyUsers);
            adapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onstop");
        unregisterReceiver(homeReceiver);
    }
}
