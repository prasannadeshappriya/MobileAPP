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
import com.a14roxgmail.prasanna.mobileapp.DAO.AttendanceDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.AttendanceEntry;
import com.a14roxgmail.prasanna.mobileapp.R;

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
    private Button btnDelete;
    private DatePicker dtPicker;
    private AttendanceDAO attendance_dao;
    private boolean isUpdate = false;
    private String uYear="", uMonth="", uDate="";

    public void setParams(String user_index, String module_name){
        this.module_name = module_name;
        this.user_index = user_index;
    }

    public void setIsUpdate(boolean update){this.isUpdate = update;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_add,container,false);
        init(v);

        if(isUpdate){
            AttendanceEntry entry = attendance_dao.getAttendanceViewInfo(
                    user_index,
                    module_name,
                    uYear,String.valueOf(Months.getMonthIndex(uMonth)-1),uDate
            );

            dtPicker.updateDate(
                    Integer.parseInt(entry.getYear()),
                    Integer.parseInt(entry.getMonth()),
                    Integer.parseInt(entry.getDate())
            );

            if(entry.getValue().equals("1")){
                chkPresent.setChecked(true);
                chkAbsent.setChecked(false);
            }else{
                chkPresent.setChecked(false);
                chkAbsent.setChecked(true);
            }

            etComment.setText(entry.getComment());

            btnDelete.setVisibility(View.VISIBLE);
            dtPicker.setEnabled(false);
        }else{
            chkAbsent.setChecked(true);
            chkPresent.setChecked(false);
            dtPicker.setEnabled(true);
            btnDelete.setVisibility(View.INVISIBLE);
        }

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
                            transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right);
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
                            transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right);
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
                            transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right);
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
                        String value = "0";
                        if (chkAbsent.isChecked()) {
                            value = "0";
                        } else if (chkPresent.isChecked()) {
                            value = "1";
                        }
                        if(!isUpdate) {
                            if(attendance_dao.isAttendanceExist(
                                    user_index,module_name,
                                    String.valueOf(dtPicker.getYear()),
                                    String.valueOf(dtPicker.getMonth()),
                                    String.valueOf(dtPicker.getDayOfMonth())
                            )){
                                Toast.makeText(getContext(), "Entry already exist !", Toast.LENGTH_SHORT).show();
                            }else {
                                if (chkAbsent.isChecked() || chkPresent.isChecked()) {
                                    AttendanceEntry entry = new AttendanceEntry(
                                            user_index,
                                            module_name,
                                            value,
                                            String.valueOf(dtPicker.getDayOfMonth()),
                                            String.valueOf(dtPicker.getMonth()),
                                            String.valueOf(dtPicker.getYear()),
                                            etComment.getText().toString()
                                    );
                                    attendance_dao.addAttendanceEntry(entry);

                                    AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                            android.R.anim.slide_out_right);
                                    transaction.replace(R.id.frmMain, attendanceViewFragment);
                                    attendanceViewFragment.setParams(user_index, module_name);
                                    transaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "Absent/Present ?", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            AttendanceEntry entry = attendance_dao.getAttendanceViewInfo(
                                    user_index,
                                    module_name,
                                    uYear,String.valueOf(Months.getMonthIndex(uMonth)-1),uDate
                            );
                            if (chkAbsent.isChecked() || chkPresent.isChecked()) {
                                attendance_dao.updateAttendanceEntry(
                                        user_index,
                                        module_name,
                                        entry.getYear(),
                                        entry.getMonth(),
                                        entry.getDate(),
                                        etComment.getText().toString(),
                                        value
                                );

                                AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                        android.R.anim.slide_out_right);
                                transaction.replace(R.id.frmMain, attendanceViewFragment);
                                attendanceViewFragment.setParams(user_index, module_name);
                                transaction.commit();
                            } else {
                                Toast.makeText(getContext(), "Absent/Present ?", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
        );

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attendance_dao.deleteAttendanceEntry(
                                user_index,module_name,
                                String.valueOf(dtPicker.getYear()),
                                String.valueOf(dtPicker.getMonth()),
                                String.valueOf(dtPicker.getDayOfMonth())
                        );

                        AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right);
                        transaction.replace(R.id.frmMain, attendanceViewFragment);
                        attendanceViewFragment.setParams(user_index, module_name);
                        transaction.commit();
                    }
                }
        );

        return v;
    }

    public void setDateForUpdate(String year, String month, String date){
        uYear=year; uMonth=month; uDate=date;
    }

    private void init(View v) {
        chkAbsent = (CheckBox)v.findViewById(R.id.chkAbsent);
        chkPresent = (CheckBox)v.findViewById(R.id.chkPresent);
        etComment = (EditText)v.findViewById(R.id.etComments);
        btnSave = (Button)v.findViewById(R.id.btnSaveDate);
        dtPicker = (DatePicker)v.findViewById(R.id.dpPicker);
        attendance_dao = new AttendanceDAO(getContext());
        btnDelete = (Button)v.findViewById(R.id.btnDeleteDate);
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
                    transaction.setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
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
