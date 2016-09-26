package com.example.john.fragmentdemo.model.CriminallIntent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.john.fragmentdemo.db.CrimeBaseHelpter;
import com.example.john.fragmentdemo.db.CrimeCursorWrapper;
import com.example.john.fragmentdemo.db.CrimeDbSchma;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeLab {
    private static CrimeLab crimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context;
        mDatabase = new CrimeBaseHelpter(context).getWritableDatabase();

    }

    public static CrimeLab getCrimeLab(Context context) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    //得到记录列表
    public List<Crime> getCrimeList() {
        List<Crime> crimeList = new ArrayList<>();
        CrimeCursorWrapper wrapper = queryCrimes(null, null);
        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                crimeList.add(wrapper.getCrime());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return crimeList;
    }

    //根据id得到 记录
    public Crime getCrime(UUID uuid) {
        CrimeCursorWrapper wrapper = queryCrimes(CrimeDbSchma.CrimeTable.Cols.UUID + " = ? ", new String[]{uuid.toString()});
        try {
            if (wrapper.getCount() == 0) {
                return null;
            }
            wrapper.moveToFirst();
            return wrapper.getCrime();
        } finally {
            wrapper.close();
        }
    }


    //添加记录
    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchma.CrimeTable.NAME, null, values);
    }

    //更新记录
    public void upDateCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.update(CrimeDbSchma.CrimeTable.NAME, values, CrimeDbSchma.CrimeTable.Cols.UUID + " = ?" +
                " ", new String[]{c.getId().toString()});
    }


    private static ContentValues getContentValues(Crime c) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchma.CrimeTable.Cols.UUID, c.getId().toString());
        values.put(CrimeDbSchma.CrimeTable.Cols.TITLE, c.getTitle());
        values.put(CrimeDbSchma.CrimeTable.Cols.DATE, c.getDate().getTime());
        values.put(CrimeDbSchma.CrimeTable.Cols.SOLVED, c.getBoolean() ? 1 : 0);
        values.put(CrimeDbSchma.CrimeTable.Cols.SUSPECT, c.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeDbSchma.CrimeTable.NAME,
                null,//select all
                whereClause,
                whereArgs, null, null, null
        );
        return new CrimeCursorWrapper(cursor);
    }

    //定位图片文件
    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFileName());
    }
}
