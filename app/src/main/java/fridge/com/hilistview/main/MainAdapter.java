package fridge.com.hilistview.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fridge.com.hilistview.R;
import fridge.com.hilistview.view.LevelIconView;

/**
 * A custom  adapter.
 */
public class MainAdapter extends BaseAdapter {

    /**
     * Current context
     */
    protected Context mContext;

    private LayoutInflater mInflater;

    private List<Integer> mList;

    // -------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------

    public MainAdapter(Context context) {
        super();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            mList.add(i);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Integer getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // -------------------------------------------------------------
    //  getView()
    // -------------------------------------------------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        }

        updateItem(position, convertView);

        return convertView;
    }

    public void updateItem(int position, View convertView) {
        Integer item = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (item != null) {
            holder.text.setText(item.toString());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

    }
    // -------------------------------------------------------------
    //  ViewHolder
    // -------------------------------------------------------------

    public Context getContext() {
        return mContext;
    }


    // -------------------------------------------------------------
    //  Getters and Setters
    // -------------------------------------------------------------

    private static class ViewHolder {

        // TODO define members for each view in the item layout
        public TextView text;
        public LevelIconView lv;


        public ViewHolder(View convertView) {
            text = (TextView) convertView.findViewById(R.id.list_item_tv);
            lv = (LevelIconView) convertView.findViewById(R.id.list_item_level);
            lv.setLevel(1,"实习粉",true);
        }

    }

}