package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.ListAdapter.CalendarAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.Entry;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.UI.HomeActivity;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Prasanna Deshappriya on 1/22/2017.
 */
public class CalendarFragment extends Fragment {
    private String userIndex;
    private Spinner spiYear;
    private Spinner spiMonth;
    private userDAO user_dao;
    private ListView lstCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        init(view);
        refresh_list();

        Button b = (Button) view.findViewById(R.id.btnTest);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),user_dao.getUserPassword(userIndex),Toast.LENGTH_LONG).show();
                        getCalendarInfo calendarTask = new getCalendarInfo(
                                user_dao,
                                userIndex,
                                2016,
                                11,
                                lstCalendar
                        );
                        calendarTask.execute();
                    }
                }
        );
        return view;
    }

    private void refresh_list() {
        ArrayList<String> arrMonth = new ArrayList<>();
        arrMonth.add("January");
        arrMonth.add("February");
        arrMonth.add("March");
        arrMonth.add("April");
        arrMonth.add("May");
        arrMonth.add("June");
        arrMonth.add("July");
        arrMonth.add("August");
        arrMonth.add("September");
        arrMonth.add("October");
        arrMonth.add("November");
        arrMonth.add("December");

        ArrayList<String> arrYear = new ArrayList<>();
        arrYear.add("2016");
        arrYear.add("2017");

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arrMonth);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arrYear);
        spiMonth.setAdapter(adapterMonth);
        spiMonth.setSelection(10);
        spiYear.setAdapter(adapterYear);
    }

    private void init(View view) {
        user_dao = new userDAO(getContext());
        spiMonth = (Spinner) view.findViewById(R.id.spiMonth);
        spiYear = (Spinner) view.findViewById(R.id.spiYear);
        lstCalendar = (ListView) view.findViewById(R.id.lstCalender);
    }

    public void setUserIndex(String userIndex){
        this.userIndex = userIndex;
    }

    public class getCalendarInfo extends AsyncTask<Void,Void,Void> {
        private String username;
        private String password;
        private userDAO user_dao;
        CalendarAdapter adapter;
        private int month;
        private int year;
        private ProgressDialog pd;
        private ArrayList<Entry> arrAdapterArray;

        public getCalendarInfo(userDAO user_dao, String userIndex, int year, int month, ListView list){
            this.user_dao = user_dao;
            this.username = userIndex;
            this.month = month; this.year = year;
            this.password = user_dao.getUserPassword(userIndex);
            pd = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
            arrAdapterArray = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Authenticating..");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response response;
                response = Jsoup.connect(Constants.MOODLE_LOGIN_URL)
                        .method(Connection.Method.GET)
                        .execute();

                String session = response.cookie(Constants.MOODLE_COOKIE_ID);
                Map<String,String> map = response.cookies();

                response = Jsoup.connect(Constants.MOODLE_LOGIN_URL)
                        .cookie(Constants.MOODLE_COOKIE_ID,session)
                        .data(Constants.MOODLE_USERNAME_ID,username)
                        .data(Constants.MOODLE_PASSWORD_ID,password)
                        .cookies(response.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                String url = Constants.MOODLE_CALENDAR_URL + "?view=month&course=1&cal_d=1&cal_m=" + month + "&cal_y=" + year;
                Log.i(Constants.LOG_TAG,"Moodle calendar view url, " + url);
                Document document = Jsoup.connect(url)
                        .cookie(Constants.MOODLE_COOKIE_ID,session)
                        .cookies(response.cookies()).get();

                Elements elements = document.select("a");
                boolean con = true;
                HashMap<Integer,Integer> list = new HashMap<>();
                ArrayList<String> arrList = new ArrayList<>();
                int date = 0;
                int count = 0;
                for (Element element : elements) {
                    if(!element.ownText().replace(" ","").equals("")) {
                        if (element.ownText().equals("Skip Navigation")) {
                            list.put(date,count);
                            break;
                        }
                        if(con){
                            if(Utility.isInteger(element.ownText())){
                                con = false;
                                date = Integer.parseInt(element.ownText());
                            }
                        }else{
                            if(Utility.isInteger(element.ownText())){
                                list.put(date,count);
                                count = 0;
                                date = Integer.parseInt(element.ownText());
                            }else{
                                arrList.add(element.ownText());
                                count++;
                            }
                        }
                    }
                }

                for (int n=0;n<32;n++) {
                    int c = 0;
                    if(list.containsKey(n)) {
                        for (int i = 0; i < 32; i++) {
                            if (i == n) {
                                int list_count = list.get(n);
                                Entry entry = new Entry(
                                        String.valueOf(n),
                                        String.valueOf(month)
                                );
                                ArrayList<String> arrEntry = new ArrayList<>();
                                for (int k = c; k < (c + list_count); k++) {
                                    Log.i(Constants.LOG_TAG, "Day " + n + " have " + arrList.get(k));
                                    arrEntry.add(arrList.get(k));
                                }
                                entry.setEvents(arrEntry);
                                arrAdapterArray.add(entry);
                                break;
                            }
                            if (list.containsKey(i)) {
                                int list_count = list.get(i);
                                c+=list_count;
                            }
                        }
                    }
                }

                adapter = new CalendarAdapter(getContext(),arrAdapterArray);

            } catch (Exception e) {
                Log.i(Constants.LOG_TAG, "Error caught :- " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            lstCalendar.setAdapter(adapter);
            pd.dismiss();
            Log.i(Constants.LOG_TAG,"Completed calendar Process");
        }

    }
}
