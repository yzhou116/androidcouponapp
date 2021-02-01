package com.cvte.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cvte.taobaounion.R;

public class LoadingView extends AppCompatImageView {
    private float degree = 0;
    private boolean needtoRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        needtoRotate = true;
        startRotate();

    }

    private void startRotate() {

        post(new Runnable() {
            @Override
            public void run() {
                degree += 10;
                if (degree >= 360) {
                    degree = 0;
                }
                invalidate();
                if (getVisibility() != VISIBLE && !needtoRotate) {

                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }


            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        needtoRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
