package com.hbdiye.lechuangsmart;


import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.fragment.HomeFragment;
import com.hbdiye.lechuangsmart.fragment.LinkageFragment;
import com.hbdiye.lechuangsmart.fragment.SceneFragment;
import com.hbdiye.lechuangsmart.fragment.SettingFragment;
import com.hbdiye.lechuangsmart.util.AndroidUniqueId;

import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.util.TipsUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_KEY = "1443835CCAD523A8D42A1BA04DE13E10";
    public String irDeviceId = "1";//如果是按红外设备授权收费则填上设备的id，否则使用KookongSDK.init(this,APP_KEY);

//    protected AgentWeb mAgentWeb;
//    private LinearLayout mLinearLayout;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String province = "";//省
    private String city = "";//市
    private String district = "";//区县
    private int areaId = -1;//默认areaId为-1


    private ImageView  mImageChats, mImageContact, mImageFind, mImageMe;
    private TextView mTextChats, mTextContact, mTextFind, mTextMe;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;

    private HomeFragment homeFragment;
    private SceneFragment sceneFragment;
    private LinkageFragment linkageFragment;
    private SettingFragment settingFragment;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar();
        irDeviceId = AndroidUniqueId.getUniqueId(this);
        initView();
        initData();
        String[] perm={Permission.CAMERA,Permission.WRITE_EXTERNAL_STORAGE,Permission.ACCESS_COARSE_LOCATION};
        AndPermission.with(this)
                .runtime()
                .permission(perm)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                    }
                })
                .start();
        fragmentManager = getSupportFragmentManager();
        changeTextViewColor();
        changeSelectedTabState(0);
        showFragment(0);

        Log.e("TAG","唯一值：：：："+irDeviceId);
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);

//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        mLocationClient.start();

//        mLinearLayout = findViewById(R.id.container);

        //1.在App的入口进行初始化, 在Application中初始化也可以
        //按红外设备授权收费的客户，需要传递自己设备唯一的标识，使用KookongSDK.init(Context context, String key, String irDeviceId);
        //其他客户使用KookongSDK.init(Context context, String key);
        if (!KookongSDK.init(this, APP_KEY, irDeviceId)) {
            SmartToast.show("KookongSDK初始化失败");
        }

//        if (!IrDevice.init(this, APP_KEY)) {
//            TipsUtil.toast(this, "IrDevice初始化失败");//手机端解压的初始化
//        }
        KookongSDK.setDebugMode(true);
//        mAgentWeb = AgentWeb.with(this)
//                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
//                .closeIndicator()
//                .setWebChromeClient(mWebChromeClient)
//                .setWebViewClient(mWebViewClient)
//                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
//                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
//                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
//                .createAgentWeb()
//                .ready()
//                .go(getUrl());
//        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface());
    }

    private void initData() {
//        serviceIntent = new Intent(MainActivity.this, WebSocketService.class);
//        startService(serviceIntent);
    }
    public void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(this);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色（该颜色根据你的App主题自行更改）
            rectView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private void initView() {
        frameLayout = findViewById(R.id.fl_container);
        RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
        RelativeLayout foundRLayout = (RelativeLayout) findViewById(R.id.seal_find);
        RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
        mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
        mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
        mImageFind = (ImageView) findViewById(R.id.tab_img_find);
        mImageMe = (ImageView) findViewById(R.id.tab_img_me);
        mTextChats = (TextView) findViewById(R.id.tab_text_chats);
        mTextContact = (TextView) findViewById(R.id.tab_text_contact);
        mTextFind = (TextView) findViewById(R.id.tab_text_find);
        mTextMe = (TextView) findViewById(R.id.tab_text_me);

        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
    }
    private void showFragment(int i) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hide(transaction);
        switch (i) {
            case 0:
                if (homeFragment!=null){
                    transaction.show(homeFragment);
                }else {
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.fl_container,homeFragment);
                }
                break;
            case 1:
                if (sceneFragment!=null){
                    transaction.show(sceneFragment);
                }else {
                    sceneFragment=new SceneFragment();
                    transaction.add(R.id.fl_container,sceneFragment);
                }
                break;
            case 2:
                if (linkageFragment!=null){
                    transaction.show(linkageFragment);
                }else {
                    linkageFragment=new LinkageFragment();
                    transaction.add(R.id.fl_container,linkageFragment);
                }
                break;
            case 3:
                if (settingFragment!=null){
                    transaction.show(settingFragment);
                }else {
                    settingFragment=new SettingFragment();
                    transaction.add(R.id.fl_container,settingFragment);
                }
                break;
        }
        transaction.commit();
    }
    private void hide(FragmentTransaction transaction) {

        if (homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if (sceneFragment!=null){
            transaction.hide(sceneFragment);
        }
        if (linkageFragment!=null){
            transaction.hide(linkageFragment);
        }
        if (settingFragment!=null){
            transaction.hide(settingFragment);
        }
    }
    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts));
        mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me));
        mTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTextFind.setTextColor(Color.parseColor("#abadbb"));
        mTextMe.setTextColor(Color.parseColor("#abadbb"));
    }
    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTextChats.setTextColor(Color.parseColor("#0099ff"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat_hover));
                break;
            case 1:
                mTextContact.setTextColor(Color.parseColor("#0099ff"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts_hover));
                break;
            case 2:
                mTextFind.setTextColor(Color.parseColor("#0099ff"));
                mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found_hover));
                break;
            case 3:
                mTextMe.setTextColor(Color.parseColor("#0099ff"));
                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me_hover));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        changeTextViewColor();
        switch (v.getId()) {
            case R.id.seal_chat:
                changeSelectedTabState(0);
                showFragment(0);
                break;
            case R.id.seal_contact_list:
                changeSelectedTabState(1);
                showFragment(1);
                break;
            case R.id.seal_find:
                changeSelectedTabState(2);
                showFragment(2);
                break;
            case R.id.seal_me:
                changeSelectedTabState(3);
                showFragment(3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(serviceIntent);
    }
    /**
     * ====================================Android与HTML通信接口===========开始===========================================
     */
//    class AndroidInterface {
//        /**
//         * 获取位置
//         */
//        @JavascriptInterface
//        public void getCity(){
//            if (province.equals("")||city.equals("")||district.equals("")){
//                Toast.makeText(MainActivity.this,"定位中。。。请稍后再试",Toast.LENGTH_SHORT).show();
//            }else {
//                mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getCity_android", province+city+district);
//            }
//        }
//        /**
//         * 获取AreaId
//         */
//        @JavascriptInterface
//        public void getAreaId() {
//            if (!province.equals("") && !city.equals("") && !district.equals("")) {
//                KookongSDK.getAreaId(province, city, district, new IRequestResult<Integer>() {
//
//                    @Override
//                    public void onSuccess(String msg, Integer result) {
//                        Logger.d("AreaId is : " + result);
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("areaId", result);
//                            areaId = result;
//                            //
//                            mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getAreaId_android", jsonObject.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//            }
//        }
//
//        /**
//         * 获取指定AreaId下的运营商列表
//         */
//        @JavascriptInterface
//        public void getOperaters() {
//            if (areaId != -1) {
//                KookongSDK.getOperaters(areaId, new IRequestResult<SpList>() {
//
//                    @Override
//                    public void onSuccess(String msg, SpList result) {
//                        Map<String, List<SpList.Sp>> map = new HashMap<>();
//                        List<SpList.Sp> list = result.spList;
//                        map.put("result", list);
//                        String s = JSON.toJSONString(map);
//                        mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getOperaters_android", s);
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//            } else {
//                TipsUtil.toast(MainActivity.this, "请先获取AreaId");
//            }
//        }
//
//        /**
//         * 获取运营商下所有的遥控器rid
//         * 获取指定运营商, 指定AreaId下的遥控器id brandid传0
//         */
//        @JavascriptInterface
//        public void getAllRemoteIds(int spId) {
//            if (areaId != -1) {
//                KookongSDK.getAllRemoteIds(Device.STB, 0, spId, areaId, new IRequestResult<RemoteList>() {
//
//                    @Override
//                    public void onSuccess(String msg, RemoteList result) {
//                        Map<String, List<Integer>> map = new HashMap<>();
//                        List<Integer> remoteids = result.rids;
//                        map.put("result", remoteids);
//                        String s = JSON.toJSONString(map);
//                        mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getAllRemoteIds_android", s);
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//            } else {
//                TipsUtil.toast(MainActivity.this, "请先获取AreaId");
//            }
//        }
//
//        /**
//         * 对码测试id为XXX的红外数据
//         * 获取的是某套红外码部分按键，仅限按红外设备授权的客户（初始化时需要传递的deviceId），如果初始化时没有传deviceId，请求接口时会提示参数错误
//         * 对码测试获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
//         */
//
//        @JavascriptInterface
//        public void testIRDataById(String rid) {
//            KookongSDK.testIRDataById(rid, Device.STB, true, new IRequestResult<IrDataList>() {
//                @Override
//                public void onSuccess(String msg, IrDataList result) {
//                    String s1 = JSON.toJSONString(result);
//                    JSONObject jsonObject=new JSONObject();
//                    try {
//                        jsonObject.put("sss",s1);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    String dsds=jsonObject.toString();
//                    IrDataBeans irDataBeans = JSON.parseObject(s1, IrDataBeans.class);
//                    String s = JSON.toJSONString(irDataBeans);
//                    //exts
//                    int size = result.getIrDataList().size();
//                    Map<String,String> ex=new HashMap<>();
//                    for (int i = 0; i < size; i++) {
//                        HashMap<Integer, String> exts = result.getIrDataList().get(i).exts;
//                        Iterator iter = exts.entrySet().iterator();
//                        //获取key和value的set
//                        while (iter.hasNext()) {
//                            Map.Entry entry = (Map.Entry) iter.next();
//                            //把hashmap转成Iterator再迭代到entry
//                            Integer key = (Integer) entry.getKey();
//                            //从entry获取key
//                            String val = (String) entry.getValue();
//                            Log.e("TAG",key+"||||"+val);
//                            ex.put(key+"",val);
//                            //从entry获取value
//                        }
//                    }
//                    String s2 = JSON.toJSONString(ex);
//                    String replace = s1.replace(":\"", "\":\"").replace("\"\":", "\":").replace("{", "{\"")
//                            .replace("{\"\"", "{\"").replace(",", ",\"").replace(",\"\"", ",\"")
//                            .replace("{\"}", "{}").replace(",\"{", ",{");
////                    Map<String, List<IrData>> map = new HashMap<>();
////                    List<IrData> irDatas = result.getIrDataList();
////                    map.put("result", irDatas);
////                    String s = JSON.toJSONString(map);
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_testIRDataById_android", dsds);
////                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_testIRDataById_android",s);
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    //按红外设备授权的客户，才会用到这个指
//                    if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                        msg = "设备总数超过了授权的额度";
//                    }
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//        /**
//         * 获取id为XXX的红外数据
//         * //这是获取完整红外码的方法
//         * 获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
//         */
//        @JavascriptInterface
//        public void getIRDataById(String rid) {
//            KookongSDK.getIRDataById("4162", 2, true, new IRequestResult<IrDataList>() {
//
//                @Override
//                public void onSuccess(String msg, IrDataList result) {
//
//
//                    String s1 = JSON.toJSONString(result);
//                    IrDataByBean irDataBeans = JSON.parseObject(s1, IrDataByBean.class);
//                    String s = JSON.toJSONString(irDataBeans);
//                    String replace = s.replace("\"exts\":\"{", "\"exts\":{").replace("\\\"", "\"").replace("}\"", "}");
////                    JSONObject jsonObject=new JSONObject();
////                    try {
////                        jsonObject.put("sss",s1);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    s1=jsonObject.toString();
////                    String replace = s1.replace(":\"", "\":\"").replace("\"\":", "\":").replace("{", "{\"")
////                            .replace("{\"\"", "{\"").replace(",", ",\"").replace(",\"\"", ",\"")
////                            .replace("{\"}", "{}").replace(",\"{", ",{");
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getIRDataById_android", replace);
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    //按红外设备授权的客户，才会用到这两个值
//                    if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
//                        msg = "下载的遥控器超过了套数限制";
//                    } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                        msg = "设备总数超过了授权的额度";
//                    }
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//        /**
//         * 获取id为XXX的空调全解红外数据
//         * 特殊客户调用的下载空调红外码（有状态的空调，IrData.type=2）
//         */
//
//        @JavascriptInterface
//        public void getNoStateIRDataById(String id) {
//            //获取rid = 2607的 红外码
//            KookongSDK.getNoStateIRDataById(id, Device.AC, true, new IRequestResult<IrDataList>() {
//
//                private String map_mod_json="";
//                private String map_zhan_json="";
//
//                @Override
//                public void onSuccess(String msg, IrDataList result) {
////                    String s1 = JSON.toJSONString(result);
////                    String replace = s1.replace("\\\"","\"");
////                    String replace = s1.replace("\\\"","\"").replace(":\"", "\":\"").replace("\"\":", "\":").replace("{", "{\"")
////                            .replace("{\"\"", "{\"").replace(",", ",\"").replace(",\"\"", ",\"")
////                            .replace("{\"}", "{}").replace(",\"{", ",{");
//
//                    List<IrData> irDatas = result.getIrDataList();
//                    for (int i = 0; i < irDatas.size(); i++) {
//                        IrData irData = irDatas.get(i);
//                        Logger.d("空调：" + irData.rid);
//                        //空调支持的模式、温度、风速
//                        HashMap<Integer, String> exts = irData.exts;
//                        //遥控器参数
//                        String remoteParam = exts.get(99999);
//                        Logger.d("遥控器参数99999：" + remoteParam);
//                        try {
//                            JSONArray ja = new JSONArray(exts.get(0));
//                            //遍历模式，模式顺序：制冷、制热、自动、送风、除湿
//                            Map<String,Map<String,String>> map_mod=new HashMap<>();
//                            //制冷模式  zlmode
//                            acMode(ja.getJSONObject(0), "zlmode",map_mod);
//                            //制热模式 zrmode
//                            acMode(ja.getJSONObject(1), "zrmode",map_mod);
//                            //自动模式 zdmode
//                            acMode(ja.getJSONObject(2), "zdmode",map_mod);
//                            //送风模式 sfmode
//                            acMode(ja.getJSONObject(3), "sfmode",map_mod);
//                            //除湿模式 csmode
//                            acMode(ja.getJSONObject(4), "csmode",map_mod);
//                            map_mod_json = JSON.toJSONString(map_mod);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        //空调的组合按键
//                        Map<String,String> map_zhan=new HashMap<>();
//                        ArrayList<IrData.IrKey> keyList = irData.keys;
//                        String keySize = irData.rid + "的组合按键个数：" + (keyList == null ? "0" : keyList.size()) + "\n";
//                        map_zhan.put("zhmenu",keyList == null ? "0" : keyList.size()+"");
//                        Logger.d(keySize);
//                        if (keyList != null) {
//                            IrData.IrKey irKey = keyList.get(0);
//                            Logger.d("按键参数：" + irKey.fkey + "=" + irKey.pulse);
//                            map_zhan.put(irKey.fkey,irKey.pulse);
//                            IrData.IrKey irKey1 = keyList.get(1);
//                            Logger.d("按键参数：" + irKey1.fkey + "=" + irKey1.pulse);
//                            map_zhan.put(irKey1.fkey,irKey1.pulse);
//                            IrData.IrKey irKey2 = keyList.get(keyList.size() - 1);
//                            Logger.d("按键参数：" + irKey2.fkey + "=" + irKey2.pulse);
//                            map_zhan.put(irKey2.fkey,irKey2.pulse);
//                        }
//                        map_zhan_json = JSON.toJSONString(map_zhan);
//                    }
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getNoStateIRDataById_android", map_zhan_json,map_mod_json);
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    //按红外设备授权的客户，才会用到这两个值
//                    if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
//                        msg = "下载的遥控器超过了套数限制";
//                    } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                        msg = "设备总数超过了授权的额度";
//                    }
//                    TipsUtil.toast(MainActivity.this, msg);
//                }
//
//            });
//        }
//
//        /**
//         * 获取IpTV红外列表
//         */
//        @JavascriptInterface
//        public void getIPTV(int spid) {
//            //根据spid 获取IPTV列表
//            KookongSDK.getIPTV(spid, new IRequestResult<StbList>() {
//
//                @Override
//                public void onSuccess(String msg, StbList result) {
//                    String s1 = JSON.toJSONString(result);
////                    String replace = s1.replace("\\\"","\"").replace(":\"", "\":\"").replace("\"\":", "\":").replace("{", "{\"")
////                            .replace("{\"\"", "{\"").replace(",", ",\"").replace(",\"\"", ",\"")
////                            .replace("{\"}", "{}").replace(",\"{", ",{");
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getIPTV_android", s1);
//                    List<StbList.Stb> stbs = result.stbList;
//                    for (int i = 0; i < stbs.size(); i++) {
//                        Logger.d("The Stb is " + stbs.get(i).bname);
//                    }
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//        /**
//         * 搜索关键字 华 机顶盒
//         */
//        @JavascriptInterface
//        public void searchSTB(String keyword) {
//            //关键词是为 "华" 的 机顶盒列表
//            KookongSDK.searchSTB(keyword, 110108, new IRequestResult<StbList>() {
//
//                @Override
//                public void onSuccess(String msg, StbList result) {
//                    String s = JSON.toJSONString(result);
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_searchSTB_android",s);
//                    List<StbList.Stb> stbs = result.stbList;
//                    for (int i = 0; i < stbs.size(); i++) {
//                        Logger.d("The Stb is " + stbs.get(i).bname);
//                    }
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//        /**
//         * 获取某设备的品牌列表
//         * public int STB = 1; //机顶盒
//         * public int TV  = 2; //电视
//         * public int BOX = 3; //网络盒子
//         * public int DVD = 4; //DVD
//         * public int AC  = 5; //空调
//         * public int PRO = 6; //投影仪
//         * public int PA  = 7; //功放
//         * public int FAN = 8; //风扇
//         * public int SLR = 9; //单反相机
//         * public int Light = 10; //开关灯泡
//         * public int AIR_CLEANER = 11;// 空气净化器
//         * public int WATER_HEATER = 12;// 热水器
//         */
//        @JavascriptInterface
//        public void getBrandListFromNet(int device) {
//            // 获取电视机, 空调等, (除STB以外) 的设备品牌列表
//            KookongSDK.getBrandListFromNet(device, new IRequestResult<BrandList>() {
//                Map<String, List<BrandList.Brand>> map = new HashMap<>();
//
//                @Override
//                public void onSuccess(String msg, BrandList result) {
//                    List<BrandList.Brand> stbs = result.brandList;
//                    map.put("result", stbs);
//                    String s = JSON.toJSONString(map);
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getBrandListFromNet_android", s);
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//        /**
//         * 指定某设备某品牌下的所有的红外码的id
//         * 指定brand下的电视机所有的红外码的id spid和areaid都传0
//         * 获取指定设备类型(机顶盒是spId和areaid,所以这里不是机顶盒的获取方
//         * 式，机顶盒的红外码都是按区域划分和品牌关系不大),指定品牌下的红外码
//         */
//        @JavascriptInterface
//        public void getAllRemoteIdsNoJDH() {
//            KookongSDK.getAllRemoteIds(Device.TV, 967, 0, 0, new IRequestResult<RemoteList>() {
//
//                @Override
//                public void onSuccess(String msg, RemoteList result) {
//                    String s = JSON.toJSONString(result);
//                    mAgentWeb.getJsAccessEntrace().quickCallJs("lc_getAllRemoteIdsNoJDH_android",s);
//                    List<Integer> remoteids = result.rids;
//                    String res = Arrays.toString(remoteids.toArray());
//                    Logger.d("tv remoteids: " + res);
//                }
//
//                @Override
//                public void onFail(Integer errorCode, String msg) {
//                    //按红外设备授权的客户，才会用到这两个值
//                    if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
//                        msg = "下载的遥控器超过了套数限制";
//                    } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                        msg = "设备总数超过了授权的额度";
//                    }
//                    TipsUtil.toast(MainActivity.this, msg);
//
//                }
//            });
//        }
//
//
//    }
    /**
     * ====================================Android与HTML通信接口===========结束===========================================
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            //获取省份
            province = location.getProvince();
            //获取城市
            city = location.getCity();
            //获取区县
            district = location.getDistrict();
            String street = location.getStreet();    //获取街道信息
            Log.e("TAG", province + "      " + city + "    " + district);
            if (!TextUtils.isEmpty(province)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(district)){
                SPUtils.put(MainActivity.this,"province",province);
                SPUtils.put(MainActivity.this,"city",city);
                SPUtils.put(MainActivity.this,"district",district);
            }
        }
    }

//    private WebViewClient mWebViewClient = new WebViewClient() {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            //do you  work
//            Log.i("Info", "BaseWebActivity onPageStarted");
//        }
//    };
//    private WebChromeClient mWebChromeClient = new WebChromeClient() {
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            //do you work
////            Log.i("Info","onProgress:"+newProgress);
//        }
//
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            super.onReceivedTitle(view, title);
//
//        }
//    };
//
//    public String getUrl() {
//
//        return "file:///android_asset/index.html";
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    protected void onPause() {
//        mAgentWeb.getWebLifeCycle().onPause();
//        super.onPause();
//
//    }

//    @Override
//    protected void onResume() {
//        mAgentWeb.getWebLifeCycle().onResume();
//        super.onResume();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Log.i("Info", "onResult:" + requestCode + " onResult:" + resultCode);
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //mAgentWeb.destroy();
//        mAgentWeb.getWebLifeCycle().onDestroy();
//    }

    /*
     * 服务器返回错误代码含义
     * 代码     	意义
     * code 1	secret 错误
     * code 2	secret 不存在
     * code 3	客户已被禁用
     * code 4	客户已超期
     * code 6	选择运营商超出数量限制
     * code 7	无访问权限
     * code 8	试用数超出限制
     * code 9	设备数超出限制
     * code 10	单台设备下载红外超出限制
     */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            //----------------------------设置流部分------------------
//            case R.id.setupflow_get_areaid:
//                //获取指定城市的AreaId
//                KookongSDK.getAreaId("北京市", "北京市", "海淀区", new IRequestResult<Integer>() {
//
//                    @Override
//                    public void onSuccess(String msg, Integer result) {
//                        Logger.d("AreaId is : " + result);
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_get_operators:
//                //获取指定AreaId下的运营商列表
//                KookongSDK.getOperaters(110108, new IRequestResult<SpList>() {
//
//                    @Override
//                    public void onSuccess(String msg, SpList result) {
//                        List<SpList.Sp> sps = result.spList;
//                        for (int i = 0; i < sps.size(); i++) {
//                            Logger.d("The sp is " + sps.get(i).spName);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_get_stb_remotes:
//                //获取指定运营商, 指定AreaId下的遥控器id brandid传0
//                KookongSDK.getAllRemoteIds(Device.STB, 0, 262, 110108, new IRequestResult<RemoteList>() {
//
//                    @Override
//                    public void onSuccess(String msg, RemoteList result) {
//                        List<Integer> remoteids = result.rids;
//                        String res = Arrays.toString(remoteids.toArray());
//                        Logger.d("remoteids: " + res);
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_test_remotedata://获取的是某套红外码部分按键，仅限按红外设备授权的客户（初始化时需要传递的deviceId），如果初始化时没有传deviceId，请求接口时会提示参数错误
//                //对码测试获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
//                KookongSDK.testIRDataById("4162", Device.STB, true, new IRequestResult<IrDataList>() {
//
//                    @Override
//                    public void onSuccess(String msg, IrDataList result) {
//                        List<IrData> irDatas = result.getIrDataList();
//                        for (int i = 0; i < irDatas.size(); i++) {
//                            Logger.d("The rid is " + irDatas.get(i).rid);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        //按红外设备授权的客户，才会用到这个指
//                        if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                            msg = "设备总数超过了授权的额度";
//                        }
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_get_remotedata://这是获取完整红外码的方法
//                //获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
//                KookongSDK.getIRDataById("4162", Device.STB, true, new IRequestResult<IrDataList>() {
//
//                    @Override
//                    public void onSuccess(String msg, IrDataList result) {
//                        List<IrData> irDatas = result.getIrDataList();
//                        for (int i = 0; i < irDatas.size(); i++) {
//                            Logger.d("The rid is " + irDatas.get(i).rid);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        //按红外设备授权的客户，才会用到这两个值
//                        if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
//                            msg = "下载的遥控器超过了套数限制";
//                        } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                            msg = "设备总数超过了授权的额度";
//                        }
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_get_remotedata_allparse://特殊客户调用的下载空调红外码（有状态的空调，IrData.type=2）
//                //获取rid = 2607的 红外码
////                KookongSDK.getNoStateIRDataById("11842", Device.AC, true, new IRequestResult<IrDataList>() {
////
////                    @Override
////                    public void onSuccess(String msg, IrDataList result) {
////                        List<IrData> irDatas = result.getIrDataList();
////                        for (int i = 0; i < irDatas.size(); i++) {
////                            IrData irData = irDatas.get(i);
////                            Logger.d("空调：" + irData.rid);
////                            //空调支持的模式、温度、风速
////                            HashMap<Integer, String> exts = irData.exts;
////                            //遥控器参数
////                            String remoteParam = exts.get(99999);
////                            Logger.d("遥控器参数99999：" + remoteParam);
////                            try {
////                                JSONArray ja = new JSONArray(exts.get(0));
////                                //遍历模式，模式顺序：制冷、制热、自动、送风、除湿
////
////                                //制冷模式
////                                acMode(ja.getJSONObject(0), "制冷模式");
////                                //制热模式
////                                acMode(ja.getJSONObject(1), "制热模式");
////                                //自动模式
////                                acMode(ja.getJSONObject(2), "自动模式");
////                                //送风模式
////                                acMode(ja.getJSONObject(3), "送风模式");
////                                //除湿模式
////                                acMode(ja.getJSONObject(4), "除湿模式");
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////
////                            //空调的组合按键
////                            ArrayList<IrData.IrKey> keyList = irData.keys;
////                            String keySize = irData.rid + "的组合按键个数：" + (keyList == null ? "0" : keyList.size()) + "\n";
////                            Logger.d(keySize);
////                            if (keyList != null) {
////                                IrData.IrKey irKey = keyList.get(0);
////                                Logger.d("按键参数：" + irKey.fkey + "=" + irKey.pulse);
////                                IrData.IrKey irKey1 = keyList.get(1);
////                                Logger.d("按键参数：" + irKey1.fkey + "=" + irKey1.pulse);
////                                IrData.IrKey irKey2 = keyList.get(keyList.size() - 1);
////                                Logger.d("按键参数：" + irKey2.fkey + "=" + irKey2.pulse);
////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onFail(Integer errorCode, String msg) {
////                        //按红外设备授权的客户，才会用到这两个值
////                        if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
////                            msg = "下载的遥控器超过了套数限制";
////                        } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
////                            msg = "设备总数超过了授权的额度";
////                        }
////                        TipsUtil.toast(MainActivity.this, msg);
////                    }
////
////                });
//                break;
//            case R.id.setupflow_get_iptv:
//                //根据spid 获取IPTV列表
//                KookongSDK.getIPTV(267, new IRequestResult<StbList>() {
//
//                    @Override
//                    public void onSuccess(String msg, StbList result) {
//                        List<StbList.Stb> stbs = result.stbList;
//                        for (int i = 0; i < stbs.size(); i++) {
//                            Logger.d("The Stb is " + stbs.get(i).bname);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//
//                break;
//            case R.id.setupflow_searchstb:
//                //关键词是为 "华" 的 机顶盒列表
//                KookongSDK.searchSTB("华", 110108, new IRequestResult<StbList>() {
//
//                    @Override
//                    public void onSuccess(String msg, StbList result) {
//                        List<StbList.Stb> stbs = result.stbList;
//                        for (int i = 0; i < stbs.size(); i++) {
//                            Logger.d("The Stb is " + stbs.get(i).bname);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//            case R.id.setupflow_get_brandList:
//                // 获取电视机, 空调等, (除STB以外) 的设备品牌列表
//                /*
//                 * public int STB = 1; //机顶盒
//                 * public int TV  = 2; //电视
//                 * public int BOX = 3; //网络盒子
//                 * public int DVD = 4; //DVD
//                 * public int AC  = 5; //空调
//                 * public int PRO = 6; //投影仪
//                 * public int PA  = 7; //功放
//                 * public int FAN = 8; //风扇
//                 * public int SLR = 9; //单反相机
//                 * public int Light = 10; //开关灯泡
//                 * public int AIR_CLEANER = 11;// 空气净化器
//                 * public int WATER_HEATER = 12;// 热水器
//                 */
//                KookongSDK.getBrandListFromNet(Device.TV, new IRequestResult<BrandList>() {
//
//                    @Override
//                    public void onSuccess(String msg, BrandList result) {
//                        List<BrandList.Brand> stbs = result.brandList;
//                        for (int i = 0; i < stbs.size(); i++) {
//                            Logger.d("The Brand is " + stbs.get(i).cname);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
//
//            case R.id.setupflow_get_ir_by_brand:
//
//                //指定brand下的电视机所有的红外码的id spid和areaid都传0
//                //获取指定设备类型(机顶盒是spId和areaid,所以这里不是机顶盒的获取方
//                //式，机顶盒的红外码都是按区域划分和品牌关系不大),指定品牌下的红外码
//                KookongSDK.getAllRemoteIds(Device.TV, 967, 0, 0, new IRequestResult<RemoteList>() {
//
//                    @Override
//                    public void onSuccess(String msg, RemoteList result) {
//                        List<Integer> remoteids = result.rids;
//                        String res = Arrays.toString(remoteids.toArray());
//                        Logger.d("tv remoteids: " + res);
//                    }
//
//                    @Override
//                    public void onFail(Integer errorCode, String msg) {
//                        //按红外设备授权的客户，才会用到这两个值
//                        if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
//                            msg = "下载的遥控器超过了套数限制";
//                        } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
//                            msg = "设备总数超过了授权的额度";
//                        }
//                        TipsUtil.toast(MainActivity.this, msg);
//
//                    }
//                });
//                break;
////            case R.id.ir_openac: {
////                Intent intent = new Intent(MainActivity.this, ACV2Activity.class);
////                startActivity(intent);
////            }
////            break;
////            case R.id.ir_opennonac: {
////                Intent intent = new Intent(MainActivity.this, NonAcActivity.class);
////                startActivity(intent);
////            }
////            break;
//            default:
//                break;
//        }
//
//    }
//
//    private void acMode(JSONObject jo, String mode,Map<String,Map<String,String>> mod) throws JSONException {
//        String speed = jo.optString("speed");
//
//        String temperature = jo.optString("temperature");
//        Map<String,String> mod_s_t=new HashMap<>();
//        mod_s_t.put("speed",speed);
//        mod_s_t.put("temperature",temperature);
//        mod.put(mode,mod_s_t);
//        if (TextUtils.isEmpty(speed) && TextUtils.isEmpty(temperature)) {
//            Logger.d("不具备" + mode);
//        } else {
//            Logger.d(mode + "支持的可调风速：" + speed + "，支持的可调温度：" + temperature);
//        }
//
//    }
}
