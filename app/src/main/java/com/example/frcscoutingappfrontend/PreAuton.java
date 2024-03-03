package com.example.frcscoutingappfrontend;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentPreAutonBinding;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreAuton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreAuton extends Fragment {

    FragmentPreAutonBinding binding;
    ArrayList<CharSequence> matchNumbers = new ArrayList<>();
    ArrayList<CharSequence> teamNumbers = new ArrayList<>();
    int numberOfMatches = 66;
    ArrayAdapter<CharSequence> matchNumberAdapter;
    ArrayAdapter<CharSequence> teamNumberAdapter;
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
        data[1] = binding.matchNumberSpinner.getSelectedItem().toString();

        if(binding.leftStart.isChecked()){
            data[2] = binding.leftStart.getText().toString();
        }
        else if(binding.middleStart.isChecked()) {
            data[2] = binding.middleStart.getText().toString();
        }
        else if(binding.rightStart.isChecked()){
            data[2] = binding.rightStart.getText().toString();
        }
        else {
            data[2] = "noShow";
        }
        data[3] = Integer.toString(teamNumbers.indexOf(binding.teamNumberInput.getSelectedItem().toString())+1);
        Toast.makeText(this.getContext(), data[3], Toast.LENGTH_LONG).show();
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
        //adds teams to arraylist (sorry)
        teamNumbers.add("8");
        teamNumbers.add("115");
        teamNumbers.add("399");
        teamNumbers.add("589");
        teamNumbers.add("687");
        teamNumbers.add("691");
        teamNumbers.add("696");
        teamNumbers.add("1138");
        teamNumbers.add("1388");
        teamNumbers.add("1661");
        teamNumbers.add("2375");
        teamNumbers.add("3216");
        teamNumbers.add("3328");
        teamNumbers.add("3476");
        teamNumbers.add("3749");
        teamNumbers.add("3863");
        teamNumbers.add("3881");
        teamNumbers.add("3925");
        teamNumbers.add("3970");
        teamNumbers.add("4014");
        teamNumbers.add("4019");
        teamNumbers.add("4276");
        teamNumbers.add("4414");
        teamNumbers.add("4481");
        teamNumbers.add("4711");
        teamNumbers.add("4738");
        teamNumbers.add("4817");
        teamNumbers.add("5012");
        teamNumbers.add("5136");
        teamNumbers.add("5285");
        teamNumbers.add("5817");
        teamNumbers.add("5835");
        teamNumbers.add("6060");
        teamNumbers.add("6764");
        teamNumbers.add("6885");
        teamNumbers.add("6934");
        teamNumbers.add("6995");
        teamNumbers.add("7323");
        teamNumbers.add("7327");
        teamNumbers.add("8005");
        teamNumbers.add("8060");
        teamNumbers.add("8521");
        teamNumbers.add("8533");
        teamNumbers.add("8768");
        teamNumbers.add("9084");
        teamNumbers.add("9172");
        teamNumbers.add("9421");
        teamNumbers.add("9635");
        teamNumberAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, teamNumbers);
        teamNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.teamNumberInput.setAdapter(teamNumberAdapter);

        //creates spinner for match number
        for(int i = 1; i<=numberOfMatches; i++) {
            matchNumbers.add(Integer.toString(i));
        }
        matchNumberAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, matchNumbers);
        matchNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.matchNumberSpinner.setAdapter(matchNumberAdapter);
        //sets margins for
        redParamsL.setMargins(dpToPixel(50),dpToPixel(1),dpToPixel(20),dpToPixel(1));
        redParamsR.setMargins(dpToPixel(85),dpToPixel(1),0,dpToPixel(1));
        blueParamsL.setMargins(dpToPixel(0),dpToPixel(1),dpToPixel(85),dpToPixel(1));
        blueParamsR.setMargins(dpToPixel(20),dpToPixel(1),dpToPixel(50),dpToPixel(1));
        binding.teamBlue.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_blue));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.middleStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_blue);
            binding.rightStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.middleStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.leftStart.setTextColor(getResources().getColorStateList(R.color.blue_text_toggle));
            binding.leftStart.setText(getResources().getText(R.string.start_amp));
            binding.rightStart.setText(getResources().getText(R.string.start_source));
            binding.leftStart.setLayoutParams(blueParamsL);
            binding.rightStart.setLayoutParams(blueParamsR);
        });
        binding.teamRed.setOnClickListener(view1 -> {
            binding.startingPosImage.setImageDrawable(getResources().getDrawable(R.drawable.frc_2024_field_red));
            binding.leftStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.middleStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.rightStart.setBackgroundResource(R.drawable.start_toggle_red);
            binding.rightStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.middleStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.leftStart.setTextColor(getResources().getColorStateList(R.color.red_text_toggle));
            binding.leftStart.setText(getResources().getText(R.string.start_source));
            binding.rightStart.setText(getResources().getText(R.string.start_amp));
            binding.leftStart.setLayoutParams(redParamsL);
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
        // Fragment transaction on "Next" button
        binding.nextButton.setOnClickListener(view1 -> {
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