package com.a14roxgmail.prasanna.mobileapp.Utilities;

import android.app.Activity;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prasanna Deshappriya on 1/16/2017.
 */
public class ServerRequest {
    ArrayList<String> params;
    ArrayList<String> keys;
    String SERVER_URL = "";
    RequestQueue requestQueue;
    String response_msg = "";
    Activity activity;

    public ServerRequest(Activity activity){
        this.activity = activity;
    }

    public ServerRequest(int arg_count, Activity activity){
        params = new ArrayList<String>(arg_count);
        keys = new ArrayList<String>(arg_count);
        this.activity = activity;
    }

    public void set_server_url(String url){
        SERVER_URL = url;
    }

    public void setParams(String param_name, String key){
        params.add(param_name);
        keys.add(key);
    }

    public String sendPostRequest() throws JSONException {
        StringRequest request = new StringRequest(Request.Method.POST, SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setResponse_msg(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setResponse_msg("Network is unreachable");
                        Log.i(Constants.LOG_TAG,"Error caught during request transmission :- "  + error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> paramMap = new HashMap<>();
                for(int i=0; i<params.size(); i++){
                    paramMap.put(keys.get(i),params.get(i));
                    Log.i(Constants.LOG_TAG,"Send Key :- " + keys.get(i) + ", Send Value :- " + params.get(i));
                }
                return paramMap;
            }


        };

        request.setTag(Constants.LOG_TAG);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
        return getResponse();
    }

    public void setResponse_msg(String msg){response_msg = msg;}
    public String getResponse(){return response_msg;}

    public void distroy(){
        requestQueue.cancelAll(Constants.LOG_TAG);
    }
}
