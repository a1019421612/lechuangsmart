package com.hbdiye.lechuangsmart.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;

public class TVNumDialog extends Dialog {
    private Context context;
    private TextView tv_num_one, tv_num_two, tv_num_three, tv_num_four, tv_num_five, tv_num_six, tv_num_seven, tv_num_eight, tv_num_nine, tv_num_back, tv_num_zero, tv_num_pick;
    private View.OnClickListener clicerm;

    public TVNumDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public TVNumDialog(Context context, int theme, View.OnClickListener clicerm) {
        super(context, theme);
        this.context = context;
        this.clicerm = clicerm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_num_layout);
        initViews();
    }

    public void initViews() {
        tv_num_one = this.findViewById(R.id.tv_num_one);
        tv_num_two = this.findViewById(R.id.tv_num_two);
        tv_num_three = this.findViewById(R.id.tv_num_three);
        tv_num_four = this.findViewById(R.id.tv_num_four);
        tv_num_five = this.findViewById(R.id.tv_num_five);
        tv_num_six = this.findViewById(R.id.tv_num_six);
        tv_num_seven = this.findViewById(R.id.tv_num_seven);
        tv_num_eight = this.findViewById(R.id.tv_num_eight);
        tv_num_nine = this.findViewById(R.id.tv_num_nine);
        tv_num_back = this.findViewById(R.id.tv_num_back);
        tv_num_zero = this.findViewById(R.id.tv_num_zero);
        tv_num_pick = this.findViewById(R.id.tv_num_pick);

        tv_num_one.setOnClickListener(clicerm);
        tv_num_two.setOnClickListener(clicerm);
        tv_num_three.setOnClickListener(clicerm);
        tv_num_four.setOnClickListener(clicerm);
        tv_num_five.setOnClickListener(clicerm);
        tv_num_six.setOnClickListener(clicerm);
        tv_num_seven.setOnClickListener(clicerm);
        tv_num_eight.setOnClickListener(clicerm);
        tv_num_nine.setOnClickListener(clicerm);
        tv_num_back.setOnClickListener(clicerm);
        tv_num_zero.setOnClickListener(clicerm);
        tv_num_pick.setOnClickListener(clicerm);
    }
}
