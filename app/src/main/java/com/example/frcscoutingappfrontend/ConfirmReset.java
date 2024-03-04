package com.example.frcscoutingappfrontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.frcscoutingappfrontend.databinding.ConfirmResetBinding;

public class ConfirmReset extends Fragment{
    ConfirmResetBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ConfirmReset() {
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
    public static ConfirmReset newInstance(String param1, String param2) {
        ConfirmReset fragment = new ConfirmReset();
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
        this.binding = ConfirmResetBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creates the references for creating json data

        // Hides the popout
        binding.cancelButton.setOnClickListener(view1 -> {
            Fragment popout = getParentFragmentManager().findFragmentByTag("H");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popout);
            ft.commit();
        });

        //submits and saves as json
        binding.confirmButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//            PreAuton startingFragment = (PreAuton) getParentFragmentManager().findFragmentByTag("A");
//            AutonFragment autonFragment = (AutonFragment) getParentFragmentManager().findFragmentByTag("B");
//            TeleopFragment teleopFragment = (TeleopFragment) getParentFragmentManager().findFragmentByTag("C");
//            ConfirmPopout popoutFragment = (ConfirmPopout) getParentFragmentManager().findFragmentByTag("D");
//            ConfirmAutonStart confirmAutonStart = (ConfirmAutonStart) getParentFragmentManager().findFragmentByTag("E");
//            ConfirmTeleopStart confirmTeleopStart = (ConfirmTeleopStart) getParentFragmentManager().findFragmentByTag("F");
//            PostMatch postMatch = (PostMatch) getParentFragmentManager().findFragmentByTag("G");

            PreAuton startingFragment = new PreAuton();
            AutonFragment autonFragment = new  AutonFragment();
            TeleopFragment teleopFragment = new TeleopFragment();
            ConfirmPopout popoutFragment = new ConfirmPopout();
            ConfirmAutonStart confirmAutonStart = new ConfirmAutonStart();
            ConfirmTeleopStart confirmTeleopStart = new ConfirmTeleopStart();
            PostMatch postMatch = new  PostMatch();
            ConfirmReset confirmReset = new ConfirmReset();

            ft.remove(startingFragment);
            ft.remove(autonFragment);
            ft.remove(teleopFragment);
            ft.remove(popoutFragment);
            ft.remove(confirmAutonStart);
            ft.remove(confirmTeleopStart);
            ft.remove(postMatch);
            ft.remove(confirmReset);

            ft.add(R.id.main_fragment, startingFragment, "A");
            ft.add(R.id.main_fragment, autonFragment, "B");
            ft.add(R.id.main_fragment, teleopFragment, "C");
            ft.add(R.id.main_fragment, postMatch, "G");
            ft.add(R.id.main_fragment, popoutFragment, "D");
            ft.add(R.id.main_fragment, confirmAutonStart, "E");
            ft.add(R.id.main_fragment, confirmTeleopStart, "F");
            ft.add(R.id.main_fragment, confirmReset, "H");
            ft.show(startingFragment);
            ft.hide(autonFragment);
            ft.hide(teleopFragment);
            ft.hide(popoutFragment);
            ft.hide(confirmAutonStart);
            ft.hide(confirmTeleopStart);
            ft.hide(postMatch);
            ft.hide(confirmReset);
            ft.commit();
        });

    }
}
