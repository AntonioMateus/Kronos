package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeHora;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeMinuto;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeNome;

public class ListAtividadesAdapter extends ArrayAdapter<Atividade>{
    private final List<Atividade> atividades;
    private final int resource;
    private LayoutInflater mLayoutInflater;
    private ListAtividadesAdapterListener listener;
    private List<Atividade> atividadesChecadas;

    public ListAtividadesAdapter(Context context, int resource,
                                 List<Atividade> atividades, List<Atividade> atividadesChecadas,
                                 ListAtividadesAdapterListener listener) {
        super(context, resource, atividades);
        this.atividades = atividades;
        this.atividadesChecadas = atividadesChecadas;
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
        /*
        View viewRecycled=view;
        //usa como base as view utilizadas na classe ViewAtividadeHolder
        ViewAtividadeHolder holder;

        if(viewRecycled==null) { //Caso não há view para reciclar
            viewRecycled=mLayoutInflater.inflate(resource, parent, false);
            holder=new ViewAtividadeHolder();

            holder.setEditTextNome((EditText) viewRecycled.findViewById(R.id.editText_activityName));
            holder.setEditTextHora((EditText) viewRecycled.findViewById(R.id.editText_activityTimeHour));
            holder.setEditTextMinuto((EditText) viewRecycled.findViewById(R.id.imageButton_activityTimeMinute));

            holder.setButtonDelete((ImageButton) viewRecycled.findViewById(R.id.imageButton_activityCancel));
            holder.setButtonRating((Button) viewRecycled.findViewById(R.id.button_activityRating));
            holder.setCheckBox((CheckBox) viewRecycled.findViewById(R.id.checkBox_activityOK));

            viewRecycled.setTag(holder);
        }else { //Caso há opção de reciclar a View
            holder = (ViewAtividadeHolder) viewRecycled.getTag();
        }
        */
        //Sem reciclar Views :S não recomendado, mas eh o jeito que esta funcionando por hora
        view = mLayoutInflater.inflate(resource, parent, false);
        ViewAtividadeHolder holder = new ViewAtividadeHolder();

        holder.setEditTextNome((EditText) view.findViewById(R.id.editText_activityName));
        holder.setEditTextHora((EditText) view.findViewById(R.id.editText_activityTimeHour));
        holder.setEditTextMinuto((EditText) view.findViewById(R.id.imageButton_activityTimeMinute));

        holder.setButtonDelete((ImageButton) view.findViewById(R.id.imageButton_activityCancel));
        holder.setButtonRating((Button) view.findViewById(R.id.button_activityRating));
        holder.setCheckBox((CheckBox) view.findViewById(R.id.checkBox_activityOK));

        final Atividade atividade = getItem(position);

        /*
        Se a atividade estava checada (de acordo com a lista de Atividades Checadas),
         esta continua checada.
         */
        if (atividadesChecadas.contains(atividade)) {
            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(true);
        }

        EditText editTextNome = holder.getEditTextNome();
        editTextNome.setText(atividade.getNome());

        EditText editTextHora = holder.getEditTextHora();
        editTextHora.setText("" + atividade.getHora());

        EditText editTextMinuto = holder.getEditTextMinuto();
        editTextMinuto.setText("" + atividade.getMinuto());

        /*
        Define que ao clicar no botao "Deletar" ao lado da atividade,
        a atividade é retirada da lista e ativa a função do Listener que define o que deve ser
        feito quando ese adapter for atualizada
         */
        ImageButton buttonDelete = holder.getImageButtonDelete();
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atividades.remove(position);
                listener.onAtividadeAdicionada(atividade);
                listener.onUncheckedAtividade(atividade);
            }
        });

        /*
        Define que ao clicar no botao de "Rating" (Estrelinha) ao lado da Atividade,
        a Qualidade da Atividade assume valor (n%5), sendo n o numero de vezes que o botao rating
        foi acionado.
         */
        final Button buttonRating = holder.getButtonRating();
        int qualidade = ((int)atividade.getQualidade());
        buttonRating.setText(qualidade+"x");
        buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atividade.switchQualidade();
                int qualidade = ((int)atividade.getQualidade());
                buttonRating.setText(qualidade + "x");
            }
        });

        /*
        Define que quando o check da Atividade está "checado" as horas da Atividade em questão
        é adicionado ao gráfico
         */
        final CheckBox checkBox = holder.getCheckBox();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /*
            Se essa Atividade for checada:
                -e não houver mais nenhuma Atividade com mesmo nome, o listener da classe é alertado
                - caso houver mais uma Atividade com mesmo nome, o check dela é retirado
            Se essa Atividade estiver tendo o seu check retirado, o listener da classe é alertado
            */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int atividadesChecadasComMesmoNome = Atividade.atividadesChecadasComMesmoNome(atividade, atividadesChecadas);
                    if(atividadesChecadasComMesmoNome == 0) {
                        listener.onCheckedAtividade(atividade);
                    } else {
                        checkBox.setChecked(false);
                    }
                } else {
                    listener.onUncheckedAtividade(atividade);
                }
            }
        });

        // TextWatcher que define o que deve acontecer quando o campo Nome da Atividade mudar
        TextWatcher textWatcherNome = new TextWatcherAtividadeNome(atividade, atividadesChecadas, listener, checkBox);
        editTextNome.addTextChangedListener(textWatcherNome);

        //TextWatcher que define o que deve acontecer quando o campo Hora da Atividade mudar
        TextWatcher textWatcherHora = new TextWatcherAtividadeHora(atividade, listener, checkBox, editTextMinuto);
        editTextHora.addTextChangedListener(textWatcherHora);

        //TextWatcher que define o que deve acontecer quando o campo Minuto da Atividade mudar
        TextWatcher textWatcherMinuto = new TextWatcherAtividadeMinuto(atividade, listener, checkBox);
        editTextMinuto.addTextChangedListener(textWatcherMinuto);

        //return viewRecycled;
        return view;
    }
}
