package com.example.frcscoutingappfrontend;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frcscoutingappfrontend.databinding.FragmentAutonBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AutonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutonFragment extends Fragment {

    FragmentAutonBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AutonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutonFragment newInstance(String param1, String param2) {
        AutonFragment fragment = new AutonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String[] getDataAsArray() {
        String[] data = new String[5];

        //Speaker scoring and missing
        data[0] = binding.speakerScoredTitle.getText().toString();
        data[1] = binding.speakerMissedTitle.getText().toString();

        //amp scoring and missing
        data[2] = binding.ampScoredTitle.getText().toString();
        data[3] = binding.ampMissedTitle.getText().toString();

        //check box
        data[4] = String.valueOf(binding.leaveQuestionCheckBox.isChecked());

        return data;
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
        // Inflate the layout for this fragment
        this.binding = FragmentAutonBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                // Fragment transaction on "Next" button
        binding.nextButton.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("B");
            Fragment secondary = getParentFragmentManager().findFragmentByTag("C");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(secondary);
            ft.hide(self);
            ft.commit();
        });

        binding.returnToPreAuton.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("B");
            Fragment secondary = getParentFragmentManager().findFragmentByTag("A");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(secondary);
            ft.hide(self);
            ft.commit();
        });
    }
}