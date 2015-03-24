package com.example.owner.dicesimulator2015;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by owner on 2015-03-23.
 */
public class DieBunch implements Parcelable {

    private Die dieBunch;

    public DieBunch(Die bookBunch){
        this.dieBunch = bookBunch;
    }

    public Die getDieBunch(){
        return dieBunch;
    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(dieBunch.getNumSides());
        out.writeInt(dieBunch.getNumColour());
        out.writeInt(dieBunch.getSideColour());

        int valuePips;
        if (!dieBunch.getPips())
            valuePips = 1;
        else
            valuePips = 0;

        out.writeInt(valuePips);
    }

    public static final Creator<DieBunch> CREATOR = new Parcelable.Creator<DieBunch>() {
        public DieBunch createFromParcel(Parcel in) {
            return new DieBunch(in);
        }

        public DieBunch[] newArray(int size) {
            return new DieBunch[size];
        }
    };

    private DieBunch(Parcel in){
        int numSides = in.readInt();
        int numColour = in.readInt();
        int sideColour = in.readInt();
        int valuePips = in.readInt();

        Boolean pips;
        if (valuePips ==1)
            pips = false;
        else
            pips = true;


        dieBunch = new Die (numSides,sideColour,numColour,pips);

    }


}
