package com.example.a15657_000.myviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import java.util.zip.Inflater;

/**
 * Created by 15657_000 on 2017/9/13 0013.
 */

public class HorizontalScrollView extends ViewGroup {

    private int lastX,startX;
    private Scroller mScroller;
    private int mScreenWidth,fatherWidth;

    public HorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        MarginLayoutParams mg = (MarginLayoutParams) getLayoutParams();
        mg.width = mScreenWidth * count;
        fatherWidth = mg.width;
        setLayoutParams(mg);
        for(int i = 0;i < count;i++){
            View child = getChildAt(i);
            child.layout(i*mScreenWidth,t,(i+1)*mScreenWidth,b);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),0);
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                startX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dx = lastX - x;
                if(getScrollX() < 0 )
                    dx = 0;
                if(getScrollX() > fatherWidth - mScreenWidth)
                    dx = 0;
                scrollBy(dx,0);
                lastX = x;
                break;
            case MotionEvent.ACTION_UP:
                int endX = getScrollX();
                int back = endX % mScreenWidth;
                if(endX < startX){
                    back = -(mScreenWidth - back);
                }
                if(back  > 0){
                    if(back < mScreenWidth/3){
                        mScroller.startScroll(getScrollX(),0,
                                                -back,0);
                    }else {
                        mScroller.startScroll(getScrollX(),0,
                                                mScreenWidth-back,0);
                    }
                }else {
                    if(-back < mScreenWidth/3){
                        mScroller.startScroll(getScrollX(),0,
                                                -back,0);
                    }else {
                        mScroller.startScroll(getScrollX(),0,
                                                -mScreenWidth-back,0);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }
}
