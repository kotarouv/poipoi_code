package com.kota_app.poipoi;

/**
 * Created by kota327 on 12/28/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class AsyncNetworkTask_change_name extends AsyncTask<String, Integer, String> {
    private String url_resource;
    private update_user_name activity;

    public AsyncNetworkTask_change_name(Context context) {
        super();
        activity = (update_user_name) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/user_update_name.php", url_resource));
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
            Toast.makeText(activity,"ユーザー名を変更しました",Toast.LENGTH_LONG).show();
            activity.startActivity(new Intent(activity, Setting_account.class));
            activity.finish();
        }
    }
}

