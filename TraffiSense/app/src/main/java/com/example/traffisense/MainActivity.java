package com.example.traffisense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffisense.Fragments.HistoryFragment;
import com.example.traffisense.Fragments.HomeFragment;
import com.example.traffisense.Models.AppDatabase;
import com.example.traffisense.Worker.MyWorker;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
   //  DrawerLayout drawer;
    ImageView imageView;
    //NavigationView navigationView;
    TextView name;
    public static boolean workerid;
    public static boolean notificationStatus=false;
    WorkManager workManager;
    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        setWorker();
        notificationStatus=false;
      //  navigationView.setCheckedItem(R.id.home_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HomeFragment()).commit();
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.home_nav:
//                        name.setText("Welcome");
//                        Toast.makeText(MainActivity.this, "events_nav", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HomeFragment()).commit();
//                        break;
//
//                    case R.id.history_nav:
//                        name.setText("Welcome");
//                        Toast.makeText(MainActivity.this, "events_nav", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HistoryFragment()).commit();
//                        break;
//                    case R.id.logout_nav    :
////                        studentactivity.this.deleteDatabase("/data/data/com.example.societyclubportal/databases/database");
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        Toast.makeText(MainActivity.this, "logout_nav", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                drawer.closeDrawer(Gravity.RIGHT);
//                return true;
//            }
//        });


    }

    private void setWorker() {
        workerid=true;
        //Data data = new Data.Builder().putString("ngrok", getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("ngrok","-")).build();
        workManager = WorkManager.getInstance(this);
        OneTimeWorkRequest startServiceRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        //  workerid=startServiceRequest.getId();
        if (!MyWorker.isworkeractive) {

            Log.d("Worker", "onCreate: done ");


            workManager.enqueue(startServiceRequest);
            startServiceRequest.getId();
        }
    }

//    public void drawertoggleonclick(View view) {
//        try {
//            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
//                drawer.closeDrawer(Gravity.RIGHT);
//            } else {
//                drawer.openDrawer(Gravity.RIGHT);
//                //   Toast.makeText(MainActivity.this, "Gravity.LEFT", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Log.d("TAG", "onClick: " + e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }
    public void initview()
    {
      //  drawer = findViewById(R.id.studentdrawerlayout1);
    //    imageView=findViewById(R.id.imageView);
      //  navigationView = findViewById(R.id.navigationview1);
       // View headerView = navigationView.getHeaderView(0);
        name=findViewById(R.id.name);
        notificationManager = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);


    }

    @Override
    protected void onPause() {
        super.onPause();
        notificationStatus=true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationStatus=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationStatus=false;
    }

    public void homebtnPressed(View view) {

        name.setText("Welcome");
                        Toast.makeText(MainActivity.this, "events_nav", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HomeFragment()).commit();

    }

    public void historybtnPressed(View view) {
        Toast.makeText(MainActivity.this, "events_nav", Toast.LENGTH_SHORT).show();
          getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HistoryFragment()).commit();
    }

    public void logoutbtnPressed(View view) {
        workManager.cancelAllWork();
        MyWorker.isworkeractive=false;
        AppDatabase.getInstance(this).task_interface().deleteall();
        notificationManager.cancelAll();
        SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("IsLogin", "false");
        myEdit.commit();
        Intent intent= new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();





    }
}