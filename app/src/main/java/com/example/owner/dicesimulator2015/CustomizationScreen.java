package com.example.owner.dicesimulator2015;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;


public class CustomizationScreen extends ActionBarActivity {

    int numSides;
    int sideColour;
    int numColour;
    Boolean pips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_screen);
        System.out.println("hello");
    }

    public void onSwitchClicked(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();

        if (on) {
            // display pips
            System.out.println("on");
            pips = true;
        } else {
            // display number
            System.out.println("off");
            pips = false;
        }
    }

    //Die newDie = new Die(6, Color.BLUE, Color.BLACK, false);
    public void onClickFaceListener(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.redFace:
                if (checked)
                    System.out.println("red");
                    sideColour = Color.RED;
                    break;
            case R.id.orangeFace:
                if (checked)
                    System.out.println("orange");
                    sideColour = Color.DKGRAY;
                    break;
            case R.id.yellowFace:
                if (checked)
                    sideColour = Color.YELLOW;
                    break;
            case R.id.greenFace:
                if (checked)
                    sideColour = Color.GREEN;
                    break;
            case R.id.lBlueFace:
                if (checked)
                    sideColour = Color.CYAN;
                    break;
            case R.id.dBlueFace:
                if (checked)
                    sideColour = Color.BLUE;
                    break;
            case R.id.purpleFace:
                if (checked)
                    sideColour = Color.MAGENTA;
                    break;
            case R.id.pinkFace:
                if (checked)
                    sideColour = Color.WHITE;
                    break;
        }
    }

    public void onClickNumberListener(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.redPip:
                if (checked)
                    System.out.println("redpip");
                numColour = Color.RED;
                    break;
            case R.id.orangePip:
                if (checked)
                    System.out.println("orangepip");
                    numColour = Color.DKGRAY;
                    break;
            case R.id.yellowPip:
                if (checked)
                    numColour = Color.YELLOW;
                    break;
            case R.id.greenPip:
                if (checked)
                    numColour = Color.GREEN;
                    break;
            case R.id.lBluePip:
                if (checked)
                    numColour = Color.CYAN;
                    break;
            case R.id.dBluePip:
                if (checked)
                    numColour = Color.BLUE;
                    break;
            case R.id.purplePip:
                if (checked)
                    numColour = Color.MAGENTA;
                    break;
            case R.id.pinkPip:
                if (checked)
                    numColour = Color.WHITE;
                    break;
        }
    }

    public void onClickSidesListener(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.sides2:
                if (checked)
                    numSides = 2;
                    break;
            case R.id.sides3:
                if (checked)
                    numSides = 3;
                    break;
            case R.id.sides4:
                if (checked)
                    numSides = 4;
                    break;
            case R.id.sides6:
                if (checked)
                    numSides = 6;
                    break;
            case R.id.sides8:
                if (checked)
                    numSides = 8;
                    break;
            case R.id.sides10:
                if (checked)
                    numSides = 10;
                    break;
            case R.id.sides12:
                if (checked)
                    numSides = 12;
                    break;
            case R.id.sides20:
                if (checked)
                    numSides = 20;
                    break;
        }
    }

    public void onClickAddDiceListener(View v){
        System.out.println("add");
        Intent j = new Intent(
                CustomizationScreen.this,
                MainActivity.class);
        startActivity(j);
        finish();

    }

    public void onClickCancelListener(View v){
        Intent j = new Intent(
                CustomizationScreen.this,
                MainActivity.class);
        startActivity(j);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customization_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent j = new Intent(
                CustomizationScreen.this,
                MainActivity.class);
        startActivity(j);
        finish();
        return true;
    }
}
