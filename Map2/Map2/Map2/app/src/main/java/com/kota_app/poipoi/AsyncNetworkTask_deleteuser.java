package com.kota_app.poipoi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kota327 on 12/29/2017.
 */

public class AsyncNetworkTask_deleteuser extends AsyncTask<String, Integer, String> {
    private ProgressBar bar;
    private Button button;
    private delete_user activity;
    private String url_resource;

    public AsyncNetworkTask_deleteuser(Context context) {
        super();
        activity = (delete_user) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        bar = (ProgressBar) activity.findViewById(R.id.progressBar_delete_user);
        button = (Button) activity.findViewById(R.id.button_delete_user);
        button.setEnabled(false);
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(String.format("http://%s/user_delete_all.php", url_resource));
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
        if(result.equals("complete")){
//            auth.signOut();
            Intent intent = new Intent(activity, ScrollingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
            Toast.makeText(activity,"アカウントを削除しました",Toast.LENGTH_LONG).show();
        } else {
            button.setEnabled(true);
            bar.setVisibility(View.GONE);
            Toast.makeText(activity,"エラーが発生しました。しばらくたってから再試行してください。",Toast.LENGTH_LONG).show();
        }
    }

}
