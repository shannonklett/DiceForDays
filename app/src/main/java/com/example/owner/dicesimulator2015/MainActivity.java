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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.ColorMatrixColorFilter;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private GestureDetector vsSliderDetector;
    private GestureDetector diceSliderDetector;
    private GestureDetector fragmentDiceSliderDetector;
    AbsoluteLayout dieZone;
    ImageView vsSlider;
    ImageView diceSlider;
    ImageView fragmentDiceSlider;
    Boolean menuIsOpen = false;
    Boolean selectingLock = false;
    Boolean selectingRemove = false;
    GridLayout fragmentGrid;
    Die currentTouchedMenuDieIndex;
    ImageButton lockButton;
    ImageButton removeButton;
    ImageView garbage;
    Boolean isOverGarbage = false;


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
        dieZone = (AbsoluteLayout) this.findViewById(R.id.dieZone);
        vsSlider = (ImageView)this.findViewById(R.id.vsSlider);
        diceSlider = (ImageView)this.findViewById(R.id.diceSlider);
        lockButton = (ImageButton)this.findViewById(R.id.lockButton);
        garbage = (ImageView)this.findViewById(R.id.garbage);

        //returning from vs screen or customization screen
        if (this.getIntent().getStringExtra("flag") != null){
            //update list of die in the menu
            dieSaved = getIntent().getParcelableArrayListExtra("dieBunch");

                for (DieBunch point : dieSaved) {
                    diceList.add(point.getDieBunch());
                }
            /*
            diceOnScreenSaved = getIntent().getParcelableArrayListExtra("dieOnScreenBunch");
                //check to see if there were dice on screen previously
                if(!(diceOnScreenSaved.isEmpty())){
                    //add dice to diceOnScreen current arrayList
                    for (DieBunch point : diceOnScreenSaved){
                        diceOnScreen.add(point.getDieBunch());
                    }
                }
            */



        }
        else
            {
                //default dice
                Die newDie = new Die(6,Color.WHITE, Color.BLACK, true);
                newDie.createImageView(this);
                diceList.add(newDie);
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
                selectingLock = false;
                lockButton.setImageDrawable(v.getContext().getDrawable(R.drawable.lockbutton));
                if (menuIsOpen) {
                    closeFragment();
                    fragmentGrid.removeViews(2, fragmentGrid.getChildCount() - 2);
                }
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
        ((ViewGroup) fragmentGrid).removeViews(2, fragmentGrid.getChildCount() - 2);
        int currentCol = 0;
        int currentRow = 0;
        GridLayout.Spec row;
        GridLayout.Spec col;
        GridLayout.LayoutParams gridLayoutParams;
        for (Die die : diceList) {


            if (currentCol > 3) {
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


    public void saveDice() {
        dieSaved.clear();
        for(Die point: diceList){
            dieSaved.add(new DieBunch(point));
        }
    }


    //add dice button listener inside fragment
    public void addDice (View v) {
        for (Die point : diceOnScreen) {
            System.out.println("Dice position: ");
            System.out.println(point.getX());
            System.out.println(point.getY());
        }

        if (diceList.size() >= 8) {
            Context context = getApplicationContext();
            CharSequence text = "Your dice drawer is full! Delete dice before creating another!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Intent j = new Intent(
                    MainActivity.this,
                    CustomizationScreen.class);
            saveDice();
            j.putExtra("dieBunch", dieSaved);
            j.putExtra("flag", "main");
            j.putExtra("caller", "MainActivity");
            startActivity(j);

        }
    }



    //Sets up listener on the Dice tab on the fragment menu to close it when flicked up
    public void setFragmentTouchListeners() {
        removeButton = (ImageButton)this.findViewById(R.id.removeButton);
        fragmentDiceSlider = (ImageView)this.findViewById(R.id.fragmentDiceSlider);
        fragmentDiceSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                fragmentDiceSliderDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void toggleRemove(View v) {
        if (selectingRemove) {
            selectingRemove = false;
            removeButton.setImageDrawable(getApplication().getDrawable(R.drawable.removedie));
        } else {
            selectingRemove = true;
            removeButton.setImageDrawable(getApplication().getDrawable(R.drawable.removedieselected));
        }
    }
    //Adds the given die to the screen (diceOnScreen arraylist as well)
    public void addDieToScreen(Die die) {
        Die dieToAdd = die.clone();
        dieToAdd.createImageView(this);
        diceOnScreen.add(dieToAdd);
        dieToAdd.getImageView().setOnTouchListener(new OnDiceTouchListener(dieToAdd));
        dieZone.addView(dieToAdd.getImageView(), 150, 150);

        Context context = getApplicationContext();
        CharSequence text = "Tap screen to roll";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    class OnClickMenuDiceListener implements View.OnClickListener {

        private Die die;
        public OnClickMenuDiceListener(Die die) {
            this.die = die;
        }

        public void onClick (View v) {
            if (selectingRemove) {
                YesNoClickListener listener = new YesNoClickListener();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Delete this die?").setPositiveButton("Yes", listener).setNegativeButton("No", listener).show();
            } else {
                addDieToScreen(die);
            }
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
            selectingRemove = false;
            removeButton.setImageDrawable(getApplication().getDrawable(R.drawable.removedie));
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
                return false;
            }
        }
    }

    //Allows user to drag dice around the screen individually.
    class OnDiceTouchListener implements OnTouchListener {

        Die die;

        public OnDiceTouchListener(Die die) {
            this.die = die;
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
                    if (selectingLock) {
                        die.toggleLock();
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    if (x < 150 && y > height - 150) {
                        parent.removeView(v);
                        diceOnScreen.remove(die);
                        garbage.setImageDrawable(getApplication().getDrawable(R.drawable.deleteicon));
                        isOverGarbage = false;

                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!selectingLock) {
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();
                        if (x_cord > width) {
                            x_cord = width;
                        }
                        if (y_cord > height) {
                            y_cord = height;
                        }
                        if (x_cord < 150 && y_cord > height - 150) {
                            if (!isOverGarbage) {
                                isOverGarbage = true;
                                garbage.setImageDrawable(getApplication().getDrawable(R.drawable.deleteiconselected));
                            }
                        } else {
                            if (isOverGarbage) {
                                isOverGarbage = false;
                                garbage.setImageDrawable(getApplication().getDrawable(R.drawable.deleteicon));
                            }
                        }

                        layoutParams.x = x_cord - layoutParams.width/2;
                        layoutParams.y = y_cord - 100;

                        v.setLayoutParams(layoutParams);

                        die.setX(layoutParams.x);
                        die.setY(layoutParams.y);
                    }
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
                    saveDice();
                    Intent newIntent = new Intent(getApplicationContext(), VsScreen.class);
                    newIntent.putExtra("dieBunch", dieSaved);
                    newIntent.putExtra("flag", "main");
                    startActivityForResult(newIntent, 0);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception ex) {
                }
            }
            return true;
        }
    }



    //Enables flick to open dice menu fragment
    class diceSliderGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getY() > event1.getY() && !menuIsOpen) {
                diceSlider.setVisibility(View.INVISIBLE);
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

                closeFragment();

            }
            return true;
        }
    }

    public void closeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);

        Fragment f = getFragmentManager().findFragmentByTag("diceMenu");
        fragmentTransaction.remove(f);
        fragmentTransaction.commit();
        menuIsOpen = false;
        diceSlider.setVisibility(View.VISIBLE);
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

    public void toggleLockMode(View v) {
        selectingLock = !selectingLock;
        if (menuIsOpen) {
            closeFragment();
        }
        if (selectingLock)
            lockButton.setImageDrawable(this.getDrawable(R.drawable.lockbuttonselected));
        else
            lockButton.setImageDrawable(this.getDrawable(R.drawable.lockbutton));
    }
}
