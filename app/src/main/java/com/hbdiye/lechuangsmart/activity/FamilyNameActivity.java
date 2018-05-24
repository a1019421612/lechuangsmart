package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FamilyNameActivity extends BaseActivity {

    @BindView(R.id.tv_family_member)
    TextView tvFamilyMember;
    @BindView(R.id.tv_qrcode)
    TextView tvQrcode;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "家庭名称";
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
        return R.layout.activity_family_name;
    }

    @OnClick({R.id.tv_family_member, R.id.tv_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_family_member:
                startActivity(new Intent(this,FamilyMemberActivity.class));
                break;
            case R.id.tv_qrcode:
                SmartToast.show("扫描二维码");
                break;
        }
    }
}
