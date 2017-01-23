package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class SettingsFragment extends Fragment {
    private ArrayList<Course> arrCourseList;
    private String user_index;
    private CheckBox chkOption1;
    private CheckBox chkOption2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        init(view);

        chkOption1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkOption2.isChecked()){
                            chkOption2.setChecked(false);
                        }
                        change_gpa_calculation_method(1);
                    }
                }
        );
        chkOption2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chkOption1.isChecked()){
                            chkOption1.setChecked(false);
                        }
                        change_gpa_calculation_method(2);
                    }
                }
        );
        return view;
    }

    private void change_gpa_calculation_method(int method){
        if(method==1){
            Toast.makeText(getContext(),"Changed to Method 1",Toast.LENGTH_SHORT).show();
        }else if(method==2){
            Toast.makeText(getContext(),"Changed to Method 2",Toast.LENGTH_SHORT).show();
        }
    }

    private void init(View view) {
        arrCourseList = new ArrayList<>();
        chkOption1 = (CheckBox)view.findViewById(R.id.chkOptionOne);
        chkOption2 = (CheckBox)view.findViewById(R.id.chkOptionTwo);

    }

    public void setParams(ArrayList<Course> arrList, String user_index){
        this.arrCourseList = arrList;
        this.user_index = user_index;
    }
}
