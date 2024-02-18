package com.example.frcscoutingappfrontend;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frcscoutingappfrontend.databinding.FragmentPreAutonBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreAuton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreAuton extends Fragment {

    FragmentPreAutonBinding binding;

    TextView textView;
    ArrayList<String> arrayList;
    Dialog dialog;
    LinearLayout.LayoutParams blueParamsR = new LinearLayout.LayoutParams(
            97,LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams blueParamsL = new LinearLayout.LayoutParams(
            97,LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsR = new LinearLayout.LayoutParams(
            97,LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsL = new LinearLayout.LayoutParams(
            97,LinearLayout.LayoutParams.MATCH_PARENT);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PreAuton() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreAuton.
     */
    // TODO: Rename and change types and number of parameters
    public static PreAuton newInstance(String param1, String param2) {
        PreAuton fragment = new PreAuton();
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

    public int dpToPixel(int dp) {
        Resources r = this.getContext().getResources();
        return ((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (float)dp,
                r.getDisplayMetrics()
        ));

    }
    public String[] getDataAsArray() {
        String[] data = new String[4];

        data[0] = binding.scouterNameInput.getText().toString();
        if(binding.teamBlue.isChecked()) data[1] = "blue";
        else data[1] = "red";
        data[2] = binding.teamNumberInput.getText().toString();
        data[3] = String.valueOf(binding.backupScoutCheckbox.isChecked());

        return data;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentPreAutonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //sets margins for
        redParamsL.setMargins(dpToPixel(50),dpToPixel(1),dpToPixel(20),dpToPixel(1));
        redParamsR.setMargins(dpToPixel(85),dpToPixel(1),0,dpToPixel(1));
//        redParamsL.setMargins(50,1,20,1);
//        redParamsR.setMargins(85,1,0,1);
        blueParamsL.setMargins(dpToPixel(0),dpToPixel(1),dpToPixel(85),dpToPixel(1));
        blueParamsR.setMargins(dpToPixel(20),dpToPixel(1),dpToPixel(50),dpToPixel(1));
        binding.teamBlue.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_blue));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.middleStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.leftStart.setLayoutParams(blueParamsL);
            binding.rightStart.setLayoutParams(blueParamsR);
        });
        binding.teamRed.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_red));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.middleStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.leftStart.setLayoutParams(redParamsL);
            binding.rightStart.setLayoutParams(redParamsR);
        });
//        //creates dropdown for team member text box
//        arrayList = new ArrayList<>();
//
//        arrayList.add("Jonathan Klein");
//        arrayList.add("Jackson Wheatley");
//        arrayList.add("Thomas Gage Evans");
//        arrayList.add("Alyssa Bocanegra");
//
//        binding.scouterNameInput.setOnClickListener(view1 -> {
//            dialog = new Dialog(this.getContext());
//            dialog.setContentView(R.layout.scouter_name_spinner);
//            dialog.getWindow().setLayout(650,800);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(PreAuton.super.getContext(), android.R.layout.simple_list_item_1,arrayList);
//            binding.scouterNameList.setAdapter(adapter);
//            binding.scouterNameInput.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    adapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//            binding.scouterNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    binding.scouterNameInput.setText(adapter.getItem(position));
//
//                    dialog.dismiss();
//                }
//            });
//        });

        // Fragment transaction on "Next" button
        binding.nextButton.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("A");
            Fragment secondary = getParentFragmentManager().findFragmentByTag("B");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(secondary);
            ft.hide(self);
            ft.commit();
        });
    }
}