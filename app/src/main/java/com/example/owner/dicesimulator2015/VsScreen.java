package com.example.owner.dicesimulator2015;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;


public class VsScreen extends ActionBarActivity {


    ImageView leftDie;
    ImageView rightDie;
    AbsoluteLayout leftSide;
    AbsoluteLayout rightSide;
    ArrayList<Die> leftDice = new ArrayList<Die>();
    ArrayList<Die> rightDice = new ArrayList<Die>();
    ArrayList<Die> diceList = new ArrayList<Die>();
    ArrayList<DieBunch> dieSaved = new ArrayList<DieBunch>();
    ImageView soloSlider;
    ImageView diceSlider;
    ImageView fragmentDiceSlider;
    Boolean menuIsOpen = false;
    GridLayout fragmentGrid;
    Die currentTouchedMenuDieIndex;
    private GestureDetector soloSliderDetector;
    private GestureDetector diceSliderDetector;
    private GestureDetector fragmentDiceSliderDetector;
    Boolean selectingSide = false;
    Boolean selectingLock = false;
    Boolean selectingRemove = false;
    Die selectedDie;
    ImageButton lockButton;
    ImageButton removeButton;
    ImageView garbage;
    Boolean isOverGarbage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        soloSliderDetector = new GestureDetector(new soloSliderGestureListener());
        diceSliderDetector = new GestureDetector(new diceSliderGestureListener());
        fragmentDiceSliderDetector = new GestureDetector(new fragmentDiceSliderGestureListener());
        leftSide = (AbsoluteLayout) this.findViewById(R.id.leftSide);
        rightSide = (AbsoluteLayout) this.findViewById(R.id.rightSide);
        soloSlider = (ImageView)this.findViewById(R.id.soloSlider);
        diceSlider = (ImageView)this.findViewById(R.id.diceSlider);
        lockButton = (ImageButton)this.findViewById(R.id.lockButton);
        garbage = (ImageView)this.findViewById(R.id.garbage);

        if (this.getIntent().getStringExtra("flag") != null){
            //update list of die in the menu
            dieSaved = getIntent().getParcelableArrayListExtra("dieBunch");

            for (DieBunch point : dieSaved) {
                diceList.add(point.getDieBunch());
            }

        }

        for (Die die : leftDice) {
            die.getImageView().setOnTouchListener(new OnDiceTouchListener(die));
        }
        for (Die die : rightDice) {
            die.getImageView().setOnTouchListener(new OnDiceTouchListener(die));
        }

        leftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectingSide) {
                    addDieToScreen(selectedDie, true);
                    selectingSide = false;
                } else {
                    if (menuIsOpen) {
                        closeFragment();
                    }
                    if (selectingLock) {
                        selectingLock = false;
                        lockButton.setImageDrawable(v.getContext().getDrawable(R.drawable.lockbutton));
                    }

                    for (Die die : leftDice) {
                        die.roll();
                    }
                }
            }
        });
        rightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectingSide) {
                    addDieToScreen(selectedDie, false);
                    selectingSide = false;
                } else {
                    if (menuIsOpen) {
                        closeFragment();
                    }
                    if (selectingLock) {
                        selectingLock = false;
                        lockButton.setImageDrawable(v.getContext().getDrawable(R.drawable.lockbutton));
                    }
                    for (Die die : rightDice) {
                        die.roll();
                    }
                }
            }
        });

        soloSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                soloSliderDetector.onTouchEvent(event);
                return true;
            }
        });

        diceSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                diceSliderDetector.onTouchEvent(event);
                return true;
            }
        });

    }

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
            Die newDie = die;
            newDie.createImageView(this);
            fragmentGrid.addView(newDie.getImageView(), gridLayoutParams);
            newDie.getImageView().setOnTouchListener(new OnMenuDiceTouchListener(die));
            newDie.getImageView().setOnClickListener(new OnClickMenuDiceListener(newDie));
            currentCol++;
            //fragmentGrid.addView(fragmentGrid.findViewById(R.id.addDice));

        }

    }

    public void addDice (View v) {
        if (diceList.size() >= 8) {
            Context context = getApplicationContext();
            CharSequence text = "Your dice drawer is full! Delete dice before creating another!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Intent j = new Intent(
                    VsScreen.this,
                    CustomizationScreen.class);
            saveDice();
            j.putExtra("dieBunch", dieSaved);
            j.putExtra("flag", "main");
            j.putExtra("caller", "VsScreen");
            startActivity(j);

        }
    }

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

    public void saveDice() {
        dieSaved.clear();
        for(Die point: diceList){
            dieSaved.add(new DieBunch(point));
        }
    }

    public void addDieToScreen(Die die, Boolean addToLeft) {

        Die dieToAdd = die.clone();
        dieToAdd.createImageView(this);
        if (addToLeft) {
            leftDice.add(dieToAdd);
            dieToAdd.getImageView().setOnTouchListener(new OnDiceTouchListener(dieToAdd));
            leftSide.addView(dieToAdd.getImageView(), 150, 150);
        } else {
            rightDice.add(dieToAdd);
            dieToAdd.getImageView().setOnTouchListener(new OnDiceTouchListener(dieToAdd));
            rightSide.addView(dieToAdd.getImageView(), 150, 150);
        }
    }

    class OnClickMenuDiceListener implements View.OnClickListener {

        private Die die;
        public OnClickMenuDiceListener(Die die) {
            this.die = die;
        }

        public void onClick (View v) {
            if (selectingSide) {

            } else {
                if (selectingRemove) {
                    YesNoClickListener listener = new YesNoClickListener();
                    AlertDialog.Builder builder = new AlertDialog.Builder(VsScreen.this);
                    builder.setMessage("Delete this die?").setPositiveButton("Yes", listener).setNegativeButton("No", listener).show();
                } else {
                    selectingSide = true;
                    Context context = getApplicationContext();
                    CharSequence text = "Tap which side you want to add the die to";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    selectedDie = die;
                }

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

    class OnMenuDiceTouchListener implements View.OnTouchListener {

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

    class soloSliderGestureListener extends GestureDetector.SimpleOnGestureListener {



        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() < event1.getX()) {
                try {
                    saveDice();
                    Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                    newIntent.putExtra("dieBunch", dieSaved);
                    newIntent.putExtra("flag", "vs");
                    startActivityForResult(newIntent, 0);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (Exception ex) {
                }
            }
            return true;
        }
    }

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

    class fragmentDiceSliderGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getY() < event1.getY()) {
                closeFragment();

            }
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customization_screen, menu);
        return true;
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


    class OnDiceTouchListener implements View.OnTouchListener {

        Die die;
        Boolean lockToggled = false;

        public OnDiceTouchListener(Die die) {
            this.die = die;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int width, height;
            AbsoluteLayout parent = (AbsoluteLayout) v.getParent();
            width = parent.getWidth();
            height = parent.getHeight();
            AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (selectingLock) {
                        die.toggleLock();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    int x = (int)event.getRawX();
                    int y = (int)event.getRawY();
                    if (parent.getLeft() > 400) {
                        x -= width;
                        if (x< 150 && y> height - 150) {
                            parent.removeView(v);
                            rightDice.remove(die);
                            isOverGarbage = false;
                            garbage.setImageDrawable(getApplication().getDrawable(R.drawable.deleteicon));
                        }
                    } else {
                        if (x > width - 150 && y > height - 150) {
                            parent.removeView(v);
                            leftDice.remove(die);
                            isOverGarbage = false;
                            garbage.setImageDrawable(getApplication().getDrawable(R.drawable.deleteicon));
                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!selectingLock) {
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();
                        if (parent.getLeft() > 400) {

                            x_cord -= width;
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
                            if (x_cord < 75) {
                                x_cord = 75;
                            }
                        } else {
                            if (x_cord > width - 150 && y_cord > height - 150) {
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
                            if (x_cord < 55) {
                                x_cord = 55;
                            }
                        }
                        if (x_cord > width - 75) {
                            x_cord = width - 75;
                        }

                        if (y_cord > height - 75) {
                            y_cord = height - 75;
                        }
                        if (y_cord < 75) {
                            y_cord = 75;
                        }

                        layoutParams.x = x_cord - 75;
                        layoutParams.y = y_cord - 75;

                        v.setLayoutParams(layoutParams);
                    }
                    break;
                default:
                    break;
            }

            return true;

        }
    }
    public void toggleLockMode(View v) {
        if (!selectingSide) {
            if (menuIsOpen) {
                closeFragment();
            }
            selectingLock = !selectingLock;
            if (selectingLock)
                lockButton.setImageDrawable(this.getDrawable(R.drawable.lockbuttonselected));
            else
                lockButton.setImageDrawable(this.getDrawable(R.drawable.lockbutton));
        }

    }
}