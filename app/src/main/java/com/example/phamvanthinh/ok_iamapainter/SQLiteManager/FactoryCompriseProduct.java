package com.example.phamvanthinh.ok_iamapainter.SQLiteManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.phamvanthinh.ok_iamapainter.GAS.ChillOfAProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 05/10/2016.
 */

public class FactoryCompriseProduct extends SQLiteOpenHelper {

    public Context context;

    private static final String DB_Name        = "DB_ChuaAnhProduct";
    public static final String DB_ProductName =  "DB_ProductName";
    public static final String DB_ProductOff  =  "DB_ProdcutOFF";

    public FactoryCompriseProduct(Context context) {
        super(context, "FolderCompriseProduct", null, 1);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table if not exists " + DB_Name + " ( "
                + DB_ProductName + " text primary key, "
                + DB_ProductOff  + " BLOB" + ")";
        db.execSQL(sql);
    }

    public int ImportProduct(ChillOfAProduct aProduct){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_ProductName, aProduct.getProductName());
        values.put(DB_ProductOff,  aProduct.getProductFF());

        return (int) db.insert(DB_Name, null, values);
    }

    public List<ChillOfAProduct> showProduct(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + DB_Name;
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()){
            List<ChillOfAProduct> showProduct = new ArrayList<ChillOfAProduct>();
            do{
                ChillOfAProduct aProduct = new ChillOfAProduct();

                aProduct.setProductName(c.getString(0));
                aProduct.setProductFF(c.getBlob(1));

                showProduct.add(aProduct);

            }while (c.moveToNext());

            return showProduct;
        }else {
            return null;
        }
    }

    public boolean EditProduct(String ProdcutName, byte[] newProdcut){
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(DB_ProductName, ProdcutName);
        values.put(DB_ProductOff,  newProdcut);

        return db.update(DB_Name, values, DB_ProductName + " = ?", new String[]{ProdcutName}) != -1;

    }

    public void DeleteProduct(String ProductName){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DB_Name, DB_ProductName + " = ?", new String[]{ProductName});
    }

    public void DeleteAllProduct(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from " + DB_Name;
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
