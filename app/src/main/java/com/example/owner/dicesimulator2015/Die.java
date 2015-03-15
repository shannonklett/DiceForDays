package com.example.owner.dicesimulator2015;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.widget.AbsoluteLayout;


public class Die {

    private int numSides;
    private int sideColour;
    private int currentNumber;
    private ColorFilter sideColourFilter;
    private int numColour;
    private ColorFilter numColourFilter;
    private Boolean pips;
    private ImageView imageView;
    private Context callContext;
    private Drawable blankFace;
    private Drawable[] numDrawables;

    public Die() {
        this(6, Color.WHITE, Color.BLACK, false);
    }

    public Die(int numSides) {
        this(numSides, Color.WHITE, Color.BLACK, false);
    }

    public Die(int numSides, int sideColour, int numColour, Boolean pips) {
        this.numSides = numSides;
        setNumColour(numColour);
        setSideColour(sideColour);
        this.pips = pips;
        currentNumber = numSides;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createImageView(Context context) {
        callContext = context;
        imageView = new ImageView(context);
        Drawable drawable = context.getDrawable(R.drawable.diewhiteblank);
        loadNumbers();
        drawable.mutate().setColorFilter(sideColourFilter);
        Drawable[] layers = {drawable, numDrawables[currentNumber-1]};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        imageView.setImageDrawable(layerDrawable);
    }

    public void setSideColour(int colour) {
        sideColour = colour;
        int red = (sideColour & 0xFF0000) / 0xFFFF;
        int green = (sideColour& 0xFF00) / 0xFF;
        int blue = sideColour & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0 };

        sideColourFilter = new ColorMatrixColorFilter(matrix);
    }

    public void setNumColour(int colour) {
        numColour = colour;
        int red = ( numColour & 0xFF0000) / 0xFFFF;
        int green = ( numColour& 0xFF00) / 0xFF;
        int blue =  numColour & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0 };

        numColourFilter = new ColorMatrixColorFilter(matrix);
    }


    private void loadNumbers() {
        int[] ID = {R.drawable.num1white, R.drawable.num2white, R.drawable.num3white, R.drawable.num4white, R.drawable.num5white, R.drawable.num6white, R.drawable.num7white, R.drawable.num8white, R.drawable.num9white, R.drawable.num10white, R.drawable.num11white, R.drawable.num12white, R.drawable.num13white, R.drawable.num14white, R.drawable.num15white, R.drawable.num16white, R.drawable.num17white, R.drawable.num18white, R.drawable.num19white, R.drawable.num20white};
        numDrawables = new Drawable[20];
        for (int i = 0; i<20; i++) {
            numDrawables[i] = callContext.getDrawable(ID[i]);
            numDrawables[i].mutate().setColorFilter(numColourFilter);
        }

    }
    public ImageView getImageView() {
        return imageView;
    }

    public void roll() {
        int randNum = (int) (Math.random() * 5);
    }

    public void setViewSize() {
        AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams)imageView.getLayoutParams();
        params.width = 150;
        params.height = 150;
        imageView.setLayoutParams(params);
    }

}