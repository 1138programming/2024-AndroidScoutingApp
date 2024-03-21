package com.example.frcscoutingappfrontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frcscoutingappfrontend.databinding.FragmentBluetoothSettingsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BluetoothSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothSettingsFragment extends Fragment {

    FragmentBluetoothSettingsBinding binding;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String currentMac;
    private int currentPort;
    public BluetoothSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetoothSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothSettingsFragment newInstance(String param1, String param2) {
        BluetoothSettingsFragment fragment = new BluetoothSettingsFragment();
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

    private void saveInfo() {
        ((MainActivity)getActivity()).setMacPort(currentMac, currentPort);
    }
    private void updateHints() {
        binding.macInput.setHint(currentMac);
        binding.portInput.setHint(String.valueOf(currentPort));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentBluetoothSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentMac = ((MainActivity)getActivity()).getMacAddress();
        currentPort = ((MainActivity)getActivity()).getPort();
        updateHints();
        binding.backButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            Fragment self = getParentFragmentManager().findFragmentByTag("I");
            ft.hide(self);
            ft.commit();
        });
        binding.saveButton.setOnClickListener(view1 -> {
            currentMac = binding.macInput.getText().toString();
            currentPort = Integer.parseInt(binding.portInput.getText().toString());
            saveInfo();
            updateHints();
        });
    }
}