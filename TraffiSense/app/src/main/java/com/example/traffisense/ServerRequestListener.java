package com.example.traffisense;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ServerRequestListener {

    void onPreResponse();
    void onPostResponse(JSONObject jsonResponse, int requestCode);
    void onPostResponse(String response, int requestCode);
    void onErrorResponse(VolleyError error);

}
