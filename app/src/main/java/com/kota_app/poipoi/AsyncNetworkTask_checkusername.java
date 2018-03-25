package com.kota_app.poipoi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class AsyncNetworkTask_checkusername extends AsyncTask<String, Integer, String> {
    private String url_resource;
    private TextInputLayout textInputLayout;
    private ProgressBar progressBar;
    private Button button;
    private ImageView imageView;
    private EnterUserName activity = null;
    private update_user_name activity_update;

    public AsyncNetworkTask_checkusername(Context context, String mes) {
        super();
        if(mes.equals("enter")) {
            activity = (EnterUserName) context;
            url_resource = activity.getResources().getString(R.string.ipaddress);
            textInputLayout = (TextInputLayout) activity.findViewById(R.id.textInputLayout);
            progressBar = (ProgressBar) activity.findViewById(R.id.progressBar5);
            button = (Button) activity.findViewById(R.id.button8);
            imageView = (ImageView) activity.findViewById(R.id.imageView_enter);
        } else if (mes.equals("update")){
            activity_update = (update_user_name) context;
            url_resource = activity_update.getResources().getString(R.string.ipaddress);
            textInputLayout = (TextInputLayout) activity_update.findViewById(R.id.textInputLayout_update_name);
            progressBar = (ProgressBar) activity_update.findViewById(R.id.progressBar_update_name);
            button = (Button) activity_update.findViewById(R.id.button_update_name);
            imageView = (ImageView) activity_update.findViewById(R.id.imageView_update_name);
        }
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/check_username.php", url_resource));
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
        progressBar.setVisibility(View.INVISIBLE);
        if(result.equals("none")){
            if(activity != null) {
                activity.setTitle("ユーザー名を設定");
            }
            textInputLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            button.setEnabled(true);
            imageView.setVisibility(View.VISIBLE);
        } else {
            textInputLayout.setError("このユーザー名は既に使われています");
        }
    }
}
