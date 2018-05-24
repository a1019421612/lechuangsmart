package com.hbdiye.lechuangsmart.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.SceneAdapter;

import java.util.ArrayList;
import java.util.Random;

public class SceneFragment extends Fragment{
    private RecyclerView mRecyclerView;
    SceneAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
//        adapter.expandAll();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            // 隐藏
            Log.e("TAG","scen"+"隐藏");
        } else {
            // 可视
            Log.e("TAG","scen"+"显示");
        }
    }
}
