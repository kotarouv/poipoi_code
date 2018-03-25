package com.kota_app.poipoi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollingActivity2 extends AppCompatActivity {

    private static final String[] id = {
            "123",
            "256",
            "98"
    };

    private static final String[] places = {
            "公園前",
            "ファミマ下井草店",
            "下井草駅2番ホーム"
    };

    private static final String[] view = {
        "123",
        "256",
        "98"
    };

    private static final String[] date = {
            "2017/06/08",
            "2017/09/12",
            "2017/12/18"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling2);
    }
}
