package com.example.owner.dicesimulator2015;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ImageView;
import android.graphics.ColorMatrixColorFilter;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private GestureDetectorCompat gestureDetectorCompat;
    AbsoluteLayout dieZone;
    ArrayList<Die> dice = new ArrayList<Die>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());
        dieZone = (AbsoluteLayout) this.findViewById(android.R.id.content).findViewById(R.id.dieZone);
        Die newDie = new Die(6, Color.BLUE, Color.BLACK, false);
        Die newDie2 = new Die(6, Color.GREEN, Color.WHITE, false);
        Die newDie3 = new Die(6, Color.RED, Color.BLUE, false);
        newDie.createImageView(this);
        newDie2.createImageView(this);

        dieZone.addView(newDie.getImageView());
        dieZone.addView(newDie2.getImageView());
        newDie.setViewSize();
        newDie2.setViewSize();
        dice.add(newDie);
        dice.add(newDie2);
        for (Die die : dice) {
            die.getImageView().setOnTouchListener(new OnDiceTouchListener());
        }

        dieZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Die die : dice) {
                    die.roll();
                }
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

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

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

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
