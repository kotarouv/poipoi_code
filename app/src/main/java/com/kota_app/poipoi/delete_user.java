package com.kota_app.poipoi;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class delete_user extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ConstraintLayout constraintLayout_delete;
    private ConstraintLayout constraintLayout_reauth;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private delete_user context;
    String providerid = user.getProviders().get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        setTitle("アカウントを削除");
        constraintLayout_delete = (ConstraintLayout) findViewById(R.id.constraintLayout_delete);
        constraintLayout_reauth = (ConstraintLayout) findViewById(R.id.constraintLayout_reauth);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout_delete_user);
        textInputEditText = (TextInputEditText) findViewById(R.id.edittext_password);
        context = this;
    }

    public void Click_delete(View view){
            user.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void Void) {
                            AsyncNetworkTask_deleteuser task_deleteuser = new AsyncNetworkTask_deleteuser(context);
                            String uid = String.format("uid=%s",auth.getUid());
                            task_deleteuser.execute(uid);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(providerid.equals("password")){
                                constraintLayout_delete.setVisibility(View.GONE);
                                constraintLayout_reauth.setVisibility(View.VISIBLE);
                            }
                        }
                    });
    }

    public void Click_reauth(View view){
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), textInputEditText.getText().toString());
            user.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void  Void) {
                            AsyncNetworkTask_deleteuser task_deleteuser = new AsyncNetworkTask_deleteuser(context);
                            String uid = String.format("uid=%s", auth.getUid());
                            task_deleteuser.execute(uid);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            textInputLayout.setError("パスワードが違います");
                        }
                    });
    }
}
