package com.example.frcscoutingappfrontend;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentPostMatchBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostMatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostMatch extends Fragment {

    FragmentPostMatchBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String booleanFalseTimestamp = "00-00-0000 00:00:00";
    private ArrayList<String> trapScored = new ArrayList<String>();
    private String successfulHang = booleanFalseTimestamp;
    private String park = booleanFalseTimestamp;
    ArrayList<Bitmap> bitmap = new ArrayList<Bitmap>();
    private int currQRIndex = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostMatch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostMatch.
     */
    // TODO: Rename and change types and number of parameters
    public static PostMatch newInstance(String param1, String param2) {
        PostMatch fragment = new PostMatch();
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
    private void incrementTrap() {
        String text = binding.trapScoredButton.getText().toString();
        int num = Integer.parseInt(text);
        if (num < 100) {
            num++;
        }
        binding.trapScoredButton.setText(String.valueOf(num));
    }
    private void decrementTrap() {
        String text = binding.trapScoredButton.getText().toString();
        int num = Integer.parseInt(text);
        if (num > 0) {
            num--;
        }
        binding.trapScoredButton.setText(String.valueOf(num));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentPostMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public ArrayList<ArrayList<String>> getDataAsArray() {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>(3);
        for(int i = 0; i<3; i++) {
            data.add(i, new ArrayList<String>());
        }

        data.get(0).add(successfulHang);
        for(String i: trapScored) {
            data.get(1).add(i);
        }
        data.get(2).add(park);

        return data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.trapScoredButton.setOnClickListener(view1 ->{
            trapScored.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            incrementTrap();
        });
        binding.subtractTrapButton.setOnClickListener(view1 -> {
            decrementTrap();
        });
        binding.successfulHangCheckbox.setOnClickListener(view1 ->{
            if(binding.successfulHangCheckbox.isChecked()) {
                successfulHang = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }
            else {
                successfulHang = booleanFalseTimestamp;
            }
        });
        binding.parkCheckbox.setOnClickListener(view1 ->{
            if(binding.successfulHangCheckbox.isChecked()) {
                park = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }
            else {
                park = booleanFalseTimestamp;
            }
        });
        binding.returnToTeleop.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("G");
            Fragment teleop = getParentFragmentManager().findFragmentByTag("C");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(self);
            ft.show(teleop);
            ft.commit();
        });
        binding.submitButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            Fragment popoutFragment = getParentFragmentManager().findFragmentByTag("D");
            ft.show(popoutFragment);
            ft.commit();
        });
    }
}