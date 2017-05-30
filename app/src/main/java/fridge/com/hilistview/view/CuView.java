package fridge.com.hilistview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Fridge on 17/5/23.
 */

public class CuView extends LinearLayout {

    private float mStartX;
    private float mStartY;
    private Scroller mScroller;
    /**
     * 第一次滑动是否完成
     */
    private boolean isFirstFinish;

    public CuView(Context context) {
        super(context);
        init(context);
    }

    public CuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    public CuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    /**
     * 让View跟着你的手指走吧
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 第一次移动完成后，我们不需要再去拿开始的位置了，否则造成View重新移动的最起始的位置。
                 */
                if (!isFirstFinish) {
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                scrollTo((int) (mStartX - event.getRawX()), (int) (mStartY - event.getRawY()));
                break;
            case MotionEvent.ACTION_UP:
                //第一次移动完成
                isFirstFinish = true;
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        Log.e("shitshit", "【" + this.getClass().getSimpleName() + "】   scrollTo() called with: " + "x = [" + x + "], y = [" + y + "]");
        super.scrollTo(x, y);
    }

    /**
     * 测试startScroll
     */
    public void startScroll() {
        /**
         * 注意Scroller移动方向，
         */
        mScroller.startScroll(20, 20, -500, -500, 5000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }
}
