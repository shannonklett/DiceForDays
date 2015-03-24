package com.example.owner.dicesimulator2015;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.graphics.ColorMatrixColorFilter;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private GestureDetector vsSliderDetector;
    private GestureDetector diceSliderDetector;
    private GestureDetector fragmentDiceSliderDetector;
    private GestureDetector menuDiceDetector;
    AbsoluteLayout dieZone;
    ImageView vsSlider;
    ImageView diceSlider;
    ImageView fragmentDiceSlider;
    Boolean menuIsOpen = false;
    GridLayout fragmentGrid;
    Die currentTouchedMenuDieIndex;


    ArrayList<Die> diceOnScreen = new ArrayList<Die>();
    ArrayList<Die> diceList = new ArrayList<Die>();
    ArrayList<DieBunch> dieSaved = new ArrayList<DieBunch>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vsSliderDetector = new GestureDetector(new vsSliderGestureListener());
        diceSliderDetector = new GestureDetector(new diceSliderGestureListener());
        fragmentDiceSliderDetector = new GestureDetector(new fragmentDiceSliderGestureListener());
        menuDiceDetector = new GestureDetector(new menuDiceGestureListener(this));
        int colours[] = {Color.BLACK, Color.WHITE, Color.YELLOW, Color.DKGRAY, Color.RED, Color.GRAY, Color.GREEN, Color.BLUE};
        dieZone = (AbsoluteLayout) this.findViewById(R.id.dieZone);
        vsSlider = (ImageView)this.findViewById(R.id.vsSlider);
        diceSlider = (ImageView)this.findViewById(R.id.diceSlider);

        System.out.println("outside if");
        System.out.println("er extras");
        if (this.getIntent().getStringExtra("flag") != null){
            //update list of die in the menu
            System.out.println("from cust");
            dieSaved = getIntent().getParcelableArrayListExtra("dieBunch");
                for (DieBunch point : dieSaved) {
                    diceList.add(point.getDieBunch());
                }
        }
        else
            {

                for (int i = 0; i<8; i++) {
                    Die newDie = new Die(6, colours[i], colours[7 - i], false);
                    newDie.createImageView(this);
                    diceList.add(newDie);
                }
                System.out.println("else");
                for(Die point: diceList){
                    dieSaved.add(new DieBunch(point));
                }
        }

        /*Die newDie = new Die(6, Color.BLUE, Color.BLACK, false);
        Die newDie2 = new Die(6, Color.GREEN, Color.WHITE, false);
        Die newDie3 = new Die(6, Color.RED, Color.BLUE, false);
        newDie.createImageView(this);
        newDie2.createImageView(this);*/

//        dieZone.addView(newDie.getImageView());
//        dieZone.addView(newDie2.getImageView());
//        newDie.setViewSize();
//        newDie2.setViewSize();
//        dice.add(newDie);
//        dice.add(newDie2);
//        for (Die die : dice) {
//            die.getImageView().setOnTouchListener(new OnDiceTouchListener());
//        }
//

        dieZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Die die : diceOnScreen) {
                    die.roll();
                }
            }
        });
//
        vsSlider.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                vsSliderDetector.onTouchEvent(event);
                return true;
            }
        });

        diceSlider.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                diceSliderDetector.onTouchEvent(event);
                return true;
            }
        });

    }
    //Adds the created dice from diceList to the fragment menu
    public void addDiceToFragment() {
        fragmentGrid = (GridLayout) this.findViewById(R.id.gridLayout);
        ((ViewGroup) fragmentGrid).removeViews(1, fragmentGrid.getChildCount() - 1);
        int currentCol = 0;
        int currentRow = 0;
        GridLayout.Spec row;
        GridLayout.Spec col;
        GridLayout.LayoutParams gridLayoutParams;
        for (Die die : diceList) {


            if (currentCol > 4) {
                currentRow++;
                currentCol = 0;
            }
            row = GridLayout.spec(currentRow, 1);
            col = GridLayout.spec(currentCol, 1);
            gridLayoutParams = new GridLayout.LayoutParams(row, col);
            gridLayoutParams.height = 140;
            gridLayoutParams.width = 140;
            gridLayoutParams.setMargins(0, 0, 10, 10);
            Die newDie = die.clone();
            newDie.createImageView(this);
            fragmentGrid.addView(newDie.getImageView(), gridLayoutParams);
            newDie.getImageView().setOnTouchListener(new OnMenuDiceTouchListener(die));
            newDie.getImageView().setOnClickListener(new OnClickMenuDiceListener(newDie));
            currentCol++;
            //fragmentGrid.addView(fragmentGrid.findViewById(R.id.addDice));

        }

    }



    //add dice button listener inside fragment
    public void addDice (View v) {
        Intent j = new Intent(
                MainActivity.this,
                CustomizationScreen.class);
        j.putExtra("dieBunch", dieSaved);
        startActivity(j);


    }



    //Sets up listener on the Dice tab on the fragment menu to close it when flicked up
    public void setFragmentTouchListeners() {
        fragmentDiceSlider = (ImageView)this.findViewById(R.id.fragmentDiceSlider);
        fragmentDiceSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                fragmentDiceSliderDetector.onTouchEvent(event);
                return true;
            }
        });
    }
    //Adds the given die to the screen (diceOnScreen arraylist as well)
    public void addDieToScreen(Die die) {
        Die dieToAdd = die.clone();
        dieToAdd.createImageView(this);
        diceOnScreen.add(dieToAdd);
        dieToAdd.getImageView().setOnTouchListener(new OnDiceTouchListener());
        dieZone.addView(dieToAdd.getImageView(), 150, 150);
    }

    class OnClickMenuDiceListener implements View.OnClickListener {

        private Die die;
        public OnClickMenuDiceListener(Die die) {
            this.die = die;
        }

        public void onClick (View v) {
            addDieToScreen(die);
        }
    }

    class YesNoClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    diceList.remove(currentTouchedMenuDieIndex);
                    addDiceToFragment();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    class OnMenuDiceTouchListener implements  OnTouchListener {

        Die dieClone;
        Die indexedDie;

        public OnMenuDiceTouchListener(Die indexedDie) {

            this.indexedDie = indexedDie;
        }

        public boolean onTouch(View v, MotionEvent event) {
            {
                currentTouchedMenuDieIndex = indexedDie;
                menuDiceDetector.onTouchEvent(event);
                return false;
            }
        }
    }

    //Allows user to drag dice around the screen individually.
    class OnDiceTouchListener implements OnTouchListener {

        public OnDiceTouchListener() {

        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int width, height;
            AbsoluteLayout parent = (AbsoluteLayout) v.getParent();
            width = parent.getWidth();
            height = parent.getHeight();
            LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x_cord = (int) event.getRawX();
                    int y_cord = (int) event.getRawY();
                    Log.d("Tag", Integer.toString(x_cord));
                    if (x_cord > width) {
                        x_cord = width;
                    }
                    if (y_cord > height) {
                        y_cord = height;
                    }

                    layoutParams.x = x_cord - 75;
                    layoutParams.y = y_cord - 75;

                    v.setLayoutParams(layoutParams);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
    //Enables flick to transition to vs screen
    class vsSliderGestureListener extends GestureDetector.SimpleOnGestureListener {



        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() > event1.getX()) {
                try {
                    Intent newIntent = new Intent(getApplicationContext(), VsScreen.class);
                    startActivityForResult(newIntent, 0);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception ex) {
                }
            }
            return true;
        }
    }



    class menuDiceGestureListener extends GestureDetector.SimpleOnGestureListener {

        Context myContext;
        public menuDiceGestureListener(Context context) {
            myContext = context;
        }

        @Override
        public void onLongPress(MotionEvent event) {

            YesNoClickListener listener = new YesNoClickListener();
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage("Delete this die?").setPositiveButton("Yes", listener).setNegativeButton("No", listener).show();
        }

    }
    //Enables flick to open dice menu fragment
    class diceSliderGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getY() > event1.getY() && !menuIsOpen) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);

                MenuFragment fragment = new MenuFragment();
                fragmentTransaction.add(R.id.fragmentContainer, fragment,"diceMenu");
                fragmentTransaction.commit();
                menuIsOpen = true;

            }
            return true;
        }
    }
    //Enables flick to close dice menu fragment
    class fragmentDiceSliderGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getY() < event1.getY()) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);

                Fragment f = getFragmentManager().findFragmentByTag("diceMenu");
                fragmentTransaction.remove(f);
                fragmentTransaction.commit();
                menuIsOpen = false;


            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
