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
                            course.get(i).getSemester(),
                            "2"
                    )
            );
        }
        adapter = new CourseGpaCalcAdapter(getContext(),courses_name);
        int i = courses_name.size();
        lstCourse.setAdapter(adapter);

        return view;
    }

    public void save_and_refresh(View view){
        double Total;
        int count = lstCourse.getAdapter().getCount();

        for(int i=0; i<count; i++){
            //View v = lstCourse.getAdapter().getView(i,view,null);
            View v = getViewByPosition(i,lstCourse);
            Spinner spiGrade = (Spinner)v.findViewById(R.id.spiGrades);
            TextView tv = (TextView) v.findViewById(R.id.adapterCourseCredits);
            String point = GpaPoints.getPoint(spiGrade.getSelectedItem().toString());
            Log.i(Constants.LOG_TAG,point + "  " + tv.getText().toString() + "   " + spiGrade.getSelectedItem().toString());

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
