package com.example.saurav.maths;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by saurav on 29-Jul-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "student_data";
    public static final String COL_1 = "Id";
    public static final String COL_2 = "Title";
    public static final String COL_3 = "Data";
    public static final String COL_4 = "Tag";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Data TEXT,Tag INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public int insertData(String title,String data,String tag){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,data);
        contentValues.put(COL_4,tag);
        long result = sqLiteDatabase.insert(TABLE_NAME, null,contentValues);
        if(result == -1){
            return 0;
        }
        else{
            return 1;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

    public boolean updateData(String id,String title,String data,String tag){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,data);
        contentValues.put(COL_4,tag);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"Id = ?",new String[] { id });
        return true ;
    }

    public void deleteData(String tag,String title,String data){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //return sqLiteDatabase.delete(TABLE_NAME,"Id = ?",new String[] {id});
        sqLiteDatabase.execSQL("delete from "+TABLE_NAME+" where tag = "+tag+" , title = "+title+" , data = "+data);
    }

    public Cursor getDataByTag(String tag){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Tag =  ?",new String[] {tag});
        return cursor;
    }

    public void deleteAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+TABLE_NAME);
    }
}

