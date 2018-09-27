package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.InfraredAdapter;
import com.hbdiye.lechuangsmart.adapter.RemoteDeviceListAdapter;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.hbdiye.lechuangsmart.Global.InterfaceManager.HWGETREMOTE;

public class RemoteDeviceListActivity extends BaseActivity {

    @BindView(R.id.rv_device)
    RecyclerView rvDevice;

    private RemoteDeviceListAdapter adapter;
    private ArrayList<InfraredBean.Remote_list.Ir_list> mList = new ArrayList<>();
    private boolean editStatus = false;//编辑状态标志，默认false
    private SceneDialog sceneDialog;
    private String uuid;
    private String id;

    @Override
    protected void initData() {
        uuid = getIntent().getStringExtra("uuid");
        ArrayList<InfraredBean.Remote_list.Ir_list> ir_list = (ArrayList<InfraredBean.Remote_list.Ir_list>) getIntent().getSerializableExtra("ir_list");
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(ir_list);
        adapter.notifyDataSetChanged();
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
                startActivityForResult(new Intent(RemoteDeviceListActivity.this, DeviceListActivity.class).putExtra("uuid", uuid), 101);
            }
        });
        ivBaseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editStatus) {
                    ivBaseEdit.setImageResource(R.drawable.bianji2);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = false;
                } else {
                    ivBaseEdit.setImageResource(R.drawable.duigou);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = true;
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDevice.setLayoutManager(manager);
        adapter = new RemoteDeviceListAdapter(mList);
        rvDevice.setAdapter(adapter);

        handlerClicker();
    }

    private void handlerClicker() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                id = mList.get(position).id;
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RemoteDeviceListActivity.this);
                        builder.setMessage("确认删除遥控？");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delRemote();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(RemoteDeviceListActivity.this, R.style.MyDialogStyle, dailogClicer, "遥控名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String type = mList.get(position).type;
                switch (type) {
                    case "1":
                        startActivity(new Intent(RemoteDeviceListActivity.this, IrKongTiaoActivity.class)
                                .putExtra("data", mList.get(position))
                                .putExtra("uuid", uuid));
                        break;
                    case "2":
                        startActivity(new Intent(RemoteDeviceListActivity.this, IrDianShiActivity.class)
                                .putExtra("data", mList.get(position))
                                .putExtra("uuid", uuid));
                        break;
                    case "3":
                        startActivity(new Intent(RemoteDeviceListActivity.this, IrJDHActivity.class)
                                .putExtra("data", mList.get(position))
                                .putExtra("uuid", uuid));
                        break;
                    case "4":
//                        SmartToast.show("DVD");
                        break;
                    case "5":
                        startActivity(new Intent(RemoteDeviceListActivity.this, IrFSActivity.class)
                                .putExtra("data", mList.get(position))
                                .putExtra("uuid", uuid));
                        break;
                    case "6":
//                        SmartToast.show("空气净化器");
                        break;
                    case "7":
//                        SmartToast.show("IPTV");
                        break;
                    case "8":
//                        SmartToast.show("投影仪");
                        break;
                    case "9":
//                        SmartToast.show("功放");
                        break;
                    case "10":
//                        SmartToast.show("热水器");
                        break;
                }
            }
        });
    }

    private void delRemote() {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.DELIRDEVICE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("id", id)
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
                            if (success) {
                                getInfraredList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public View.OnClickListener dailogClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String remoteName = sceneDialog.getSceneName();
                    if (TextUtils.isEmpty(remoteName)) {
                        SmartToast.show("遥控名称不能为空");
                    } else {
                        editIrDevice(remoteName);
                    }
                    break;
            }
        }
    };

    private void editIrDevice(final String remoteName) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.EDITIRDEVICE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("name", remoteName)
                .addParams("id", id)
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
                            if (success) {
                                if (sceneDialog != null) {
                                    sceneDialog.dismiss();
                                }
                                getInfraredList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 101) {
            getInfraredList();
        }
    }

    private void getInfraredList() {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(HWGETREMOTE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接出错");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        InfraredBean infraredBean = new Gson().fromJson(response, InfraredBean.class);
                        if (infraredBean.success) {
                            List<InfraredBean.Remote_list> remote_list1 = infraredBean.remote_list;
                            for (int i = 0; i < remote_list1.size(); i++) {
                                String uuid1 = remote_list1.get(i).uuid;
                                if (uuid1.equals(uuid)) {
                                    ArrayList<InfraredBean.Remote_list.Ir_list> ir_list = (ArrayList<InfraredBean.Remote_list.Ir_list>) remote_list1.get(i).ir_list;
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }
                                    mList.addAll(ir_list);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                    }
                });
    }
}
