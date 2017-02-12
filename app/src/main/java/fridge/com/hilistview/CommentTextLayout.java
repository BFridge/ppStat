package fridge.com.hilistview;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Fridge on 16/10/31.
 */

public class CommentTextLayout extends LinearLayout {
    TextView  mainContent;
    View more;

    public CommentTextLayout(Context context) {
        super(context);
    }

    public CommentTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMainContent(TextView mainContent) {
        this.mainContent = mainContent;
    }

    public void setMore(View more) {
        this.more = more;
    }

    public void setText(CharSequence text){
        mainContent.setText(text);
        mainContent.post(new Runnable() {
            @Override
            public void run() {
                Layout l = mainContent.getLayout();
                if ( l != null){
                    int lines = l.getLineCount();

                    if ( lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            more.setVisibility(VISIBLE);
                        }else{
                            more.setVisibility(GONE);
                        }
                    }

                }
            }
        });


    }


}
