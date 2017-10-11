package com.example.a15657_000.myviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by 15657_000 on 2017/9/13 0013.
 */

public class MyViewGroup extends ViewGroup {

    private int lastY,startY,endY;
    private int mScreenHeight;
    private Scroller scroller;
    private int fatherHeight;



    public MyViewGroup(Context context) {
        super(context);
        init(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreenHeight = dm.heightPixels;
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
         }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int count =  getChildCount();
            MarginLayoutParams mp = (MarginLayoutParams) getLayoutParams();
            mp.height = mScreenHeight * count;
            //Log.d("height", "onLayout: "+mp.height);
        fatherHeight = mp.height;
            setLayoutParams(mp);
            for(int i = 0;i < count;i++){
                View child = getChildAt(i);
                if(child.getVisibility()!=GONE){
                    child.layout(l,i*mScreenHeight,r,(i+1)*mScreenHeight);
                }
            }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(0,scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                startY = getScrollY();//所谓的getScrollY()可以理解为获取当前屏幕坐标系y轴的坐标 屏幕一开始的左上角坐标为0,0, 之后也不会发生改变
                break;
            case MotionEvent.ACTION_MOVE:
                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                }
                int dy = lastY - y;
                if(getScrollY() < 0)//到顶了停止
                {
                    dy = 0;
                }
               // Log.d("value", "onTouchEvent: Y:"+getScrollY() +" H:"+getHeight()+" SH:"+mScreenHeight);
                if(getScrollY() > fatherHeight - mScreenHeight)//到底了停止
                {
                    dy = 0;
                }
                scrollBy(0,dy);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                int mEnd = getScrollY();//这个值永远大于0
                int dScrollY = mEnd % mScreenHeight;//屏幕往下移
                if(mEnd < startY)//屏幕往上移
                {
                    dScrollY = -(mScreenHeight - dScrollY);
                }
                //当対向上或者向下的最后一个子View而言 滑动超过自身及屏幕长的三分之一时 自动展开 反之 自动收缩
                if(dScrollY > 0){
                    if(dScrollY < mScreenHeight/3){
                        scroller.startScroll(0,getScrollY(),
                                            0,-dScrollY);
                    }else{
                        scroller.startScroll(0,getScrollY(),
                                            0,mScreenHeight-dScrollY);
                    }
                }else {
                    if(-dScrollY < mScreenHeight/3){
                        scroller.startScroll(0,getScrollY(),
                                            0,-dScrollY);
                    }else {
                        scroller.startScroll(0,getScrollY(),
                                            0,-mScreenHeight-dScrollY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }
}
