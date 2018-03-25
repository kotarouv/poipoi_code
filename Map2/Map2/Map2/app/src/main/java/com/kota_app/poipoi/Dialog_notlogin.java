package com.kota_app.poipoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kota327v on 2017/11/20.
 */

public class Dialog_notlogin extends DialogFragment {
    private static final int RC_SIGN_IN = 123;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("この操作はログインしていないと実行できません\nログイン・新規登録の画面に移動しますか？")
                .setPositiveButton("移動する",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int id){
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
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }


}
