package fridge.com.hilistview.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Fridge on 17/5/4.
 */

public class VListView extends ListView {
    public VListView(Context context) {
        super(context);
    }

    public VListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getLastTotalVisiblePosition() {
        View lastView = getChildAt(getChildCount() - 1);
        return getLastVisiblePosition() - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("shitshit", "【" + this.getClass().getSimpleName() + "】   onMeasure() called with: " + "widthMeasureSpec = [" + MeasureSpec.getSize(widthMeasureSpec) + "], heightMeasureSpec = [" + MeasureSpec.getSize(heightMeasureSpec) + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
