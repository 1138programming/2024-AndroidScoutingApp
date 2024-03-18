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
    private String trapScored = booleanFalseTimestamp;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentPostMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public String[] getDataAsArray() {
        String[] data = new String[3];

        data[0] = successfulHang;
        data[1] = trapScored;
        data[2] = park;

        return data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.trapCheckbox.setOnClickListener(view1 ->{
            if(binding.trapCheckbox.isChecked()) {
                trapScored = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
            }
            else {
                successfulHang = booleanFalseTimestamp;
            }
        });
        binding.successfulHangCheckbox.setOnClickListener(view1 ->{
            if(binding.successfulHangCheckbox.isChecked()) {
                successfulHang = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
            }
            else {
                successfulHang = booleanFalseTimestamp;
            }
        });
        binding.parkCheckbox.setOnClickListener(view1 ->{
            if(binding.successfulHangCheckbox.isChecked()) {
                park = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
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