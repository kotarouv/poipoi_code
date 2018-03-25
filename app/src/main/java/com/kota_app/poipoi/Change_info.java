package com.kota_app.poipoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by kota327 on 1/28/2018.
 */

public class Change_info extends DialogFragment {
    private static final int RC_SIGN_IN = 123;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_dialoglayout_changeinfo, null);

        final LinearLayout layout_main = (LinearLayout) layout.findViewById(R.id.changeinfo_main);
        final LinearLayout layout_type = (LinearLayout) layout.findViewById(R.id.changeinfo_type);
        final LinearLayout layout_place = (LinearLayout) layout.findViewById(R.id.changeinfo_place);
        final LinearLayout layout_canuse = (LinearLayout) layout.findViewById(R.id.changeinfo_canuse);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ListView listView = (ListView) layout.findViewById(R.id.listview_changeinfo);
        ArrayAdapter<String> adapter;
        String[] kinds = getResources().getStringArray(R.array.change_info);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, kinds); //noinspection GroovyAssignabilityCheck
        listView.setAdapter(adapter);
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
                button_positive.setEnabled(false);
                button_negative.setEnabled(false);
                getActivity().setTitle("アカウント設定");

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if(position == 0){
                            layout_main.setVisibility(View.GONE);
                            layout_type.setVisibility(View.VISIBLE);
                            button_positive.setEnabled(true);
                            button_negative.setEnabled(true);
                        } else if(position == 1){
                            layout_main.setVisibility(View.GONE);
                            layout_place.setVisibility(View.VISIBLE);
                            button_positive.setEnabled(true);
                            button_negative.setEnabled(true);
                        } else if(position == 2){
                            layout_main.setVisibility(View.GONE);
                            layout_canuse.setVisibility(View.VISIBLE);
                            button_positive.setEnabled(true);
                            button_negative.setEnabled(true);
                        } else if(position == 3){
                            layout_main.setVisibility(View.GONE);
                            layout_type.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        return d;
    }

    public void asyncstart(){
    }
}
