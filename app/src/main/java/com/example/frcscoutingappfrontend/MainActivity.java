package com.example.frcscoutingappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    MainFragment startingFragment = new MainFragment();
    TeleopFragment secondaryFragment = new TeleopFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment, startingFragment, "A");
        ft.add(R.id.main_fragment, secondaryFragment, "B");
        ft.show(startingFragment);
        ft.hide(secondaryFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
}