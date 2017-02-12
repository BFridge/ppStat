package fridge.com.hilistview;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    private ListView listView;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new SimpleAdapter(this));
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

}
