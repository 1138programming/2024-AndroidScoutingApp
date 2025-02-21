package com.example.frcscoutingappfrontend;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentPreAutonBinding;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreAuton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  PreAuton extends Fragment {

    FragmentPreAutonBinding binding;
    ArrayList<CharSequence> matchNumbers = new ArrayList<>();
    ArrayList<CharSequence> teamNumbers = new ArrayList<>();
    ArrayList<CharSequence> scouterNames = new ArrayList<>();
    int numberOfMatches = 200;
    ArrayAdapter<CharSequence> matchNumberAdapter;
    ArrayAdapter<CharSequence> teamNumberAdapter;
    ArrayAdapter<CharSequence> scouterAdapter;
    int[] usedScouters = new int[]{
            -1,5,6,10,13,26,29,36,27,15,37,17,38,39,21,40};
    LinkedHashMap<String, Integer> teamNumberIds = new LinkedHashMap<String, Integer>();
    LinkedHashMap<String, Integer> scouterNameIds = new LinkedHashMap<String, Integer>();
    LinearLayout.LayoutParams blueParamsR = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams blueParamsMR = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams blueParamsML = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams blueParamsL = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsR = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsMR = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsML = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams redParamsL = new LinearLayout.LayoutParams(
            97, LinearLayout.LayoutParams.MATCH_PARENT);
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
                (float) dp,
                r.getDisplayMetrics()
        ));

    }

    public void sendTabletInfo() {
        if(!MainActivity.checkConnectivity()) return;
        MainActivity mainActivity = (MainActivity)getActivity();
        String output = binding.scouterNameSpinner.getSelectedItem().toString()+" "+binding.matchNumberSpinner.getSelectedItem().toString()+": "+mainActivity.getDeviceName();
        mainActivity.provideTabletInformation(output.getBytes(StandardCharsets.UTF_8));
    }

    public String[] getDataAsArray() {
        String[] data = new String[5];
        data[0] = Integer.toString(scouterNameIds.get(binding.scouterNameSpinner.getSelectedItem().toString()));
        data[1] = Integer.toString(matchNumbers.indexOf(binding.matchNumberSpinner.getSelectedItem().toString())+1);
        data[2] = Integer.toString(teamNumberIds.get(binding.teamNumberInput.getSelectedItem().toString()));
        if(binding.leftStart.isChecked()){
            data[3] = binding.leftStart.getText().toString();
        }
        else if(binding.middleLeftStart.isChecked()) {
            data[3] = binding.middleLeftStart.getText().toString();
        }
        else if(binding.middleRightStart.isChecked()) {
            data[3] = binding.middleRightStart.getText().toString();
        }      else if(binding.rightStart.isChecked()){
            data[3] = binding.rightStart.getText().toString();
        }
        else {
            data[3] = "noShow";
        }
        if(binding.teamRed.isChecked()) {
            data[4] = "1";
        }
        else {
            data[4] = "2";
        }
        return data;
    }
    public String getFileTitle() {
        String title = binding.scouterNameSpinner.getSelectedItem().toString()
            + " Match #"+binding.matchNumberSpinner.getSelectedItem().toString();
        return title;
    }
    public void setBtStatus(boolean status) {
        if(status) {
            binding.btConnectionStatus.setText(getResources().getString(R.string.bluetooth_connected_status), TextView.BufferType.NORMAL);
            Toast.makeText(this.getContext(), "connected", Toast.LENGTH_LONG).show();
        }
        else {
            binding.btConnectionStatus.setText(getResources().getString(R.string.bluetooth_disconnected_status), TextView.BufferType.NORMAL);
            Toast.makeText(this.getContext(), "disconnected", Toast.LENGTH_LONG).show();
        }
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
        //sets connectivity based on connection after submitted
        if(MainActivity.checkConnectivity()) {
            setBtStatus(true);
        }
        //sorry
        teamNumberIds.put("4",102);
        teamNumberIds.put("359",103);
        teamNumberIds.put("597",104);
        teamNumberIds.put("599",105);
        teamNumberIds.put("687",106);
        teamNumberIds.put("696",107);
        teamNumberIds.put("973",108);
        teamNumberIds.put("980",109);
        teamNumberIds.put("1138",110);
        teamNumberIds.put("1148",111);
        teamNumberIds.put("1197",112);
        teamNumberIds.put("1452",113);
        teamNumberIds.put("1622",114);
        teamNumberIds.put("2102",115);
        teamNumberIds.put("2429",116);
        teamNumberIds.put("2659",117);
        teamNumberIds.put("3128",118);
        teamNumberIds.put("3158",119);
        teamNumberIds.put("3512",120);
        teamNumberIds.put("3759",121);
        teamNumberIds.put("3863",122);
        teamNumberIds.put("3925",123);
        teamNumberIds.put("4079",124);
        teamNumberIds.put("4201",125);
        teamNumberIds.put("4322",126);
        teamNumberIds.put("4414",127);
        teamNumberIds.put("4415",128);
        teamNumberIds.put("4419",129);
        teamNumberIds.put("5199",130);
        teamNumberIds.put("6017",131);
        teamNumberIds.put("6647",132);
        teamNumberIds.put("7042",133);
        teamNumberIds.put("7102",134);
        teamNumberIds.put("7157",135);
        teamNumberIds.put("7415",136);
        teamNumberIds.put("8006",137);
        teamNumberIds.put("9084",138);
        teamNumberIds.put("9408",139);
        teamNumberIds.put("9908",140);
        teamNumberIds.put("9912",141);
        teamNumberIds.put("9914",142);
        teamNumberIds.put("9915",143);
        teamNumberIds.put("9928",144);
        teamNumberIds.put("9938",145);
        teamNumberIds.put("9948",146);
        teamNumberIds.put("9987",147);
        teamNumberIds.put("9996",148);
        teamNumberIds.put("9999",149);

        scouterNameIds.put("Other", -1);
        scouterNameIds.put("Abhinav", 1);
        scouterNameIds.put("Katie", 2);
        scouterNameIds.put("Leo", 3);
        scouterNameIds.put("Luci", 4);
        scouterNameIds.put("Robert B", 5);
        scouterNameIds.put("Seumas", 6);
        scouterNameIds.put("Thomas Gage", 7);
        scouterNameIds.put("Viren", 8);
        scouterNameIds.put("Marcus", 9);
        scouterNameIds.put("Luke B", 10);
        scouterNameIds.put("Joshua D",11);
        scouterNameIds.put("Jonathan",12);
        scouterNameIds.put("Addy", 13);
        scouterNameIds.put("Robert S", 15);
        scouterNameIds.put("Elizabeth", 16);
        scouterNameIds.put("Alec G", 17);
        scouterNameIds.put("Luca", 18);
        scouterNameIds.put("Noah", 19);
        scouterNameIds.put("Jackson", 20);
        scouterNameIds.put("Miles g", 21);
        scouterNameIds.put("Will", 22);
        scouterNameIds.put("Aditya", 23);
        scouterNameIds.put("Trevor", 24);
        scouterNameIds.put("Zoey", 25);
        scouterNameIds.put("David", 26);
        scouterNameIds.put("Jayden", 27);
        scouterNameIds.put("Tony", 28);
        scouterNameIds.put("Breanna", 29);
        scouterNameIds.put("Christine", 31);
        scouterNameIds.put("Mira", 32);
        scouterNameIds.put("Cole", 33);
        scouterNameIds.put("Dylan T", 36);
        scouterNameIds.put("Fife", 37);
        scouterNameIds.put("Zarya", 38);
        scouterNameIds.put("Shoshanna", 39);
        scouterNameIds.put("Sam L", 40);
        //adds team numbers to arrayList
        for(String i : teamNumberIds.keySet()) {
            teamNumbers.add(String.valueOf(i));
        }
        //adds scouter names to arrayList
        scouterNames.addAll(scouterNameIds.keySet());

        scouterAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_layout, scouterNames);
        scouterAdapter.setDropDownViewResource(R.layout.spinner_layout);
        binding.scouterNameSpinner.setAdapter(scouterAdapter);

        teamNumberAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_layout, teamNumbers);
        teamNumberAdapter.setDropDownViewResource(R.layout.spinner_layout);
        binding.teamNumberInput.setAdapter(teamNumberAdapter);

        //creates spinner for match number
        for(int i = 1; i<=numberOfMatches; i++) {
            matchNumbers.add(Integer.toString(i));
        }
        for(int i = 1; i<=100; i++) {
            matchNumbers.add("Practice "+i);
        }
        for(int i = 1; i<=13; i++) {
            matchNumbers.add("Playoffs "+i);
        }
        for(int i = 1; i<=3; i++) {
            matchNumbers.add("Finals "+i);
        }
        matchNumberAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_layout, matchNumbers);
        matchNumberAdapter.setDropDownViewResource(R.layout.spinner_layout);
        binding.matchNumberSpinner.setAdapter(matchNumberAdapter);
        //sets dynamic movement for starting position
        redParamsL.setMargins(dpToPixel(40),dpToPixel(1),dpToPixel(20),dpToPixel(1));
        redParamsML.setMargins(0,dpToPixel(1),dpToPixel(15),dpToPixel(1));
        redParamsMR.setMargins(0,dpToPixel(1),dpToPixel(1),dpToPixel(1));
        redParamsR.setMargins(dpToPixel(10),dpToPixel(1),0,dpToPixel(1));

        blueParamsL.setMargins(0,dpToPixel(1),dpToPixel(10),dpToPixel(1));
        blueParamsML.setMargins(dpToPixel(1),dpToPixel(1),0,dpToPixel(1));
        blueParamsMR.setMargins(dpToPixel(15),dpToPixel(1),0,dpToPixel(1));
        blueParamsR.setMargins(dpToPixel(20),dpToPixel(1),dpToPixel(40),dpToPixel(1));
        binding.teamBlue.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_blue));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.middleRightStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.middleLeftStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.rightStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.middleRightStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.middleLeftStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.leftStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.leftStart.setText(getResources().getText(R.string.start_amp));
            binding.middleLeftStart.setText(getResources().getText(R.string.start_amp_speaker));
            binding.middleRightStart.setText(getResources().getText(R.string.start_source_speaker));
            binding.rightStart.setText(getResources().getText(R.string.start_source));
            binding.leftStart.setLayoutParams(blueParamsL);
            binding.middleLeftStart.setLayoutParams(blueParamsML);
            binding.middleRightStart.setLayoutParams(blueParamsMR);
            binding.rightStart.setLayoutParams(blueParamsR);
        });
        binding.teamRed.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_red));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.middleLeftStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.middleRightStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.rightStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.middleRightStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.middleLeftStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.leftStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.leftStart.setText(getResources().getText(R.string.start_source));
            binding.middleLeftStart.setText(getResources().getText(R.string.start_source_speaker));
            binding.middleRightStart.setText(getResources().getText(R.string.start_amp_speaker));
            binding.rightStart.setText(getResources().getText(R.string.start_amp));
            binding.leftStart.setLayoutParams(redParamsL);
            binding.middleLeftStart.setLayoutParams(redParamsML);
            binding.middleRightStart.setLayoutParams(redParamsMR);
            binding.rightStart.setLayoutParams(redParamsR);
        });
        binding.startingLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.noShowCheckbox.setChecked(false);
        });
        binding.noShowCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(binding.noShowCheckbox.isChecked()) {
                binding.startingLocation.clearCheck();
                binding.noShowCheckbox.setChecked(true);
            }
        });
        binding.scouterNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                    final int position, long id) {
                sendTabletInfo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        binding.matchNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                sendTabletInfo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        //bluetooth button
        binding.archiveButton.setOnClickListener(view1 -> {
            Fragment popup = getParentFragmentManager().findFragmentByTag("H");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(popup);
            ft.commit();
        });
        // Fragment transaction on "Next" button
        binding.nextButton.setOnClickListener(view1 -> {
            sendTabletInfo();
            Fragment self = getParentFragmentManager().findFragmentByTag("A");
            Fragment secondary = getParentFragmentManager().findFragmentByTag("B");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(secondary);
            ft.hide(self);
            ft.commit();
            AutonFragment autonFragment = (AutonFragment) getParentFragmentManager().findFragmentByTag("B");
            autonFragment.openAuton();
        });
    }
}