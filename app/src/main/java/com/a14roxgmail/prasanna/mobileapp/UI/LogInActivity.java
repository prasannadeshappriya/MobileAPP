package com.a14roxgmail.prasanna.mobileapp.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.Token;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.User;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.ServerRequest;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LogInActivity extends AppCompatActivity implements Serializable {
    private EditText etUserName;
    private EditText etPassword;
    private Button btnSignIn;
    private HashMap<String,String> details;
    private ProgressDialog pd;
    private userDAO user_DAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize
        init();

        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SignIn();
                    }
                }
        );

    }



    public void SignIn(){
        if(Validate()){
            if(user_DAO.isUserExist(etUserName.getText().toString())){
                Log.i(Constants.LOG_TAG,"User " + etUserName.getText().toString() + " is exist on the database");

                if(CheckInternetAccess()){
                    Log.i(Constants.LOG_TAG,"Start sync process for user :- " + etUserName.getText().toString());

                }else {
                    Log.i(Constants.LOG_TAG,"No internet connection available, loading local data");

                    User user = user_DAO.getUser(etUserName.getText().toString());
                    details = new HashMap<>();

                    //Hashmap_keys
                    //          sitename
                    //          username
                    //          firstname
                    //          lastname
                    //          fullname
                    details.put("username", user.getUserIndex());
                    details.put("firstname", user.getFirstName());
                    details.put("lastname", user.getLastName());
                    details.put("fullname", user.getFullName());

                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    i.putExtra("Values",details);
                    this.finish();
                    startActivity(i);
                    Log.i(Constants.LOG_TAG,"Completed SignIn Process");

                }
            }else {
                Log.i(Constants.LOG_TAG,"User " + etUserName.getText().toString() + " is not exist on the database");
                getToken();
            }
        }
    }

    public boolean CheckInternetAccess(){
        return false;
    }

    public boolean Validate(){
        //Validate email address and password
        return true;
    }

    private void init(){
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        pd = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        user_DAO = new userDAO(getApplicationContext());
    }

    public void getToken(){
        final ServerRequest request = new ServerRequest(3, this);
        request.set_server_url(Constants.SERVER_GET_TOKEN);
        request.setParams(etUserName.getText().toString(), "username");
        request.setParams(etPassword.getText().toString(), "password");
        request.setParams("moodle_mobile_app","service");
        try {
            String req = request.sendPostRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //final ProgressDialog pd = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Authenticating..");
        pd.show();

        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                process_token_response(request);
                Log.i(Constants.LOG_TAG, "LogInActivity Class - OnFinish method triggered");
                //pd.dismiss();
            }

            @Override
            public void onTick(long millisLeft) {}
        };
        timer.start();
    }


    private void process_token_response(ServerRequest request) {
        String response = request.getResponse();
        if (response.equals("")) {
            Toast.makeText(this, "Server timeout", Toast.LENGTH_LONG).show();
        } else {

            Log.i(Constants.LOG_TAG, "Server Response :- " + response);
            JSONObject objResponse = null;
            try {
                objResponse = new JSONObject(response);
                Token.setToken(objResponse.getString("token"));
                Log.i(Constants.LOG_TAG, "Newest Token is :- " + Token.getToken());
                getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void process_user_info_response(ServerRequest request) {
        String response = request.getResponse();
        if (response.equals("")) {
            Toast.makeText(this, "Server timeout", Toast.LENGTH_LONG).show();
        } else {
            Log.i(Constants.LOG_TAG, "Server Response :- " + response.toString());

            details = Utility.XMLPaser(response.toString());
            //Hashmap_keys
            //          sitename
            //          username
            //          firstname
            //          lastname
            //          fullname

            user_DAO.addUser(
                    new User(
                            details.get("firstname"),
                            details.get("lastname"),
                            details.get("fullname"),
                            etUserName.getText().toString(),
                            Token.getToken(),
                            Constants.USER_LOGIN_FLAG
                    )
            );

            getCourseInfo getCourseInfoTask = new getCourseInfo(pd);
            getCourseInfoTask.execute();
        }
    }

    public void getUserInfo(){
        final ServerRequest request = new ServerRequest(2, this);
        request.set_server_url(Constants.SERVER_GET_USER_INFO);
        request.setParams(Token.getToken(), "wstoken");
        request.setParams("moodle_webservice_get_siteinfo", "wsfunction");
        try {
            String req = request.sendPostRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //final ProgressDialog pd = new ProgressDialog(this, R.style.AppTheme);
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Getting details..");
        //pd.show();

        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                process_user_info_response(request);
                Log.i(Constants.LOG_TAG, "LogInActivity Class - OnFinish method triggered");
                //pd.dismiss();
            }

            @Override
            public void onTick(long millisLeft) {}
        };
        timer.start();
    }

    public class getCourseInfo extends AsyncTask<Void,Void,Void>{
        private String username;
        private String password;
        private Elements elements;
        private ProgressDialog pd;
        private CourseDAO course_dao;
        private ArrayList<Course> arrCouseList;

        public getCourseInfo(ProgressDialog pd){
            this.pd = pd;
            course_dao = new CourseDAO(getApplicationContext());
            arrCouseList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            username = etUserName.getText().toString();
            password = etPassword.getText().toString();
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Getting Course Info..");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response response;
                response = Jsoup.connect("https://lms.mrt.ac.lk/login.php")
                        .method(Connection.Method.GET)
                        .execute();

                String session = response.cookie("PHPSESSID");

                response = Jsoup.connect("https://lms.mrt.ac.lk/login.php")
                        .cookie("PHPSESSID",session)
                        .data("LearnOrgUsername",username)
                        .data("LearnOrgPassword",password)
                        .data("LearnOrgLogin","Login")
                        .cookies(response.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                Document document = Jsoup.connect("https://lms.mrt.ac.lk/enrolments.php")
                        .cookie("PHPSESSID",session)
                        .cookies(response.cookies()).get();

                elements = document.getElementsByClass("noramlTableCell");
            } catch (Exception e) {
                Log.i(Constants.LOG_TAG, "Error cought while getting course data :- " + e.toString());
            }

            int i=0;
            Course course = null;
            for (Element element : elements) {
                if (!element.ownText().replace(" ","").equals("")) {
                    if(i==0) {
                        course = new Course(username);
                        String semester = element.ownText().substring(element.ownText().length()-1);
                        Log.i(Constants.LOG_TAG, "Semester :- '" + semester + "'");
                        course.setSemester(semester);
                        i++;
                    }else if(i==1) {
                        Log.i(Constants.LOG_TAG, "Module Code :- " + element.ownText());
                        course.setCourseCode(element.ownText());
                        i++;
                    }else if(i==2) {
                        Log.i(Constants.LOG_TAG, "Module :- " + element.ownText());
                        course.setCourseName(element.ownText());
                        i++;
                    }else if(i==3) {
                        Log.i(Constants.LOG_TAG, "Credits :- " + element.ownText());
                        course.setCredits(element.ownText());
                        arrCouseList.add(course);
                        i=0;
                    }
                }
            }

            course_dao.addCourseList(arrCouseList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            i.putExtra("Values",details);
            startActivity(i);
            Log.i(Constants.LOG_TAG,"Completed SignIn Process");
        }

    }

}
