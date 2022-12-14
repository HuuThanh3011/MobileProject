package edu.hanu.a2_1901040203.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.a2_1901040203.models.Product;

public class CartDatabase extends SQLiteOpenHelper {

    public static final String CART_TABLE = "CART_TABLE";
    public static final String COLUMN_PRODUCT_ID = "ID";
    public static final String COLUMN_PRODUCT_NAME = "NAME";
    public static final String COLUMN_THUMBNAIL = "THUMBNAIL";
    public static final String COLUMN_PRODUCT_PRICE = "UNITPRICE";
    public static final String COLUMN_QUANTITY = "QUANTITY";

    public CartDatabase(@Nullable Context context) {
        super(context, "cart.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + CART_TABLE + " (" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY , " + COLUMN_PRODUCT_NAME + " TEXT, " + COLUMN_THUMBNAIL + " TEXT, " + COLUMN_PRODUCT_PRICE + " INT, " + COLUMN_QUANTITY + " INT)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean addToCart(Product product){
        SQLiteDatabase dbs = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_ID, product.getProductID());
        contentValues.put(COLUMN_PRODUCT_NAME, product.getName());
        contentValues.put(COLUMN_THUMBNAIL, product.getThumbnail());
        contentValues.put(COLUMN_PRODUCT_PRICE, product.getUnitPrice());
        contentValues.put(COLUMN_QUANTITY, product.getQuantity());
        dbs.insert(CART_TABLE, null, contentValues);
        dbs.close();
        return true;
    }
    public void updateDatabase(Product product){
        SQLiteDatabase dbs = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, product.getName());
        contentValues.put(COLUMN_THUMBNAIL, product.getThumbnail());
        contentValues.put(COLUMN_PRODUCT_PRICE, product.getUnitPrice());
        contentValues.put(COLUMN_QUANTITY, product.getQuantity());
        dbs.update(CART_TABLE, contentValues,COLUMN_PRODUCT_ID +"= ?", new String[]{String.valueOf(product.getProductID())} );
    }
    public List<Product> getALlProductCart(){
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM " + CART_TABLE;
        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor cursor = dbs.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                int productID = cursor.getInt(0);
                String productName = cursor.getString(1);
                String thumbnail = cursor.getString(2);
                int unitprice = cursor.getInt(3);
                int quantity =cursor.getInt(4);
                Product product = new Product(productID, productName, thumbnail, unitprice, quantity);
                list.add(product);

            }while (cursor.moveToNext());
        }
        cursor.close();
        dbs.close();
        return list;
    }
    public  void deleteProduct(Product product){
        SQLiteDatabase dbs = this.getWritableDatabase();
        dbs.delete(CART_TABLE, COLUMN_PRODUCT_ID+"= ?", new String[]{String.valueOf(product.getProductID())});
    }
}
