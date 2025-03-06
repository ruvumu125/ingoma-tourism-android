package com.ingoma.tourism.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.ingoma.tourism.R;

public class GoTextView extends AppCompatTextView {
    public GoTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            m();
        }
    }

    public final void m() {
        try {
            if (getTag() == null) {
                setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.medium))) {
                setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.bold))) {
                setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            } else if (getTag().equals(getResources().getString(R.string.thin))) {
                setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.regular))) {
                setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.light))) {
                setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.light_underline))) {
                setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                setPaintFlags(getPaintFlags() | 8);
            } else if (getTag().equals(getResources().getString(R.string.condensed_regular))) {
                setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.condensed_light))) {
                setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
            } else if (getTag().equals(getResources().getString(R.string.condensed_bold))) {
                setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
            } else if (getTag().equals(getResources().getString(R.string.font_medium_normal))) {
                setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            } else {
                setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            }
        } catch (Exception unused) {
        }
    }
}
