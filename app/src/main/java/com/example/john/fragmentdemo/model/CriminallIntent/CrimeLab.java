package com.example.john.fragmentdemo.model.CriminallIntent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeLab {
    private static CrimeLab crimeLab;
    private List<Crime> mCrimeList;

    private CrimeLab(Context context) {
        mCrimeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setBoolean(i % 2 == 0); //每隔一个
            mCrimeList.add(crime);
        }
    }

    public static CrimeLab getCrimeLab(Context context) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime c : mCrimeList) {
            if (c.getId().equals(uuid)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimeList.add(c);
    }
}
