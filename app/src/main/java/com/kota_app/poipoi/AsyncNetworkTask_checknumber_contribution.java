package com.kota_app.poipoi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * Created by kota327 on 12/25/2017.
 */

public class AsyncNetworkTask_checknumber_contribution extends AsyncTask<String, Integer, String> {
    private String url_resource;
    private ScrollingActivity activity;
    private TextView textView_bottom;
    private CardView cardView;
    private LinearLayout linearLayout;


    public AsyncNetworkTask_checknumber_contribution(Context context) {
        super();
        activity = (ScrollingActivity) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        textView_bottom = (TextView) activity.findViewById(R.id.textView_bottom_contribution);
        cardView = (CardView) activity.findViewById(R.id.cardview_let);
        linearLayout = (LinearLayout) activity.findViewById(R.id.linearlayout_user_info);
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/check_number_usercontribution.php", url_resource));
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
        if (result.equals("0")){
            cardView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
        textView_bottom.setText(String.format("投稿数：%s", result));
    }
}
