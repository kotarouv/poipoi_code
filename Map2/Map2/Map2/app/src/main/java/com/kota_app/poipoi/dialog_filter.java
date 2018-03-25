package com.kota_app.poipoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by kota327v on 2017/08/07.
 */

public class dialog_filter extends DialogFragment{

    private boolean all;
    private boolean combustibles;
    private boolean incombustibles;
    private boolean pet;
    private boolean can;
    private ListView listView;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.dialoglayout_filter, null);
        listView = (ListView) layout.findViewById(R.id.listview_filter);
        all = getArguments().getBoolean("all");
        final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup_filter);
        combustibles = getArguments().getBoolean("combustibles");
        incombustibles = getArguments().getBoolean("incombustibles");
        pet = getArguments().getBoolean("pet");
        can = getArguments().getBoolean("can");
        ArrayAdapter<String> adapter;
        String[] kinds = getResources().getStringArray(R.array.gomi_kind);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, kinds); //noinspection GroovyAssignabilityCheck
        listView.setAdapter(adapter);

        listView.invalidateViews();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("")
                .setView(layout);
        final AlertDialog d = builder.create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                listView.setItemChecked(0,combustibles);
                listView.setItemChecked(1,incombustibles);
                listView.setItemChecked(2,pet);
                listView.setItemChecked(3,can);
                if(all == true){
                    radioGroup.check(R.id.rbAll);
                } else {
                    radioGroup.check(R.id.rbSelect);
                    listView.setVisibility(View.VISIBLE);
                }
                combustibles = false;

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                        if (radioGroup == group){
                            final RadioButton radioButton = (RadioButton) layout.findViewById(i);
                            switch (radioButton.getId()){
                                case R.id.rbAll:
                                    all = true;
                                    listView.setVisibility(View.GONE);
                                    send_result();
                                    break;
                                case R.id.rbSelect:
                                    all = false;
                                    listView.setVisibility(View.VISIBLE);
                                    send_result();
                                    break;
                            }
                        }
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        send_result();
                    }
                });

            }
        });
        return d;
    }
    public void send_result(){
        ScrollingActivity callingActivity = (ScrollingActivity) getActivity();
        callingActivity.onReturnValue(all, listView.isItemChecked(0), listView.isItemChecked(1),listView.isItemChecked(2), listView.isItemChecked(3));
    }
}
