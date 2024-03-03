package com.example.frcscoutingappfrontend;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentTeleopBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
     * 0 = ampScore
     * 1 = ampMiss
     * 2 = speakerScore
     * 3 = speakerMiss
     * */
    private Stack<Integer> inputStack = new Stack<Integer>();
    private Stack<String> timestamps = new Stack<String>();
    private Stack<Integer> redoStack = new Stack<Integer>();
    private Stack<String> redoTimestamps = new Stack<String>();
    private String teleopStart;
    private ArrayList<TwoThings> defenseTimestamps = new ArrayList<>();
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
        redoTimestamps.push(timestamps.pop());
        switch(redoStack.peek()) {
            case 0:
                Toast.makeText(getContext(), "Undid Amp Score", Toast.LENGTH_SHORT).show();
                decrementView(binding.ampScored);
                break;
            case 1:
                Toast.makeText(getContext(), "Undid Amp Miss", Toast.LENGTH_SHORT).show();
                decrementView(binding.ampMissed);
                break;
            case 2:
                Toast.makeText(getContext(), "Undid Speaker Score", Toast.LENGTH_SHORT).show();
                decrementView(binding.speakerScored);
                break;
            case 3:
                Toast.makeText(getContext(), "Undid Speaker Miss", Toast.LENGTH_SHORT).show();
                decrementView(binding.speakerMissed);
                break;
            case 5:
                Toast.makeText(getContext(), "Undid Hang Start", Toast.LENGTH_SHORT).show();
                binding.hangQuestionCheckBox.setEnabled(true);
                binding.hangQuestionCheckBox.setChecked(false);
                break;
            default:
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void redo() {
        if(redoStack.empty()) return;
        switch(redoStack.peek()) {
            case 0:
                Toast.makeText(getContext(), "Redid Amp Score", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampScored, false);
                break;
            case 1:
                Toast.makeText(getContext(), "Redid Amp Miss", Toast.LENGTH_SHORT).show();
                incrementView(binding.ampMissed, false);
                break;
            case 2:
                Toast.makeText(getContext(), "Redid Speaker Score", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerScored, false);
                break;
            case 3:
                Toast.makeText(getContext(), "Redid Speaker Miss", Toast.LENGTH_SHORT).show();
                incrementView(binding.speakerMissed, false);
                break;
            case 5:
                Toast.makeText(getContext(), "Redid Hang Start", Toast.LENGTH_SHORT).show();
                binding.hangQuestionCheckBox.setEnabled(false);
                binding.hangQuestionCheckBox.setChecked(true);
                break;
            default:
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        inputStack.push(redoStack.pop());
        timestamps.push(redoTimestamps.pop());
    }
    private boolean incrementView(Button button, boolean undo) {
        String text = button.getText().toString();
        int num = Integer.parseInt(text);
        num++;
        if (num > 99) {
            num = 99;
            undo = false;
        }
        button.setText(String.valueOf(num));
        return undo;
    }
    private void decrementView(Button button) {
        String text = button.getText().toString();
        int num = Integer.parseInt(text);
        if (num > 0) {
            num--;
        }
        button.setText(String.valueOf(num));
    }
    public void startTeleop() {
        if(teleopStart == null) {
            teleopStart = new SimpleDateFormat("HH:mm:ss").format(new Date());
        }
    }
    public void openTeleop() {
        if(teleopStart == null) {
            Fragment popout = getParentFragmentManager().findFragmentByTag("F");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(popout);
            ft.commit();
        }
    }
    public ArrayList<ArrayList<String>> getDataAsArray() {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>(8);
        Stack<String> reversedTimestamps = new Stack<String>();
        for(int i = 0; i<10; i++) {
            data.add(i, new ArrayList<String>());
        }
        while(timestamps.size()>0){
            reversedTimestamps.push(timestamps.pop());
        }
        //Speaker and amp timestamps as well as hang start
        for(int i : inputStack) {
            data.get(i).add(reversedTimestamps.pop());
        }
        //amplify box
        data.get(7).add(String.valueOf(binding.robotBreakCheckbox.isChecked()));

        //defense button timestamps
        for(TwoThings timestampPair : defenseTimestamps) {
            data.get(8).add(timestampPair.start);
            data.get(9).add(timestampPair.end);
        }

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
        binding.nextButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            Fragment popout = getParentFragmentManager().findFragmentByTag("D");
            ft.show(popout);
            ft.commit();
        });
        // Increment & decrement view functions
        binding.ampScored.setOnClickListener(view1 -> {
            if(incrementView(binding.ampScored, true)) {
                inputStack.push(0);
                timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                redoStack = new Stack<Integer>();
            }
        });
        binding.ampMissed.setOnClickListener(view1 -> {
            if(incrementView(binding.ampMissed, true)) {
                inputStack.push(1);
                timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                redoStack = new Stack<Integer>();
            }
        });

        binding.speakerScored.setOnClickListener(view1 -> {
            if(incrementView(binding.speakerScored, true)){
                inputStack.push(2);
                timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                redoStack = new Stack<Integer>();
            }
        });
        binding.speakerMissed.setOnClickListener(view1 -> {
            if(incrementView(binding.speakerMissed, true)) {
                inputStack.push(3);
                timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                redoStack = new Stack<Integer>();
            }
        });
        binding.hangQuestionCheckBox.setOnClickListener(view1 -> {
            inputStack.push(4);
            timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            redoStack = new Stack<Integer>();
            binding.hangQuestionCheckBox.setEnabled(false);
        });
        binding.pickupButton.setOnClickListener(view1 -> {
            inputStack.push(5);
            timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            redoStack = new Stack<Integer>();
        });
        binding.amplifyButton.setOnClickListener(view1 -> {
            binding.amplifyButton.setBackgroundColor(getResources().getColor(R.color.pressed_defense));
            binding.amplifyButton.setEnabled(false);
            inputStack.push(6);
            timestamps.push(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            redoStack = new Stack<Integer>();
            Toast.makeText(TeleopFragment.this.getContext(), "Amplification Started", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    binding.amplifyButton.setBackgroundColor(getResources().getColor(R.color.chaminade_orange));
                    binding.amplifyButton.setEnabled(true);
                }
            }, 10000);
        });
        //Defense button that tracks timestamps
        binding.defensiveActionButton.setOnTouchListener((view1, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TwoThings timePeriod = new TwoThings();
                    timePeriod.start = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    defenseTimestamps.add(timePeriod);
                    break;
                case MotionEvent.ACTION_UP:
                    defenseTimestamps.set(defenseTimestamps.size()-1, new TwoThings(defenseTimestamps.get(defenseTimestamps.size()-1).start, new SimpleDateFormat("HH:mm:ss").format(new Date())));
                    Toast.makeText(TeleopFragment.this.getContext(), "Start: "+defenseTimestamps.get(defenseTimestamps.size()-1).start+" End: "
                            +defenseTimestamps.get(defenseTimestamps.size()-1).end, Toast.LENGTH_LONG).show();
            }
            return false;
        });
    }
}