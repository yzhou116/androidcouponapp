package com.cvte.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.cvte.taobaounion.R;

import java.util.ArrayList;
import java.util.List;

public class TextFloatLayout extends ViewGroup {

    private List<String> mTextList = new ArrayList<>();
    public final static int DEFAULT_SPACE = 10;
    private float mHorizontalSpace = DEFAULT_SPACE;
    private float mVerticalSpace = DEFAULT_SPACE;
    public int selfWidth;
    public int mItemHeight;
    private onFloatTextListener mOnFloatTextLisenter = null;

    public float getmHorizontalSpace() {
        return mHorizontalSpace;
    }

    public void setmHorizontalSpace(float mHorizontalSpace) {
        this.mHorizontalSpace = mHorizontalSpace;
    }

    public float getmVerticalSpace() {
        return mVerticalSpace;
    }

    public void setmVerticalSpace(float mVerticalSpace) {
        this.mVerticalSpace = mVerticalSpace;
    }

    public TextFloatLayout(Context context) {
        this(context, null);
    }

    public TextFloatLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到相关属性：
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FloatTextStyle);
        mHorizontalSpace = ta.getDimension(R.styleable.FloatTextStyle_horizontalSpace, DEFAULT_SPACE);
        mVerticalSpace = ta.getDimension(R.styleable.FloatTextStyle_verticalSpace, DEFAULT_SPACE);

        ta.recycle();

    }

    public void setText(List<String> textList) {
        mTextList = textList;
        for (String text : mTextList) {
            TextView inflate = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.float_text_view,
                    this, false);
            inflate.setText(text);
            inflate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFloatTextLisenter != null) {
                        mOnFloatTextLisenter.onFloatTextClick(text);
                    }
                }
            });
            addView(inflate);
        }
    }
    //这个是描述单行：

    //这个是描述所有行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 0) {
            return;
        }
        List<View> line = null;
        lines.clear();
        selfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //测量
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                continue;
            }
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            if (line == null) {
                line = createNewline(itemView);
            } else {
                if (canBeAdd(itemView, line)) {
                    line.add(itemView);
                } else {
                    line = createNewline(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight
                + mVerticalSpace * (lines.size() + 1) + 0.5f);
        setMeasuredDimension(selfWidth, selfHeight);
    }

    private List<View> createNewline(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);

        return line;
    }
    //判断当前行是否可以添加：

    private boolean canBeAdd(View itemView, List<View> line) {
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度：
        totalWidth += mHorizontalSpace * (line.size() + 1);
        return totalWidth <= selfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放
        int topoffset = (int) mHorizontalSpace;
        for (List<View> views : lines) {
            int leftoffset = (int) mHorizontalSpace;

            for (View view : views) {
                view.layout(leftoffset, topoffset, leftoffset + view.getMeasuredWidth(), topoffset + view.getMeasuredHeight());
                leftoffset += view.getMeasuredWidth() + mHorizontalSpace;
            }
            topoffset = (int) (mItemHeight + mHorizontalSpace);

        }

    }

    public void setFloatTextClick(onFloatTextListener listener) {
        this.mOnFloatTextLisenter = listener;
    }

    public interface onFloatTextListener {
        void onFloatTextClick(String text);
    }
}
