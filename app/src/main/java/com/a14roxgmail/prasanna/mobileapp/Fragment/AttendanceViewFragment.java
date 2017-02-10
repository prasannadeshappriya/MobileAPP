package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import java.util.ArrayList;

/**
 * Created by prasanna on 2/10/17.
 */

public class AttendanceViewFragment extends Fragment{
    private TextView tvModuleName;
    private Button btnAdd;
    private ListView lstAbsent;
    private ListView lstPresent;

    private String user_index;
    private String module_name;
    private String semester;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_view,container,false);
        init(v);

        tvModuleName.setText(module_name);
        ArrayList<String> arrPresent = new ArrayList<>();
        arrPresent.add("2017-01-08");
        arrPresent.add("2017-01-09");
        arrPresent.add("2017-01-10");
        arrPresent.add("2017-01-12");
        arrPresent.add("2017-01-15");
        arrPresent.add("2017-01-20");
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrPresent);
        lstPresent.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(lstPresent);

        ArrayList<String> arrAbcent = new ArrayList<>();
        arrAbcent.add("2017-02-08");
        arrAbcent.add("2017-05-09");
        arrAbcent.add("2017-04-10");
        arrAbcent.add("2017-08-12");
        arrAbcent.add("2017-09-15");
        arrAbcent.add("2017-06-20");
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrAbcent);
        lstAbsent.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(lstAbsent);


        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AttendanceAddFragment attendanceAddFragment = new AttendanceAddFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frmMain,attendanceAddFragment);
                        transaction.commit();
                    }
                }
        );
        return v;
    }

    private void init(View v) {
        tvModuleName = (TextView)v.findViewById(R.id.tvModuleName);
        btnAdd = (Button)v.findViewById(R.id.btnAddDate);
        lstAbsent = (ListView)v.findViewById(R.id.lstAbcentDates);
        lstPresent = (ListView)v.findViewById(R.id.lstPresentDates);

    }

    public void setParams(String user_index, String module_name){
        this.module_name = module_name;
        this.user_index = user_index;
    }
}
