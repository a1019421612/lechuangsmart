package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.InfraredAdapter;
import com.hbdiye.lechuangsmart.adapter.SceneAdapter;
import com.hbdiye.lechuangsmart.bean.InfraredBean;
import com.hbdiye.lechuangsmart.bean.SceneBean;
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

/**
 *
 */
public class InfraredActivity extends BaseActivity {

    @BindView(R.id.rv_infrared)
    RecyclerView rvInfrared;

    private InfraredAdapter adapter;
    private List<InfraredBean.Remote_list> mList = new ArrayList<>();
    private boolean editStatus = false;//编辑状态标志，默认false
    private SceneDialog sceneDialog;
    private String uuid="";

    @Override
    protected void initData() {
        getInfraredList();
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
                        if (TextUtils.isEmpty(response)){
                            return;
                        }
                        InfraredBean infraredBean = new Gson().fromJson(response, InfraredBean.class);
                        if (infraredBean.success) {
                            if (mList.size() > 0) {
                                mList.clear();
                            }
                            List<InfraredBean.Remote_list> remote_list = infraredBean.remote_list;
                            mList.addAll(remote_list);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected String getTitleName() {
        return "红外";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseAdd.setVisibility(View.VISIBLE);
        ivBaseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InfraredActivity.this, ZuWangActivity.class),100);
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
        rvInfrared.setLayoutManager(manager);
        adapter = new InfraredAdapter(mList);
        rvInfrared.setAdapter(adapter);

        handlerClicker();
    }

    private void handlerClicker() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(InfraredActivity.this,RemoteDeviceListActivity.class));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                InfraredBean.Remote_list remote_list = mList.get(position);
                uuid = remote_list.uuid;
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
                        AlertDialog.Builder builder=new AlertDialog.Builder(InfraredActivity.this);
                        builder.setMessage("确认删除场景？");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delRemote();
                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(InfraredActivity.this, R.style.MyDialogStyle, dailogClicer, "遥控中心名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                }
            }
        });
    }

    private void delRemote() {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.DELREMOTE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("uuid", uuid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String info = jsonObject.getString("info");
                            SmartToast.show(info);
                            if (success){
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
                        SmartToast.show("遥控中心名称不能为空");
                    } else {
                        editremote(remoteName);
                    }
                    break;
            }
        }
    };

    private void editremote(String name) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.EDITREMOTE))
                .addParams("app_id", InterfaceManager.APPID)
                .addParams("app_type", InterfaceManager.APPKEY)
                .addParams("name", name)
                .addParams("uuid", uuid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String info = jsonObject.getString("info");
                            SmartToast.show(info);
                            if (sceneDialog!=null){
                                sceneDialog.dismiss();
                            }
                            if (success){
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
        return R.layout.activity_infrared;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==100){
            getInfraredList();
        }
    }
}
