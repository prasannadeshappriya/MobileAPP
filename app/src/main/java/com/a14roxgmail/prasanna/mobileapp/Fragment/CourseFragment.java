package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.a14roxgmail.prasanna.mobileapp.ListAdapter.CourseAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.Semester;
import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class CourseFragment extends Fragment {
    private List<Semester> lstCourse;
    private CourseAdapter adapter;
    private ListView lstCourstView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course,container,false );
        //initialize
        init(view);

        ArrayList<Course> c1 = new ArrayList<>();
        c1.add(new Course("1","Prasanna","Deshapppriya"));
        c1.add(new Course("2","Hiruni Kalanika","Damn Shit"));

        ArrayList<Course> c2 = new ArrayList<>();
        c2.add(new Course("1","Jeffry","asdds"));
        c2.add(new Course("2","Fucker","mafucker"));

        lstCourse.add(new Semester(1,c1));
        lstCourse.add(new Semester(2,c2));

        adapter = new CourseAdapter(getContext(),lstCourse);
        lstCourstView.setAdapter(adapter);

        return view;
    }

    private void init(View view) {
        lstCourse = new ArrayList<>();
        lstCourstView = (ListView)view.findViewById(R.id.lstCourse);
    }
}
