package fridge.com.hilistview.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import fridge.com.hilistview.KeyBoardUtils;
import fridge.com.hilistview.R;
import fridge.com.hilistview.view.VListView;

public class MainActivity extends Activity {

    private ListView listView;
    private EditText input;
    private TextView mGoTopHint;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        listView = (VListView) findViewById(R.id.list_view);
        listView.setAdapter(new MainAdapter(this));
        mGoTopHint = (TextView) findViewById(R.id.gotop);
        mGoTopHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoTopHint.setVisibility(View.GONE);
                mGoTopHint.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(0);
                    }
                });
            }
        });
        listView.setOnScrollListener(new TopHintController());
        input = (EditText) findViewById(R.id.bottom);

    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (input.isFocused()) {
                Rect outRec = new Rect();
                input.getGlobalVisibleRect(outRec);
                if (!outRec.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    Log.e("shitshit", "【" + this.getClass().getSimpleName() + "】   onOutsideClicked() called with: " + "event = [" + ev + "]");
                    KeyBoardUtils.hideSoftKeyBoard(this, input);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private  class TopHintController implements AbsListView.OnScrollListener {
        private static final int GOAL = 20;
        private boolean hasReach = false;
        private int idleCount = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (!hasReach) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (idleCount == 3) {
                    mGoTopHint.setVisibility(View.VISIBLE);
                }else{
                    idleCount ++;
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount  >= GOAL ) {
                hasReach = true;
            }
            if (firstVisibleItem == 0) {
                mGoTopHint.setVisibility(View.GONE);
                init();
            }
        }

        private void init(){
            idleCount = 0;
            hasReach = false;
        }
    }

}
