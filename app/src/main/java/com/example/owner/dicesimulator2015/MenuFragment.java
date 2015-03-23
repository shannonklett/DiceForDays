package com.example.owner.dicesimulator2015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class MenuFragment extends Fragment {

    Button diceSlider;
    private GestureDetector diceSliderDetector;
    private View inflatedView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Activity currentActivity = getActivity();
        if (currentActivity instanceof MainActivity) {
            MainActivity newActivity = (MainActivity) currentActivity;
            newActivity.setFragmentTouchListeners();
        } else {
            VsScreen newActivity = (VsScreen) currentActivity;
            newActivity.closeFragment();
        }
    }


    public MenuFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_menu_fragment, container, false);
    }



}