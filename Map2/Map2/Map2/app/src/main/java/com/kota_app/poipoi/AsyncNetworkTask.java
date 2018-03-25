package com.kota_app.poipoi;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncNetworkTask extends AsyncTask<String, Integer, String> {
    private ProgressBar bar;
    private ScrollingActivity activity;
    private LinearLayout layout_success;
    private LinearLayout layout_failed;
    private String url_resource;

    public AsyncNetworkTask(Context context, ProgressBar progressBar, LinearLayout success, LinearLayout failed) {
        super();
        activity = (ScrollingActivity) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        bar = progressBar;
        layout_success = success;
        layout_failed = failed;
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(String.format("http://%s/marker_add.php", url_resource));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
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
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    protected void onPostExecute(String result) {
        bar.setVisibility(View.GONE);
        if(result.equals("Success")){
            layout_success.setVisibility(View.VISIBLE);
        } else {
            layout_failed.setVisibility(View.VISIBLE);
        }
    }

}
