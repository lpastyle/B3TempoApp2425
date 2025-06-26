package com.example.b3tempoapp2425;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class DayColorView extends View {
    private static final float CIRCLE_SCALE = 0.9f; // circle will occupy 90% of room's view
    // Custom attributes data model
    private String captionText;
    private int captionColor = Color.BLACK;
    private float captionTextSize = 0;
    private int dayCircleColor = Color.GRAY;
    private Context context;

    private TextPaint textPaint;
    private Paint circlePaint;

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
        this.context=context;
        // Load attributes
        try (TypedArray a = getContext().obtainStyledAttributes( // object in try-with-resource must implement AutoCloseable (RAII pattern)
                attrs,
                R.styleable.DayColorView,
                defStyle,
                0)) {
            captionText = a.getString(R.styleable.DayColorView_captionText);
            if (captionText == null) {
                captionText = context.getString(R.string.not_set);
            }
            captionColor = a.getColor(R.styleable.DayColorView_captionTextColor, captionColor);
            captionTextSize = a.getDimension(R.styleable.DayColorView_captionTextSize, getResources().getDimension(R.dimen.tempo_color_view_text_size));
            dayCircleColor = a.getColor(R.styleable.DayColorView_dayCircleColor, ContextCompat.getColor(context, R.color.tempo_undecided_day_bg));
        }

        // Set up a default TextPaint object
        textPaint = new TextPaint();
        setTextPaintAndMeasurements();

        // set up a default paint object
        circlePaint = new Paint();
        setCirclePaint();

    }

    private void setTextPaintAndMeasurements() {
        // set up a default TextPaint object
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(captionTextSize);
        textPaint.setColor(captionColor);
    }

    private void setCirclePaint() {
        // set up a paint object to draw circle
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(dayCircleColor);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw circle
        float radius = Math.min(contentHeight, contentWidth) * 0.5f * CIRCLE_SCALE;
        canvas.drawCircle(
                paddingLeft + contentWidth * 0.5f,
                paddingTop + contentHeight * 0.5f,
                radius,
                circlePaint);

        // Draw the text.
        canvas.drawText(captionText,
                paddingLeft + contentWidth  * 0.5f,
                paddingTop + contentHeight  * 0.5f,
                textPaint);
    }

    public void setDayCircleColor(TempoColor color) {
        dayCircleColor = ContextCompat.getColor(context, color.getColorResId());
        setCirclePaint();
        invalidate();
    }
}

