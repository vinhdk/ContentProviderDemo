package se1302.demo.contentproviderdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends ArrayAdapter<T> {

    private LayoutInflater mInflater;

    public BaseAdapter(Context context, ArrayList<T> list) {
        super(context, 0, list);
        this.mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        TextView textView;
        private ViewHolder(View rootView) {
            textView = (TextView) rootView.findViewById(android.R.id.text1);
        }
    }
    public abstract void drawText(TextView textView, T model);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        drawText(viewHolder.textView, getItem(position));

        return convertView;
    }
}
