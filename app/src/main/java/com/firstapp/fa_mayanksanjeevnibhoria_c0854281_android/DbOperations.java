package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DbOperations extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "record";
    public static final String TABLE_NAME = "addresses";
    public DbOperations(Context context) {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ TABLE_NAME +"(id integer primary key autoincrement, pname text, paddr text, plat text, plng text, pv text, pdate text)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insert(BeanPlaces b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pname", b.getPname());
        contentValues.put("paddr", b.getPaddr());
        contentValues.put("plat", b.getPlat());
        contentValues.put("plng", b.getPlng());
        contentValues.put("pv", b.getPv());
        contentValues.put("pdate", b.getPdate());
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public BeanPlaces getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "pname", "paddr",
                        "plat", "plng", "pv", "pdate" }, "id" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        BeanPlaces b = new BeanPlaces();

        b.setId(cursor.getInt(0));
        b.setPname(cursor.getString(1));
        b.setPaddr(cursor.getString(2));
        b.setPlat(cursor.getString(3));
        b.setPlng(cursor.getString(4));
        b.setPv(cursor.getString(5));
        b.setPdate(cursor.getString(6));
        return b;
    }


    public List<BeanPlaces> getAllPlaces() {
        List<BeanPlaces> li = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BeanPlaces b = new BeanPlaces();

                b.setId(cursor.getInt(0));
                b.setPname(cursor.getString(1));
                b.setPaddr(cursor.getString(2));
                b.setPlat(cursor.getString(3));
                b.setPlng(cursor.getString(4));
                b.setPv(cursor.getString(5));
                b.setPdate(cursor.getString(6));

                li.add(b);

            } while (cursor.moveToNext());
        }
        else
        {
            Log.e("cursor","null");
        }

        return li;
    }

    public int update(BeanPlaces b) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("pname", b.getPname());
        contentValues.put("paddr", b.getPaddr());
        contentValues.put("plat", b.getPlat());
        contentValues.put("plng", b.getPlng());
        contentValues.put("pv", b.getPv());
        contentValues.put("pdate", b.getPdate());

        return db.update(TABLE_NAME, contentValues, "id" + " = ?",
                new String[] { String.valueOf(b.getId()) });
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id" + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}