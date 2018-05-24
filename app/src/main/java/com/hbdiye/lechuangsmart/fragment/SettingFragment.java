package com.hbdiye.lechuangsmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.activity.AboutUsActivity;
import com.hbdiye.lechuangsmart.activity.EditPswActivity;
import com.hbdiye.lechuangsmart.activity.FamilyManagerActivity;
import com.hbdiye.lechuangsmart.activity.FamilyNameActivity;
import com.hbdiye.lechuangsmart.activity.VersionActivity;
import com.hbdiye.lechuangsmart.activity.WiFiActivity;

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
    private Unbinder unbinder;

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

    @OnClick({R.id.tv_setting_family_name, R.id.tv_setting_editpsw, R.id.tv_family_manager, R.id.tv_setting_wifi, R.id.tv_setting_about_us, R.id.tv_setting_version})
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
        }
    }
}
