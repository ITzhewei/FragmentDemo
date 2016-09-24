package com.example.john.fragmentdemo.model.CriminallIntent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.john.fragmentdemo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeListFragment extends Fragment {

    @BindView(R.id.crime_recycle_view)
    RecyclerView mCrimeRecycleView;

    private CrimeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        ButterKnife.bind(this, view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> crimeList = crimeLab.getCrimeList();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimeList);
            mCrimeRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter {

        private List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> crimeList) {
            mCrimeList = crimeList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimeList.get(position);
            CrimeHolder crimeHolder = (CrimeHolder) holder;
            crimeHolder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

        class CrimeHolder extends RecyclerView.ViewHolder {

            public TextView mTitleTextView;
            public TextView mDateTextView;
            public CheckBox mSolvedCheckBox;

            public CrimeHolder(View itemView) {
                super(itemView);
                mTitleTextView = (TextView) itemView.findViewById(R.id.tv_list_item_title);
                mDateTextView = (TextView) itemView.findViewById(R.id.tv_list_item_date);
                mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.cb_list_item_solved);

            }

            public void bindCrime(final Crime crime) {
                mTitleTextView.setText(crime.getTitle());
                mDateTextView.setText(crime.getDate().toString());
                mSolvedCheckBox.setChecked(crime.isBoolean());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                        startActivity(intent);
                    }
                });

            }

        }
    }


}
