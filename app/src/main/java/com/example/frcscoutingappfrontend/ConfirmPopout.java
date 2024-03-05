package com.example.frcscoutingappfrontend;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import com.google.zxing.WriterException;

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
    private JSONObject newJsonTemplate(String[] preAutonData) throws JSONException {
        JSONObject datapointTemplate = new JSONObject();

        //creates template for all datapoints
        datapointTemplate.put("scouterID", preAutonData[0]);
        datapointTemplate.put("matchID", preAutonData[1]);
        datapointTemplate.put("teamID", preAutonData[2]);
        return datapointTemplate;
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
            PostMatch postMatch = (PostMatch) getParentFragmentManager().findFragmentByTag("G");
            Fragment popout = getParentFragmentManager().findFragmentByTag("D");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            //gets data from various fragments
            String[] preAutonData = preAuton.getDataAsArray();
            ArrayList<ArrayList<String>> autonData = auton.getDataAsArray();
            ArrayList<ArrayList<String>> teleopData = teleop.getDataAsArray();
            String[] postMatchData = postMatch.getDataAsArray();
            String booleanFalse = "00:00:00";
            try {
                JSONObject jsonFile = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                JSONObject tempJson = new JSONObject();

                //pre-auton json
                tempJson = newJsonTemplate(preAutonData);
                tempJson.put("datapointID", 0);
                tempJson.put("datapointValue", "true");
                tempJson.put("DCTimestamp", preAutonData[3]);
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                //auton json
                tempJson.put("datapointID", 1);
                tempJson.put("datapointValue", "true");
                tempJson.put("DCTimestamp", autonData.get(6).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                for(int i = 0; i<4; i++) {
                    for(String j : autonData.get(i)){
                        tempJson.put("datapointID", i+2);
                        tempJson.put("datapointValue", "true");
                        tempJson.put("DCTimestamp", j);
                        jsonArr.put(tempJson);
                        tempJson = newJsonTemplate(preAutonData);
                    }
                }

                tempJson.put("datapointID", 6);
                if(autonData.get(4).get(0).equals(booleanFalse)) {
                    tempJson.put("datapointValue", "false");
                }
                else {
                    tempJson.put("datapointValue", "true");
                }
                tempJson.put("DCTimestamp", autonData.get(4).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                tempJson.put("datapointID", 7);
                if(autonData.get(5).get(0).equals(booleanFalse)) {
                    tempJson.put("datapointValue", "false");
                }
                else {
                    tempJson.put("datapointValue", "true");
                }
                tempJson.put("DCTimestamp", autonData.get(5).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                //teleop json
                tempJson.put("datapointID", 8);
                tempJson.put("datapointValue", "true");
                tempJson.put("DCTimestamp", teleopData.get(10).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                for(int i = 0; i<7; i++) {
                    for(String j : teleopData.get(i)){
                        tempJson.put("datapointID", i+9);
                        tempJson.put("datapointValue", "true");
                        tempJson.put("DCTimestamp", j);
                        jsonArr.put(tempJson);
                        tempJson = newJsonTemplate(preAutonData);
                    }
                }
                tempJson.put("datapointID", 16);
                if(teleopData.get(7).get(0).equals(booleanFalse)) {
                    tempJson.put("datapointValue", "false");
                }
                else {
                    tempJson.put("datapointValue", "true");
                }
                tempJson.put("DCTimestamp", teleopData.get(7).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                for(int i = 8; i<10; i++) {
                    for(String j : teleopData.get(i)){
                        tempJson.put("datapointID", i+9);
                        tempJson.put("datapointValue", "true");
                        tempJson.put("DCTimestamp", j);
                        jsonArr.put(tempJson);
                        tempJson = newJsonTemplate(preAutonData);
                    }
                }

                //post match
                tempJson.put("datapointID", 19);
                if(postMatchData[0].equals(booleanFalse)) {
                    tempJson.put("datapointValue", "false");
                }
                else {
                    tempJson.put("datapointValue", "true");
                }
                tempJson.put("DCTimestamp", postMatchData[0]);
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                tempJson.put("datapointID", 20);
                if(postMatchData[1].equals(booleanFalse)) {
                    tempJson.put("datapointValue", "false");
                }
                else {
                    tempJson.put("datapointValue", "true");
                }
                tempJson.put("DCTimestamp", postMatchData[1]);
                jsonArr.put(tempJson);

                jsonFile.put("scoutingData", jsonArr);

//                Toast.makeText(getActivity(), Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();

                String userString = jsonFile.toString(4);
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
                postMatch.generateQRCode(jsonFile);
            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            ft.hide(popout);
            ft.commit();
        });

    }
}
