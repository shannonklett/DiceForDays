package com.example.owner.dicesimulator2015;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;


public class VsScreen extends ActionBarActivity {


    ImageView leftDie;
    ImageView rightDie;
    AbsoluteLayout leftSide;
    AbsoluteLayout rightSide;
    ArrayList<Die> leftDice = new ArrayList<Die>();
    ArrayList<Die> rightDice = new ArrayList<Die>();
    ImageView soloSlider;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        gestureDetector = new GestureDetector(new MyGestureListener());
        leftSide = (AbsoluteLayout) this.findViewById(R.id.leftSide);
        rightSide = (AbsoluteLayout) this.findViewById(R.id.rightSide);
        soloSlider = (ImageView)this.findViewById(R.id.soloSlider);


        Die newDie = new Die(6, Color.BLUE, Color.BLACK, false);
        Die newDie2 = new Die(6, Color.GREEN, Color.WHITE, false);
        Die newDie3 = new Die(6, Color.RED, Color.BLUE, false);
        Die newDie4 = new Die(12, Color.CYAN, Color.BLACK, false);
        newDie.createImageView(this);
        newDie2.createImageView(this);
        newDie3.createImageView(this);
        newDie4.createImageView(this);

        leftSide.addView(newDie.getImageView());
        leftSide.addView(newDie3.getImageView());
        rightSide.addView(newDie2.getImageView());
        rightSide.addView(newDie4.getImageView());
        newDie.setViewSize();
        newDie2.setViewSize();
        newDie3.setViewSize();
        newDie4.setViewSize();
        leftDice.add(newDie);
        leftDice.add(newDie3);
        rightDice.add(newDie2);
        rightDice.add(newDie4);

        for (Die die : leftDice) {
            die.getImageView().setOnTouchListener(new OnDiceTouchListener());
        }
        for (Die die : rightDice) {
            die.getImageView().setOnTouchListener(new OnDiceTouchListener());
        }

        leftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Die die : leftDice) {
                    die.roll();
                }
            }
        });
        rightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Die die : rightDice) {
                    die.roll();
                }
            }
        });

        soloSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customization_screen, menu);
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


    class OnDiceTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int width, height;
            AbsoluteLayout parent = (AbsoluteLayout) v.getParent();
            width = parent.getWidth();
            height = parent.getHeight();
            AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x_cord = (int) event.getRawX();
                    int y_cord = (int) event.getRawY();
                    Log.d("Tag", Integer.toString(x_cord));
                    if (parent.getLeft() > 400) {
                        x_cord -= width;
                        if (x_cord < 95) {
                            x_cord = 95;
                        }
                    } else {
                        if (x_cord < 75) {
                            x_cord = 75;
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
                    break;
                default:
                    break;
            }
            return true;
        }
    }
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() < event1.getX()) {
                try {
                    Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(newIntent, 0);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (Exception ex) {
                }
            }
            return true;
        }
    }
}
