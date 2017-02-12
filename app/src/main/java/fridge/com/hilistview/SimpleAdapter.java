package fridge.com.hilistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fridge on 17/1/20.
 */
public class SimpleAdapter extends BaseAdapter {
    private Context mContext;
    List<Integer> list = new ArrayList<>();

    public SimpleAdapter(Context context) {
        this.mContext = context;
        for (int i = 0; i < 300; i++) {
            list.add(i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
        TextView textView = (TextView) item.findViewById(R.id.list_item_tv);
        textView.setText("" + i + textView.getText());
        return item;
    }

}
