package com.example.john.fragmentdemo.model.CriminallIntent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.john.fragmentdemo.R;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeFragment extends Fragment {

    @BindView(R.id.crime_title)
    EditText mCrimeTitle;
    @BindView(R.id.crime_date)
    Button mCrimeDate;
    @BindView(R.id.crime_solved)
    CheckBox mCrimeSolved;
    private Crime mCrime;

    private static final String ARG_CRIME_ID = "crime_id";

    public static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, id);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(id);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }



    private void initView(View v) {
                mCrimeTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mCrime.setTitle(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                mCrimeDate.setText(mCrime.getDate().toString());
                mCrimeDate.setEnabled(false);
                mCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mCrime.setBoolean(isChecked);
                    }
                });
                mCrimeSolved.setChecked(mCrime.isBoolean());
    }


}
