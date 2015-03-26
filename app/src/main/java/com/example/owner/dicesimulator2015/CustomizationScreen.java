package com.example.owner.dicesimulator2015;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.ArrayList;


public class CustomizationScreen extends ActionBarActivity {

    //default
    int numSides = 2 ;
    //Color s = getResources().getColor(android.R.color.dark_red);
    int sideColour = Color.WHITE;
    int numColour= Color.BLACK;
    Boolean pips = false;
    Die preview;
    Switch pipSwitch;


    ArrayList<DieBunch> diceList;
    private ArrayList<Die> dieSaved = new ArrayList<Die>();
    private ArrayList<DieBunch> dieSavedUpdated = new ArrayList<DieBunch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_screen);
        System.out.println("hello");

        //saved list of dice from menu
        pipSwitch = (Switch)this.findViewById(R.id.pipSwitch);
        pipSwitch.setEnabled(false);
        diceList = getIntent().getParcelableArrayListExtra("dieBunch");
        preview = new Die(numSides, sideColour, numColour, pips);
        preview.createImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(150, 150);
        params.leftMargin = 300;
        params.topMargin = 500;
        ((FrameLayout)this.findViewById(R.id.demoDiceFrame)).addView(preview.getImageView(), params);
        for(DieBunch point: diceList){
            dieSaved.add(point.getDieBunch());
        }
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
        updatePreview();
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
                    sideColour = getResources().getColor(R.color.dark_red);
                    break;
            case R.id.orangeFace:
                if (checked)
                    System.out.println("orange");
                    sideColour = getResources().getColor(R.color.orange);
                    break;
            case R.id.yellowFace:
                if (checked)
                    sideColour = getResources().getColor(R.color.yellow);
                    break;
            case R.id.greenFace:
                if (checked)
                    sideColour = getResources().getColor(R.color.green);
                    break;
            case R.id.lBlueFace:
                if (checked)
                    sideColour = getResources().getColor(R.color.blue);
                    break;
            case R.id.dBlueFace:
                if (checked)
                    sideColour = getResources().getColor(R.color.purple);
                    break;
            case R.id.purpleFace:
                if (checked)
                    sideColour = Color.DKGRAY;
                    break;
            case R.id.pinkFace:
                if (checked)
                    sideColour = Color.WHITE;
                    break;
        }
        updatePreview();
    }

    public void onClickNumberListener(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.redPip:
                if (checked)
                    System.out.println("redpip");
                numColour = getResources().getColor(R.color.dark_red);
                    break;
            case R.id.orangePip:
                if (checked)
                    System.out.println("orangepip");
                    numColour = getResources().getColor(R.color.orange);
                    break;
            case R.id.yellowPip:
                if (checked)
                    numColour = getResources().getColor(R.color.yellow);
                    break;
            case R.id.greenPip:
                if (checked)
                    numColour = getResources().getColor(R.color.green);
                    break;
            case R.id.lBluePip:
                if (checked)
                    numColour = getResources().getColor(R.color.blue);
                    break;
            case R.id.dBluePip:
                if (checked)
                    numColour = getResources().getColor(R.color.purple);
                    break;
            case R.id.purplePip:
                if (checked)
                    numColour = Color.BLACK;
                    break;
            case R.id.pinkPip:
                if (checked)
                    numColour = Color.WHITE;
                    break;
        }
        updatePreview();
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
            case R.id.sides4:
                if (checked)
                    System.out.println("4");
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

        View pip = findViewById(R.id.Pips);
        View pipsSwitch = findViewById(R.id.pipSwitch);

        if (numSides == 6) {
            pip.setVisibility(View.VISIBLE);
            pipsSwitch.setVisibility(View.VISIBLE);

            pipSwitch.setEnabled(true);
        } else {
            pipSwitch.setChecked(false);
            pips = false;
            pipSwitch.setEnabled(false);

            pip.setVisibility(View.GONE);
            pipsSwitch.setVisibility(View.GONE);
        }
        updatePreview();
    }

    public void onClickAddDiceListener(View v){

        Die die1 = new Die(numSides,sideColour,numColour,pips);
        dieSaved.add(die1);

        //update list of Books
        for(Die point: dieSaved){
            dieSavedUpdated.add(new DieBunch(point));
        }

        String caller = getIntent().getStringExtra("caller");
        Intent j;
        if (caller.equals("MainActivity")) {
            j = new Intent(
                    CustomizationScreen.this,
                    MainActivity.class);
        } else {
            j = new Intent(
                    CustomizationScreen.this,
                    VsScreen.class);
        }
        j.putExtra("dieBunch", dieSavedUpdated);
        j.putExtra("flag", "cust");
        startActivity(j);
        finish();

    }

    public void onClickCancelListener(View v){

        for(Die point: dieSaved){
            dieSavedUpdated.add(new DieBunch(point));
        }
        String caller = getIntent().getStringExtra("caller");
        Intent j;
        if (caller.equals("MainActivity")) {
            j = new Intent(
                    CustomizationScreen.this,
                    MainActivity.class);
        } else {
            j = new Intent(
                    CustomizationScreen.this,
                    VsScreen.class);
        }
        j.putExtra("dieBunch", dieSavedUpdated);
        j.putExtra("flag", "cust");
        startActivity(j);
        finish();
    }

    public void updatePreview() {
        preview.setNumColour(numColour);
        preview.setNumSides(numSides);
        preview.setSideColour(sideColour);
        preview.setPips(pips);
        preview.generateImage();
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
