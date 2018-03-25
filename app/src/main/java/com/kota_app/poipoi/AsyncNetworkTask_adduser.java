package com.kota_app.poipoi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kota327v on 2017/11/10.
 */

public class AsyncNetworkTask_adduser extends AsyncTask<String, Integer, String> {
    private String url_resource;
    private EnterUserName activity;

    public AsyncNetworkTask_adduser(Context context) {
        super();
        activity = (EnterUserName) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/user_add.php", url_resource));
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
        if(result.equals("success")){
            activity.startActivity(new Intent(activity, ScrollingActivity.class));
            activity.finish();
        }
    }
}
