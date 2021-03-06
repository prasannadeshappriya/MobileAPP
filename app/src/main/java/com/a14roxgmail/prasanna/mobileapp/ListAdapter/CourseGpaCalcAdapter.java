package com.a14roxgmail.prasanna.mobileapp.ListAdapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.GpaPoints;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class CourseGpaCalcAdapter extends BaseAdapter {
    private List<Course> lstCourse;
    private Context context;
    private HashMap<Integer, Integer> viewHandler = new HashMap<>();
    boolean init = true;

    public CourseGpaCalcAdapter(Context context, List<Course> lstCourse) {
        this.context = context;
        this.lstCourse = lstCourse;
    }

    @Override
    public int getCount() {
        return lstCourse.size();
    }

    @Override
    public Object getItem(int i) {
        return lstCourse.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(lstCourse.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_gpa_semester,null);

        TextView tvCourseName = (TextView)v.findViewById(R.id.adapterCourseName) ;
        TextView tvCourseCredit = (TextView)v.findViewById(R.id.adapterCourseCredits) ;

        String course_name = lstCourse.get(i).getCourseName();
        tvCourseName.setText(course_name);

        String course_credit = lstCourse.get(i).getCredits();
        tvCourseCredit.setText("(Credits - " + course_credit + ")");


        final int position = i;

        final Spinner spiGrades = (Spinner)v.findViewById(R.id.spiGrades);
        spiGrades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewHandler.put(position,spiGrades.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> arrGrades = GpaPoints.getGradeList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrGrades);
        spiGrades.setAdapter(adapter);
        spiGrades.setSelection(GpaPoints.getIndex(lstCourse.get(i).getGrade()));
        int dd = GpaPoints.getIndex(lstCourse.get(i).getGrade());
        Log.i(Constants.LOG_TAG,String.valueOf(dd));
        if (viewHandler.containsKey(position)) {
            spiGrades.setSelection(viewHandler.get(position));
        }

        return v;
    }
}
