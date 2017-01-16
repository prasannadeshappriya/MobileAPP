package com.a14roxgmail.prasanna.mobileapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.VolleyRequest.ServerRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {
    private EditText etUserName;
    private EditText etPassword;
    private Button btnSignIn;

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
            getToken();
        }
    }

    public boolean Validate(){
        //Validate email address and password
        return true;
    }

    private void init(){
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
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

        final ProgressDialog pd = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Authenticating..");
        pd.show();

        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                process_token_response(request);
                Log.i(Constants.LOG_TAG, "LogInActivity Class - OnFinish method triggered");
                pd.dismiss();
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
            HashMap<String,String> details = XMLPaser(response.toString());
            //Hashmap_ids
            //          sitename
            //          username
            //          firstname
            //          lastname
            //          fullname
            Log.i(Constants.LOG_TAG, details.get("fullname"));
            HomeActivity home = new HomeActivity();
            Intent i = new Intent(this,HomeActivity.class);
            i.putExtra("Values",details);
            this.finish();
            startActivity(i);
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

        final ProgressDialog pd = new ProgressDialog(this, R.style.AppTheme);
        pd.setIndeterminate(true);
        pd.setMessage("Authenticating..");
        pd.show();

        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                process_user_info_response(request);
                Log.i(Constants.LOG_TAG, "LogInActivity Class - OnFinish method triggered");
                pd.dismiss();
            }

            @Override
            public void onTick(long millisLeft) {}
        };
        timer.start();
    }

    private HashMap<String,String> XMLPaser(String response){
        HashMap<String,String> map = new HashMap();
        int count = 0;
        boolean con = false;
        for(int i=0; i<response.length(); i++){
            String tmp1 = response.substring(i,i+8).replace(" ","").toLowerCase();
            if (tmp1.equals("keyname")){
                String key="";
                String value="";
                boolean key_or_value = true; //true for key
                for(int j=0; j<response.length(); j++){
                    String ret2 = response.substring(i+j+8,i+j+9);
                    if(ret2.equals(">")){
                        if(key_or_value) {
                            key = response.substring(i + 10, i + 7 + j);
                            Log.i(Constants.LOG_TAG, "Key :- " + key);
                            key_or_value = false;
                        }else{
                            String tmp3 = response.substring(i+j+8);
                            for(int k=0; k<response.length(); k++){
                                ret2 = tmp3.substring(k,k+1);
                                if(ret2.equals("<")){
                                    value = response.substring(i+j+9, i+j+k+8);
                                    Log.i(Constants.LOG_TAG, "Value :- " + value);
                                    break;
                                }
                            }
                            map.put(key,value);
                            break;
                        }
                    }
                }
                count++;
                if(count>=5){con = true;}
            }
            if(con){break;}
        }
        return map;
    }
}
