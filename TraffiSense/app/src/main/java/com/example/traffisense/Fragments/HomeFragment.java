package com.example.traffisense.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.traffisense.Adapter.AlertAdapter;
import com.example.traffisense.Models.AlertModel;
import com.example.traffisense.Models.AlertViewModel;
import com.example.traffisense.Models.Alert_Interface;
import com.example.traffisense.Models.AppDatabase;
import com.example.traffisense.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
//    public HomeFragment(Context context)
//    {
//        alertViewModel= new ViewModelProvider(context).get(AlertModel.class);
//    }
    AlertAdapter adapter;
    RecyclerView recyclerView;
    List<AlertModel> alertModelList;
    LinearLayoutManager linearLayoutManager;
    AlertViewModel alertViewModel;
    AppDatabase db;
    Alert_Interface alert_interface;
    CheckBox filter;
    Boolean ischecked = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alertViewModel = new ViewModelProvider(getActivity()).get(AlertViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        alertModelList=new ArrayList<>();


        db = AppDatabase.getInstance(context);
        alert_interface=db.task_interface();

        adapter=new AlertAdapter(context,alertModelList);
        alertViewModel=ViewModelProviders.of((FragmentActivity) context).get(AlertViewModel.class);
        alertViewModel.getAllTodayAlert().observe((LifecycleOwner) context, new Observer<List<AlertModel>>() {
            @Override
            public void onChanged(List<AlertModel> alertModels) {
                if(!ischecked)
                {
                    Log.d("TAG", "onChanged: "+ischecked+alertModels.size());
                adapter.updatelist(alertModels);
                }
            }
        });
        alertViewModel.getTodayAlertFiltered().observe((LifecycleOwner)context, new Observer<List<AlertModel>>() {
            @Override
            public void onChanged(List<AlertModel> alertModels) {
                if(ischecked)
                {
                    Log.d("TAG", "onChanged: "+ischecked + alertModels.size());
                    adapter.updatelist(alertModels);
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        filter=view.findViewById(R.id.checkBox);
        filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    ischecked=true;
                   adapter.updatelist(alertViewModel.nonliveFilteredData());

                }
                else
                {   ischecked=false;
                    adapter.updatelist(alertViewModel.nonliveData());
                }
            }
        });
        return view;
    }
}