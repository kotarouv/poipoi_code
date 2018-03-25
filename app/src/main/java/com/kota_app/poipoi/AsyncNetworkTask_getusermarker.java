package com.kota_app.poipoi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kota327v on 2017/12/21.
 */

public class AsyncNetworkTask_getusermarker extends AsyncTask<String, Integer, String> {
    private String type;
    private RecyclerView recyclerView_lately;
    private RecyclerView recyclerView_popular;
    private CoordinatorLayout coordinatorLayout;
    private String url_resource;

    public AsyncNetworkTask_getusermarker(Context context, String string) {
        super();
        ScrollingActivity activity = (ScrollingActivity) context;
        type = string;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        recyclerView_lately = (RecyclerView) activity.findViewById(R.id.recyclerview_lately);
        recyclerView_popular = (RecyclerView) activity.findViewById(R.id.recyclerview_popular);
        recyclerView_lately.setHasFixedSize(true);
        recyclerView_popular.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_lately = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager_popular = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_lately.setLayoutManager(layoutManager_lately);
        recyclerView_popular.setLayoutManager(layoutManager_popular);
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/user_marker_get.php", url_resource));
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
                JSONArray datas = json.getJSONArray("data");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    if (i >= 1) {
                        list.append("=~~=");
//                    mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
                    }
                    list.append(data.getString("id")+ ":~~:" + data.getString("place") + ":~~:" + data.getString("view") + ":~~:" + data.getString("date"));
                }
            } catch (JSONException e){
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
        List<String> itemId = new ArrayList<String>();
        List<String> itemPlace = new ArrayList<String>();
        List<String> itemView = new ArrayList<String>();
        List<String> itemDate = new ArrayList<String>();
        if(result.equals("error")){
            Snackbar.make(coordinatorLayout, R.string.info_failed, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (result != ""){
            final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);

            try {
                String[] data = result.split("=~~=");
                for (int i = 0; i < data.length; i++) {
                    String[] info = data[i].split(":~~:");
                    itemId.add(info[0]);
                    itemPlace.add(info[1]);
                    itemView.add(info[2]);
                    itemDate.add(info[3]);
                }
            } finally {
            }
        }
        if (type.equals("lately")) {
            RecyclerView.Adapter mAdapter = new Adapter_smallcard(itemId, itemPlace, itemView, itemDate);
            recyclerView_lately.setAdapter(mAdapter);
        } else if (type.equals("popular")){
            RecyclerView.Adapter mAdapter = new Adapter_smallcard(itemId, itemPlace, itemView, itemDate);
            recyclerView_popular.setAdapter(mAdapter);}
    }
}
