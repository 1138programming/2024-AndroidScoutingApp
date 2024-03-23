package com.example.frcscoutingappfrontend;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.FileUtils;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.frcscoutingappfrontend.databinding.FragmentArchiveBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveFragment extends Fragment {

    FragmentArchiveBinding binding;
    private final File filePath = new File("/data/data/com.example.frcscoutingappfrontend/files/scoutingData");
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public boolean btConnected = false;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveFragment newInstance(String param1, String param2) {
        ArchiveFragment fragment = new ArchiveFragment();
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
    public void sendViaBT(String fileName) {
        Toast.makeText(this.getContext(), "Opening: "+fileName, Toast.LENGTH_SHORT).show();
        File file = new File(filePath, fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            //double uhoh
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader((inputStreamReader))) {
            String line = reader.readLine();
            while(line != null) {
                sb.append(line).append('\n');
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            //error :skull:
        }
        finally {
            String contents = sb.toString();
            ((MainActivity)getActivity()).writeBTCode(contents.getBytes(StandardCharsets.UTF_8));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         this.binding = FragmentArchiveBinding.inflate(inflater, container, false);
         return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //creates content for list
        File[] files = filePath.listFiles();
        if(files != null) {
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
//            Toast.makeText(this.getContext(), fileNames[i], Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_layout, fileNames);
            binding.submissionList.setAdapter(adapter);
            binding.submissionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {

                    ArchiveConfirmFragment archiveConfirmFragment = (ArchiveConfirmFragment)getParentFragmentManager().findFragmentByTag("J");
                    archiveConfirmFragment.setFileName(binding.submissionList.getItemAtPosition(position).toString());
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    ft.show(archiveConfirmFragment);
                    ft.commit();
                }
            });
        }
        binding.closeButton.setOnClickListener(view1 -> {
            Fragment popup = getParentFragmentManager().findFragmentByTag("H");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.hide(popup);
            ft.commit();
        });
        binding.bluetoothConnect.setOnClickListener(view1 -> {
            ((MainActivity)getActivity()).enableConnectBT();
        });
        binding.inputMacButton.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            Fragment btSettings = getParentFragmentManager().findFragmentByTag("I");
            ft.show(btSettings);
            ft.commit();
        });
    }
}