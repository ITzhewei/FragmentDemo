package com.example.john.fragmentdemo.model.CriminallIntent;

import android.support.v4.app.Fragment;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
