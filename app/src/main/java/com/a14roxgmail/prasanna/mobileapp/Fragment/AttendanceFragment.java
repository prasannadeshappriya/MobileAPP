package com.a14roxgmail.prasanna.mobileapp.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.ArrayList;

/**
 * Created by prasanna on 2/9/17.
 */

public class AttendanceFragment extends Fragment{
    private Spinner spiSemester;
    private ListView lstModules;
    private CourseDAO course_dao;
    private String user_index;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance,container,false);
        init(v);

        if(spiSemester.getAdapter().getCount()!=0) {
            refresh_course_list(0);
        }

        spiSemester.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(spiSemester.getAdapter().getCount()!=0) {
                            refresh_course_list(i);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                }
        );

        lstModules.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AttendanceViewFragment attendanceViewFragment = new AttendanceViewFragment();
                        String module_name = lstModules.getAdapter().getItem(i).toString();
                        attendanceViewFragment.setParams(user_index,module_name);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frmMain,attendanceViewFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );
        return v;
    }

    private void refresh_course_list(int i) {
        try{
            String semester = String.valueOf(i+1);
            ArrayList<String> arrCourseList = course_dao.getAllCourseNamesBySemester(semester,user_index);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arrCourseList);
            lstModules.setAdapter(adapter);
            Log.i(Constants.LOG_TAG,String.valueOf(semester));
        }catch (Exception e){
            Log.i(Constants.LOG_TAG,"Error on loading modules [AttendanceFragment] :- " + e.toString());
        }
    }

    private void init(View v) {
        spiSemester = (Spinner)v.findViewById(R.id.spiSemester);
        lstModules = (ListView) v.findViewById(R.id.lstAttendanceModules);
        course_dao = new CourseDAO(getContext());
        int max_sem = Integer.parseInt(course_dao.getMaxSemester(user_index));
        ArrayList<String> arrSemester = new ArrayList<>();
        for(int i=0; i<max_sem; i++){
            arrSemester.add(String.valueOf(i+1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arrSemester);
        spiSemester.setAdapter(adapter);
        spiSemester.setSelection(max_sem-1);
    }

    public void setUserIndex(String user_index){
        this.user_index = user_index;
    }
}
