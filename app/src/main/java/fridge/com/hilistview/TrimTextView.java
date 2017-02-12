package fridge.com.hilistview;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Carlyle_Pro on 15/10/17.
 * <p>
 * Modified by Langjian 16/09/29 优化计算显示在TextView中最长的文案长度的算法
 * TextView默认的省略文案，末尾将连续的数字或者英文字符作为一个单词省略掉
 * 因此，V7.10 修改自定义TrimTextView，专门处理末尾省略内容截断逻辑
 */
public class TrimTextView extends TextView {


    int dens;

    public TrimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setSingleLine();
        dens = (int) (3 * context.getResources().getDisplayMetrics().density);
    }

    public TrimTextView(Context context) {
        super(context);
        this.setSingleLine();
        dens = (int) (3 * context.getResources().getDisplayMetrics().density);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthInPx = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (widthInPx > 0) {
            int widthCustom = getMaxWidthOfVisibleText(widthInPx) + getPaddingRight() + getPaddingLeft();
            if (mode != MeasureSpec.EXACTLY) {
                if (widthCustom > widthInPx) {
                    widthCustom = widthInPx;
                }
            }
            setMeasuredDimension(widthCustom, getMeasuredHeight());
        }
    }

    /**
     * 获取TextView可见文案部分的宽度
     *
     * @param maxMeasureWidth
     * @return
     */
    private int getMaxWidthOfVisibleText(int maxMeasureWidth) {
        int leftSpaceSize = maxMeasureWidth - getPaddingLeft() - getPaddingRight();
        leftSpaceSize -= dens;
        int drawableLength = 0;
        Paint paint = getPaint();
        int drawablePadding = getCompoundDrawablePadding();
        Drawable drawables[] = getCompoundDrawables();
        if (drawables[0] != null) {//减去leftDrawable
            drawableLength += drawables[0].getBounds().width();
            drawableLength += drawablePadding;
        }
        if (drawables[2] != null) {//减去rightDrawable
            drawableLength += drawables[2].getBounds().width();
            drawableLength += drawablePadding;
        }
        leftSpaceSize -= drawableLength;
        int appendLen = 0;
        CharSequence textAll = getText();
        if (textAll instanceof SpannedString ||
                textAll instanceof SpannableString) {
            appendLen = maxMeasureWidth;//不支持末尾'...'截断Spanned处理的图像
        } else {

            if (!TextUtils.isEmpty(textAll)) {
                int len = textAll.length();
                float[] widths = new float[len];
                paint.getTextWidths(textAll.toString(), widths);
                for (int j = 0; j < len; j++) {
                    appendLen += (int) Math.ceil(widths[j]);
                    if (appendLen >= leftSpaceSize) {
                        appendLen -= widths[j];
                        if (j - 1 > 0) {//从新设置TextView文案，系统自动使用'...'截断。
                            setText(textAll.subSequence(0, j - 1) + " ");//<p>加空格原因是，如果被截断的字符串末尾出现连续的数字或者字母，
                            //<p>系统会省略整个连续的字符串。</p>
                        }
                        break;
                    }
                }
            }
        }

        int rtn = dens + 1 + appendLen + drawableLength;
        return rtn;

    }
}
