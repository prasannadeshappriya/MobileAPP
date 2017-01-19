package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
                        openEditWindow(view,gpa);
                        return false;
                    }
                }
        );

        return view;
    }

    private void openEditWindow(View view, GPA gpa){
        if(gpa.getType().equals("sgpa")){
            GpaSemFragment gpaSemFragment = new GpaSemFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frmMain,gpaSemFragment);
            gpaSemFragment.setGpa(gpa);
            transaction.commit();
        }
    }

    public void refreshListView(){
        if(!grade_dao.isGPAExist(user_index)){
            lstGpa.add(new GPA(1, "sgpa","1","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(2, "sgpa","2","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(3, "sgpa","3","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(4, "sgpa","4","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(5, "sgpa","5","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(6, "sgpa","6","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(7, "sgpa","7","Not Calculated Yet",user_index));
            lstGpa.add(new GPA(8, "sgpa","8","Not Calculated Yet",user_index));
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
