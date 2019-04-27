package com.xon.mymidsemproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String email,String name, String phone,String password,byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO USER VALUES (?, ?, ?, ?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, email);
        statement.bindString(2, name);
        statement.bindString(3, phone);
        statement.bindString(4, password);
        statement.bindBlob(5, image);

        statement.executeInsert();
    }

   public void updateData(String OldEmail,String email,String name, String phone,String password,byte[] image) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE USER SET email =?, name = ?, phone = ?, password =?, image = ? WHERE  email = '"+OldEmail+"'";
        SQLiteStatement statement = database.compileStatement(sql);

       statement.bindString(1, email);
       statement.bindString(2, name);
       statement.bindString(3, phone);
       statement.bindString(4, password);
       statement.bindBlob(5, image);

        statement.execute();
        database.close();
    }

    public  void deleteData(String email) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM USER WHERE email = '"+email+"'";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.execute();
        database.close();
    }

    public boolean loginCheck(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM USER WHERE EMAIL = ? AND PASSWORD = ?",new String[]{email,password});
        if(cs.getCount()>0){return true;}
        else{
            return false;
        }
    }

    public boolean emailCheck(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM USER WHERE EMAIL = ?",new String[]{email});
        if(cs.getCount()>0){return true;}
        //to avoid Data Duplication
        else{
            return false;
        }
    }

    public Cursor getData(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE EMAIL = ?",new String[]{email});
        return cursor;
    }




    public Bitmap getImage(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap bt=null;
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE email = ?",new String[]{email});
        if(cursor.moveToFirst()) {
            byte[] img = cursor.getBlob(4);

            bt = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        return bt;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
