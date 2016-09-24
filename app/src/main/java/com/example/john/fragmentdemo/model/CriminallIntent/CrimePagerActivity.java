package com.example.john.fragmentdemo.model.CriminallIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.john.fragmentdemo.R;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimePagerActivity extends FragmentActivity {
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    @BindView(R.id.vp_crime_pager)
    ViewPager mVpCrimePager;

    private List<Crime> mCrimeList;

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        ButterKnife.bind(this);

        UUID extra = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        final FragmentManager fm = getSupportFragmentManager();
        mCrimeList = CrimeLab.getCrimeLab(this).getCrimeList();

        mVpCrimePager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimeList.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });

        for (int i = 0; i < mCrimeList.size(); i++) {
            if (mCrimeList.get(i).getId().equals(extra)) {
                mVpCrimePager.setCurrentItem(i);
                break;
            }
        }
    }

}
