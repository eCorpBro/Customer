package com.github.ecorpbro.database;

import android.database.Cursor;
import android.database.CursorWrapper;

public class SettingsCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public SettingsCursorWrapper(Cursor cursor) {
        super(cursor);
    }
}
