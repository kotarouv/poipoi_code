package com.kota_app.poipoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onLoginClick(View view) {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, EnterUserName.class));
            finish();

        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    public void onNonLoginClick(View view){
        startActivity(new Intent(this, ScrollingActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in
                startActivity(new Intent(this, EnterUserName.class));
                finish();
            } else {
                Toast.makeText(this,"サインインできませんでした。",Toast.LENGTH_LONG);
            }
        }
    }
}
