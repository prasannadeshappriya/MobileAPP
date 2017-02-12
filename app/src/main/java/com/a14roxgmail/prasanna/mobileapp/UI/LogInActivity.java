package com.a14roxgmail.prasanna.mobileapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import com.a14roxgmail.prasanna.mobileapp.DAO.SettingsDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Fragment.CalendarFragment;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.Setting;
import com.a14roxgmail.prasanna.mobileapp.Model.User;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.EncryptPass;
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

        try {
            //Check user already login before
            User user = user_DAO.getSignInUserDetails();
            if (user != null) {
                autoLoginUser(user);
            }
        }catch (Exception e){
            Log.i(Constants.LOG_TAG,"Error on start :- " + e.toString());
        }

        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etUserName.setText(etUserName.getText().toString().toUpperCase());
                        SignIn();
                    }
                }
        );

    }

    public void SignIn(){
        if(Validate()){
            if(user_DAO.isUserExist(etUserName.getText().toString())){
                Log.i(Constants.LOG_TAG,"User " + etUserName.getText().toString() + " is exist on the database");

                User user = user_DAO.getUser(etUserName.getText().toString());
                details = new HashMap<>();

                //Hashmap_keys
                //          username
                //          firstname
                //          lastname
                //          fullname
                //          password
                details.put("username", user.getUserIndex());
                details.put("firstname", user.getFirstName());
                details.put("lastname", user.getLastName());
                details.put("fullname", user.getFullName());
                details.put("password", user.getPassword());


                if (etPassword.getText().toString().equals(details.get("password"))) {
                    user_DAO.updateLoginStatus(Constants.USER_LOGIN_FLAG,etUserName.getText().toString());
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("Values", details);
                    this.finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    Log.i(Constants.LOG_TAG, "Completed SignIn Process");
                } else {
                    Toast.makeText(this, "Invalid login details !", Toast.LENGTH_LONG).show();
                    Log.i(Constants.LOG_TAG, "SignIn process terminated due to invalid details");
                }

            }else {
                Log.i(Constants.LOG_TAG, "User " + etUserName.getText().toString() + " is not exist on the database");
                CheckInternetAccess checkInternetAccess = new CheckInternetAccess();
                checkInternetAccess.execute();
            }
        }
    }

    private class CheckInternetAccess extends AsyncTask<Void,Void,Void>{
        private boolean con = false;
        @Override
        protected void onPostExecute(Void aVoid) {
            if(con) {
                Log.i(Constants.LOG_TAG, "Internet connection available");
                getToken();
            }else{
                Log.i(Constants.LOG_TAG, "No internet connection available");
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"No internet connection !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Connecting..");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Ping is not working for emulator
            //Check weather the app is running on emulator or not
            if(Build.PRODUCT.matches(".*_?sdk_?.*")){
                con = true;
            }else {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    con = (exitValue == 0);
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "Error :- " + e.toString());
                    con = false;
                }
            }
            return null;
        }
    }

    public void autoLoginUser(User user){
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
        details.put("password", user.getPassword());

        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("Values", details);
        this.finish();
        startActivity(i);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
        Log.i(Constants.LOG_TAG, "Completed SignIn Process");
    }

    public boolean Validate(){
        //Validate email address and password
        if(etUserName.getText().toString().replace(" ","").equals("")){
            etUserName.setError("Invalid user index");
            return false;
        }
        if(etPassword.getText().toString().replace(" ","").equals("")){
            etPassword.setError("Invalid password");
            return false;
        }
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

        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Authenticating..");

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
            pd.dismiss();
        } else {

            Log.i(Constants.LOG_TAG, "Server Response :- " + response);
            JSONObject objResponse = null;
            try {
                objResponse = new JSONObject(response);
                if (objResponse.has("error")){
                    Log.i(Constants.LOG_TAG, "Error :- " + objResponse.getString("error"));
                    Toast.makeText(getApplicationContext(),"Invalid login details",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }else {
                    if(objResponse.has("token")){
                        Token.setToken(objResponse.getString("token"));
                        Log.i(Constants.LOG_TAG, "Newest Token is :- " + Token.getToken());
                        getUserInfo();
                    }else{
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void process_user_info_response(ServerRequest request){
        String response = request.getResponse();
        if (response.equals("")) {
            Toast.makeText(this, "Server timeout", Toast.LENGTH_LONG).show();
            pd.dismiss();
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
                            Constants.USER_LOGIN_FLAG,
                            EncryptPass.encrypt(etPassword.getText().toString())
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
        private SettingsDAO settings_dao;
        private ArrayList<Course> arrCouseList;

        public getCourseInfo(ProgressDialog pd){
            this.pd = pd;
            course_dao = new CourseDAO(getApplicationContext());
            settings_dao = new SettingsDAO(getApplicationContext());
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
                response = Jsoup.connect(Constants.LMS_LOGIN_URL)
                        .method(Connection.Method.GET)
                        .execute();

                String session = response.cookie(Constants.LMS_COOKIE_ID);

                response = Jsoup.connect(Constants.LMS_LOGIN_URL)
                        .cookie(Constants.LMS_COOKIE_ID,session)
                        .data(Constants.LMS_USERNAME_ID,username)
                        .data(Constants.LMS_PASSWORD_ID,password)
                        .data(Constants.LMS_LOGIN_ID,Constants.LMS_LOGIN_ID_VALUE)
                        .cookies(response.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                Document document = Jsoup.connect(Constants.LMS_ENROLMENTS_URL)
                        .cookie(Constants.LMS_COOKIE_ID,session)
                        .cookies(response.cookies()).get();

                elements = document.getElementsByClass(Constants.LMS_SOURCE_TAG);
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
            settings_dao.addSettings(new Setting(
                    username,
                    "0",
                    Utility.getCurrentDate()
            ));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            i.putExtra("Values",details);
            killLoginPage();
            startActivity(i);
            Log.i(Constants.LOG_TAG,"Completed SignIn Process");
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        }

    }

    private void killLoginPage(){
        this.finish();
    }

}
