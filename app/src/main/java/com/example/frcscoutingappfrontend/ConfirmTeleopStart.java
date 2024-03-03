package com.example.frcscoutingappfrontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.frcscoutingappfrontend.databinding.ConfirmTeleopStartBinding;

public class ConfirmTeleopStart extends Fragment{
    ConfirmTeleopStartBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ConfirmTeleopStart() {
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
    public static ConfirmTeleopStart newInstance(String param1, String param2) {
        ConfirmTeleopStart fragment = new ConfirmTeleopStart();
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
        this.binding = ConfirmTeleopStartBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creates the references for creating json data

        // Hides the popout
        binding.backButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("F");
            Fragment auton = getParentFragmentManager().findFragmentByTag("B");
            Fragment teleop = getParentFragmentManager().findFragmentByTag("C");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.hide(teleop);
            ft.show(auton);
            ft.commit();
        });

        //submits and saves as json
        binding.startButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("F");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.commit();
            TeleopFragment teleopFragment = (TeleopFragment) getParentFragmentManager().findFragmentByTag("C");
            teleopFragment.startTeleop();
        });

    }
}
