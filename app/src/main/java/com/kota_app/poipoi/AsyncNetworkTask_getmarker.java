package com.kota_app.poipoi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
 * Created by kota327v on 2017/06/03.
 */

public class AsyncNetworkTask_getmarker extends AsyncTask<String, Integer, String> {
    private GoogleMap mMap;
    private Marker markers[];
    private SQLiteDatabase db;
    private Marker workm;
    private  String workm_id[];
    private Integer get_counts[];
    private  CoordinatorLayout coordinatorLayout;
    private String url_resource;

    public AsyncNetworkTask_getmarker(Context context, GoogleMap googleMap, Marker marker[], Marker work, String work_id[], Integer count[], CoordinatorLayout get_coordinatorLayout) {
        super();
        ScrollingActivity activity = (ScrollingActivity) context;
        url_resource = activity.getResources().getString(R.string.ipaddress);
        mMap = googleMap;
        markers = marker;
        DataHelper_marker helper = new DataHelper_marker(context);
        db = helper.getWritableDatabase();
        workm = work;
        workm_id = work_id;
        get_counts = count;
        coordinatorLayout = get_coordinatorLayout;
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        StringBuilder list = new StringBuilder();

        try {
            URL url = new URL(String.format("http://%s/marker_get.php", url_resource));
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
                JSONArray markers = json.getJSONArray("markers");
                for (int i = 0; i < markers.length(); i++) {
                    JSONObject marker = markers.getJSONObject(i);
                    if (i >= 1) {
                        list.append("=~~=");
//                    mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
                    }
                    list.append(marker.getString("id")+ ":~~:" + marker.getString("latitude") + ",~~," + marker.getString("longitude"));
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
        if(result.equals("error")){
            Snackbar.make(coordinatorLayout, R.string.info_failed, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (result != ""){
            final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);

            if (workm != null) {
                for(int i = 0; i <= get_counts[0]; i++){
                    markers[i].remove();
                }
            } else {
                mMap.clear();
            }

            try {
                db.delete("marker_info",null,null);
                ContentValues cv = new ContentValues();
                String[] data = result.split("=~~=");
                for (int i = 0; i < data.length; i++) {
                    String[] info = data[i].split(":~~:");
                    String[] location = info[1].split(",~~,");
                    double latitude = Double.parseDouble(location[0]);
                    double longitude = Double.parseDouble(location[1]);
                    LatLng marker_loc = new LatLng(latitude, longitude);
                    markers[i] = mMap.addMarker(new MarkerOptions().position(marker_loc));
                    cv.put("id", info[0]);
                    cv.put("marker_id", markers[i].getId());
                    db.insert("marker_info", null, cv);
                    String id = markers[i].getId();
                    String aaa = id + "";
                    get_counts[0] = i;
                }
            } finally {
                    db.close();
            }
        }
    }
}
