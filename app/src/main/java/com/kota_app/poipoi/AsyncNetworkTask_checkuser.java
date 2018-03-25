package com.kota_app.poipoi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kota327v on 2017/06/03.
 */

public class AsyncNetworkTask_checkuser extends AsyncTask<String, Integer, String> {
    private String url_resource;
    private TextInputLayout textInputLayout;
    private ProgressBar progressBar;
    private Button button;
    private EnterUserName activity_enter;
    private ScrollingActivity activity_scrolling;
    private update_user_name activity_update_user_name;
    private TextView textView;
    private TextView textView_bottom;
    private TextView textView_update;
    private Button button_positive;
    private String activity_name;
    private ConstraintLayout layout;

    public AsyncNetworkTask_checkuser(Context context, String mes, TextView textView_get, Button button_get) {
        super();
        if(mes.equals("enter")) {
            activity_enter = (EnterUserName) context;
            textInputLayout = (TextInputLayout) activity_enter.findViewById(R.id.textInputLayout);
            progressBar = (ProgressBar)activity_enter.findViewById(R.id.progressBar2);
            button = (Button)activity_enter.findViewById(R.id.button8);
            url_resource = activity_enter.getResources().getString(R.string.ipaddress);
        }else if(mes.equals("userinfo")){
            activity_scrolling = (ScrollingActivity) context;
            layout = (ConstraintLayout) LayoutInflater.from(activity_scrolling)
                    .inflate(R.layout.activity_dialoglayout_userinfo, null);
            textView = textView_get;
            button_positive = button_get;
            url_resource = activity_scrolling.getResources().getString(R.string.ipaddress);
        } else if (mes.equals("bottom")){
            activity_scrolling = (ScrollingActivity) context;
            textView_bottom = (TextView)activity_scrolling.findViewById(R.id.textView_bottom_name);
            url_resource = activity_scrolling.getResources().getString(R.string.ipaddress);
        } else if (mes.equals("update")){
             activity_update_user_name = (update_user_name) context;
             textView_update = (TextView) activity_update_user_name.findViewById(R.id.textView_update_name);
            url_resource = activity_update_user_name.getResources().getString(R.string.ipaddress);
        }
        activity_name = mes;
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/check_user.php", url_resource));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content^Type", "text/plain; charset=utf-8");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.print(params[0]);
            ps.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        }  catch (java.net.SocketTimeoutException e) {
            list.append("error");
        }catch (IOException e) {
            list.append("error");
            e.printStackTrace();
        }
        return builder.toString();
    }

    protected void onPostExecute(String result) {
        if(activity_name.equals("enter")) {
            progressBar.setVisibility(View.INVISIBLE);
            if (result.equals("none")) {
                activity_enter.setTitle("ユーザー名を設定");
                textInputLayout.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                activity_enter.asyncstart_checkusername();
            } else {
                activity_enter.startActivity(new Intent(activity_enter, ScrollingActivity.class));
                activity_enter.finish();
            }
        } else if(activity_name.equals("userinfo")){
            if (result.equals("none")){
                textView.setText("サインインしていません");
                button_positive.setText("ログイン");
            } else {
                textView.setText(result);
                button_positive.setText("ログアウト");
            }
        } else if (activity_name.equals("bottom")){
            textView_bottom.setText(result);
        } else if (activity_name.equals("update")){
            textView_update.setText(String.format("現在のユーザー名：%s",result));
        }
    }
}
