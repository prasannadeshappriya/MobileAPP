package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.annotation.TargetApi;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a14roxgmail.prasanna.mobileapp.Algorithm.GPA1;
import com.a14roxgmail.prasanna.mobileapp.Algorithm.GPA2;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.GpaPoints;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SettingsDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SyncVerifyDAO;
import com.a14roxgmail.prasanna.mobileapp.ListAdapter.CourseGpaCalcAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Sync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GpaSemFragment extends Fragment{
    private Button btnSave;
    private SyncVerifyDAO syncVerifyDAO;
    private CourseGpaCalcAdapter adapter;
    private List<Course> courses_name;
    private ListView lstCourse;
    private String semester;
    private CourseDAO course_dao;
    private GPA gpa;
    private GradeDAO gradeDAO;
    private String userIndex;
    private SettingsDAO settings_dao;

    public void setArgs(String semester, String userIndex, GPA gpa){
        setSemester(semester);
        setGpa(gpa);
        setUserIndex(userIndex);
    }

    private void setSemester(String semester){
        this.semester = semester;
    }

    private void setUserIndex(String userIndex){
        this.userIndex = userIndex;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gpa_semester,container,false);
        init(view);

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        save_and_refresh(view);
                    }
                }
        );

        ArrayList<Course> course = course_dao.getAllCourseBySemester(semester,userIndex);
        for(int i=0; i<course.size(); i++){
            String grade = course.get(i).getGrade();
            courses_name.add(
                    new Course(
                            String.valueOf(i+1),
                            course.get(i).getUserIndex(),
                            course.get(i).getCourseName(),
                            course.get(i).getCourseCode(),
                            course.get(i).getCredits(),
                            course.get(i).getSemester(),
                            grade
                    )
            );
        }
        adapter = new CourseGpaCalcAdapter(getContext(),courses_name);
        lstCourse.setAdapter(adapter);

        View v = getViewByPosition(0,lstCourse);
        Spinner spiGrade = (Spinner)v.findViewById(R.id.spiGrades);
        spiGrade.setSelection(5);

        return view;
    }

    public void save_and_refresh(View view){
        double total = 0.0;
        double total_cedits = 0.0;
        int count = lstCourse.getAdapter().getCount();
        try{
            syncVerifyDAO.updateSyncStatus(userIndex,"0");
        }catch (Exception e){
            Log.i(Constants.LOG_TAG,"Error : " + e.toString());
        }
        for(int i=0; i<count; i++){
            View v = getViewByPosition(i,lstCourse);
            Spinner spiGrade = (Spinner) v.findViewById(R.id.spiGrades);
            TextView tv = (TextView) v.findViewById(R.id.adapterCourseCredits);
            TextView tvName = (TextView) v.findViewById(R.id.adapterCourseName);
            double point = Double.parseDouble(GpaPoints.getPoint(spiGrade.getSelectedItem().toString()));

            double credit = Double.parseDouble(
                    tv.getText().toString().substring(
                            11,
                            tv.getText().toString().length()-1)
            );

            Log.i(Constants.LOG_TAG,"total :- " + total + ", credit :- " + credit + ", point :- " + point );
            total = total + (credit*point);
            Log.i(Constants.LOG_TAG,"total :- " + total);
            if(!spiGrade.getSelectedItem().toString().equals("Non - GPA")){
                total_cedits = total_cedits + credit;
                Log.i(Constants.LOG_TAG,"course is not non-gpa, total credits :- " + total_cedits);
            }
            course_dao.updateGrade(userIndex,tvName.getText().toString(),spiGrade.getSelectedItem().toString());
        }

        double sgpa = (total /total_cedits);
        Log.i(Constants.LOG_TAG, "semester gpa :- " + sgpa);

        if (gradeDAO.isSGPAExist(userIndex, semester)) {
            if(Double.isNaN(sgpa)){
                gradeDAO.deleteSGPA(userIndex,semester);
            }else {
                gradeDAO.updateSGPA(
                        userIndex,
                        String.valueOf(sgpa),
                        semester,
                        String.valueOf(total_cedits)
                );
            }
        } else {
            if(!Double.isNaN(sgpa)) {
                gradeDAO.addGpa(
                        new GPA(
                                Constants.SGPA_FLAG,
                                semester,
                                String.valueOf(sgpa),
                                userIndex,
                                String.valueOf(total_cedits)
                        )

                );
            }
        }


        if(getOption()) {
            calculate_overall_gpa_option_a();
        }else{
            calculate_overall_gpa_option_b();
        }
    }

    private void calculate_overall_gpa_option_a() {
        GPA1 gpaAlgo1 = new GPA1(getContext(),gradeDAO,userIndex);
        gpaAlgo1.calculate();

        GpaFragment gpaFragment = new GpaFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frmMain,gpaFragment);
        gpaFragment.setUser_index(userIndex);
        transaction.commit();

    }

    private void calculate_overall_gpa_option_b() {
        GPA2 gpaAlgo2 = new GPA2(getContext(),gradeDAO,userIndex);
        gpaAlgo2.calculate();

        GpaFragment gpaFragment = new GpaFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frmMain,gpaFragment);
        gpaFragment.setUser_index(userIndex);
        transaction.commit();
    }

    private boolean getOption(){
        String option = settings_dao.getGpaOperation(userIndex);
        if(option.equals("0")) {
            return true;
        }else{
            return false;
        }
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void setGpa(GPA gpa){this.gpa = gpa;}

    private void init(View view) {
        courses_name = new ArrayList<>();
        lstCourse = (ListView) view.findViewById(R.id.lstSemesterGpa);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        course_dao = new CourseDAO(getContext());
        gradeDAO = new GradeDAO(getContext());
        settings_dao = new SettingsDAO(getContext());
        syncVerifyDAO = new SyncVerifyDAO(getContext());
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
                    GpaFragment gpaFragment = new GpaFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    transaction.replace(R.id.frmMain,gpaFragment);
                    gpaFragment.setUser_index(userIndex);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}
