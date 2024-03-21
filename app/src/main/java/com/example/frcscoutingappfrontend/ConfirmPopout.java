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
import android.widget.TextView;

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
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

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
        // red = 1
        // blue = 2
        datapointTemplate.put("allianceID", preAutonData[4]);
        return datapointTemplate;
    }
    private String normalizeTimestamps(String start, String oldTimestamp) {
        String newTimestamp;
        StringBuilder sb = new StringBuilder();
        sb.append(oldTimestamp.substring(0,11));
        sb.append(subtractStrings(oldTimestamp.substring(11,13),start.substring(11,13)));
        sb.append(":");
        sb.append(subtractStrings(oldTimestamp.substring(14,16),start.substring(14,16)));
        sb.append(":");
        sb.append(subtractStrings(oldTimestamp.substring(17),start.substring(17)));
        newTimestamp = sb.toString();
        return newTimestamp;
    }
    private String subtractStrings(String s1, String s2) {
        String returnString;
        int value = Integer.parseInt(s1)-Integer.parseInt(s2);
        if(value < 0) value = 0;
        if(value<10) returnString = "0"+String.valueOf(value);
        else returnString = String.valueOf(value);

        return returnString;
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
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            PreAuton preAuton = (PreAuton) getParentFragmentManager().findFragmentByTag("A");
            AutonFragment auton = (AutonFragment) getParentFragmentManager().findFragmentByTag("B");
            TeleopFragment teleop = (TeleopFragment) getParentFragmentManager().findFragmentByTag("C");
            PostMatch postMatch = (PostMatch) getParentFragmentManager().findFragmentByTag("G");
            //gets data from various fragments
            String[] preAutonData = preAuton.getDataAsArray();
            ArrayList<ArrayList<String>> autonData = auton.getDataAsArray();
            ArrayList<ArrayList<String>> teleopData = teleop.getDataAsArray();
            ArrayList<ArrayList<String>> postMatchData = postMatch.getDataAsArray();
            String booleanFalse = "0000-00-00 00:00:00";
            try {
                JSONObject jsonFile = new JSONObject();
                JSONArray jsonArr = new JSONArray();
                JSONObject tempJson = newJsonTemplate(preAutonData);

                /*auton scoring
                * 1 = autonSpeakerScore
                * 2 = autonAmpScore
                * 3 = autonSpeakerMiss
                * 4 = autonAmpMiss
                * */
                for(int i = 0; i<4; i++) {
                    for(String j : autonData.get(i)){
                        tempJson.put("datapointID", String.valueOf(i+1));
                        tempJson.put("DCValue", "true");
                        tempJson.put("DCTimestamp", normalizeTimestamps(autonData.get(6).get(0),j));
                        jsonArr.put(tempJson);
                        tempJson = newJsonTemplate(preAutonData);
                    }
                }

                // 5 = cross center line
                tempJson.put("datapointID", String.valueOf(5));
                if(autonData.get(5).get(0).equals(booleanFalse)) {
                    tempJson.put("DCValue", "false");
                    tempJson.put("DCTimestamp", autonData.get(5).get(0));
                }
                else {
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(autonData.get(6).get(0),autonData.get(5).get(0)));
                }
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                // 6 = taxi
                tempJson.put("datapointID", String.valueOf(6));
                if(autonData.get(4).get(0).equals(booleanFalse)) {
                    tempJson.put("DCValue", "false");
                    tempJson.put("DCTimestamp", autonData.get(4).get(0));
                }
                else {
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(autonData.get(6).get(0),autonData.get(4).get(0)));
                }
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                /* teleop scores/misses and amplify
                * 7 = teleopSpeakerScore
                * 8 = teleopAmpScore
                * 9 = teleopSpeakerMiss
                * 10 = teleopAmpMiss
                * 11 = teleopAmplify (no longer on tablets)
                * */
                for(int i = 0; i<5; i++) {
                    for(String j : teleopData.get(i)){
                        tempJson.put("datapointID", String.valueOf(i+7));
                        tempJson.put("DCValue", "true");
                        tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),j));
                        jsonArr.put(tempJson);
                        tempJson = newJsonTemplate(preAutonData);
                    }
                }

                // 12 = successful hang
                tempJson.put("datapointID", String.valueOf(12));
                if(postMatchData.get(0).get(0).equals(booleanFalse)) {
                    tempJson.put("DCValue", "false");
                }
                else {
                    tempJson.put("DCValue", "true");
                }
                tempJson.put("DCTimestamp", postMatchData.get(0).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                // 13 = hang start
                if(teleopData.get(5).size() != 0) {
                    tempJson.put("datapointID", String.valueOf(13));
                    if (teleopData.get(5).get(0).equals(booleanFalse)) {
                        tempJson.put("DCValue", "false");
                        tempJson.put("DCTimestamp", teleopData.get(5).get(0));
                    } else {
                        tempJson.put("DCValue", "true");
                        tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),teleopData.get(5).get(0)));
                    }
                    jsonArr.put(tempJson);
                    tempJson = newJsonTemplate(preAutonData);
                }

                // 14 = trap
                for(String i : postMatchData.get(1)) {
                    tempJson.put("datapointID", String.valueOf(14));
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", i);
                    jsonArr.put(tempJson);
                    tempJson = newJsonTemplate(preAutonData);
                }

                // 15 = defense (start)
                for(String j : teleopData.get(9)){
                    tempJson.put("datapointID", String.valueOf(15));
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),j));
                    jsonArr.put(tempJson);
                    tempJson = newJsonTemplate(preAutonData);
                }

                // 16 = break
                tempJson.put("datapointID", String.valueOf(16));
                if(teleopData.get(8).get(0).equals(booleanFalse)) {
                    tempJson.put("DCValue", "false");
                    tempJson.put("DCTimestamp", teleopData.get(8).get(0));
                }
                else {
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),teleopData.get(8).get(0)));
                }
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                // 17 = position on field
                tempJson = newJsonTemplate(preAutonData);
                tempJson.put("datapointID", String.valueOf(17));
                tempJson.put("DCValue", preAutonData[3]);
                tempJson.put("DCTimestamp", booleanFalse);
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);


                // 18 = auton start
                tempJson = newJsonTemplate(preAutonData);
                tempJson.put("datapointID", String.valueOf(18));
                tempJson.put("DCValue", "true");
                tempJson.put("DCTimestamp", autonData.get(6).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                //19 = teleop start
                tempJson.put("datapointID", String.valueOf(19));
                tempJson.put("DCValue", "true");
                tempJson.put("DCTimestamp", teleopData.get(11).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);

                // 20 = pickup
                for(String i : teleopData.get(6)) {
                    tempJson.put("datapointID", String.valueOf(20));
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),i));
                    jsonArr.put(tempJson);
                }
                tempJson = newJsonTemplate(preAutonData);

                // 21 = defense end
                for(String j : teleopData.get(10)){
                    tempJson.put("datapointID", String.valueOf(21));
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),j));
                    jsonArr.put(tempJson);
                    tempJson = newJsonTemplate(preAutonData);
                }

                // 22 = park
                tempJson.put("datapointID", String.valueOf(22));
                if(postMatchData.get(2).get(0).equals(booleanFalse)) {
                    tempJson.put("DCValue", "false");
                }
                else {
                    tempJson.put("DCValue", "true");
                }
                tempJson.put("DCTimestamp", postMatchData.get(2).get(0));
                jsonArr.put(tempJson);
                tempJson = newJsonTemplate(preAutonData);
                
                // 23 = no show
                tempJson.put("datapointID", String.valueOf(18));
                if(preAutonData[3].equals("noShow")) {
                    tempJson.put("DCValue", "true");
                }
                else {
                    tempJson.put("DCValue", "false");
                }
                tempJson.put("DCTimestamp", preAutonData[3]);
                jsonArr.put(tempJson);

                // 24 = pickupFromGround
                for(String i : teleopData.get(7)) {
                    tempJson.put("datapointID", String.valueOf(24));
                    tempJson.put("DCValue", "true");
                    tempJson.put("DCTimestamp", normalizeTimestamps(teleopData.get(11).get(0),i));
                    jsonArr.put(tempJson);
                }
                tempJson = newJsonTemplate(preAutonData);
                
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
                boolean fileExists = true;
                String temp = preAuton.getFileTitle();
                File scoutingFile = new File(folderDir, temp+".json");
                for(int i = 1; fileExists; i++) {

                    fileExists = false;
                    for(File j : Objects.requireNonNull(folderDir.listFiles())) {
                        if(scoutingFile.getName().equals(j.getName())) {
                            fileExists = true;
                        }
                    }
                    if(fileExists)
                        scoutingFile = new File(folderDir, temp+"("+i+").json");
                }
                FileWriter fileWriter = new FileWriter(scoutingFile, false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(userString);
                bufferedWriter.close();
                MainActivity mainActivity = (MainActivity)getActivity();
                if(MainActivity.checkConnectivity()) {
                    mainActivity.writeBTCode(userString.getBytes(StandardCharsets.UTF_8));
                }
                else {
                    mainActivity.enableConnectBT();
                    Toast.makeText(this.getContext(), "data has not been uploaded because bluetooth isn't connected", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            preAuton = new PreAuton();
            auton = new  AutonFragment();
            teleop = new TeleopFragment();
            postMatch = new PostMatch();
            ConfirmPopout confirmPopout = new ConfirmPopout();
            ConfirmAutonStart confirmAutonStart = new ConfirmAutonStart();
            ConfirmTeleopStart confirmTeleopStart = new ConfirmTeleopStart();
            ArchiveFragment archiveFragment = new ArchiveFragment();
            BluetoothSettingsFragment bluetoothSettingsFragment = new BluetoothSettingsFragment();

            ft.remove(preAuton);
            ft.remove(auton);
            ft.remove(teleop);
            ft.remove(confirmPopout);
            ft.remove(confirmAutonStart);
            ft.remove(confirmTeleopStart);
            ft.remove(postMatch);
            ft.remove(archiveFragment);
            ft.remove(bluetoothSettingsFragment);

            ft.add(R.id.main_fragment, preAuton, "A");
            ft.add(R.id.main_fragment, auton, "B");
            ft.add(R.id.main_fragment, teleop, "C");
            ft.add(R.id.main_fragment, postMatch, "G");
            ft.add(R.id.main_fragment, confirmPopout, "D");
            ft.add(R.id.main_fragment, confirmAutonStart, "E");
            ft.add(R.id.main_fragment, confirmTeleopStart, "F");
            ft.add(R.id.main_fragment, archiveFragment, "H");
            ft.add(R.id.main_fragment, bluetoothSettingsFragment, "I");

            ft.show(preAuton);
            ft.hide(auton);
            ft.hide(teleop);
            ft.hide(confirmPopout);
            ft.hide(confirmAutonStart);
            ft.hide(confirmTeleopStart);
            ft.hide(postMatch);
            ft.hide(archiveFragment);
            ft.hide(bluetoothSettingsFragment);
            ft.commit();
        });

    }
}
