package com.example.owner.dicesimulator2015;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.widget.AbsoluteLayout;


public class Die implements Parcelable {

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
    private Drawable overlay;
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
        switch(numSides) {
            case 2:     blankFace = context.getDrawable(R.drawable.d6blank);
                        overlay = context.getDrawable(R.drawable.d6overlay);
                        break;
            case 4:     blankFace = context.getDrawable(R.drawable.d4blank);
                        overlay = context.getDrawable(R.drawable.d4overlay);
                        break;
            case 6:     blankFace = context.getDrawable(R.drawable.d6blank);
                        overlay = context.getDrawable(R.drawable.d6overlay);
                        break;
            case 8:     blankFace = context.getDrawable(R.drawable.d8blank);
                        overlay = context.getDrawable(R.drawable.d8overlay);
                        break;
            case 10:    blankFace = context.getDrawable(R.drawable.d10blank);
                        overlay = context.getDrawable(R.drawable.d10overlay);
                        break;
            case 12:    blankFace = context.getDrawable(R.drawable.d12blank);
                        overlay = context.getDrawable(R.drawable.d12overlay);
                        break;
            case 20:    blankFace = context.getDrawable(R.drawable.d20blank);
                        overlay = context.getDrawable(R.drawable.d20overlay);
                        break;
            default:    blankFace = context.getDrawable(R.drawable.d6blank);
                        overlay = context.getDrawable(R.drawable.d6overlay);
                        break;
        }

        loadNumbers();
        colourNumbers();
        colourSides();
        generateImage();
    }

    public void generateImage() {
        Drawable[] layers = {blankFace, overlay, numDrawables[currentNumber-1]};
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
        if (imageView != null) {
            colourSides();
        }

    }

    private void colourSides() {
        blankFace.mutate().setColorFilter(sideColourFilter);
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
        if (imageView != null) {
            colourNumbers();
        }
    }

    @Override
    public Die clone() {
        return new Die(this.numSides, this.sideColour, this.numColour, this.pips);
    }


    private void loadNumbers() {
        int[] ID = {R.drawable.num1white, R.drawable.num2white, R.drawable.num3white, R.drawable.num4white, R.drawable.num5white, R.drawable.num6white, R.drawable.num7white, R.drawable.num8white, R.drawable.num9white, R.drawable.num10white, R.drawable.num11white, R.drawable.num12white, R.drawable.num13white, R.drawable.num14white, R.drawable.num15white, R.drawable.num16white, R.drawable.num17white, R.drawable.num18white, R.drawable.num19white, R.drawable.num20white};
        numDrawables = new Drawable[20];
        for (int i = 0; i<20; i++) {
            numDrawables[i] = callContext.getDrawable(ID[i]);
        }
        colourNumbers();
    }

    private void colourNumbers() {
        for (int i = 0; i<20; i++) {
            numDrawables[i].mutate().setColorFilter(numColourFilter);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void roll() {
        currentNumber = (int) (Math.random() * (numSides)) + 1;
        generateImage();
    }

    public void setViewSize() {
        AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams)imageView.getLayoutParams();
        params.width = 150;
        params.height = 150;
        imageView.setLayoutParams(params);
    }

    public int getNumSides(){
        return numSides;
    }

    public int getNumColour(){
        return numColour;
    }

    public int getSideColour(){
        return sideColour;
    }

    public Boolean getPips(){
        return pips;
    }

    //parcel part
    public Die(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.numSides = Integer.parseInt(data[0]);
        this.numColour = Integer.parseInt(data[1]);
        this.sideColour = Integer.parseInt(data[2]);

        int valuePips;
        if (!this.pips)
            valuePips = 1;
        else
            valuePips = 0;

        valuePips = Integer.parseInt((data[3]));
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeStringArray(new String[]{String.valueOf(this.numSides),String.valueOf(this.numColour), String.valueOf(this.numSides), String.valueOf(this.pips)});
    }

    public static final Parcelable.Creator<Die> CREATOR= new Parcelable.Creator<Die>() {

        @Override
        public Die createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Die(source);  //using parcelable constructor
        }

        @Override
        public Die[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Die[size];
        }
    };

}
