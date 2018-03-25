package com.kota_app.poipoi;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class update_user_name extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextInputEditText textInputEditText;
    private ConstraintLayout constraintLayout;
    private InputMethodManager inputMethodMgr;
    private TextInputLayout textInputLayout;
    private Button button;
    public ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_name);
        textInputEditText = (TextInputEditText) findViewById(R.id.textinputedittext_update_name);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout_update_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_update_name);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintlayout_update_name);
        inputMethodMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        button = (Button) findViewById(R.id.button_update_name);
        imageView = (ImageView) findViewById(R.id.imageView_update_name);
        setTitle("ユーザー名を変更");
        final String mes = "update";
        AsyncNetworkTask_checkuser task = new AsyncNetworkTask_checkuser(this, mes, null, null);
        String uid = String.format("uid=%s",auth.getUid());
        task.execute(uid);
        textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // フォーカスが外れた場合キーボードを非表示にする
                    InputMethodManager inputMethodMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if(textInputEditText.getText().toString().replace(" ","").length() != 0 && textInputEditText.getText().toString().replace("　","").length() != 0) {
                        asyncstart_checkusername();
                    }else {
                        textInputLayout.setError("入力してください");
                    }
                }else {
                    textInputLayout.setError("");
                    button.setEnabled(false);
                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void asyncstart_checkusername(){
        AsyncNetworkTask_checkusername task_checkusername = new AsyncNetworkTask_checkusername(this, "update");
        String name = String.format("name=%s",textInputEditText.getText());
        task_checkusername.execute(name);
    }

    public void button_click(View view){
        AsyncNetworkTask_change_name task_checkusername = new AsyncNetworkTask_change_name(this);
        String name = String.format("uid=%s&name=%s",auth.getUid(),textInputEditText.getText());
        task_checkusername.execute(name);
    }

    public void focus_change(View view){}
}
