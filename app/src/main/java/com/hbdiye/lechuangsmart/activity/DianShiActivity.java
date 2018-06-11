package com.hbdiye.lechuangsmart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;

import org.json.JSONException;
import org.json.JSONObject;

public class DianShiActivity extends BaseActivity {

    @Override
    protected void initData() {
        KookongSDK.getAreaId("北京市", "北京市", "昌平区", new IRequestResult<Integer>() {

                    @Override
                    public void onSuccess(String msg, Integer result) {
                        Logger.d("AreaId is : " + result);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("areaId", result);

                            //
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(Integer errorCode, String msg) {
                        SmartToast.show(msg);

                    }
                });
    }

    @Override
    protected String getTitleName() {
        return "电视";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_dian_shi;
    }
}
