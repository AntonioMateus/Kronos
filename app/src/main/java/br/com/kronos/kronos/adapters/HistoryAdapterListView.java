package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import br.com.kronos.kronos.R;

public class HistoryAdapterListView extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<HistoryListViewItem> itens;

    public HistoryAdapterListView(Context context, List<HistoryListViewItem> itens) {
        //ListView itens
        this.itens = itens;
        //Object responsible for taking the layout of the item
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return itens.size();
    }
    @Override
    public HistoryListViewItem getItem(int position) {
        return itens.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HistoryListHolder itemHolder;
        //if the view is null, we inflate the layout in it.
        if (view == null) {
            //inflate the layout so that we can get the views
            view = mInflater.inflate(R.layout.list_history_item, null);

            //creates an support item so that we do not have to
            //inflate the same information
            itemHolder = new HistoryListHolder();
            itemHolder.actName = ((TextView) view.findViewById(R.id.history_activity));
            itemHolder.actQuality = ((TextView) view.findViewById(R.id.history_activity_quality));
            itemHolder.actDuration = ((TextView) view.findViewById(R.id.history_activity_duration));

            //define the itens in the view;
            view.setTag(itemHolder);
        } else {
            //if the view already exists get the itens
            itemHolder = (HistoryListHolder) view.getTag();
        }

        //get the data from the list
        //and set the item values.
        HistoryListViewItem item = itens.get(position);
        itemHolder.actName.setText(item.getActivityName());
        itemHolder.actQuality.setText("" + (int) item.getQuality());

        String duration = "";
        if(item.getDuration() >= 1) {
            duration += (int) item.getDuration() + "h";
        }
        int minutes = (int) ((item.getDuration() - (int) item.getDuration()) * 60);
        if( minutes > 0) {
            duration += minutes + "min";
        }
        itemHolder.actDuration.setText(duration);

        //retorna a view com as informações
        return view;
    }

}

