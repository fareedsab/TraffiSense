package com.example.traffisense.Worker;

import static android.content.Context.MODE_PRIVATE;
import static com.example.traffisense.MainActivity.notificationStatus;
import static com.example.traffisense.MainActivity.workerid;
import static com.example.traffisense.Utils.Urls.getDetections;
import static com.example.traffisense.Utils.Urls.getcurrentDetections;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traffisense.Applications;
import com.example.traffisense.LoginActivity;
import com.example.traffisense.MainActivity;
import com.example.traffisense.Models.AlertModel;
import com.example.traffisense.Models.Alert_Interface;
import com.example.traffisense.Models.AppDatabase;
import com.example.traffisense.R;
import com.example.traffisense.SplashActivity;
import com.example.traffisense.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.content.SharedPreferences;
public class MyWorker extends Worker {
    List<AlertModel> alerts;
    String channel_id = "notification_channel2";
    Context context;
    SharedPreferences sharedPreferences;
    NotificationManager notificationManager1;
    public static boolean isworkeractive = false;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat outputTimeFormat = new SimpleDateFormat("h:mm:ss a");
    Calendar c = Calendar.getInstance();
    AppDatabase db;
    Alert_Interface task_interface;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        sharedPreferences=context.getSharedPreferences("MySharedPref", MODE_PRIVATE);


    }

    @NonNull
    @Override
    public Result doWork() {
        isworkeractive=true;

        Log.d("TAG", "starting service from doWork");
        Log.d("TAG", "doWork called for: " + this.getId());

        db=AppDatabase.getInstance(context);
        task_interface=db.task_interface();
     //   workerkaam();

        if(haveNetworkConnection())
        {
            if(isworkeractive && workerid)
            {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, getDetections , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "onResponse: "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.has("httpStatusCode"))
                            {
                                if(jsonObject.getString("httpStatusCode").equalsIgnoreCase("200"))
                                {
                                    JSONArray dataArray = new JSONObject(response).getJSONArray("data");
                                    List<AlertModel> dataList = new ArrayList<>();
                                    Log.d("TAG", "onDataResponse: "+dataArray.toString());

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);
                                        String id = dataObject.getString("id");
                                        int isSynced = dataObject.getInt("isSynced");
                                        String fire = String.valueOf(dataObject.getInt("fire"));
                                        String traffic = String.valueOf(dataObject.getInt("traffic"));
                                        String weapon = String.valueOf(dataObject.getInt("weapon"));
                                        String timeAndDate = dataObject.getString("timeAndDate");

                                        Date date = inputFormat.parse(timeAndDate);
                                        String formattedDate = outputDateFormat.format(date);
                                        String formattedTime = outputTimeFormat.format(date);
                                        if(!db.task_interface().is_exist(formattedDate,formattedTime)) {
                                            db.task_interface().insert(new AlertModel(formattedDate, formattedTime, fire, traffic, weapon, isSynced));
                                        }
                                        else {
                                            AlertModel alertModel = task_interface.getAlertBytimeDate(formattedDate,formattedTime);
                                            if(!traffic.equals(alertModel.getTraffic()) || !weapon.equalsIgnoreCase(alertModel.getWeopon()) || !fire.equals(alertModel.getFire()) )
                                            {
                                                Log.d("TAG", "onUpdate: "+alertModel.toString());
                                                task_interface.update(fire,weapon,traffic,0,alertModel.getId());}
                                        }
                                        }

                                }
                            }

                        } catch (JSONException e) {
                            Log.d("TAG", "onResponse: "+e.getLocalizedMessage());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: "+error.getLocalizedMessage());

                    }
                });
                int socketTimeout = 360000;
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        socketTimeout,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }

        }
        try {

            if(!notificationStatus)
            {
                Calendar myCalendar=Calendar.getInstance();
                alerts=task_interface.findtodaytask(outputDateFormat.format(myCalendar.getTime()));

                if(alerts.size()!=0)
                {
                    for(AlertModel alertModel : alerts)
                    {
                        if(alertModel.getIsnotified()==0)
                        { String message = alertModel.getAlertdate()+" "+alertModel.getAlerttime();
                            Log.d("TAG", "doWork: "+alertModel.getAlerttime());
                            if(!alertModel.getFire().equalsIgnoreCase("0") || !alertModel.getTraffic().equalsIgnoreCase("0")
                                    || !alertModel.getWeopon().equalsIgnoreCase("0"))
                            {
                                message=message+" Anomaly Detected in this timestamp";
                                showNotification(context,"Traffi Sense Alert",message,alertModel.getId());
                                task_interface.updatenotify(alertModel.getId(),1);
                                Log.d("update", "doWork: "+"update");
                            }
                        }
                    }

                }

            }
        }catch (Exception e)
        {
            Log.d("TAG", "doWork: "+e.getLocalizedMessage());
        }
        final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("simple_work")
                .build();
        WorkManager.getInstance().enqueue(simpleRequest);
return Result.success();
    }

    private void workerkaam() {
        try {
            Log.d("TAG", "workerkaam: wowwww");
// Get the resource ID for your JSON file
            int resourceId = context.getResources().getIdentifier("response", "raw", context.getPackageName());

// Open an InputStream to the file
            InputStream inputStream = context.getResources().openRawResource(resourceId);

// Read the contents of the file into a String
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

// Close the InputStream and BufferedReader
            inputStream.close();
            reader.close();

// Parse the JSON string
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            Log.d("TAG", "workerkaam: "+jsonString.toString());

//            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("httpStatusCode"))
            {
                if(jsonObject.getString("httpStatusCode").equalsIgnoreCase("200"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    List<AlertModel> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String id = dataObject.getString("id");
                        int isSynced = dataObject.getInt("isSynced");
                        String fire = String.valueOf(dataObject.getInt("fire"));
                        String traffic = String.valueOf(dataObject.getInt("traffic"));
                        String weapon = String.valueOf(dataObject.getInt("weapon"));
                        String timeAndDate = dataObject.getString("timeAndDate");

                        Date date = inputFormat.parse(timeAndDate);
                        String formattedDate = outputDateFormat.format(date);
                        String formattedTime = outputTimeFormat.format(date);
                        if(!db.task_interface().is_exist(formattedDate,formattedTime)) {
                            db.task_interface().insert(new AlertModel(formattedDate, formattedTime, fire, traffic, weapon, isSynced));
                        }
                        else {
                            AlertModel alertModel = task_interface.getAlertBytimeDate(formattedDate,formattedTime);
                            if(!traffic.equals(alertModel.getTraffic()) || !weapon.equalsIgnoreCase(alertModel.getWeopon()) || !fire.equalsIgnoreCase(alertModel.getFire()) )
                            {task_interface.update(fire,weapon,traffic,0,alertModel.getId());}
                        }
                    }

                }
            }

        } catch (JSONException e) {
            Log.d("TAG", "onResponse: "+e.getLocalizedMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;


        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public void showNotification(Context co, String title, String message, int id) {
        // Pass the intent to switch to the MainActivity

        Intent intent = new Intent(co, SplashActivity.class);
        // Assign channel ID

        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(co,
                    0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(co,
                    0, intent, 0);
        }


        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(Applications.getAppContext(),
                channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);


        notificationManager1 = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager1.createNotificationChannel(notificationChannel);
        }
        notificationManager1.notify(id, builder.build());
    }
}
