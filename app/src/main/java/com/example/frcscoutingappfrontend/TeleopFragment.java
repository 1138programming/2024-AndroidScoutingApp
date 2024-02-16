package com.example.frcscoutingappfrontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentTeleopBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.util.Stack;

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
    /*
     * 0 = ampDec
     * 1 = ampInc
     * 2 = ampMissDec
     * 3 = ampMissInc
     * 4 = speakerDec
     * 5 = speakerInc
     * 6 = speakerMissDec
     * 7 = speakerMissInc
     * */
    private Stack<Integer> inputStack = new Stack<Integer>();
    private Stack<Integer> redoStack = new Stack<Integer>();
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

    public void undo() {
        if(inputStack.empty()) return;
        redoStack.push(inputStack.pop());
        switch(redoStack.peek()) {
            case 0:
                Toast.makeText(getContext(), "Undid Amp decrease", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampScoredTitle, false);
                break;
            case 1:
                Toast.makeText(getContext(), "Undid Amp increase", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.ampScoredTitle, false);
                break;
            case 2:
                Toast.makeText(getContext(), "Undid Amp miss decrease", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampMissedTitle, false);
                break;
            case 3:
                Toast.makeText(getContext(), "Undid Amp miss increase", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.ampMissedTitle, false);
                break;
            case 4:
                Toast.makeText(getContext(), "Undid Speaker decrease", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerScoredTitle, false);
                break;
            case 5:
                Toast.makeText(getContext(), "Undid Speaker increase", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.speakerScoredTitle, false);
                break;
            case 6:
                Toast.makeText(getContext(), "Undid Speaker miss decrease", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerMissedTitle, false);
                break;
            case 7:
                Toast.makeText(getContext(), "Undid Speaker miss increase", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.speakerMissedTitle, false);
                break;
            default:
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void redo() {
        if(redoStack.empty()) return;
        switch(redoStack.peek()) {
            case 0:
                Toast.makeText(getContext(), "Redid Amp decrease", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.ampScoredTitle, false);
                break;
            case 1:
                Toast.makeText(getContext(), "Redid Amp increase", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampScoredTitle, false);
                break;
            case 2:
                Toast.makeText(getContext(), "Redid Amp miss decrease", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.ampMissedTitle, false);
                break;
            case 3:
                Toast.makeText(getContext(), "Redid Amp miss increase", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampMissedTitle, false);
                break;
            case 4:
                Toast.makeText(getContext(), "Redid Speaker decrease", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.speakerScoredTitle, false);
                break;
            case 5:
                Toast.makeText(getContext(), "Redid Speaker increase", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerScoredTitle, false);
                break;
            case 6:
                Toast.makeText(getContext(), "Redid Speaker miss decrease", Toast.LENGTH_SHORT).show();
                decrementViewWithCheck(binding.speakerMissedTitle, false);
                break;
            case 7:
                Toast.makeText(getContext(), "Redid Speaker miss increase", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerMissedTitle, false);
                break;
            default:
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        inputStack.push(redoStack.pop());
    }
    private boolean incrementView(TextView number, boolean undo) {
        String text = number.getText().toString();
        int num = Integer.parseInt(text);
        num++;
        if (num > 99) {
            num = 99;
            undo = false;
        }
        number.setText(Integer.toString(num));
        return undo;
    }
    private boolean decrementViewWithCheck(TextView number, boolean undo) {
        String text = number.getText().toString();
        int num = Integer.parseInt(text);
        if (num > 0) {
            num--;
        }
        else undo = false;
        number.setText(Integer.toString(num));
        return undo;
    }

    public String[] getDataAsArray() {
        String[] data = new String[8];

        //Speaker scoring and missing
        data[0] = binding.speakerScoredTitle.getText().toString();
        data[1] = binding.speakerMissedTitle.getText().toString();

        //amp scoring and missing
        data[2] = binding.ampScoredTitle.getText().toString();
        data[3] = binding.ampMissedTitle.getText().toString();

        //check boxes
        data[4] = String.valueOf(binding.hangQuestionCheckBox.isChecked());
        data[5] = String.valueOf(binding.trapQuestionCheckBox.isChecked());
        data[6] = String.valueOf(binding.robotBreakCheckbox.isChecked());

        //time on timer
        data[7] = String.valueOf(binding.startedHangingInput.getText().toString());

        return data;
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

        binding.undoButton.setOnClickListener(view1 -> {
            undo();
        });
        binding.redoButton.setOnClickListener(view1 -> {
            redo();
        });

        binding.returnToAuton.setOnClickListener(view1 -> {
            Fragment self = getParentFragmentManager().findFragmentByTag("C");
            Fragment primary = getParentFragmentManager().findFragmentByTag("B");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(self);
            ft.show(primary);
            ft.commit();
        });
        binding.submitButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            Fragment popout = getParentFragmentManager().findFragmentByTag("D");
            ft.show(popout);
            ft.commit();
        });
        // Increment & decrement view functions
        binding.ampScoredMinus.setOnClickListener(view1 -> {
            if(decrementViewWithCheck(binding.ampScoredTitle, true)) {
                inputStack.push(0);
                redoStack = new Stack<Integer>();
            }
        });
        binding.ampScoredPlus.setOnClickListener(view1 -> {
            if(incrementView(binding.ampScoredTitle, true)) {
                inputStack.push(1);
                redoStack = new Stack<Integer>();
            }
        });
        binding.ampMissedMinus.setOnClickListener(view1 -> {
            if(decrementViewWithCheck(binding.ampMissedTitle, true)){
                inputStack.push(2);
                redoStack = new Stack<Integer>();
            }
        });

        binding.ampMissedPlus.setOnClickListener(view1 -> {
            if(incrementView(binding.ampMissedTitle, true)){
                inputStack.push(3);
                redoStack = new Stack<Integer>();
            }
        });

        binding.speakerScoredMinus.setOnClickListener(view1 -> {
            if(decrementViewWithCheck(binding.speakerScoredTitle, true)){
                inputStack.push(4);
                redoStack = new Stack<Integer>();
            }
        });
        binding.speakerScoredPlus.setOnClickListener(view1 -> {
            if(incrementView(binding.speakerScoredTitle, true)) {
                inputStack.push(5);
                redoStack = new Stack<Integer>();
            }
        });
        binding.speakerMissedMinus.setOnClickListener(view1 -> {
            if(decrementViewWithCheck(binding.speakerMissedTitle, true)) {
                inputStack.push(6);
                redoStack = new Stack<Integer>();
            }
        });
        binding.speakerMissedPlus.setOnClickListener(view1 -> {
            if(incrementView(binding.speakerMissedTitle, true)) {
                inputStack.push(7);
                redoStack = new Stack<Integer>();
            }
        });
        binding.defensiveActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
//        new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        }
    }
}