package com.example.spider_recognition;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    public static final int HISTORY_DIR = 0;

    public static final int HISTORY_ITEM = 1;

    public static final String AUTHORITY = "com.example.databasetest.provider";

    private static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "history", HISTORY_DIR);
        uriMatcher.addURI(AUTHORITY, "history/#", HISTORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "SpiderHistory.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Query data from provider
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case HISTORY_DIR:
                cursor = db.query("History", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HISTORY_ITEM:
                String historyId = uri.getPathSegments().get(1);
                cursor = db.query("History", projection, "id = ?", new String[] { historyId }, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert data from provider
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case HISTORY_DIR:
            case HISTORY_ITEM:
                long newHistoryId = db.insert("History", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/history/" + newHistoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Update data from provider
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case HISTORY_DIR:
                updatedRows = db.update("History", values, selection, selectionArgs);
                break;
            case HISTORY_ITEM:
                String historyId = uri.getPathSegments().get(1);
                updatedRows = db.update("History", values, "id = ?", new String[] { historyId });
                break;
            default:
                break;
        }
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Delete data from provider
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case HISTORY_DIR:
                deletedRows = db.delete("History", selection, selectionArgs);
                break;
            case HISTORY_ITEM:
                String historyId = uri.getPathSegments().get(1);
                deletedRows = db.delete("History", "id = ?", new String[] { historyId });
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case HISTORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest. provider.history";
            case HISTORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest. provider.history";
        }
        return null;
    }

}