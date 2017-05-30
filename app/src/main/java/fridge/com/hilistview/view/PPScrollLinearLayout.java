package fridge.com.hilistview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;


import fridge.com.hilistview.utils.FloatUtils;


public class PPScrollLinearLayout extends LinearLayout {

    private final static int ANIMATOR_DURATION = 200; // 默认动画时长
    protected int mTouchSlop; // 滑动阈值

    private ValueAnimator mAnimator; // 显示隐藏的动画
    protected float mLastY = 0; // 上次Y位置
    private float mInitialX = 0; // 初始X位置
    private float mInitialY = 0; // 初始Y位置
    private boolean mHasDownEvent = false; // 是否接收到了Down事件
    protected IPPScrollControlListener mControlListener; // 滑动控制

    public PPScrollLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public PPScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PPScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private final void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 事件分发开始方法
     */
    protected void onDispatchStart() {
    }

    /**
     * 向上事件处理拦截
     *
     * @return 是否拦截
     */
    protected boolean onUpIntercept() {
        return false;
    }

    /**
     * 向下事件处理拦截
     *
     * @return 是否拦截
     */
    protected boolean onDownIntercept() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean interceptSelf = false;

        // 检查是否需要滑动
        if (mControlListener != null && mControlListener.shouldDispatchTouch()) {
            onDispatchStart();
            int action = ev.getActionMasked();
            int scrollDistance = mControlListener.getScrollDistance();
            float translationY = getCurrentTranslationY();
            // 停止当前的动画
            stopAnimator();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // 第一个手指按下时 初始化状态
                    mInitialX = ev.getRawX();
                    mInitialY = mLastY = ev.getRawY();

                    mHasDownEvent = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!mHasDownEvent) {
                        break;
                    }
                    final float currentX = ev.getRawX();
                    final float currentY = ev.getRawY();
                    // 判断手指滑动状态 横向滑动的时候不处理
                    float xDiff = Math.abs(currentX - mInitialX);
                    float yDiff = Math.abs(currentY - mInitialY);
                    if (xDiff > mTouchSlop || yDiff > mTouchSlop) {
                        if (yDiff > xDiff) {
                            float deltaY = ev.getRawY() - mLastY;
                            if (deltaY > 0) {
                                if (onDownIntercept()) {
                                    break;
                                }
                                // 下拉，显示top
                                if (translationY < 0) {
                                    interceptSelf = true;
                                    if (translationY + deltaY > 0) {
                                        innerTranslationY(0);
                                    } else {
                                        innerTranslationY(translationY + deltaY);
                                    }
                                }
                            } else {
                                if (onUpIntercept()) {
                                    break;
                                }
                                // 上滑，隐藏top
                                if (translationY > -scrollDistance) {
                                    interceptSelf = true;
                                    if (translationY + deltaY < -scrollDistance) {
                                        innerTranslationY(-scrollDistance);
                                    } else {
                                        innerTranslationY(translationY + deltaY);
                                    }
                                }
                            }
                        }
                    }

                    mLastY = currentY;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (!mHasDownEvent) {
                        break;
                    }
                    // 松手的时候自动显示完整或者隐藏
                    if (translationY < 0 && translationY > -scrollDistance) {
                        if (translationY < -scrollDistance / 2f) {
                            hideScrollSpace();
                        } else {
                            showScrollSpace();
                        }
                    }
                    mHasDownEvent = false;
                    break;
                default:
                    break;
            }
        }
        return interceptSelf || super.dispatchTouchEvent(ev);
    }

    /**
     * 停止动画
     * 对每一个ChildView在Y轴上位移
     *
     * @param translationY Y轴位移距离
     */
    public void translationY(float translationY) {
        stopAnimator();
        innerTranslationY(translationY);
    }

    /**
     * 内部移动 不会停止动画
     */
    private void innerTranslationY(float translationY) {
        setTranslationY(translationY);
    }



    /**
     * 停止动画
     */
    private void stopAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    /**
     * 显示隐藏的顶部空间
     */
    public void showScrollSpace() {
        if (mControlListener == null || !mControlListener.shouldDispatchTouch()) {
            return;
        }
        float translationY = getCurrentTranslationY();
        if (FloatUtils.floatsEqual(translationY, 0)) {
            return;
        }
        mAnimator = ObjectAnimator.ofFloat(this, "TranslationY", translationY, 0).setDuration(ANIMATOR_DURATION);
        mAnimator.setInterpolator(new DecelerateInterpolator(2));
        mAnimator.start();
    }

    /**
     * 隐藏顶部空间
     */
    public void hideScrollSpace() {
        if (mControlListener == null || !mControlListener.shouldDispatchTouch()) {
            return;
        }
        int distance = mControlListener.getScrollDistance();
        float translationY = getCurrentTranslationY();
        if (FloatUtils.floatsEqual(translationY + distance, 0)) {
            return;
        }
        mAnimator = ObjectAnimator.ofFloat(this, "TranslationY", translationY, -distance).setDuration(ANIMATOR_DURATION);
        mAnimator.setInterpolator(new AccelerateInterpolator(2));
        mAnimator.start();
    }

    /**
     * 设置ScrollControlListener
     */
    public void setIPPScrollControlListener(IPPScrollControlListener controlListener) {
        if (this.mControlListener != controlListener) {
            this.mControlListener = controlListener;
        }
    }

    /**
     * 获取当前的Y轴位移
     *
     * @return 当前的Y轴位移
     */
    public float getCurrentTranslationY() {
        return getTranslationY();
    }

    /**
     * 滑动控制的interface
     */
    public interface IPPScrollControlListener {

        /**
         * @return 是否应该滑动
         */
        boolean shouldDispatchTouch();

        /**
         * @return 滑动距离
         */
        int getScrollDistance();

    }

}

