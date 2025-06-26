package com.example.b3tempoapp2425;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class DayColorView extends View {
    // Custom attributes data model
    private String captionText;
    private int captionColor = Color.BLACK;
    private float captionTextSize = 0;
    private int dayCircleColor = Color.GRAY;
    private Context context;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public DayColorView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DayColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DayColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        TypedArray a = getContext().obtainStyledAttributes( // object in try-with-resource must implement AutoCloseable (RAII pattern)
                attrs,
                R.styleable.DayColorView,
                defStyle,
                0);


    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

    }
}

