package com.hbdiye.lechuangsmart.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;

public class SceneDialog extends Dialog {
    private Context context;
    private TextView cancle_tv,sure_tv,title_tv;
    private EditText applayloandailog_code_edt;
    private View.OnClickListener clicerm;
    private String title;
    public SceneDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public SceneDialog(Context context, int theme, View.OnClickListener clicerm,String title) {
        super(context, theme);
        this.context = context;
        this.clicerm=clicerm;
        this.title=title;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenedialog);
        initViews();
    }
    public void initViews(){
        title_tv=this.findViewById(R.id.tv_dialog_title);
        cancle_tv=(TextView)this.findViewById(R.id.app_cancle_tv);
        sure_tv=(TextView)this.findViewById(R.id.app_sure_tv);
        applayloandailog_code_edt=(EditText)this.findViewById(R.id.applayloandailog_code_edt);
        title_tv.setText(title);
        cancle_tv.setOnClickListener(clicerm);
        sure_tv.setOnClickListener(clicerm);
    }
    public String getSceneName(){
        return applayloandailog_code_edt.getText().toString().trim();
    }
}
