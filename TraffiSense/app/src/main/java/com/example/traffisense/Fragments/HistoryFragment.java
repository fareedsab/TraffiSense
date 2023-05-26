package com.example.traffisense.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traffisense.Adapter.AlertAdapter;
import com.example.traffisense.Models.AlertModel;
import com.example.traffisense.Models.Alert_Interface;
import com.example.traffisense.Models.AppDatabase;
import com.example.traffisense.OnSingleClickListener;
import com.example.traffisense.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    AlertAdapter adapter;
    RecyclerView recyclerView;
    List<AlertModel> alertModelList;
    List<AlertModel> alertModelFilterList;

    LinearLayoutManager linearLayoutManager;
    TextView selecteddate;
    private ImageView datefilter;
    AppDatabase db;
    Alert_Interface alert_interface;
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        String currentdate=android.text.format.DateFormat.format("dd MMM", new java.util.Date()).toString()+ " - "+android.text.format.DateFormat.format("dd MMM", new java.util.Date()).toString();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date(Calendar.DATE);

        adapter.updatelist(alert_interface.findtodaytask(outputDateFormat.format(date)));
        if(selecteddate!=null)
        {
        selecteddate.setText(currentdate);}
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();



    }
    CheckBox filter;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        adapter=new AlertAdapter(context,alertModelList);
        alertModelList=new ArrayList<>();
        db = AppDatabase.getInstance(context);
        alert_interface=db.task_interface();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }
    Boolean ischecked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView=view.findViewById(R.id.filter_recycleView);
        datefilter=view.findViewById(R.id.dateCalendar);
        selecteddate=view.findViewById(R.id.dateText);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        filter=view.findViewById(R.id.checkBox);
        filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    ischecked=true;
                    adapter.updatelist(alertModelFilterList);

                }
                else
                {   ischecked=false;
                    adapter.updatelist(alertModelList);
                }
            }
        });
        datefilter.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(materialDatePicker.getShowsDialog())
                {

                    materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");}
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {

                    // start date
                    Calendar start = Calendar.getInstance();
                    start.setTimeInMillis(selection.first);
                    TimeZone timeZoneUTC = TimeZone.getDefault();
                    // It will be negative, so that's the -1
                    int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;

                    // Create a date format, then a date object with our offset
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                    Date date = new Date(selection.first);
                    String startdate=  outputDateFormat.format(date).toString();

                    String enddate= outputDateFormat.format(new Date(selection.second));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        alertModelList=alert_interface.getDataByDate(startdate,enddate);
                        alertModelFilterList=filterlist(alertModelList);
                        if(ischecked)
                        {
                            adapter.updatelist(alertModelFilterList);
                        }
                        else
                        {
                            adapter.updatelist(alertModelList);
                        }

                    }
                    selecteddate.setText(materialDatePicker.getHeaderText());


                    // end date
                    Calendar end = Calendar.getInstance();
                    end.setTimeInMillis(selection.second);
//                        Toast.makeText(TotalSalesScreenNew.this, " "+ selection.first, Toast.LENGTH_SHORT).show();


                });
            }
        });
        return view;
    }
    List<AlertModel> filterlist(List<AlertModel> list)
    {
        List<AlertModel> filterlist = new ArrayList<>();
        for(AlertModel model : list)
        {
            if(!model.getFire().equalsIgnoreCase("0") || !model.getWeopon().equalsIgnoreCase("0")
            || !model.getTraffic().equalsIgnoreCase("0"))
            {
                filterlist.add(model);
            }
        }
        return filterlist;
    }




}