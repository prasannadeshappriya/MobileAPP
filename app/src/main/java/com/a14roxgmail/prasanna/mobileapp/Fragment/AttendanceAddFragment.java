package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.Months;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

/**
 * Created by prasanna on 2/10/17.
 */

public class AttendanceAddFragment extends Fragment {
    private String user_index;
    private String module_name;
    private CheckBox chkPresent;
    private CheckBox chkAbsent;
    private EditText etComment;
    private Button btnSave;
    private DatePicker dtPicker;

    public void setParams(String user_index, String module_name){
        this.module_name = module_name;
        this.user_index = user_index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_add,container,false);
        init(v);
        chkAbsent.setChecked(true);
        chkPresent.setChecked(false);

        chkPresent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkAbsent.isChecked()){
                            chkAbsent.setChecked(false);
                        }
                    }
                }
        );

        chkPresent.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                            AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frmMain,attendanceViewFragment);
                            attendanceViewFragment.setParams(user_index,module_name);
                            transaction.commit();
                            return true;
                        }
                        return false;
                    }
                }
        );

        chkAbsent.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                            AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frmMain,attendanceViewFragment);
                            attendanceViewFragment.setParams(user_index,module_name);
                            transaction.commit();
                            return true;
                        }
                        return false;
                    }
                }
        );

        etComment.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                            AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frmMain,attendanceViewFragment);
                            attendanceViewFragment.setParams(user_index,module_name);
                            transaction.commit();
                            return true;
                        }
                        return false;
                    }
                }
        );

        chkAbsent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkPresent.isChecked()){
                            chkPresent.setChecked(false);
                        }
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(Constants.LOG_TAG,"Year :- " + dtPicker.getYear() + ", Month :- " + Months.getMonth(String.valueOf(dtPicker.getMonth())) + ", Date :- " + dtPicker.getDayOfMonth());
                        dtPicker.updateDate(1994,10,9);

                    }
                }
        );

        return v;
    }

    private void init(View v) {
        chkAbsent = (CheckBox)v.findViewById(R.id.chkAbsent);
        chkPresent = (CheckBox)v.findViewById(R.id.chkPresent);
        etComment = (EditText)v.findViewById(R.id.etComments);
        btnSave = (Button)v.findViewById(R.id.btnSaveDate);
        dtPicker = (DatePicker)v.findViewById(R.id.dpPicker);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frmMain,attendanceViewFragment);
                    attendanceViewFragment.setParams(user_index,module_name);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}
