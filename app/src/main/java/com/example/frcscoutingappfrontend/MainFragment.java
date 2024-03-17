package com.example.frcscoutingappfrontend;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.ActionBar;
import android.app.StatusBarManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.ActivityMainBinding;
import com.example.frcscoutingappfrontend.databinding.FragmentMainBinding;

import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    FragmentMainBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
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
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        this.binding = FragmentMainBinding.inflate(inflater, container, false);

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
            Fragment self = getParentFragmentManager().findFragmentByTag("A");
            Fragment secondary = getParentFragmentManager().findFragmentByTag("B");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(secondary);
            ft.hide(self);
            ft.commit();
        });
        binding.bluetoothButton.setOnClickListener(view1 -> {
            ((MainActivity)getActivity()).enableConnectBT();
        });
        binding.sendData.setOnClickListener(view1 -> {
            MainActivity activity = (MainActivity)(getActivity());
            byte[] arr = new byte[1];
            arr[0] = 1;
            activity.writeBTCode((new String("{\"scoutingData\":[{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"17\",\"datapointValue\":\"Speaker\",\"DCTimestamp\":\"00:00:00\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"18\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:17\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"1\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:21\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"1\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:24\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"1\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:27\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"3\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:22\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"6\",\"datapointValue\":\"false\",\"DCTimestamp\":\"00:00:00\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"5\",\"datapointValue\":\"false\",\"DCTimestamp\":\"00:00:00\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"19\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:31:36\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"13\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:33:43\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"16\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:32:09\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"12\",\"datapointValue\":\"true\",\"DCTimestamp\":\"09:34:02\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"14\",\"datapointValue\":\"false\",\"DCTimestamp\":\"00:00:00\"},{\"scouterID\":\"8\",\"matchID\":\"4\",\"teamID\":\"37\",\"allianceID\":\"blue\",\"datapointID\":\"22\",\"datapointValue\":\"false\",\"DCTimestamp\":\"00:00:00\"}]}")).getBytes(StandardCharsets.UTF_8));
        });
    }
}