package com.kota_app.poipoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by kota327v on 2017/07/06.
 */

public class Dialog_add extends DialogFragment {

    private String data;
    private LinearLayout layout_success;
    private LinearLayout layout_failed;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String Position = getArguments().getString("LatLng");
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.dialoglayout_add, null);
        final LinearLayout layout1 = (LinearLayout) layout.findViewById(R.id.line1);
        final LinearLayout layout2 = (LinearLayout) layout.findViewById(R.id.line2);
        final LinearLayout layout3 = (LinearLayout) layout.findViewById(R.id.line3);
        final LinearLayout layout4 = (LinearLayout) layout.findViewById(R.id.line4);
        layout_success = (LinearLayout) layout.findViewById(R.id.success);
        layout_failed = (LinearLayout) layout.findViewById(R.id.failed);
        final Button button_Yes = (Button) layout.findViewById(R.id.button_Yes);
        final Button button_No = (Button) layout.findViewById(R.id.button_No);
        final Button button_retry = (Button) layout.findViewById(R.id.button_retry);
        final ListView listView = (ListView) layout.findViewById(R.id.listview_type);
        final TextView textView_error = (TextView) layout.findViewById(R.id.textView_error);
        final ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar_dialog);
        final EditText editText = (EditText) layout.findViewById(R.id.edittext);
        final EditText editText2 = (EditText) layout.findViewById(R.id.edittext2);
        final TextInputLayout textInputLayout = (TextInputLayout) layout.findViewById(R.id.input) ;
        final TextInputLayout textInputLayout2 = (TextInputLayout) layout.findViewById(R.id.input2) ;
        ArrayAdapter<String> adapter;
        String[] kinds = getResources().getStringArray(R.array.gomi_kind);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, kinds); //noinspection GroovyAssignabilityCheck
        listView.setAdapter(adapter);

        listView.invalidateViews();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("")
                .setView(layout)
                .setPositiveButton(R.string.next, null)
                .setNegativeButton(R.string.back, null);

        Dialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final AlertDialog d = (AlertDialog) dialog;
                final Button button_positive = d.getButton(d.BUTTON_POSITIVE);
                final Button button_negative = d.getButton(d.BUTTON_NEGATIVE);
                button_negative.setEnabled(false);
                // NEUTRALボタンを取得してView.OnClickListenerで上書き
                d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(layout1.getVisibility() == View.VISIBLE){
                            if (!listView.isItemChecked(0) && !listView.isItemChecked(1) && !listView.isItemChecked(2) && !listView.isItemChecked(3)){
                                textView_error.setVisibility(View.VISIBLE);
                                return;
                            }
                            textView_error.setVisibility(View.GONE);
                            button_negative.setEnabled(true);
                            layout1.setVisibility(View.GONE);
                            layout2.setVisibility(View.VISIBLE);
                        } else if(layout2.getVisibility() == View.VISIBLE){
                            if(String.valueOf(editText.getText()).equals("")) {
                                textInputLayout.setError(" ");
                                return;
                            }
                            textInputLayout.setError(null);
                            layout2.setVisibility(View.GONE);
                            layout3.setVisibility(View.VISIBLE);
                            button_positive.setEnabled(false);
                        } else  if (layout4.getVisibility() == View.VISIBLE){
                            if(String.valueOf(editText2.getText()).equals("")) {
                                textInputLayout2.setError(" ");
                                return;
                            }
                            data = data + String.format("&reason=%s", editText2.getText());
                            button_positive.setEnabled(false);
                            button_negative.setEnabled(false);
                            async_start(layout4, progressBar,listView, Position);
                        }

                    }
                });

                d.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(layout2.getVisibility() == View.VISIBLE){
                            textInputLayout.setError(null);
                            button_positive.setEnabled(true);
                            button_negative.setEnabled(false);
                            layout1.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                        } else if (layout3.getVisibility() == View.VISIBLE){
                            button_positive.setEnabled(true);
                            layout2.setVisibility(View.VISIBLE);
                            layout3.setVisibility(View.GONE);
                        } else if (layout4.getVisibility() == View.VISIBLE){
                            textInputLayout2.setError(null);
                            button_positive.setEnabled(false);
                            layout3.setVisibility(View.VISIBLE);
                            layout4.setVisibility(View.GONE);
                        }
                    }
                });

                button_Yes.setOnClickListener(new View.OnClickListener(){
                   @Override
                    public void onClick(View v){
                       data = "open=True&reason=" + "&place=" + String.format("&place=%s" ,editText.getText());
                       button_positive.setEnabled(false);
                       button_negative.setEnabled(false);
                       async_start(layout3, progressBar,listView, Position);
                   }
                });

                button_No.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        data = "open=False" + String.format("&place=%s" ,editText.getText());
                        button_positive.setEnabled(true);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.VISIBLE);
                    }
                });

                button_retry.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        async_start(layout_failed, progressBar, listView, Position);
                    }
                });

                editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            // フォーカスが外れた場合キーボードを非表示にする
                            InputMethodManager inputMethodMgr = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });

                editText2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            // フォーカスが外れた場合キーボードを非表示にする
                            InputMethodManager inputMethodMgr = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });

            }
        });
        return d;
    }

    public void async_start(LinearLayout linearLayout, ProgressBar progressBar, ListView listView, String position){
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String[] loc = position.split(",");
        String data_dialog = String.format("location=%s,%s&combustibles=%s&incombustibles=%s&pet=%s&can=%s&uid=%s&%s", loc[0], loc[1],listView.isItemChecked(0),listView.isItemChecked(1),listView.isItemChecked(2),listView.isItemChecked(3), auth.getUid(), data);
        AsyncNetworkTask task = new AsyncNetworkTask(getActivity(), progressBar, layout_success, layout_failed);
        task.execute(data_dialog);
    }
}
