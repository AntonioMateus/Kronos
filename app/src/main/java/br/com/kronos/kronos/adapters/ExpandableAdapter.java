package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.Meta;
import br.com.kronos.kronos.R;

/**
 * Created by antonio on 01/12/15.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
    private List<String> listGroup;
    private HashMap<String, List<Meta>> listData;
    private LayoutInflater inflater;
    private Context context;

    public ExpandableAdapter(Context context, HashMap<String, List<Meta>> listData, List<String> listGroup) {
        this.context = context;
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getGroupCount() {
        return listGroup.size();
    }

    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroup.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listGroup.get(groupPosition)).get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.header_expandable_list_view,null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
        }
        else {
            holder = (ViewHolderGroup) convertView.getTag();
        }
        holder.tvGroup.setText(listGroup.get(groupPosition));
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;
        Meta meta = (Meta) getChild(groupPosition,childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expandable_list_view,null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);
            holder.progressBar_goal = (ProgressBar) convertView.findViewById(R.id.progressBar_goal);
        }
        else {
            holder = (ViewHolderItem) convertView.getTag();
        }
        KronosDatabase database = new KronosDatabase(this.context);
        holder.progressBar_goal.setIndeterminate(false);
        holder.progressBar_goal.setProgress(database.devolveProgressoMeta(meta.getDescricao()));
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup {
        TextView tvGroup;
    }

    class ViewHolderItem {
        ProgressBar progressBar_goal;
    }
}