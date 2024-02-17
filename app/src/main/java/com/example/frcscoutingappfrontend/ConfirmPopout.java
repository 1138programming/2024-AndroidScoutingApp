package com.example.frcscoutingappfrontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



import com.example.frcscoutingappfrontend.databinding.ConfirmPopoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;


public class ConfirmPopout extends Fragment{
    ConfirmPopoutBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<Integer, String> jsonReference = new HashMap<>();
    public ConfirmPopout() {
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
    public static ConfirmPopout newInstance(String param1, String param2) {
        ConfirmPopout fragment = new ConfirmPopout();
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
        this.binding = ConfirmPopoutBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creates the references for creating json data
        jsonReference.put(0,"autonAmpScore");
        jsonReference.put(1,"autonAmpMiss");
        jsonReference.put(2,"autonSpeakerScore");
        jsonReference.put(3,"autonSpeakerMiss");
        jsonReference.put(4,"teleopAmpScore");
        jsonReference.put(5,"teleopAmpMiss");
        jsonReference.put(6,"teleopSpeakerScore");
        jsonReference.put(7,"teleopSpeakerMiss");

        // Hides the popout
        binding.cancelButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("D");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.commit();
        });

        //submits and saves as json
        binding.submitButton.setOnClickListener(view1 -> {
            PreAuton preAuton = (PreAuton) getParentFragmentManager().findFragmentByTag("A");
            AutonFragment auton = (AutonFragment) getParentFragmentManager().findFragmentByTag("B");
            TeleopFragment teleop = (TeleopFragment) getParentFragmentManager().findFragmentByTag("C");
            Fragment popout = getParentFragmentManager().findFragmentByTag("D");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            String[] preAutonData = preAuton.getDataAsArray();
            String[] autonData = auton.getDataAsArray();
            String[] teleopData = teleop.getDataAsArray();

            try {
                JSONObject jsonFile = new JSONObject();
                JSONObject tempJson = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                //pre-auton json
                tempJson.put("scouterName", preAutonData[0]);
                tempJson.put("teamColor", preAutonData[1]);
                tempJson.put("teamNumber", preAutonData[2]);
                tempJson.put("teamNoShow", preAutonData[3]);
                jsonArr.put(tempJson);
                tempJson = new JSONObject();

                //auton json
                tempJson.put("autonSpeakerScored", autonData[0]);
                tempJson.put("autonSpeakerMissed", autonData[1]);
                tempJson.put("autonAmpScored", autonData[2]);
                tempJson.put("autonAmpMissed", autonData[3]);
                tempJson.put("robotLeft", autonData[4]);
                tempJson.put("robotCrossedCenter", autonData[5]);
                jsonArr.put(tempJson);
                tempJson = new JSONObject();

                //teleop json
                tempJson.put("teleopSpeakerScored", teleopData[0]);
                tempJson.put("teleopSpeakerMissed", teleopData[1]);
                tempJson.put("teleopAmpScored", teleopData[2]);
                tempJson.put("teleopAmpMissed", teleopData[3]);
                tempJson.put("robotHung", teleopData[4]);
                tempJson.put("teleopTimeHung", teleopData[7]);
                tempJson.put("trapScored", teleopData[5]);
                tempJson.put("robotBroke", teleopData[6]);
                jsonArr.put(tempJson);

                jsonFile.put("scoutingData", jsonArr);

//                Toast.makeText(getActivity(), Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();

                String userString = jsonFile.toString();
                File folderDir = new File("/data/data/com.example.frcscoutingappfrontend/files/scoutingData");
                //creates the directory if it doesn't exist
                if(!folderDir.isDirectory()) {
                    if(!folderDir.mkdir()) {
                        Toast.makeText(this.getContext(), "Files Broke", Toast.LENGTH_LONG).show();
                    }
                }
                File scoutingFile = new File(folderDir, Calendar.getInstance().getTime()+".json");
                FileWriter fileWriter = new FileWriter(scoutingFile, false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userString);
                bufferedWriter.close();

            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            ft.remove(preAuton);
            ft.remove(auton);
            ft.remove(teleop);
            ft.remove(popout);

            teleop = new TeleopFragment();
            auton = new AutonFragment();
            preAuton = new PreAuton();
            ft.add(R.id.main_fragment, preAuton, "A");
            ft.add(R.id.main_fragment, auton, "B");
            ft.add(R.id.main_fragment, teleop, "C");
            ft.add(R.id.main_fragment, popout, "D");
            ft.show(preAuton);
            ft.hide(auton);
            ft.hide(teleop);
            ft.hide(popout);
            ft.commit();
        });

    }
}
