package com.example.frcscoutingappfrontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frcscoutingappfrontend.databinding.FragmentPostMatchBinding;
import com.example.frcscoutingappfrontend.databinding.FragmentPreAutonBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostMatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostMatch extends Fragment {

    FragmentPostMatchBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostMatch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostMatch.
     */
    // TODO: Rename and change types and number of parameters
    public static PostMatch newInstance(String param1, String param2) {
        PostMatch fragment = new PostMatch();
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
        this.binding = FragmentPostMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public String[] getDataAsArray() {
        String[] data = new String[4];

//        data[0] = binding.scouterNameInput.getText().toString();
//        if(binding.teamBlue.isChecked()) data[1] = "blue";
//        else data[1] = "red";
//        data[2] = binding.teamNumberInput.getText().toString();
//        data[3] = String.valueOf(binding.backupScoutCheckbox.isChecked());

        return data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}