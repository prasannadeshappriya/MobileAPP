package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.GpaPoints;
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
    GPA gpa;
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

        courses_name.add(new Course("1","CS2023", "In14-S5-CS3332 - Industrial Instrumentation & Control"));
        courses_name.add(new Course("1","CS4854", "In14-S5-CS3322 - Computer-integrated Control System Applications"));
        courses_name.add(new Course("1","CS6298", "In14-S5-CS3282 - Industrial Computer Engineering Project"));
        courses_name.add(new Course("1","CS6598", "In14-S1-MN1012 - Engineering in Context"));
        courses_name.add(new Course("1","CS65498", "In14-S1-EL1012 - Language Skills Enhancement I"));
        courses_name.add(new Course("1","CS65928", "In14-S1-EL1012 - Language Skills Enhancement I Language Skills Enhancement ILanguage Skills Enhancement I"));

        adapter = new CourseGpaCalcAdapter(getContext(),courses_name);
        lstCourse.setAdapter(adapter);

        return view;
    }

    public void save_and_refresh(View view){
        double Total;
        int count = lstCourse.getCount();

        for(int i=0; i<count; i++){
            View v = lstCourse.getChildAt(i);
            Spinner spiGrade = (Spinner)v.findViewById(R.id.spiGrades);

            String point = GpaPoints.getPoint(spiGrade.getSelectedItem().toString());

        }


    }

    public void setGpa(GPA gpa){this.gpa = gpa;}

    private void init(View view) {
        courses_name = new ArrayList<>();
        lstCourse = (ListView) view.findViewById(R.id.lstSemesterGpa);
        btnSave = (Button) view.findViewById(R.id.btnSave);
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
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}
