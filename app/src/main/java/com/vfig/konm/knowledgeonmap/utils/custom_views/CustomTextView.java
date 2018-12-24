package com.vfig.konm.knowledgeonmap.utils.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.vfig.konm.knowledgeonmap.R;

/**
 * Created by pmishra on 1/19/2016.
 */
public class CustomTextView extends android.support.v7.widget.AppCompatTextView {


    private final static int MerriweatherSans_Regular = 0;
    private final static int MerriweatherSans_Bold = 1;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.CustomTextView_typeface, 0);

        switch (typeface) {

            case MerriweatherSans_Regular:
                setTypeface(Typeface.createFromAsset(
                        context.getAssets(), "MerriweatherSans_Regular.otf"));
                break;

            case MerriweatherSans_Bold:
                setTypeface(Typeface.createFromAsset(
                        context.getAssets(), "MerriweatherSans_Bold.otf"));
                break;
        }

        values.recycle();
    }
}
