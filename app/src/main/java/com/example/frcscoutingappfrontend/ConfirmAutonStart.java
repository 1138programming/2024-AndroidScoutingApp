package com.example.frcscoutingappfrontend;

import  android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.frcscoutingappfrontend.databinding.ConfirmAutonStartBinding;
import com.example.frcscoutingappfrontend.databinding.ConfirmPopoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ConfirmAutonStart extends Fragment{
    ConfirmAutonStartBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ConfirmAutonStart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeleopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmAutonStart newInstance(String param1, String param2) {
        ConfirmAutonStart fragment = new ConfirmAutonStart();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = ConfirmAutonStartBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creates the references for creating json data

        // Hides the popout
        binding.backButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("E");
            Fragment auton = getParentFragmentManager().findFragmentByTag("B");
            Fragment preAuton = getParentFragmentManager().findFragmentByTag("A");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.hide(auton);
            ft.show(preAuton);
            ft.commit();
        });

        //submits and saves as json
        binding.startButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("E");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.commit();
            AutonFragment autonFragment = (AutonFragment) getParentFragmentManager().findFragmentByTag("B");
            autonFragment.startAuton();
        });

    }
}
