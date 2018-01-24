package com.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.assignment.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 24/1/18.
 */

public class DBHelper {
    private SQLiteDatabase db;
    private final Context context;
    private final TablesClass dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    public static DBHelper getInstance(Context context) {
        try {
            if (db_helper == null) {
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        } catch (IllegalStateException e) {
            //db_helper already open
        }
        return db_helper;
    }

    /*
     * set context of the class and initialize TableClass Object
	 */

    public DBHelper(Context c) {
        context = c;
        dbHelper = new TablesClass(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /*
     * close databse.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean dbOpenCheck() {
        try {
            return db.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public long insertContentVals(String tableName, ContentValues content) {
        long id = 0;
        try {
            db.beginTransaction();
            id = db.insert(tableName, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public Cursor getTableRecords(String tablename, String[] columns, String where, String orderby) {
        Cursor cursor = db.query(false, tablename, columns, where, null, null, null, orderby, null);
        return cursor;
    }

    /*
	 * Get count of all tables in a table as per the condition
	 */

    public int getFullCount(String table, String where) {
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }


    public void deleteRecords(String table, String whereClause, String[] whereArgs) {
        try {
            db.beginTransaction();
            db.delete(table, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /*
	 * Get value of any table as per the condition.
	 */

    public String getValue(String table, String column, String where) {
        Cursor result = db.query(false, table, new String[]{column}, where,
                null, null, null, Constants.ID, null);
        String value = "";
        try {
            if (result.moveToFirst()) {
                value = result.getString(0);
            } else {
                return null;
            }
        } finally {
            result.close();
        }
        return value;
    }
	/*
	 * Get Multiple Values from column of any specified table.
	 */

    public String[] getValues(boolean b, String table, String column,
                              String where, String orderBy) {
        ArrayList <String> savedAns = new ArrayList <String>();
        Cursor result = null;
        String[] y;
        try {
            result = db.query(b, table, new String[]{column}, where, null,
                    null, null, orderBy, null);
            if (result.moveToFirst()) {
                do {
                    savedAns.add(result.getString(result.getColumnIndex(column)));
                } while (result.moveToNext());
            } else {
                return null;
            }
            y = savedAns.toArray(new String[result.getCount()]);
        } finally {
            result.close();
        }
        return y;
    }

    public int updateRecords(String table, ContentValues values,
                             String whereClause, String[] whereArgs) {
        int a = 0;
        try {
            db.beginTransaction();
            a = db.update(table, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return a;
    }

    // Read records related to the search term
    public List <String> getProduct(String searchTerm) {

        List <String> recordsList = new ArrayList <String>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + Constants.PRODUCT_RECORD;
        sql += " WHERE " + Constants.PRODUCT_NAME + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + null + " DESC";
        sql += " LIMIT 0,5";

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(Constants.PRODUCT_NAME));
                // add to list
                recordsList.add(objectName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    // Read records related to the search term
    public List <String> getAllProduct() {

        List <String> recordsList = new ArrayList <String>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + Constants.PRODUCT_RECORD;

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(Constants.PRODUCT_NAME));
                // add to list
                recordsList.add(objectName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }


}
