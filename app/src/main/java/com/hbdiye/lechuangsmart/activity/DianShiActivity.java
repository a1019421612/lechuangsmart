package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.ImageUtil;
import com.hbdiye.lechuangsmart.views.RoundMenuView;
import com.hbdiye.lechuangsmart.views.TVNumDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电视遥控器
 */
public class DianShiActivity extends BaseActivity {


    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;
    @BindView(R.id.tv_ds_power)
    TextView tvDsPower;
    @BindView(R.id.tv_ds_inputsource)
    TextView tvDsInputsource;
    @BindView(R.id.tv_ds_number)
    TextView tvDsNumber;
    @BindView(R.id.tv_ds_menu)
    TextView tvDsMenu;
    @BindView(R.id.tv_ds_novoice)
    TextView tvDsNovoice;
    @BindView(R.id.tv_ds_exp)
    TextView tvDsExp;
    @BindView(R.id.tv_ds_channel_up)
    TextView tvDsChannelUp;
    @BindView(R.id.tv_ds_channel_down)
    TextView tvDsChannelDown;
    @BindView(R.id.tv_ds_refresh)
    TextView tvDsRefresh;
    @BindView(R.id.tv_ds_voice_up)
    TextView tvDsVoiceUp;
    @BindView(R.id.tv_ds_voice_down)
    TextView tvDsVoiceDown;

    private TVNumDialog dialog;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "电视";
    }

    @Override
    protected void initView() {
        initRoundMenu();
    }

    private void initRoundMenu() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(DianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了1");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(DianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了2");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(DianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了3");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon = ImageUtil.drawable2Bitmap(DianShiActivity.this, R.mipmap.right);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了4");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor(R.color.gray_f2f2),
                getResources().getColor(R.color.gray_9999), getResources().getColor(R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(DianShiActivity.this, R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SmartToast.show("点击了中心圆圈");
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_dian_shi;
    }

    @OnClick({R.id.tv_ds_power, R.id.tv_ds_inputsource, R.id.tv_ds_number, R.id.tv_ds_menu, R.id.tv_ds_novoice,
            R.id.tv_ds_exp, R.id.tv_ds_channel_up, R.id.tv_ds_channel_down, R.id.tv_ds_refresh, R.id.tv_ds_voice_up, R.id.tv_ds_voice_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ds_power:
                //开关
                break;
            case R.id.tv_ds_inputsource:
                //输入源
                break;
            case R.id.tv_ds_number:
                //数字键盘
                dialog = new TVNumDialog(this, R.style.MyDialogStyle, NumOnClick);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.tv_ds_menu:
                //菜单
                break;
            case R.id.tv_ds_novoice:
                //静音
                break;
            case R.id.tv_ds_exp:
//                扩展键
                break;
            case R.id.tv_ds_channel_up:
//                频道+
                break;
            case R.id.tv_ds_channel_down:
//                频道-
                break;
            case R.id.tv_ds_refresh:
//                刷新
                break;
            case R.id.tv_ds_voice_up:
//                声音+
                break;
            case R.id.tv_ds_voice_down:
//                声音-
                break;
        }
    }

    public View.OnClickListener NumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_num_one:
                    SmartToast.show("1");
                    break;
                case R.id.tv_num_two:
                    SmartToast.show("2");
                    break;
                case R.id.tv_num_three:
                    SmartToast.show("3");
                    break;
                case R.id.tv_num_four:
                    SmartToast.show("4");
                    break;
                case R.id.tv_num_five:
                    SmartToast.show("5");
                    break;
                case R.id.tv_num_six:
                    SmartToast.show("6");
                    break;
                case R.id.tv_num_seven:
                    SmartToast.show("7");
                    break;
                case R.id.tv_num_eight:
                    SmartToast.show("8");
                    break;
                case R.id.tv_num_nine:
                    SmartToast.show("9");
                    break;
                case R.id.tv_num_back:
                    SmartToast.show("back");
                    break;
                case R.id.tv_num_zero:
                    SmartToast.show("0");
                    break;
                case R.id.tv_num_pick:
                    SmartToast.show("-/--");
                    break;
            }
        }
    };
}
