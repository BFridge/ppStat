package fridge.com.hilistview.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import fridge.com.hilistview.utils.FansLevelUtils;
import fridge.com.hilistview.utils.ViewUtils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class LevelIconView extends TextView {
    /**
     * 适配GT-N7100机型
     */
    private static final String GT_N7100_PHONE_BUILD_MODEL = "GT-N7100";
    public LevelIconView(Context context) {
        super(context);
        init(context);
    }

    public LevelIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public LevelIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if(Build.MODEL.equals(GT_N7100_PHONE_BUILD_MODEL)){
            setMinimumWidth(ViewUtils.dp2px(context,50));
        }
        setMaxWidth(ViewUtils.dp2px(context,101));
        setMaxHeight(ViewUtils.dp2px(context,17));
        setGravity(Gravity.CENTER_VERTICAL);
    }


    public void setLevel(int level, String levelName,boolean showLevelName) {
        if (level > 0 && level <= 15) {
            setBackgroundDrawable(getResources().getDrawable(FansLevelUtils.getLevelDrawable(level)));
            if (!TextUtils.isEmpty(levelName) && showLevelName) {
                setText(getLevelText(level) + " " + levelName);
            } else {
                setText(getLevelText(level));
            }
            setTextColor(getResources().getColor(FansLevelUtils.getLevelTextColor(level)));
        }
    }

    private String getLevelText(int level) {
        return "LV" + String.valueOf(level);
    }
}
