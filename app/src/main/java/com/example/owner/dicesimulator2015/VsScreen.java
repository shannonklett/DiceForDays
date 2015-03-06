package com.example.owner.dicesimulator2015;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class VsScreen extends ActionBarActivity {


    ImageView leftDie;
    ImageView rightDie;
    FrameLayout leftSide;
    FrameLayout rightSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        leftSide = (FrameLayout) this.findViewById(android.R.id.content).findViewById(R.id.leftSide);
        rightSide = (FrameLayout) this.findViewById(android.R.id.content).findViewById(R.id.rightSide);

        leftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollLeftDice(v);
            }
        });
        rightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollRightDice(v);
            }
        });

        setInitialDrawables();
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

    private void setInitialDrawables() {
        Drawable drawLeft = getResources().getDrawable(R.drawable.diered1);
        drawLeft = scaleDrawable(drawLeft, 150, 150);
        leftDie = (ImageView) findViewById(R.id.imageView);
        leftDie.setImageDrawable(drawLeft);

        Drawable drawRight = getResources().getDrawable(R.drawable.diewhite4);
        drawRight = scaleDrawable(drawRight, 150, 150);
        rightDie = (ImageView) findViewById(R.id.imageView2);
        rightDie.setImageDrawable(drawRight);


    }

    private Drawable scaleDrawable(Drawable toScale, int width, int height) {
        Bitmap bitmap = ((BitmapDrawable) toScale).getBitmap();
        return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    private void rollLeftDice(View v) {
        View tempView;
        ImageView child;
        int randNum;
        for (int i = 0; i < ((ViewGroup) v).getChildCount(); ++i) {
            tempView = ((ViewGroup) v).getChildAt(i);
            if (tempView instanceof ImageView) {
                child = (ImageView)tempView;
                randNum = (int) (Math.random() * 5);
                if (randNum == 0) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered1), 150, 150));
                } else if (randNum == 1) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered2), 150, 150));
                } else if (randNum == 2) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered3), 150, 150));
                } else if (randNum == 3) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered4), 150, 150));
                } else if (randNum == 4) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered5), 150, 150));
                } else {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diered6), 150, 150));
                }
            }
        }

    }

    private void rollRightDice(View v) {
        View tempView;
        ImageView child;
        int randNum;
        for (int i = 0; i < ((ViewGroup) v).getChildCount(); ++i) {
            tempView = ((ViewGroup) v).getChildAt(i);
            if (tempView instanceof ImageView) {
                child = (ImageView)tempView;
                randNum = (int) (Math.random() * 5);
                if (randNum == 0) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite1), 150, 150));
                } else if (randNum == 1) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite2), 150, 150));
                } else if (randNum == 2) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite3), 150, 150));
                } else if (randNum == 3) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite4), 150, 150));
                } else if (randNum == 4) {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite5), 150, 150));
                } else {
                    child.setImageDrawable(scaleDrawable(getResources().getDrawable(R.drawable.diewhite6), 150, 150));
                }
            }
        }
    }
}
