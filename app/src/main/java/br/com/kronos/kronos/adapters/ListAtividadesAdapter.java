package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeHora;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeMinuto;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeNome;

public class ListAtividadesAdapter extends ArrayAdapter<Atividade>{
    private final int resource;
    private LayoutInflater mLayoutInflater;
    private ListAtividadesAdapterListener listener;

    public ListAtividadesAdapter(Context context, int resource,
                                 ListAtividadesAdapterListener listener) {

        super(context, resource, listener.getAtividades());
        mLayoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.listener = listener;
    }

    /*
    Devolve o tamanho da lista de atividades
     */
    @Override
    public int getCount() {
        return listener.getAtividades().size();
    }

    /*
    Retorna a Atividade referente a posição passada como parâmetro
     */
    @Override
    public Atividade getItem(int position) {
        return listener.getAtividades().get(position);
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
        final List<Atividade> atividadesChecadas = listener.getAtividadesChecadas();

        //View do item da Atividade
        EditText editTextNome = holder.getEditTextNome();
        EditText editTextHora = holder.getEditTextHora();
        EditText editTextMinuto = holder.getEditTextMinuto();
        final CheckBox checkBox = holder.getCheckBox();

        /*
        Preenchendo EditTexts com os dados da Atividade
         */
        editTextNome.setText(atividade.getNome());
        editTextHora.setText("" + atividade.getHora());
        editTextMinuto.setText("" + atividade.getMinuto());

        /*
        Se a atividade estava checada (de acordo com a lista de Atividades Checadas), esta continua checada.
         */
        if ( atividadesChecadas.contains(atividade) ) {
            checkBox.setChecked(true);
        }

        /*
        Define que ao clicar no botao "Deletar" ao lado da atividade,
        a atividade é retirada da lista e ativa a função do Listener que define o que deve ser
        feito quando ese adapter for atualizada
         */
        ImageButton buttonDelete = holder.getImageButtonDelete();
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAtividadeRemovida(atividade);
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
        é adicionado ao gráfico. Se já houver uma Atividade checada e com o nome da Atividade que
        está sendo checada, o check desta Atividade mais recente não é dado. Se essa Atividade for
        checada e o número de horas do dia for excedido, o check é desfeito.
         */
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!atividadesChecadas.contains(atividade)) {
                        try {
                            listener.onCheckedAtividade(atividade);
                        } catch (HorasDiaExcedidoException e) {
                            Toast.makeText(getContext(), R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
                            checkBox.setChecked(false);
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.atividadeChecadaComMesmoNome, Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false);
                    }
                } else {
                    listener.onUncheckedAtividade(atividade);
                }
            }
        });

        // TextWatcher que define o que deve acontecer quando o campo Nome da Atividade mudar
        TextWatcher textWatcherNome = new TextWatcherAtividadeNome(atividade, listener, checkBox);
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
