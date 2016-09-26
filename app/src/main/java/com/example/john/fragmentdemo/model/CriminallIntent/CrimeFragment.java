package com.example.john.fragmentdemo.model.CriminallIntent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.john.fragmentdemo.R;
import com.example.john.fragmentdemo.model.date.DatePickerFragment;
import com.example.john.fragmentdemo.utils.PictureUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZheWei on 2016/9/24.
 */
public class CrimeFragment extends Fragment {

    private static final String DIALOGDATE = "DialogDate";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTENT = 1;
    private static final int REQUEST_PHOTO = 2;

    @BindView(R.id.crime_title)
    EditText mCrimeTitle;
    @BindView(R.id.crime_date)
    Button mCrimeDate;
    @BindView(R.id.crime_solved)
    CheckBox mCrimeSolved;
    @BindView(R.id.btn_choose)
    Button mBtnChoose;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.ib_camera)
    ImageButton mIbCamera;

    private Crime mCrime;
    private Intent mPickContent;
    private File mPhotoFile;
    private Intent mPhotoIntent;

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
        mPhotoFile = CrimeLab.getCrimeLab(getActivity()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).upDateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date extra = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(extra);
            setButtonDate();
        } else if (requestCode == REQUEST_CONTENT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            if (cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setSuspect(suspect);
            mBtnChoose.setText(suspect);
        } else if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();

        }
    }

    //更新图片的视图
    private void updatePhotoView() {
        Bitmap scaledBitmap = PictureUtil.getScaledBitmap(mPhotoFile.getPath(), getActivity());
        mIvPhoto.setImageBitmap(scaledBitmap);
    }

    private void setButtonDate() {
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String date = dateFormat.format(mCrime.getDate());
        mCrimeDate.setText(date);
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
        setButtonDate();
        mCrimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOGDATE);
            }
        });
        mCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setBoolean(isChecked);
            }
        });
        mCrimeSolved.setChecked(mCrime.getBoolean());
        mBtnChoose.setText(mCrime.getSuspect());
        //检测系统是否有联系人应用
        mPickContent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //        mPickContent.addCategory(Intent.CATEGORY_HOME); //验证过滤器
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(mPickContent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mBtnChoose.setEnabled(false);
        }
        //检测系统中是否有照相机应用
        mPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mPhotoFile == null || mPhotoIntent.resolveActivity(packageManager) == null) {
            mIbCamera.setEnabled(false);
        } else {//添加使用文件系统储存照片,这样可以获得全尺寸的照片
            Uri uri = Uri.fromFile(mPhotoFile);
            mPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        //更新图片
        updatePhotoView();
    }


    @OnClick({R.id.btn_choose, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose:
                //打开选择联系人的应用
                startActivityForResult(mPickContent, REQUEST_CONTENT);
                break;
            case R.id.btn_send:
                //打开应用并分享这条消息
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Crime Report");
                intent = Intent.createChooser(intent, "Send crime report via");
                startActivity(intent);
                break;
        }
    }

    @OnClick(R.id.ib_camera)
    public void onClick() {
        startActivityForResult(mPhotoIntent, REQUEST_PHOTO);
    }

    //向外部应用发送的文体
    private String getCrimeReport() {
        String solvedString = null;
        solvedString = mCrime.getBoolean() ? "the case is solved" : "the case is not solved";
        String dataformat = "EEE,MMM dd";
        CharSequence dateString = android.text.format.DateFormat.format(dataformat, mCrime.getDate());
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = "there is no suspect";
        } else {
            suspect = "the suspect is " + suspect;
        }
        return mCrime.getTitle() + dateString + solvedString + suspect;
    }


}
