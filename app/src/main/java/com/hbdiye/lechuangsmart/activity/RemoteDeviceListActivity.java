package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.InfraredAdapter;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.hbdiye.lechuangsmart.views.SceneDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemoteDeviceListActivity extends BaseActivity {

    @BindView(R.id.rv_device)
    RecyclerView rvDevice;

    private InfraredAdapter adapter;
    private List<InfraredBean.Remote_list> mList = new ArrayList<>();
    private boolean editStatus = false;//编辑状态标志，默认false
    private SceneDialog sceneDialog;
    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "设备列表";
    }

    @Override
    protected void initView() {
        ivBaseAdd.setVisibility(View.VISIBLE);
        ivBaseEdit.setVisibility(View.VISIBLE);

        ivBaseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemoteDeviceListActivity.this,DeviceListActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_remote_device_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
