package com.example.traffisense.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlertViewModel extends AndroidViewModel {
    AppDatabase appDatabase;
    private LiveData<List<AlertModel>> allTodayAlert;
    private LiveData<List<AlertModel>> filteredalerts;
    private List<AlertModel> nonlivedata;
    String myFormat = "yyyy-mm-dd";
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
    public AlertViewModel(@NonNull Application application) {
        super(application);
        appDatabase=AppDatabase.getInstance(application.getApplicationContext());

        allTodayAlert=appDatabase.task_interface().findalltasksbydate(outputDateFormat.format(Calendar.getInstance().getTime()));
        filteredalerts=appDatabase.task_interface().findalltasksbydatenonempty(outputDateFormat.format(Calendar.getInstance().getTime()));
    //    nonlivedata=
      //  nonlivedatafiltered=appDatabase.task_interface().findtodaytask(outputDateFormat.format(Calendar.getInstance().getTime()));
    }


    public LiveData<List<AlertModel>> getAllTodayAlert() {
        return allTodayAlert;
    }
    public LiveData<List<AlertModel>> getTodayAlertFiltered()
    {
        return filteredalerts;

    }
    public List<AlertModel> nonliveData()
    {
        return appDatabase.task_interface().findtodaytask(outputDateFormat.format(Calendar.getInstance().getTime()));
    }
    public List<AlertModel> nonliveFilteredData()
    {
        return appDatabase.task_interface().findtodaytasknonempty(outputDateFormat.format(Calendar.getInstance().getTime()));
    }
}
