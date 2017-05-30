package fridge.com.activitytrans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * A custom  adapter.
 */
public class MyAdapter extends BaseAdapter {

    /**
     * Current context
     */
    protected Context mContext;

    private LayoutInflater mInflater;

    private List<Integer> mList;

    // -------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------

    public MyAdapter(Context context, List<Integer> objects) {
        super();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = objects;
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


        public ViewHolder(View convertView) {
            text = (TextView) convertView.findViewById(R.id.list_item_tv);
        }

    }

}