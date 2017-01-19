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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.GpaPoints;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.ListAdapter.CourseGpaCalcAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GpaSemFragment extends Fragment{
    Button btnSave;
    CourseGpaCalcAdapter adapter;
    List<Course> courses_name;
    ListView lstCourse;
    String semester;
    CourseDAO course_dao;
    GPA gpa;
    GradeDAO gradeDAO;
    String userIndex;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gpa_semester,container,false);
        init(view);

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(Constants.LOG_TAG,semester);
                        save_and_refresh(view);

                    }
                }
        );


        ArrayList<Course> course = course_dao.getAllCourseBySemester(semester,userIndex);
        for(int i=0; i<course.size(); i++){
            courses_name.add(
                    new Course(
                            String.valueOf(i+1),
                            course.get(i).getUserIndex(),
                            course.get(i).getCourseName(),
                            course.get(i).getCourseCode(),
                            course.get(i).getCredits(),
                            course.get(i).getSemester()
                    )
            );
        }
        adapter = new CourseGpaCalcAdapter(getContext(),courses_name);
        int i = courses_name.size();
        lstCourse.setAdapter(adapter);

        return view;
    }

    public void save_and_refresh(View view){
        double total = 0.0;
        double total_cedits = 0.0;
        int count = lstCourse.getAdapter().getCount();

        for(int i=0; i<count; i++){
            View v = getViewByPosition(i,lstCourse);
            Spinner spiGrade = (Spinner)v.findViewById(R.id.spiGrades);
            TextView tv = (TextView) v.findViewById(R.id.adapterCourseCredits);
            TextView tvName = (TextView) v.findViewById(R.id.adapterCourseName);
            double point = Double.parseDouble(GpaPoints.getPoint(spiGrade.getSelectedItem().toString()));

            double credit = Double.parseDouble(
                    tv.getText().toString().substring(
                            11,
                            tv.getText().toString().length()-1)
            );

            Log.i(Constants.LOG_TAG,point + "  '" + credit + "'   " + spiGrade.getSelectedItem().toString());
            total = total + (credit*point);
            if(!spiGrade.getSelectedItem().toString().equals("Non - GPA")){
                total_cedits = total_cedits + credit;
            }
            course_dao.updateGrade(userIndex,tvName.getText().toString(),spiGrade.getSelectedItem().toString());
        }

        double sgpa = (total /total_cedits);
        Log.i(Constants.LOG_TAG, "semester gpa :- " + sgpa);

        gradeDAO.addGpa(
                new GPA(
                        Constants.SGPA_FLAG,
                        semester,
                        String.valueOf(sgpa),
                        userIndex,
                        String.valueOf(total_cedits)
                )

        );

        if(getOption()) {
            calculate_overall_gpa_option_a();
        }else{
            calculate_overall_gpa_option_b();
        }
    }

    private void calculate_overall_gpa_option_a() {
        ArrayList<GPA> sgpa_list = gradeDAO.getSGPA(userIndex);
        double totalSgpa = 0.0;
        double totalCredit = 0.0;
        for(int i=0; i<sgpa_list.size(); i++){
            GPA gpa = sgpa_list.get(i);
            totalSgpa+= Double.parseDouble(gpa.getGpa());
            Log.i(Constants.LOG_TAG, gpa.getTotal_credits().toString());
            totalCredit+= Double.parseDouble(gpa.getTotal_credits().toString());
        }
        double gpa = (totalSgpa/sgpa_list.size());
        Log.i(Constants.LOG_TAG,"Calculater log using method 1 :- " + gpa);

        gradeDAO.addGpa(
                new GPA(
                        Constants.GPA_FLAG,
                        semester,
                        String.valueOf(gpa),
                        userIndex,
                        String.valueOf(totalCredit)
                )
        );

        GpaFragment gpaFragment = new GpaFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frmMain,gpaFragment);
        gpaFragment.setUser_index(userIndex);
        transaction.commit();

    }

    private void calculate_overall_gpa_option_b() {
        //
    }

    private boolean getOption(){
        return true;
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
