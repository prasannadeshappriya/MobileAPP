package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.a14roxgmail.prasanna.mobileapp.Constants.Months;
import com.a14roxgmail.prasanna.mobileapp.DAO.AttendanceDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.AttendanceEntry;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import java.util.ArrayList;

/**
 * Created by prasanna on 2/10/17.
 */

public class AttendanceViewFragment extends Fragment{
    private TextView tvModuleName;
    private ImageButton btnAdd;
    private ListView lstAbsent;
    private ListView lstPresent;

    private String user_index;
    private String module_name;
    private ArrayAdapter<String> adapter;
    private AttendanceDAO attendance_dao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_view,container,false);
        init(v);

        ArrayList<String> arrPresent = new ArrayList<>();
        ArrayList<String> arrAbcent = new ArrayList<>();
        tvModuleName.setText(module_name);

        ArrayList<AttendanceEntry> arrList = attendance_dao.getAttendanceInfo(user_index,module_name);
        if(!arrList.isEmpty()) {
            for (AttendanceEntry entry : arrList) {
                String msg = "";
                if(entry.getValue().equals("1")){
                    msg = entry.getYear() + "-" + Months.getMonth(String.valueOf(Integer.parseInt(entry.getMonth())+1)) + "-" + entry.getDate();
                    if(!entry.getComment().equals("")) {
                        msg += ",  " + entry.getComment();
                    }
                    arrPresent.add(msg);
                }else if (entry.getValue().equals("0")){
                    msg = entry.getYear() + "-" + Months.getMonth(String.valueOf(Integer.parseInt(entry.getMonth())+1)) + "-" + entry.getDate();
                    if(!entry.getComment().equals("")){
                        msg += ",  " + entry.getComment();
                    }
                    arrAbcent.add(msg);
                }
            }
        }

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrPresent);
        lstPresent.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(lstPresent);

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrAbcent);
        lstAbsent.setAdapter(adapter);
        Utility.setListViewHeightBasedOnItems(lstAbsent);


        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AttendanceAddFragment attendanceAddFragment = new AttendanceAddFragment();
                        attendanceAddFragment.setParams(user_index,module_name);
                        attendanceAddFragment.setIsUpdate(false);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right);
                        transaction.replace(R.id.frmMain,attendanceAddFragment);
                        transaction.commit();
                    }
                }
        );

        lstAbsent.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String msg = lstAbsent.getAdapter().getItem(i).toString();
                        int start = 0;
                        int count = 0;
                        String year= "", month = "", date = "";
                        for (int j=1;j <msg.length(); j++){
                            if(count<3) {
                                if (msg.substring(j - 1, j).equals("-") || msg.substring(j - 1, j).equals(",") || msg.substring(j - 1, j).equals("")) {
                                    if(count==0){year = msg.substring(start, j-1);}
                                    else if(count==1){month = msg.substring(start, j-1);}
                                    else if(count==2){date = msg.substring(start, j-1);}
                                    start = j++;
                                    count++;
                                }
                            }else{break;}
                        }
                        if(date.equals("")){
                            if(msg.substring(msg.length()-3,msg.length()-2).equals("-")){
                                date = msg.substring(msg.length()-2).replace(" ","");
                            }else if(msg.substring(msg.length()-2,msg.length()-1).equals("-")){
                                date = msg.substring(msg.length()-1).replace(" ","");
                            }
                        }
                        if(!year.equals("") && !month.equals("") && !date.equals("")){
                            AttendanceAddFragment attendanceAddFragment = new AttendanceAddFragment();
                            attendanceAddFragment.setParams(user_index,module_name);
                            attendanceAddFragment.setIsUpdate(true);
                            attendanceAddFragment.setDateForUpdate(year,month,date);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            transaction.replace(R.id.frmMain,attendanceAddFragment);
                            transaction.commit();
                        }
                        return true;
                    }
                }
        );

        lstPresent.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String msg = lstPresent.getAdapter().getItem(i).toString();
                        int start = 0;
                        int count = 0;
                        String year= "", month = "", date = "";
                        for (int j=1;j <msg.length(); j++){
                            if(count<3) {
                                if (msg.substring(j - 1, j).equals("-") || msg.substring(j - 1, j).equals(",")) {
                                    if(count==0){year = msg.substring(start, j-1);}
                                    else if(count==1){month = msg.substring(start, j-1);}
                                    else if(count==2){date = msg.substring(start, j-1);}
                                    start = j++;
                                    count++;
                                }
                            }else{break;}
                        }
                        if(date.equals("")){
                            if(msg.substring(msg.length()-3,msg.length()-2).equals("-")){
                                date = msg.substring(msg.length()-2).replace(" ","");
                            }else if(msg.substring(msg.length()-2,msg.length()-1).equals("-")){
                                date = msg.substring(msg.length()-1).replace(" ","");
                            }
                        }
                        if(!year.equals("") && !month.equals("") && !date.equals("")){
                            AttendanceAddFragment attendanceAddFragment = new AttendanceAddFragment();
                            attendanceAddFragment.setParams(user_index,module_name);
                            attendanceAddFragment.setIsUpdate(true);
                            attendanceAddFragment.setDateForUpdate(year,month,date);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            transaction.replace(R.id.frmMain,attendanceAddFragment);
                            transaction.commit();
                        }
                        return true;
                    }
                }
        );

        calculate_80_pr();

        return v;
    }

    private void calculate_80_pr() {
        double present_count = lstPresent.getAdapter().getCount();
        double absent_count = lstAbsent.getAdapter().getCount();

        double total_count = present_count +absent_count;
        double avg = (present_count/total_count)*100;

        if(!Double.isNaN(avg)) {
            tvModuleName.setText(
                    tvModuleName.getText().toString() + " [" + Utility.roundInToDecimals(avg) + " %]"
            );
        }
    }

    private void init(View v) {
        tvModuleName = (TextView)v.findViewById(R.id.tvModuleName);
        btnAdd = (ImageButton) v.findViewById(R.id.btnAddDate);
        lstAbsent = (ListView)v.findViewById(R.id.lstAbcentDates);
        lstPresent = (ListView)v.findViewById(R.id.lstPresentDates);
        attendance_dao = new AttendanceDAO(getContext());
    }

    public void setParams(String user_index, String module_name){
        this.module_name = module_name;
        this.user_index = user_index;
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
                    AttendanceFragment attendanceFragment = new AttendanceFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    transaction.replace(R.id.frmMain,attendanceFragment);

                    attendanceFragment.setUserIndex(user_index);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}
