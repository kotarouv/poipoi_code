package com.kota_app.poipoi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class update_password extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setTitle("パスワードを再設定");
        textView = (TextView) findViewById(R.id.textView_email);
        if(auth != null) {
            textView.setText(auth.getCurrentUser().getEmail());
        }
    }

    public void button_sent_Click(View view){
        try {
            auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail());
        }finally {
            Toast.makeText(this, "メールの送信をリクエストしました", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Setting_account.class));
        }
    }
}
