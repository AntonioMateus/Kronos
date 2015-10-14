package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;

public class ListAtividadesAdapter extends ArrayAdapter<Atividade>{
    private final List<Atividade> atividades;
    private final int resource;
    private LayoutInflater mLayoutInflater;
    private ListAtividadesAdapterListener listener;

    public ListAtividadesAdapter(Context context, int resource, List<Atividade> atividades, ListAtividadesAdapterListener listener) {
        super(context, resource, atividades);
        this.atividades = atividades;
        mLayoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.listener = listener;
    }

    /*
    Devolve o tamanho da lista de atividades
     */
    @Override
    public int getCount() {
        return atividades.size();
    }

    /*
    Retorna a Atividade referente a posição passada como parâmetro
     */
    @Override
    public Atividade getItem(int position) {
        return atividades.get(position);
    }

    /*
    Usado para construir uma listView de Atividades.
    Retorna as Views com o layout formatado para Atividade
    Recicla view inutiladas para poupar memoria quando preciso.

     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View viewRecycled=view;
        //usa como base as view utilizadas na classe CiewAtividadeHolder
        ViewAtividadeHolder holder;

        if(viewRecycled==null) { //Caso não há view para reciclar
            viewRecycled=mLayoutInflater.inflate(resource, parent, false);
            holder=new ViewAtividadeHolder();

            holder.setEditTextNome((EditText) viewRecycled.findViewById(R.id.editText_activityName));
            holder.setEditTextHora((EditText) viewRecycled.findViewById(R.id.editText_activityTimeHour));
            holder.setEditTextMinuto((EditText) viewRecycled.findViewById(R.id.imageButton_activityTimeMinute));
            holder.setButtonDelete((ImageButton) viewRecycled.findViewById(R.id.imageButton_activityCancel));

            holder.rowNumber = position;

            viewRecycled.setTag(holder);
        }else { //Caso há opção de reciclar a View
            holder = (ViewAtividadeHolder) viewRecycled.getTag();
        }

        Atividade atividade = getItem(position);

        holder.getEditTextNome().setText(atividade.getNome());
        holder.getEditTextHora().setText(""+atividade.getHora());
        holder.getEditTextMinuto().setText("" + atividade.getMinuto());

        ImageButton buttonDelete = holder.getImageButtonDelete();
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atividades.remove(position);
                listener.onAdapterUpdate();
            }
        });

        return viewRecycled;
    }
}
