package com.github.ecorpbro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.ecorpbro.database.SettingsDbSchema.DefaultUrlTable;
import com.github.ecorpbro.database.SettingsDbSchema.SourceUrlTable;

public class SettingsBaseManager {
    public static final String TAG = "SettingsBaseManager";

    private static SettingsBaseManager sSettingsBaseManager;
    public static final String NOSORUCE = "Пустое имя источника";

    private SQLiteDatabase mDatabase;

    private Context mContext;

    private SettingsBaseManager(Context context) {
        mContext = context;
    }

    public static SettingsBaseManager get(Context context) {
        if (sSettingsBaseManager == null) {
            sSettingsBaseManager = new SettingsBaseManager(context);
        }
        return sSettingsBaseManager;
    }

    public SettingsCursorWrapper getCursorWrapper(String table_name, String[] columns, String whereClause, String[] whereArgs) {
        mDatabase = new SettingsBaseHelper(mContext).getWritableDatabase();
        Cursor cursor = mDatabase.query(
                table_name,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        SettingsCursorWrapper cursorWrapper = new SettingsCursorWrapper(cursor);
        return cursorWrapper;
    }

    public String getDefUrl() {
        SettingsCursorWrapper cursor = getCursorWrapper(DefaultUrlTable.DB_TABLE_NAME, null, null,null);
        String result = NOSORUCE;
        if (cursor.getCount() == 0) {
            return result;
        }
        try {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
        } finally {
            cursor.close();
        }
        return result;
    }

    public String getDefName() {
        String defUrl = getDefUrl();
        SettingsCursorWrapper cursor = getCursorWrapper(SourceUrlTable.DB_TABLE_NAME,
                null,
                SourceUrlTable.Cols.URL + " = ? ",
                new String[]{defUrl});
        if (cursor.getCount() == 0) {
            return defUrl;
        }
        cursor.moveToLast();
        String result = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.NAME));
        cursor.close();
        return result;
    }

    private static ContentValues getContentValues(String name, String url) {
        ContentValues values = new ContentValues();
        if (name != null) {
            values.put(SourceUrlTable.Cols.NAME, name);
        }
        if (url != null) {
            values.put(SourceUrlTable.Cols.URL, url);
        }
        return values;
    }

    public void addSourceUrl(String name, String url) {
        if (mDatabase == null) {
            mDatabase = new SettingsBaseHelper(mContext).getWritableDatabase();
        }
            ContentValues values = getContentValues(name, url);
        if (values != null) {
            mDatabase.insert(SourceUrlTable.DB_TABLE_NAME, null, values);
        }
    }

    public void addDefUrl(String url) {
        if (mDatabase == null) {
            mDatabase = new SettingsBaseHelper(mContext).getWritableDatabase();
        }
        ContentValues values = getContentValues(null,url);
        if (values != null) {
            mDatabase.delete(DefaultUrlTable.DB_TABLE_NAME, null, null);
            mDatabase.insert(DefaultUrlTable.DB_TABLE_NAME, null, values);
        }
    }

    public void deleteSourceUrl() {
        if (mDatabase == null) {
            mDatabase = new SettingsBaseHelper(mContext).getWritableDatabase();
        }
        String url = getDefUrl();
        mDatabase.delete(SourceUrlTable.DB_TABLE_NAME, SourceUrlTable.Cols.URL + " = ?", new String[]{url});
    }

    public void updateSource(String name, String url) {

        ContentValues values = getContentValues(name,url);
        if (mDatabase == null) {
            mDatabase = new SettingsBaseHelper(mContext).getWritableDatabase();
        }
        mDatabase.update(SourceUrlTable.DB_TABLE_NAME, values,SourceUrlTable.Cols.URL + " = ?", new String[]{url});
    }

    public boolean check(String name, String url) {
        if (url.equals(null)) {
            return false;
        }
        SettingsCursorWrapper cursor = getCursorWrapper(SourceUrlTable.DB_TABLE_NAME, null, null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String urlBd = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
                String nameBd = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.NAME));
                if (url.equals(urlBd) || name.equals(nameBd)) {
                    return false;
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return true;
    }

    public int getPositionUrl(String url) {
        int result = 0;
        SettingsCursorWrapper cursor = getCursorWrapper(SourceUrlTable.DB_TABLE_NAME, null, null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String urlDB = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
                if (url.equals(urlDB) ) {
                    return result;
                }
                result += 1;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return result;
    }

    public String setFirst() {
        SettingsCursorWrapper cursor = getCursorWrapper(SourceUrlTable.DB_TABLE_NAME, null, null,null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
    }

    //Reading BD in to Log
    public void readDbSourceToLog(String where) {
        SettingsCursorWrapper cursor = getCursorWrapper(SourceUrlTable.DB_TABLE_NAME, null, null,null);
        Log.d(TAG, "Where - " + where);
        Log.d(TAG, "Table Source");
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.NAME));
                String url = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
                Log.d(TAG,"_id = " + id + " name = " + name + " url = " + url);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        SettingsCursorWrapper cursorDef = getCursorWrapper(DefaultUrlTable.DB_TABLE_NAME, null, null,null);
        Log.d(TAG, "Table Default");
        if (cursorDef.getCount() == 0) {
            Log.d(TAG,"url = null");
            return;
        }
        try {
            cursorDef.moveToFirst();
            while (!cursorDef.isAfterLast()) {
                String url = cursorDef.getString(cursorDef.getColumnIndex(DefaultUrlTable.Cols.URL));
                Log.d(TAG,"url = " + url);
                cursorDef.moveToNext();
            }
        } finally {
            cursorDef.close();
        }
    }
}
