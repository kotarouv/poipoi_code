package com.kota_app.poipoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * Created by kota327v on 2017/11/21.
 */

public class Dialog_userinfo extends DialogFragment {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView textView;
    private Button button;
    private static final int RC_SIGN_IN = 123;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_dialoglayout_userinfo, null);
        textView = (TextView) layout.findViewById(R.id.textView18);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("")
                .setView(layout)
                .setPositiveButton(" ", null);

        Dialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                final AlertDialog d = (AlertDialog) dialog;
                button = d.getButton(d.BUTTON_POSITIVE);
                asyncstart();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(auth.getUid()!=null){
                            auth.signOut();
                            Toast.makeText(getActivity(), "ログアウトしました", Toast.LENGTH_LONG).show();
                            d.cancel();
                        } else {
                            getActivity().startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(
                                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                                    ))
                                            .setIsSmartLockEnabled(false)
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                });
            }
        });
        return d;
    }

    public void asyncstart(){
        final String mes = "userinfo";
        AsyncNetworkTask_checkuser task = new AsyncNetworkTask_checkuser(getActivity(), mes, textView, button);
        String uid = String.format("uid=%s",auth.getUid());
        task.execute(uid);
    }
}
