package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    AutonFragment startingFragment = new AutonFragment();
    TeleopFragment secondaryFragment = new TeleopFragment();
    ConfirmPopout popoutFragment = new ConfirmPopout();

    public static void createPopupWindow() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment, startingFragment, "A");
        ft.add(R.id.main_fragment, secondaryFragment, "B");
        ft.add(R.id.main_fragment, popoutFragment, "C");
        ft.show(startingFragment);
        ft.hide(secondaryFragment);
        ft.hide(popoutFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
}