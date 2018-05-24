package com.hbdiye.lechuangsmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.activity.AboutUsActivity;
import com.hbdiye.lechuangsmart.activity.EditPswActivity;
import com.hbdiye.lechuangsmart.activity.FamilyManagerActivity;
import com.hbdiye.lechuangsmart.activity.FamilyNameActivity;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.VersionActivity;
import com.hbdiye.lechuangsmart.activity.WiFiActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.GetPhotoPopwindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends Fragment {
    @BindView(R.id.tv_setting_family_name)
    TextView tvSettingFamilyName;
    @BindView(R.id.tv_setting_editpsw)
    TextView tvSettingEditpsw;
    @BindView(R.id.tv_family_manager)
    TextView tvFamilyManager;
    @BindView(R.id.tv_setting_wifi)
    TextView tvSettingWifi;
    @BindView(R.id.tv_setting_about_us)
    TextView tvSettingAboutUs;
    @BindView(R.id.tv_setting_version)
    TextView tvSettingVersion;
    @BindView(R.id.tv_settting_logout)
    TextView tvSetttingLogout;
    @BindView(R.id.ll_parent)
    LinearLayout parent_ll;
    private Unbinder unbinder;

    private GetPhotoPopwindow getPhotoPopwindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_setting_family_name, R.id.tv_setting_editpsw, R.id.tv_family_manager, R.id.tv_setting_wifi, R.id.tv_setting_about_us, R.id.tv_setting_version,R.id.tv_settting_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_family_name:
                startActivity(new Intent(getActivity(), FamilyNameActivity.class));
                break;
            case R.id.tv_setting_editpsw:
                startActivity(new Intent(getActivity(), EditPswActivity.class));
                break;
            case R.id.tv_family_manager:
                startActivity(new Intent(getActivity(), FamilyManagerActivity.class));
                break;
            case R.id.tv_setting_wifi:
                startActivity(new Intent(getActivity(), WiFiActivity.class));
                break;
            case R.id.tv_setting_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.tv_setting_version:
                startActivity(new Intent(getActivity(), VersionActivity.class));
                break;
            case R.id.tv_settting_logout:
                getPhotoPopwindow = new GetPhotoPopwindow(getActivity(), photoclicer);
                getPhotoPopwindow.showPopupWindowBottom(parent_ll);
                break;
        }
    }

    public View.OnClickListener photoclicer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    //注销账号
                    getPhotoPopwindow.dismiss();
                    SPUtils.clear(getActivity());
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    break;
                case R.id.item_popupwindows_Photo:
                    //退出应用
                    getPhotoPopwindow.dismiss();
                    getActivity().finish();
                    break;
                case R.id.item_popupwindows_cancel:
                    getPhotoPopwindow.dismiss();
                    break;
            }
        }
    };
}
