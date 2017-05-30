package fridge.com.hilistview.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liyong on 16/5/5.
 */
public class ViewUtils {
    public static final String TAG = " ViewUtils";

    private ViewUtils() {

    }

    public static int px2dp(Context context, float pixelValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pixelValue / density + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    public static void setBackground(View view, int resId) {
        if (view == null) {
            return;
        }
        view.setBackgroundResource(resId);
    }

    public static void setDrawableLeftOfTextView(View view, int resId) {
        if (view instanceof TextView) {
            TextView temp = (TextView) view;
            temp.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        }
    }

    public static void setDrawableTopOfTextView(View view, int resId) {
        if (view instanceof TextView) {
            TextView temp = (TextView) view;
            temp.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
        }
    }


    public static <V extends View> boolean setGone(final V view) {
        return setGone(view, true);
    }

    public static <V extends View> boolean setVisible(final V view) {
        return setGone(view, false);
    }

    /**
     * 设置view组件gone或者visible
     *
     * @return 如果返回true表示设置成功了;false代表不成功
     */
    public static <V extends View> boolean setGone(final V view, final boolean gone) {
        if (view != null) {
            if (gone) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }


    /**
     * 设置view组件invisible或者visible
     *
     * @return view
     */
    public static void setVisibility(final View view, final boolean isVisible) {
        setGone(view, !isVisible);
    }


    /**
     * 设置view组件invisible或者visible
     *
     * @return view
     */
    public static <V extends View> V setInvisible(final V view, final boolean invisible) {
        if (view != null) {
            if (invisible) {
                if (View.INVISIBLE != view.getVisibility()) view.setVisibility(View.INVISIBLE);
            } else {
                if (View.VISIBLE != view.getVisibility()) view.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    /**
     * 设置view组件数组gone或者visible
     *
     * @return view
     */
    public static <V extends View> void setGone(final boolean gone, V... view) {
        for (View v : view) {
            if (v != null) {
                if (gone) {
                    if (View.GONE != v.getVisibility()) v.setVisibility(View.GONE);
                } else {
                    if (View.VISIBLE != v.getVisibility()) v.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    public static void setGone(View... views) {
        for (View v : views) {
            setGone(v);
        }
    }


    public static void setVisible(View... views) {
        for (View v : views) {
            setVisible(v);
        }
    }



    /**
     * textview setText 判空作保护
     */
    public static void setText(TextView text, String content) {
        if (text == null) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            text.setText("");
        } else {
            text.setText(content);
        }
    }

    /**
     * 增加view的点击区域，当view是一张小图或者不方便点击时可采用此方法
     */
    public static void increaseHitRectBy(final float amount, final View delegate) {
        increaseHitRectBy(amount, amount, amount, amount, delegate);
    }

    /**
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param delegate
     */
    public static void increaseHitRectBy(final float top, final float left, final float bottom,
            final float right, final View delegate) {
        final View parent = (View) delegate.getParent();
        if (parent != null && delegate.getContext() != null) {
            parent.post(new Runnable() {
                // Post in the parent's message queue to make sure the parent
                // lays out its children before we call getHitRect()
                public void run() {
                    final float densityDpi =
                            delegate.getContext().getResources().getDisplayMetrics().densityDpi;
                    final Rect r = new Rect();
                    delegate.getHitRect(r);
                    r.top -= transformToDensityPixel(top, densityDpi);
                    r.left -= transformToDensityPixel(left, densityDpi);
                    r.bottom += transformToDensityPixel(bottom, densityDpi);
                    r.right += transformToDensityPixel(right, densityDpi);
                    parent.setTouchDelegate(new TouchDelegate(r, delegate));
                }
            });
        }
    }

    public static int transformToDensityPixel(int regularPixel, DisplayMetrics displayMetrics) {
        return transformToDensityPixel(regularPixel, displayMetrics.densityDpi);
    }

    public static int transformToDensityPixel(float regularPixel, float densityDpi) {
        return (int) (regularPixel * densityDpi);
    }

    /**
     * 设置沉浸模式
     */
    public static void setImmersionMode(View view) {
        int visibility = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        if (Build.VERSION.SDK_INT >= 11) {
            view.setSystemUiVisibility(visibility);
        }
    }

    /**
     * 根据layoutId解析成View
     */
    public static View inflateViewLayout(@Nullable Context context, @LayoutRes int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public static <T extends View> T findById(@Nullable View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    public static <T extends View> T findById(@Nullable Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T findById(@Nullable Dialog dialog, @IdRes int id) {
        return (T) dialog.findViewById(id);
    }

    /**
     * findviewById 并添加点击事件
     */
    public static <V extends View> V findViewAttachOnclick(Activity activity, @IdRes int resId,
            View.OnClickListener onClickListener) {
        View view = activity.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }

    /**
     * findviewById 并添加点击事件
     */
    public static <V extends View> V findViewAttachOnclick(View rootView, @IdRes int resId,
            View.OnClickListener onClickListener) {
        //noinspection unchecked
        View view = rootView.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }

    /**
     * 获取顶部status bar高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取虚拟按键栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    public static boolean hasNavBar(Context context) {

        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    public static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 判断是否meizu手机
     */
    public static boolean isMeizu() {
        return Build.BRAND.equals("Meizu");
    }

    /**
     * 获取魅族手机底部虚拟键盘高度
     */
    public static int getSmartBarHeight(Context context) {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("mz_action_button_min_height");
            int height = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置TextView的Drawable （适用于Button, EditText, RadioButton等任意继承TextView的view）
     *
     * @param textView        textView
     * @param gravity         Drawable位置
     * @param drawablePadding Drawable与文字的间距,如果是-1则不设置间距
     * @param drawableWidth   Drawable宽
     * @param drawableHeight  Drawable高
     * @param drawableRes     Drawable资源
     */
    public static void setTextViewDrawable(TextView textView, int gravity, int drawablePadding,
            int drawableWidth, int drawableHeight, @DrawableRes int drawableRes) {
        Drawable drawable = getDrawableForSize(textView.getContext(), drawableWidth, drawableHeight,
                drawableRes);
        if (drawable == null) return;
        if (drawablePadding >= 0) {
            textView.setCompoundDrawablePadding(drawablePadding);
        }

        switch (gravity) {
            case Gravity.LEFT:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;

            case Gravity.TOP:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;

            case Gravity.RIGHT:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;

            case Gravity.BOTTOM:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;
        }
    }

    public static Drawable getDrawableForSize(Context context, int drawableWidth,
            int drawableHeight, @DrawableRes int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        if (drawable == null) return null;
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        return drawable;
    }

    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }
        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }
        return drawable;
    }


    public static ViewGroup.LayoutParams getLayoutParams(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return params;
    }

    /**
     * 横屏对于有虚拟导航栏的手机需要隐藏，并且不让视频尺寸发生变化
     *
     * @param isToHide 是否需要隐藏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void showOrHideNavigationBar(Activity activity, boolean isToHide) {
        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (isToHide) {
            activity.getWindow()
                    .setFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN,
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            activity.getWindow().clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void showOrHideSystemUI(Activity activity, boolean isToHideNavigationBar,
            boolean isToHideStatusBar) {
        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        int uiOptions;
        if (isToHideNavigationBar && isToHideStatusBar) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        } else if (!isToHideNavigationBar && isToHideStatusBar) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


    public static void hideStatusBarAndNavigationBar(Activity activity) {
        if (activity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            showOrHideSystemUI(activity, true, true);
        }
    }



    /**
     * V7.9 add 生成水平分隔线
     *
     * @return 分隔线View
     */
    public static View getHorizontalDividerView(Context mContext) {
        View dividerView = new View(mContext);
        LinearLayout.LayoutParams itemLayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dividerView.setLayoutParams(itemLayoutParams);
        dividerView.setBackgroundColor(
                Color.parseColor("#cccccc"));
        return dividerView;
    }

    /**
     * View的id作为key设置tag
     *
     * @param object tag value
     */
    public static void bindOnClickAndTag(@NonNull View view, Object object,
                                         View.OnClickListener listener) {
        view.setTag(view.getId(), object);
        view.setOnClickListener(listener);
    }

    public static boolean isVisible(View textView) {
        return textView.getVisibility() == View.VISIBLE;
    }

    public static View.OnClickListener getEmptyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public static void setEmptyClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    /**
     * 获取系统内对于旋转荧屏的设置
     */
    public static boolean isSystemRotationEnabled(Context context) {
        int flag = Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0);
        return flag == 1;
    }


    public static RelativeLayout.LayoutParams setLayoutWidthInRelativeLayout(Context context,
            float widthInDp, float heightInDp) {
        int width = ViewUtils.dp2px(context, widthInDp);
        int height = ViewUtils.dp2px(context, heightInDp);
        return new RelativeLayout.LayoutParams(width, height);
    }

    public static void setLayoutWidthInLinearLayout(Context context, View view, float widthInDp,
            float heightInDp) {
        int width = ViewUtils.dp2px(context, widthInDp);
        int height = ViewUtils.dp2px(context, heightInDp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        view.setLayoutParams(layoutParams);
    }

    public static Point getWindowSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    public static int getActivityHeight(Activity activity) {
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }


        return metrics.heightPixels;

    }

    public static int getActivityWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;

    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }

        return hasNavigationBar;

    }

    public static Bitmap shot(View view) {
        //View是你需要截图的View
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static void setBackgroud(View view, int resId) {
        int bottom = view.getPaddingBottom();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int left = view.getPaddingLeft();
        view.setBackgroundResource(resId);
        view.setPadding(left, top, right, bottom);
    }

    /**
     * V7.10 add scrollView滑动到底部
     */
    public static void scrollToBottom(final ScrollView parentScroll) {
        if (parentScroll != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    parentScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    /***
     * 获取AbsListView Y方向的滑动距离
     */
    public static int getScrollY(AbsListView view) {
        int dis = 0;

        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int top = c.getTop();
        int firstVisiblePosition = view.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {//与悬浮头部等高的空白header
            dis = -top;
        } else {
            dis = -c.getHeight();
        }
        //Log.e("cwk", "getScrollY() top= " + top + ", mOffsetTop = " + mOffsetTop + ",
        // firstvisiblepos = " + firstVisiblePosition + ", dis = " + dis);
        return dis;
    }

    public static float getDimension(Context context, int id) {
        return context.getResources().getDimension(id);
    }

    /**
     * V8.2 add 计算View在手机屏幕中的绝对高度，减去statusBar的高度
     */
    public static int calculateCoordinateY(Context context, View targetView) {
        if (targetView != null) {
            Rect measureRect = new Rect();
            if (targetView.getGlobalVisibleRect(measureRect)) {
                return measureRect.bottom - ViewUtils.getStatusBarHeight(context);
            }
        }
        return 0;
    }

    /**
     * 4.4以上状态栏全透明
     */
    public static void setWindowTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}


