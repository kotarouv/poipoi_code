package com.kota_app.poipoi;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class UserMenu extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        final TextView textView = (TextView) findViewById(R.id.textView20);
        final TextView textView1 = (TextView) findViewById(R.id.textView21);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                textView.setText(String.valueOf(appBarLayout.getTotalScrollRange() + verticalOffset));
//                textView1.setText(String.valueOf(appBarLayout.getTotalScrollRange()));
                if (verticalOffset == 0){
                } else if (appBarLayout.getTotalScrollRange() + verticalOffset <= 20){
                }
            }
        });
    }
}
