package com.a14roxgmail.prasanna.mobileapp.ListAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.Semester;
import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class CourseAdapter extends BaseAdapter {
    List<Semester> arr;
    Context context;

    public CourseAdapter(Context context,List<Semester> arr ) {
        this.arr = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return arr.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_course_list,null);

        ListView lstSubCourse = (ListView)v.findViewById(R.id.lstSubCourse);
        TextView tvSemName = (TextView)v.findViewById(R.id.adapterCLSemName);

        ArrayList<Course> course_list = (ArrayList<Course>) arr.get(i).getCourseList();
        List<String> name_list = new ArrayList<>();

        for(int j=0; j<course_list.size();j++){
            name_list.add(course_list.get(j).getFullname().toString());
        }

        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,name_list);
        lstSubCourse.setAdapter(adapter);

        tvSemName.setText(String.valueOf(arr.get(i).getId()));
        return v;
    }
}
