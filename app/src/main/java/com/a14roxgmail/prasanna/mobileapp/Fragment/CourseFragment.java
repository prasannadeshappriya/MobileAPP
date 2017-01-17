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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class CourseFragment extends Fragment {
    private List<Semester> lstCourse;
    private CourseAdapter adapter;
    private ListView lstCourstView;
    private ArrayList<JSONObject> serverCourseList;

    private ArrayList<Course> sem1, sem2, sem3, sem4, sem5, sem6, sem7, sem8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course,container,false );
        //initialize
        init(view);
        syncCourseList();

        if(!sem1.isEmpty()){lstCourse.add(new Semester(1,sem1));}
        if(!sem2.isEmpty()){lstCourse.add(new Semester(2,sem2));}
        if(!sem3.isEmpty()){lstCourse.add(new Semester(3,sem3));}
        if(!sem4.isEmpty()){lstCourse.add(new Semester(4,sem4));}
        if(!sem5.isEmpty()){lstCourse.add(new Semester(5,sem5));}
        if(!sem6.isEmpty()){lstCourse.add(new Semester(6,sem6));}
        if(!sem7.isEmpty()){lstCourse.add(new Semester(7,sem7));}
        if(!sem8.isEmpty()){lstCourse.add(new Semester(8,sem8));}

        adapter = new CourseAdapter(getContext(),lstCourse);
        lstCourstView.setAdapter(adapter);

        return view;
    }

    private void syncCourseList(){
        for (int i=0; i<serverCourseList.size(); i++){
            try {
                String cName = serverCourseList.get(i).getString("shortname");
                if(cName.substring(7,8).equals("-")){
                    String sem = cName.substring(5,7);
                    if (sem.toLowerCase().equals("s1")){
                        addElements(sem1,i);
                    }else if (sem.toLowerCase().equals("s2")){
                        addElements(sem2,i);
                    }else if (sem.toLowerCase().equals("s3")){
                        addElements(sem3,i);
                    }else if (sem.toLowerCase().equals("s4")){
                        addElements(sem4,i);
                    }else if (sem.toLowerCase().equals("s5")){
                        addElements(sem5,i);
                    }else if (sem.toLowerCase().equals("s6")){
                        addElements(sem6,i);
                    }else if (sem.toLowerCase().equals("s7")){
                        addElements(sem7,i);
                    }else if (sem.toLowerCase().equals("s8")){
                        addElements(sem8,i);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addElements(ArrayList<Course> ret, int index){
        try {
            ret.add(
                    new Course(
                            serverCourseList.get(index).getString("id"),
                            serverCourseList.get(index).getString("shortname"),
                            serverCourseList.get(index).getString("fullname")
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        lstCourse = new ArrayList<>();
        lstCourstView = (ListView)view.findViewById(R.id.lstCourse);
        sem1 = new ArrayList<>();
        sem2 = new ArrayList<>();
        sem3 = new ArrayList<>();
        sem4 = new ArrayList<>();
        sem5 = new ArrayList<>();
        sem6 = new ArrayList<>();
        sem7 = new ArrayList<>();
        sem8 = new ArrayList<>();
    }

    public void setServerCourseList(ArrayList<JSONObject> arr){
        this.serverCourseList = arr;
    }
}
