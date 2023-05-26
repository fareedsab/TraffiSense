package com.example.traffisense;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traffisense.Utils.Urls;
import com.example.traffisense.Utils.Utils;
import com.example.traffisense.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    public void login_succesfull(View view) {
        if (binding.username.getText().toString().equalsIgnoreCase("")) {
            binding.username.setError("Enter Correct Username");
            return;
        } else if (binding.password.getText().toString().equalsIgnoreCase("")) {
            binding.password.setError("Enter Password");
            return;
        }
//        else if (binding.ngrokURL.getText().toString().isEmpty() && sharedPreferences.getString("ngork","-").equalsIgnoreCase("-"))
//        {
//            binding.ngrokURL.setError("Enter NGROk");
//        }
        else {
            Utils.showProgressDialog(this,"Please Wait...");
            Map<String, String> params = new HashMap<String, String>();
//            if(!binding.ngrokURL.getText().toString().isEmpty()) {
//                SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                myEdit.putString("NGROK", binding.ngrokURL.getText().toString());
//                myEdit.commit();
//
//
//
//            }
            params.put("userName",binding.username.getText().toString());
            params.put("password",binding.password.getText().toString());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName",binding.username.getText().toString());
                    params.put("password",binding.password.getText().toString());
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    String url = Urls.Get_LOGIN_URL;
                    Uri.Builder builder = Uri.parse(url).buildUpon();
                    builder.appendQueryParameter("userName", binding.username.getText().toString());
                    builder.appendQueryParameter("password", binding.password.getText().toString());
                    String url1 = builder.build().toString();


// Request a string response from the provided URL.
//                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, binding.ngrokURL.getText().toString()+"/api/User/LoginUser", new JSONObject(params), new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Log.d("TAG", "onResponse: "+response.toString());
//                                if(response.has("httpStatusCode"))
//                                {
//                                    if(response.getString("httpStatusCode").equalsIgnoreCase("200"))
//                                    {
//                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            Utils.hideProgressDialog();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.d("TAG", "onError: "+error.toString());
//                            Utils.hideProgressDialog();
//                        }
//                    });
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Log.d("TAG", "onResponse: "+response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(jsonObject.has("httpStatusCode"))
                                        {
                                            if(jsonObject.getString("httpStatusCode").equalsIgnoreCase("200"))
                                            {
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                myEdit.putString("IsLogin", "true");
                                                myEdit.commit();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    Utils.hideProgressDialog();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", "onError: "+error.toString());
                            Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();

                            Utils.hideProgressDialog();
                        }
                    });
                    int socketTimeout = 360000;
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            socketTimeout,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
                    queue.add(stringRequest);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            },1000);

        }
    }


}