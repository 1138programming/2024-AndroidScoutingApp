package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    PreAuton startingFragment = new PreAuton();
    AutonFragment autonFragment = new AutonFragment();
    ConfirmAutonStart confirmAutonStart = new ConfirmAutonStart();
    ConfirmTeleopStart confirmTeleopStart = new ConfirmTeleopStart();
    TeleopFragment teleopFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment, startingFragment, "A");
        ft.add(R.id.main_fragment, autonFragment, "B");
        ft.add(R.id.main_fragment, teleopFragment, "C");
        ft.add(R.id.main_fragment, popoutFragment, "D");
        ft.add(R.id.main_fragment, confirmAutonStart, "E");
        ft.add(R.id.main_fragment, confirmTeleopStart, "F");
        ft.show(startingFragment);
        ft.hide(autonFragment);
        ft.hide(teleopFragment);
        ft.hide(popoutFragment);
        ft.hide(confirmAutonStart);
        ft.hide(confirmTeleopStart);
        // Complete the changes added above
        ft.commit();
    }
}