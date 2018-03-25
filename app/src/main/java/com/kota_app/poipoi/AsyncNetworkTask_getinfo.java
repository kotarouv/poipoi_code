package com.kota_app.poipoi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kota327v on 2017/08/02.
 */

public class AsyncNetworkTask_getinfo extends AsyncTask<String, Integer, String> {
    private GoogleMap mMap;
    private Marker markers[];
    private SQLiteDatabase db;
    private CardView notice_card;
    private TextView textView_place;
    private TextView textView_notice;
    private TextView textView_reason;
    private TextView textView_limit;
    private TextView textView_public;
    private TextView textView_name;
    private TextView textView_view;
    private TextView textView_date;
    private ImageView imageView_notice;
    private ImageView imageView_combustibles;
    private ImageView imageView_incombustibles;
    private ImageView imageView_pet;
    private ImageView imageView_can;
    private String url_resource;

    public AsyncNetworkTask_getinfo(Context context) {
        super();
        ScrollingActivity activity = (ScrollingActivity) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        DataHelper_marker helper = new DataHelper_marker(context);
        db = helper.getWritableDatabase();

        notice_card = (CardView) activity.findViewById(R.id.notice_card);
        textView_place = (TextView) activity.findViewById(R.id.textView_place);
        textView_notice = (TextView) activity.findViewById(R.id.textView_notice);
        textView_reason = (TextView) activity.findViewById(R.id.textView_reason);
        textView_limit = (TextView) activity.findViewById(R.id.textView_limit);
        textView_public = (TextView) activity.findViewById(R.id.textView_public);
        textView_name = (TextView)activity.findViewById(R.id.textView_update_name);
        textView_view = (TextView)activity.findViewById(R.id.textView_select_view);
        textView_date = (TextView)activity.findViewById(R.id.textView_select_date);
        imageView_notice = (ImageView) activity.findViewById(R.id.imageView_notice);
        imageView_combustibles = (ImageView) activity.findViewById(R.id.imageView_combustibles);
        imageView_incombustibles = (ImageView) activity.findViewById(R.id.imageView_incombustibles);
        imageView_pet = (ImageView) activity.findViewById(R.id.imageView_pet);
        imageView_can = (ImageView)activity.findViewById(R.id.imageView_can);
        imageView_notice.setImageResource(R.drawable.ic_hourglass_empty_black_24dp);
        textView_place.setText("");
        textView_notice.setText(R.string.loading);
        imageView_combustibles.setImageResource(R.drawable.ic_moeru_gray);
        imageView_incombustibles.setImageResource(R.drawable.ic_moenai_gray);
        imageView_pet.setImageResource(R.drawable.ic_pet_gray);
        imageView_can.setImageResource(R.drawable.ic_can_gray);
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/info_get.php", url_resource));
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
            try {
                JSONObject json = new JSONObject(builder.toString());
                JSONArray infos = json.getJSONArray("info");
                for (int i = 0; i < infos.length(); i++) {
                    JSONObject info = infos.getJSONObject(i);
                    if (i >= 1) {
                        list.append("=~~=");
                    }
                    list.append(info.getString("combustibles")+ ":~~:" + info.getString("incombustibles") + ":~~:" + info.getString("pet") + ":~~:" + info.getString("can") + ":~~:" + info.getString("place") + ":~~:" + info.getString("open_bit") + ":~~:" + info.getString("reason") + ":~~:" + info.getString("name") + ":~~:" + info.getString("view") + ":~~:" + info.getString("date"));
                }
            } catch (JSONException e){
                list.append("error");
                e.printStackTrace();
            }
        }  catch (java.net.SocketTimeoutException e) {
            list.append("error");
        }catch (IOException e) {
            list.append("error");
            e.printStackTrace();
        }
        return list.toString();
    }

    protected void onPostExecute(String result) {
        if(result.equals("error")){
            notice_card.setVisibility(View.GONE);
            textView_notice.setText(R.string.info_failed);
            imageView_notice.setImageResource(R.drawable.ic_error_outline_black_24dp_2);
            return;
        } else if (result != ""){
            notice_card.setVisibility(View.VISIBLE);
            String[] data = result.split("=~~=");
            for (int i = 0; i < data.length; i++) {
                String[] info = data[i].split(":~~:");
                if(info[0].equals("0")){
                    imageView_combustibles.setImageResource(R.drawable.ic_moeru_gray);
                } else  if(info[0].equals("1")){
                    imageView_combustibles.setImageResource(R.drawable.ic_moeru);
                }
                if(info[1].equals("0")){
                    imageView_incombustibles.setImageResource(R.drawable.ic_moenai_gray);
                } else if (info[1].equals("1")){
                    imageView_incombustibles.setImageResource(R.drawable.ic_moenai);
                }
                if(info[2].equals("0")){
                    imageView_pet.setImageResource(R.drawable.ic_pet_gray);
                } else if(info[2].equals("1")){
                    imageView_pet.setImageResource(R.drawable.ic_pet);
                }
                if(info[3].equals("0")){
                    imageView_can.setImageResource(R.drawable.ic_can_gray);
                } else if (info[3].equals("1")){
                    imageView_can.setImageResource(R.drawable.ic_can);
                }
                if(info[4]!=null){
                    textView_place.setText(info[4]);
                }
                if(info[5].equals("0")){
                    textView_notice.setText(R.string.nouse);
                    textView_limit.setVisibility(View.VISIBLE);
                    textView_public.setVisibility(View.GONE);
                    imageView_notice.setImageResource(R.drawable.ic_announcement_black_24dp);
                    if(info[6]!=null){
                        textView_reason.setText(info[6]);
                    }
                } else if(info[5].equals("1")){
                    textView_notice.setText(R.string.use);
                    textView_limit.setVisibility(View.GONE);
                    textView_public.setVisibility(View.VISIBLE);
                    imageView_notice.setImageResource(R.drawable.ic_chat);
                    textView_reason.setText("");
                }
                if(info[7]!=null && info[8]!=null && info[9]!=null){
                    textView_name.setText(info[7]);
                    textView_view.setText(info[8]);
                    textView_date.setText(info[9]);
                }
            }

        }
    }

}
