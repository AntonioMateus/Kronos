package br.com.kronos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;

public class AssociarAtividadesListAdapter extends ArrayAdapter<Atividade>{
    private List<Atividade> atividades;
    private LayoutInflater inflater;
    private int resource;

    public AssociarAtividadesListAdapter(Context context, int resource, List<Atividade> atividades) {
        super(context, resource);
        this.atividades = atividades;
        this.inflater = ((Activity) context).getLayoutInflater();
        this.resource = resource;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        ViewAtividadeAssociadaHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);

            holder = new ViewAtividadeAssociadaHolder();
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox_atividadeAssociada));

            convertView.setTag(holder);
        } else {
            holder = (ViewAtividadeAssociadaHolder) convertView.getTag();
        }
        */
        convertView = inflater.inflate(resource, parent, false);
        ViewAtividadeAssociadaHolder holder = new ViewAtividadeAssociadaHolder();
        holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox_atividadeAssociada));

        CheckBox checkBox = holder.getCheckBox();
        final Atividade atividade = atividades.get(position);
        CharSequence atividadeNome = atividade.getNome();
        checkBox.setText(atividadeNome);
        checkBox.setChecked(atividade.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                atividade.setChecked(isChecked);
            }
        });

        return convertView;
    }
}
