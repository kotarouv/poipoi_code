package com.kota_app.poipoi;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EnterUserName extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextInputEditText textInputEditText;
    private ConstraintLayout constraintLayout;
    private InputMethodManager inputMethodMgr;
    private TextInputLayout textInputLayout;
    private Button button;
    public ProgressBar progressBar;
    private ProgressBar progressBar2;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_name);
        textInputEditText = (TextInputEditText) findViewById(R.id.textinputedittext);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar5);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout4);
        inputMethodMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        button = (Button) findViewById(R.id.button8);
        imageView = (ImageView) findViewById(R.id.imageView_enter);
        setTitle("情報を取得しています");

        textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // フォーカスが外れた場合キーボードを非表示にする
                    InputMethodManager inputMethodMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    progressBar2.setVisibility(View.VISIBLE);
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

        if (auth.getCurrentUser() != null) {
            String user = auth.getCurrentUser().getDisplayName();
            textInputEditText.setText(user);
            asyncstart();
        }
    }

    public void asyncstart(){
        final String mes = "enter";
        AsyncNetworkTask_checkuser task = new AsyncNetworkTask_checkuser(this,mes, null, null);
        String uid = String.format("uid=%s",auth.getUid());
        task.execute(uid);
    }

    public void asyncstart_checkusername(){
            AsyncNetworkTask_checkusername task_checkusername = new AsyncNetworkTask_checkusername(this, "enter");
            String name = String.format("name=%s",textInputEditText.getText());
            task_checkusername.execute(name);
    }

    public void button_click(View view){
        AsyncNetworkTask_adduser task = new AsyncNetworkTask_adduser(this);
        String user = String.format("uid=%s&name=%s",auth.getUid(),textInputEditText.getText());
        task.execute(user);
    }

    public void focus_change(View view){}

}
