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
    private Drawable[] pipDrawables;
    private Drawable lockDrawable;
    private Boolean locked = false;
    private int x;
    private int y;

    public Die() {
        this(6, Color.WHITE, Color.BLACK, false, 150, 150);
    }

    public Die(int numSides) {
        this(numSides, Color.WHITE, Color.BLACK, false, 150, 150);
    }

    public Die(int numSides, int sideColour, int numColour, Boolean pips) {
        this.numSides = numSides;
        setNumColour(numColour);
        setSideColour(sideColour);
        this.pips = pips;
        currentNumber = numSides;
        if (numSides != 6) {
            pips = false;
        }

        x = 150;
        y = 150;
    }

    public Die(int numSides, int sideColour, int numColour, Boolean pips,int x,int y) {
        this.numSides = numSides;
        setNumColour(numColour);
        setSideColour(sideColour);
        this.pips = pips;
        currentNumber = numSides;
        if (numSides != 6) {
            pips = false;
        }
        this.x = x;
        this.y = y;
    }



    public void toggleLock() {
        locked = !locked;
        generateImage();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createImageView(Context context) {

        callContext = context;
        imageView = new ImageView(context);
        lockDrawable = context.getDrawable(R.drawable.lock);
        setNumSides(numSides);
        setPips(pips);
        loadNumbers();
        colourNumbers();
        colourSides();
        generateImage();
    }

    public void generateImage() {
        if (locked) {
            Drawable layers[] = new Drawable[4];
            layers[0] = blankFace;
            layers[1] = overlay;
            if (pips) {
                layers[2] = pipDrawables[currentNumber-1];
            } else {
                layers[2] = numDrawables[currentNumber-1];
            }
            layers[3] = lockDrawable;
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            imageView.setImageDrawable(layerDrawable);
        } else {
            Drawable layers[] = new Drawable[3];
            layers[0] = blankFace;
            layers[1] = overlay;
            if (pips) {
                layers[2] = pipDrawables[currentNumber-1];
            } else {
                layers[2] = numDrawables[currentNumber-1];
            }
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            imageView.setImageDrawable(layerDrawable);
        }
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

    public void setNumSides(int num) {
        numSides = num;
        currentNumber = numSides;
        if (numSides != 6) {
            pips = false;
        }
        switch(numSides) {
            case 2:
                blankFace = callContext.getDrawable(R.drawable.d2blank);
                overlay = callContext.getDrawable(R.drawable.d2overlay);
                break;
            case 4:
                blankFace = callContext.getDrawable(R.drawable.d4blank);
                overlay = callContext.getDrawable(R.drawable.d4overlay);
                break;
            case 6:
                blankFace = callContext.getDrawable(R.drawable.d6blank);
                overlay = callContext.getDrawable(R.drawable.d6overlay);
                break;
            case 8:
                blankFace = callContext.getDrawable(R.drawable.d8blank);
                overlay = callContext.getDrawable(R.drawable.d8overlay);
                break;
            case 10:
                blankFace = callContext.getDrawable(R.drawable.d10blank);
                overlay = callContext.getDrawable(R.drawable.d10overlay);
                break;
            case 12:
                blankFace = callContext.getDrawable(R.drawable.d12blank);
                overlay = callContext.getDrawable(R.drawable.d12overlay);
                break;
            case 20:
                blankFace = callContext.getDrawable(R.drawable.d20blank);
                overlay = callContext.getDrawable(R.drawable.d20overlay);
                break;
            default:
                blankFace = callContext.getDrawable(R.drawable.d6blank);
                overlay = callContext.getDrawable(R.drawable.d6overlay);
                break;
        }
    }

    public void setPips(Boolean pip) {
        pips = pip;
        if (numSides != 6) {
            pips = false;
        }
    }

    @Override
    public Die clone() {
        return new Die(this.numSides, this.sideColour, this.numColour, this.pips, this.x, this.y);
    }


    private void loadNumbers() {
        int[] ID = {R.drawable.num1white, R.drawable.num2white, R.drawable.num3white, R.drawable.num4white, R.drawable.num5white, R.drawable.num6white, R.drawable.num7white, R.drawable.num8white, R.drawable.num9white, R.drawable.num10white, R.drawable.num11white, R.drawable.num12white, R.drawable.num13white, R.drawable.num14white, R.drawable.num15white, R.drawable.num16white, R.drawable.num17white, R.drawable.num18white, R.drawable.num19white, R.drawable.num20white};
        numDrawables = new Drawable[20];
        for (int i = 0; i<20; i++) {
            numDrawables[i] = callContext.getDrawable(ID[i]);
        }
        int pipID[] = {R.drawable.pip1, R.drawable.pip2, R.drawable.pip3, R.drawable.pip4, R.drawable.pip5, R.drawable.pip6};
        pipDrawables = new Drawable[6];
        for (int i = 0; i<6; i++) {
            pipDrawables[i] = callContext.getDrawable(pipID[i]);
        }

        colourNumbers();
    }

    private void colourNumbers() {
        for (int i = 0; i<20; i++) {
            numDrawables[i].mutate().setColorFilter(numColourFilter);
        }
        for (int i = 0; i<6; i++) {
            pipDrawables[i].mutate().setColorFilter(numColourFilter);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void roll() {
        if (!locked) {
            currentNumber = (int) (Math.random() * (numSides)) + 1;
            generateImage();
        }

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

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int xVal) {x = xVal;}

    public void setY(int yVal) {y = yVal;}

    //parcel part
    public Die(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.numSides = Integer.parseInt(data[0]);
        this.numColour = Integer.parseInt(data[1]);
        this.sideColour = Integer.parseInt(data[2]);
        this.x = Integer.parseInt(data[3]);
        this.y = Integer.parseInt(data[4]);

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
        dest.writeStringArray(new String[]{String.valueOf(this.numSides),String.valueOf(this.numColour), String.valueOf(this.numSides), String.valueOf(this.pips), String.valueOf(this.x), String.valueOf(this.y)});
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
