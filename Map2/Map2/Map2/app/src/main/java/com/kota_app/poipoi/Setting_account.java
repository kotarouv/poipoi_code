package com.kota_app.poipoi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Setting_account extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    String providerid = user.getProviders().get(0);
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        setTitle("アカウント設定");
        listView = (ListView) findViewById(R.id.listview_account_menu);
        String[] menuitem = null;
        String providerid_string = null;
        if(providerid.equals("password")) {
            menuitem = getResources().getStringArray(R.array.setting_account_email);
            providerid_string = "メールアドレスとパスワード";
        } else if (providerid.equals("google.com")){
            menuitem = getResources().getStringArray(R.array.setting_account_google);
            providerid_string = "Googleアカウント";
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, menuitem);
        listView.setAdapter(arrayAdapter);
        TextView textView_providerid = (TextView) findViewById(R.id.textView_account_providerId);
        textView_providerid.setText(String.format("認証方法：%s", providerid_string));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0){
                    startActivity(new Intent(getApplicationContext(), update_user_name.class));
                } else if(position == 1 && providerid.equals("password")){
                    startActivity(new Intent(getApplicationContext(), update_password.class));
                } else if(position == 1 && providerid.equals("google.com")){
                    startActivity(new Intent(getApplicationContext(), delete_user.class));
                } else if(position == 2 && providerid.equals("password")){
                    startActivity(new Intent(getApplicationContext(), delete_user.class));
                }
            }
        });
    }
}
