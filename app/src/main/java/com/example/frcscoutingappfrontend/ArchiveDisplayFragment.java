package com.example.frcscoutingappfrontend;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.frcscoutingappfrontend.databinding.FragmentArchiveDisplayBinding;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchiveDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveDisplayFragment extends Fragment {

    FragmentArchiveDisplayBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int currQRIndex = 0;
    ArrayList<Bitmap> bitmap = new ArrayList<Bitmap>();
    public ArchiveDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveDisplayFragment newInstance(String param1, String param2) {
        ArchiveDisplayFragment fragment = new ArchiveDisplayFragment();
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
        // Inflate the layout for this fragment
        this.binding = FragmentArchiveDisplayBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    public void openWithQRCode(String json) {
        Fragment popup = getParentFragmentManager().findFragmentByTag("J");
        Fragment archive =  getParentFragmentManager().findFragmentByTag("I");
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.show(popup);
        ft.hide(archive);
        ft.commit();

        //create qrcode
        ArrayList<QRGEncoder> qrgEncoder = new ArrayList<QRGEncoder>();
        ArrayList<String> segmentedJson = new ArrayList<String>();
        bitmap = new ArrayList<Bitmap>();
        currQRIndex = 0;
        int qrSize = 2000;
        int borderSize = 25;
        WindowManager manager = (WindowManager) this.getContext().getSystemService(this.getContext().WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        for(int i = 0; i<((double)json.length())/qrSize; i++) {
            if(json.length() > qrSize*(i+1)) {
                segmentedJson.add(json.substring(qrSize * i, qrSize * (i + 1)));
            }
            else {
                segmentedJson.add(json.substring(qrSize * i));
            }
        }
        for(int i = 0; i<segmentedJson.size(); i++) {
            qrgEncoder.add(new QRGEncoder(segmentedJson.get(i),null, QRGContents.Type.TEXT, dimen));
        }
        for(QRGEncoder qrEncoder : qrgEncoder) {
            qrEncoder.setColorBlack(getResources().getColor(R.color.white));
            qrEncoder.setColorWhite(getResources().getColor(R.color.black));
            bitmap.add(Bitmap.createBitmap(qrEncoder.getBitmap(), borderSize,borderSize,
                    dimen-borderSize*2, dimen-borderSize*2));
        }
        // getting our qrcode in the form of bitmap.
        binding.QRcode.setImageBitmap(bitmap.get(currQRIndex));

        //resetting back and next buttons
        binding.backButton.setEnabled(false);
        binding.backButton.setBackgroundColor(getResources().getColor(R.color.simple_light_grey));
        if(bitmap.size() > 1) {
            binding.nextButton.setEnabled(true);
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.chaminade_orange));
        }
        else {
            binding.nextButton.setEnabled(false);
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.simple_light_grey));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.returnToArchiveButton.setOnClickListener(view1 -> {
            Fragment popup = getParentFragmentManager().findFragmentByTag("J");
            Fragment archive = getParentFragmentManager().findFragmentByTag("I");
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.show(archive);
            ft.hide(popup);
            ft.commit();
        });
        binding.backButton.setOnClickListener(view1 -> {
            if(currQRIndex > 0) {
                currQRIndex--;
                binding.QRcode.setImageBitmap(bitmap.get(currQRIndex));
                if (currQRIndex == 0) {
                    binding.backButton.setEnabled(false);
                    binding.backButton.setBackgroundColor(getResources().getColor(R.color.simple_light_grey));
                }
                if(currQRIndex == bitmap.size()-2) {
                    binding.nextButton.setEnabled(true);
                    binding.nextButton.setBackgroundColor(getResources().getColor(R.color.chaminade_orange));
                }
            }
        });
        binding.nextButton.setOnClickListener(view1 -> {
            if(bitmap.size() > currQRIndex+1) {
                currQRIndex++;
                binding.QRcode.setImageBitmap(bitmap.get(currQRIndex));
                if(bitmap.size() == currQRIndex+1) {
                    binding.nextButton.setEnabled(false);
                    binding.nextButton.setBackgroundColor(getResources().getColor(R.color.simple_light_grey));
                }
                if(currQRIndex == 1) {
                    binding.backButton.setEnabled(true);
                    binding.backButton.setBackgroundColor(getResources().getColor(R.color.chaminade_orange));
                }
            }
        });
    }
}