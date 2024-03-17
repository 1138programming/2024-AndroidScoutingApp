package com.example.frcscoutingappfrontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



import com.example.frcscoutingappfrontend.databinding.ConfirmPopoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;


public class ConfirmPopout extends Fragment{
    ConfirmPopoutBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        // Hides the popout
        binding.cancelButton.setOnClickListener(view1 -> {
            Fragment auton = getParentFragmentManager().findFragmentByTag("C");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(auton);
            ft.commit();
        });

        //submits and saves as json
        binding.submitButton.setOnClickListener(view1 -> {
            TeleopFragment teleop = (TeleopFragment) getParentFragmentManager().findFragmentByTag("B");
            MainFragment auton = (MainFragment) getParentFragmentManager().findFragmentByTag("A");
            Fragment popout = getParentFragmentManager().findFragmentByTag("C");
//            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            String[] autonData = auton.getDataAsArray();
            String[] teleopData = teleop.getDataAsArray();

            try {
                JSONObject jsonFile = new JSONObject();
                jsonFile.put("autonSpeakerScored", autonData[0]);
                jsonFile.put("autonSpeakerMissed", autonData[1]);
                jsonFile.put("autonAmpScored", autonData[2]);
                jsonFile.put("autonAmpMissed", autonData[3]);
                jsonFile.put("robotLeft", autonData[4]);
                jsonFile.put("teleopSpeakerScored", teleopData[0]);
                jsonFile.put("teleopSpeakerMissed", teleopData[1]);
                jsonFile.put("teleopAmpScored", teleopData[2]);
                jsonFile.put("teleopAmpMissed", teleopData[3]);
                jsonFile.put("robotHung", teleopData[4]);
                jsonFile.put("teleopTimeHung", teleopData[7]);
                jsonFile.put("trapScored", teleopData[5]);
                jsonFile.put("robotBroke", teleopData[6]);


                String userString = jsonFile.toString();
//                File folderDir = new File("/data/data/com.example.frcscoutingappfrontend/files/scoutingData");
//                File scoutingFile = new File(folderDir, Calendar.getInstance().getTime().toString()+".json");
//                FileWriter fileWriter = new FileWriter(scoutingFile, false);
//                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//                bufferedWriter.write(userString);
//                bufferedWriter.close();
                MainActivity mainActivity = (MainActivity)(getActivity());
                mainActivity.writeBTCode(userString.getBytes(StandardCharsets.UTF_8));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

//            ft.remove(auton);
//            ft.remove(teleop);
//            ft.remove(popout);
//
//            teleop = new TeleopFragment();
//            auton = new MainFragment();
//            ft.add(R.id.main_fragment, auton, "A");
//            ft.add(R.id.main_fragment, teleop, "B");
//            ft.add(R.id.main_fragment, popout, "C");
//            ft.show(auton);
//            ft.hide(teleop);
//            ft.hide(popout);
//            ft.commit();
        });

    }
}
