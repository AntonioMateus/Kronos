package br.com.kronos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.Meta;
import br.com.kronos.kronos.R;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private List<String> categorias;
    private HashMap<String, List<Meta>> listData;
    private LayoutInflater inflater;
    private Context context;

    public ExpandableAdapter(Context context, List<Meta> metas) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        HashMap<String, List<Meta>> mapaCategoriaMetas = new HashMap<>();
        for (Meta meta : metas) {
            String categoria = meta.getCategoria();
            if (categoria.equals(Meta.SEM_CATEGORIA)) {
                categoria = context.getString(R.string.categoria_default);
            }
            List<Meta> metasCategoria = mapaCategoriaMetas.get(categoria);
            if(metasCategoria == null){
                metasCategoria = new LinkedList<>();
            }
            metasCategoria.add(meta);

            mapaCategoriaMetas.put(categoria, metasCategoria);
        }

        this.categorias = new LinkedList<>(mapaCategoriaMetas.keySet());
        this.listData = mapaCategoriaMetas;
    }

    public int getGroupCount() {
        return categorias.size();
    }

    public int getChildrenCount(int groupPosition) {
        return listData.get(categorias.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return categorias.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(categorias.get(groupPosition)).get(childPosition);
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
        holder.tvGroup.setText(categorias.get(groupPosition));
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;
        Meta meta = (Meta) getChild(groupPosition,childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expandable_list_view,null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);
            holder.progressBarGoal = (ProgressBar) convertView.findViewById(R.id.progressBar_goal);
            holder.textViewGoalName = (TextView) convertView.findViewById(R.id.tvItem1);
            holder.textViewGoalProgress = (TextView) convertView.findViewById(R.id.tvItem2);
        }else {
            holder = (ViewHolderItem) convertView.getTag();
        }
        holder.textViewGoalName.setText(meta.getDescricao());
        KronosDatabase database = new KronosDatabase(this.context);
        int progresso = database.devolveProgressoMeta(meta.getDescricao());
        holder.progressBarGoal.setIndeterminate(false);
        holder.progressBarGoal.setProgress(progresso);
        holder.progressBarGoal.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        holder.textViewGoalProgress.setText(progresso + context.getString(R.string.progresso_meta));
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup {
        TextView tvGroup;
    }

    class ViewHolderItem {
        ProgressBar progressBarGoal;
        TextView textViewGoalName;
        TextView textViewGoalProgress;
    }
}