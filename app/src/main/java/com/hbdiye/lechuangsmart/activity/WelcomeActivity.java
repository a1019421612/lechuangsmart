package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.hbdiye.lechuangsmart.MainActivity;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.SPUtils;

public class WelcomeActivity extends AppCompatActivity {
    private String userName;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();

                    break;
                case 200:
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userName= (String) SPUtils.get(this,"mobilephone","");
        if (TextUtils.isEmpty(userName)){
            handler.sendEmptyMessageDelayed(200,3000);
        }else {
            handler.sendEmptyMessageDelayed(100,3000);
        }
    }
}
