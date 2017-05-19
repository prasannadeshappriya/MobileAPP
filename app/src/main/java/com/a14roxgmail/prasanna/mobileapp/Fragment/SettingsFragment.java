package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Algorithm.GPA1;
import com.a14roxgmail.prasanna.mobileapp.Algorithm.GPA2;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SettingsDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SyncVerifyDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Sync;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class SettingsFragment extends Fragment {
    private ArrayList<Course> arrCourseList;
    private String user_index;
    private CheckBox chkOption1;
    private CheckBox chkOption2;
    private SyncVerifyDAO syncVerifyDAO;
    private TextView tvSync;
    private Button btnSyncNow;
    private Sync sync;
    private CourseDAO course_dao;
    private SettingsDAO settings_dao;
    private Button btnUpdate;
    private EditText etNewPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        init(view);

        chkOption1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkOption2.isChecked()){
                            chkOption2.setChecked(false);
                        }
                        settings_dao.updateGpaCalcOperation(user_index,"0");
                        GPA1 gpa1 = new GPA1(
                                getContext(),
                                new GradeDAO(getContext()),
                                user_index
                        );
                        gpa1.calculate();
                    }
                }
        );
        chkOption2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkOption1.isChecked()){
                            chkOption1.setChecked(false);
                        }
                        settings_dao.updateGpaCalcOperation(user_index,"1");
                        GPA2 gpa2 = new GPA2(
                                getContext(),
                                new GradeDAO(getContext()),
                                user_index
                        );
                        gpa2.calculate();
                    }
                }
        );
        btnSyncNow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(sync.isSyncNeed()){
                            try{
                                syncVerifyDAO.updateSyncStatus(user_index,"0");
                            }catch (Exception e){
                                Log.i(Constants.LOG_TAG,"Error : " + e.toString());
                            }
                            sync.startSyncProcess();
                            tvSync.setText(settings_dao.getLastSyncDate(user_index));
                        }else{
                            Toast.makeText(getContext(),"Database is up to date",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etNewPassword.getText().toString().replace(" ","").equals("")){
                            etNewPassword.setError("Invalid password");
                        }else {
                            sync.setNew_password(etNewPassword.getText().toString());
                            sync.updatePassword();
                            etNewPassword.setText("");
                        }
                    }
                }
        );
        return view;
    }

    private void init(View view) {
        chkOption1 = (CheckBox)view.findViewById(R.id.chkOptionOne);
        chkOption2 = (CheckBox)view.findViewById(R.id.chkOptionTwo);
        tvSync = (TextView) view.findViewById(R.id.tvSync);
        btnSyncNow = (Button) view.findViewById(R.id.btnSyncNow);
        course_dao = new CourseDAO(getContext());
        syncVerifyDAO = new SyncVerifyDAO(getContext());
        settings_dao = new SettingsDAO(getContext());

        String lastSyncDate = settings_dao.getLastSyncDate(user_index);
        String option = settings_dao.getGpaOperation(user_index);

        if(option.equals("0")){
            chkOption1.setChecked(true);
            chkOption2.setChecked(false);
        }else{
            chkOption1.setChecked(false);
            chkOption2.setChecked(true);
        }

        tvSync.setText(lastSyncDate);

        btnUpdate = (Button)view.findViewById(R.id.btnUpdate);
        etNewPassword = (EditText) view.findViewById(R.id.tvNewPass);

        sync = new Sync(
                getContext(),
                user_index,
                arrCourseList,
                course_dao,
                getActivity(),
                etNewPassword.getText().toString()
        );
    }

    public void setParams(ArrayList<Course> arrList, String user_index){
        this.arrCourseList = arrList;
        this.user_index = user_index;
    }
}
