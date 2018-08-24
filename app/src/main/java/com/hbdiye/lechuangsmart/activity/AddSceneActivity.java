package com.hbdiye.lechuangsmart.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.ImageAdapter;
import com.hbdiye.lechuangsmart.bean.ImageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;

public class AddSceneActivity extends BaseActivity {

    @BindView(R.id.et_add_scene_name)
    EditText etAddSceneName;
    @BindView(R.id.tv_add_scene_icon)
    TextView tvAddSceneIcon;
    @BindView(R.id.et_add_scene_group)
    EditText etAddSceneGroup;
    @BindView(R.id.tv_add_scene_num)
    TextView tvAddSceneNum;
    @BindView(R.id.tv_add_scene_ok)
    TextView tvAddSceneOk;
    @BindView(R.id.iv_scene_setting_icon)
    ImageView ivSceneSettingIcon;

    private PopupWindow popupWindow;
    private RecyclerView rv_image_list;
    private List<ImageBean> mList_image = new ArrayList<>();
    private String[] array_scene = {"场景一", "场景二", "场景三", "场景四", "场景五", "场景六", "场景七", "场景八"};
    private String[] array_scene_num = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private int flag = 0;
    private String scene_num = "0";

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;

    private String default_image = "changjing1";

    @Override
    protected void initData() {
        mConnection = SingleWebSocketConnection.getInstance();
//        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SATP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
//        socketConnection();
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initImageData();

    }

    @Override
    protected String getTitleName() {
        return "添加新场景";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_scene;
    }

    @OnClick({R.id.tv_add_scene_icon, R.id.tv_add_scene_num, R.id.tv_add_scene_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_scene_icon:
                showPopWindow(getView());
                break;
            case R.id.tv_add_scene_num:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择场景");
                builder.setSingleChoiceItems(array_scene, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = which;
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scene_num = array_scene_num[flag];
                        tvAddSceneNum.setText(array_scene[flag]);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.tv_add_scene_ok:
                String sceneName = etAddSceneName.getText().toString().trim();
                String sceneGroup = etAddSceneGroup.getText().toString().trim();
                if (TextUtils.isEmpty(sceneName)) {
                    SmartToast.show("场景名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(sceneGroup)) {
                    SmartToast.show("分组号不能为空");
                    return;
                }
                int i = Integer.parseInt(sceneGroup);
                if (i < 1 || i > 30000) {
                    SmartToast.show("分组号已超出范围");
                    return;
                }
                if (scene_num.equals("0")) {
                    SmartToast.show("请选择场景");
                    return;
                }
                mConnection.sendTextMessage("{\"pn\":\"SATP\",\"icon\":\"" + default_image + "\",\"name\":\"" + sceneName + "\",\"groupNo\":\"" + sceneGroup + "\",\"sceneNo\":\"" + scene_num + "\"}");
                break;
        }
    }

    private void showPopWindow(View view) {
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
        popupWindow.setBackgroundDrawable(cd);
        int[] location = new int[2];
        ivSceneSettingIcon.getLocationOnScreen(location);
        backgroundAlpha(0.5f);// 设置背景半透明
        popupWindow.showAsDropDown(ivSceneSettingIcon);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow = null;// 当点击屏幕时，使popupWindow消失
                backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
            }
        });
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }

    private View getView() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_image_list, null);
        rv_image_list = view.findViewById(R.id.rv_image_list);
        rv_image_list.setLayoutManager(new GridLayoutManager(this, 9));
        ImageAdapter imageAdapter = new ImageAdapter(mList_image);
        rv_image_list.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Glide.with(AddSceneActivity.this).load(ContentConfig.sceneIcon(mList_image.get(position).getIconName())).into(ivSceneSettingIcon);
                default_image = mList_image.get(position).getIconName();
//                String s = tvSceneName.getText().toString();
//                mConnection.sendTextMessage("{\"pn\":\"SUTP\",\"sceneID\":\"" + sceneID + "\",\"icon\":\"" + mList_image.get(position).getIconName() + "\",\"name\":\"" + s + "\"}");
                popupWindow.dismiss();
            }
        });
        return view;
    }

    private void initImageData() {
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.changjing1);
            setIconName("changjing1");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.huijia);
            setIconName("changjing2");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.youxi);
            setIconName("changjing3");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.ipod);
            setIconName("changjing4");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.zixingche);
            setIconName("changjing5");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.chuang);
            setIconName("changjing6");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.jita);
            setIconName("changjing7");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.dianhua);
            setIconName("changjing8");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.dangao);
            setIconName("changjing9");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.guangdie);
            setIconName("changjing10");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.erji);
            setIconName("changjing11");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.suo);
            setIconName("changjing12");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.kaisuo);
            setIconName("changjing13");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.mojing);
            setIconName("changjing14");
        }});
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("SATP")) {
                Log.e("bbb",payload);
//                parseData(payload);
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    String stCode = jsonObject.getString("stCode");
                    if (stCode.equals("200")){
                        SmartToast.show("添加成功");
                        finish();
                    }else if (stCode.equals("481")){
                        SmartToast.show("相同组号超出9次");
                    }else if (stCode.equals("482")){
                        SmartToast.show("场景添加失败");
                    }else if (stCode.equals("801")){
                        SmartToast.show("非法数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}
