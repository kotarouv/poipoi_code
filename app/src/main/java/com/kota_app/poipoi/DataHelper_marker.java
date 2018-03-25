package com.kota_app.poipoi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by kota327v on 2017/08/01.
 */

public class DataHelper_marker extends SQLiteOpenHelper {
    static final private String DBNAME = "marker_info.sqlite";
    static final private int VERSION = 1;

    //コントラスターb
    public DataHelper_marker(Context context){
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    //データベース作成時にテーブルとテストデータを作成
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE marker_info (" +
                "id INTEGER PRIMARY KEY, marker_id TEXT)");
    }

    //データベースをバージョンアップした時、テーブルを再生成
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v){
        db.execSQL("DROP TABLE IF EXISTS marker_info");
        onCreate(db);
    }
}