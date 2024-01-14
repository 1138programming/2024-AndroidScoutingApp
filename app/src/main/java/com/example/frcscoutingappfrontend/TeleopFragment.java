package com.example.frcscoutingappfrontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frcscoutingappfrontend.databinding.FragmentTeleopBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeleopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeleopFragment extends Fragment {

    FragmentTeleopBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeleopFragment() {
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
    public static TeleopFragment newInstance(String param1, String param2) {
        TeleopFragment fragment = new TeleopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void incrementView(TextView number) {
        String text = number.getText().toString();
        int num = Integer.parseInt(text);
        num++;
        if (num > 99) {
            num = 99;
        }
        number.setText(Integer.toString(num));
    }
    private void decrementViewWithCheck(TextView number) {
        String text = number.getText().toString();
        int num = Integer.parseInt(text);
        if (num > 0) {
            num--;
        }
        number.setText(Integer.toString(num));
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
        this.binding = FragmentTeleopBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.returnToAuton.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("B");
            Fragment primary = getParentFragmentManager().findFragmentByTag("A");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(self);
            ft.show(primary);
            ft.commit();
        });
        // __ Increment & decrement view functions
        binding.ampMissedPlus.setOnClickListener(view1 -> {
            incrementView(binding.ampMissedTitle);
        });
        binding.ampMissedMinus.setOnClickListener(view1 -> {
            decrementViewWithCheck(binding.ampMissedTitle);
        });

        binding.ampScoredPlus.setOnClickListener(view1 -> {
            incrementView(binding.ampScoredTitle);
        });
        binding.ampScoredMinus.setOnClickListener(view1 -> {
            decrementViewWithCheck(binding.ampScoredTitle);
        });

        binding.speakerMissedPlus.setOnClickListener(view1 -> {
            incrementView(binding.speakerMissedTitle);
        });
        binding.speakerMissedMinus.setOnClickListener(view1 -> {
            decrementViewWithCheck(binding.speakerMissedTitle);
        });

        binding.speakerScoredPlus.setOnClickListener(view1 -> {
            incrementView(binding.speakerScoredTitle);
        });
        binding.speakerScoredMinus.setOnClickListener(view1 -> {
            decrementViewWithCheck(binding.speakerScoredTitle);
        });
    }
}