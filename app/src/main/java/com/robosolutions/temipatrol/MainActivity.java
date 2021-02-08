package com.robosolutions.temipatrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalViewModel viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        viewModel.initializeTemiRobot();
        setContentView(R.layout.activity_main);
    }
}