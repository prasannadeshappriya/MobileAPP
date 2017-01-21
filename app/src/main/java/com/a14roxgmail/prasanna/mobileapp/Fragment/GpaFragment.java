package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.ListAdapter.GpaViewAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class GpaFragment extends Fragment {
    private GpaViewAdapter adapter;
    private List<GPA> lstGpa;
    private ListView listView;
    private GradeDAO grade_dao;
    private CourseDAO course_dao;
    private String user_index;

    public void setUser_index(String user_index){
        this.user_index = user_index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gpa,container,false);
        init(view);
        refreshListView();

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        GPA gpa = (GPA)adapterView.getItemAtPosition(i);
                        openEditWindow(view,gpa,i);
                        return false;
                    }
                }
        );

        Toast.makeText(getContext(),"Tuch and hold semester to edit", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void openEditWindow(View view, GPA gpa, int i){
        if(gpa.getType().equals("sgpa")){
            GpaSemFragment gpaSemFragment = new GpaSemFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frmMain,gpaSemFragment);
            gpaSemFragment.setArgs(String.valueOf(i+1),user_index,gpa);
            transaction.commit();
        }
    }

    public void refreshListView(){
        if(grade_dao.isGPAExist(user_index)){
            int max = Integer.parseInt(course_dao.getMaxSemester(user_index));
            for(int i=0; i<max; i++) {
                if (grade_dao.isSGPAExist(user_index, String.valueOf(i+1))) {
                    GPA gpa = grade_dao.getSGPA(user_index,String.valueOf(i+1));
                    lstGpa.add(new GPA(
                            (i+1),
                            gpa.getType(),
                            gpa.getSemester(),
                            gpa.getGpa(),
                            gpa.getUserIndex()
                    ));
                }else{
                    lstGpa.add(new GPA(
                            (i+1),
                            "sgpa",
                            String.valueOf(i+1),
                            "Not Calculated Yet",
                            user_index
                    ));
                }
            }
            GPA overall = grade_dao.getGPA(user_index);
            lstGpa.add(new GPA(
                    max,
                    overall.getType(),
                    "-",
                    overall.getGpa(),
                    overall.getUserIndex()
            ));
        }else{
            int a = Integer.parseInt(course_dao.getMaxSemester(user_index));
            for(int i=0;i<a;i++){
                lstGpa.add(new GPA((i+1), "sgpa",String.valueOf(i+1),"Not Calculated Yet",user_index));
            }
            lstGpa.add(new GPA(9, "gpa","-","Not Calculated Yet",user_index));
        }

        adapter = new GpaViewAdapter(getContext(),lstGpa);
        listView.setAdapter(adapter);
    }

    private void init(View view) {
        lstGpa = new ArrayList<>();
        listView = (ListView)view.findViewById(R.id.lstGpaViewList);
        grade_dao = new GradeDAO(getContext());
        course_dao = new CourseDAO(getContext());
    }
}
