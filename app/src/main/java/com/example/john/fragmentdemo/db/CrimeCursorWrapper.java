package com.example.john.fragmentdemo.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.john.fragmentdemo.model.CriminallIntent.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ZheWei on 2016/9/25.
 */
public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDbSchma.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchma.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchma.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchma.CrimeTable.Cols.SOLVED));
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setBoolean(isSolved != 0);
        return crime;
    }
}
