package br.com.kronos.kronos.adapters;

import android.content.Context;
import android.text.Editable;
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

import br.com.kronos.exceptions.NomeAtividadeInvalidoExcpetion;
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
            holder.setButtonRating((Button) viewRecycled.findViewById(R.id.button_activityRating));
            holder.setCheckBox((CheckBox) viewRecycled.findViewById(R.id.checkBox_activityOK));

            viewRecycled.setTag(holder);
        }else { //Caso há opção de reciclar a View
            holder = (ViewAtividadeHolder) viewRecycled.getTag();
        }

        final Atividade atividade = getItem(position);

        final EditText editTextNome = holder.getEditTextNome();
        editTextNome.setText(atividade.getNome());

        EditText editTextHora = holder.getEditTextHora();
        editTextHora.setText("" + atividade.getHora());

        final EditText editTextMinuto = holder.getEditTextMinuto();
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
                listener.onAtividadeAdicionada();
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
        CheckBox checkBox = holder.getCheckBox();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listener.onCheckedAtividade(atividade);
                } else {
                    listener.onUncheckedAtividade(atividade);
                }
            }
        });

        /*
        Define TextWatcher para o campo de Nome da Atividade
        */
        TextWatcher textWatcherNome = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Faz nada
            }

            @Override
            public void onTextChanged(CharSequence nomeNovo, int i, int i1, int i2) {
                //Faz nada
            }

            /*
            Apos o texto ser editado, o nome da instancia da Atividade eh alterado. Caso o nome da
             Atividade seja vazio, ele altera para o nome default de Atividade.
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String nomeNovo = editable.toString();
                if (!nomeNovo.equals("")) {
                    atividade.setNome(nomeNovo);
                } else{
                    atividade.setNome(getContext().getString(R.string.activityDefaultName));
                }
                listener.onAtividadeUpdated();
            }
        };
        editTextNome.addTextChangedListener(textWatcherNome);

        /*
        Define TextWatcher para o campo de Hora da Atividade
        */
        TextWatcher textWatcherHora = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Faz nada
            }

            @Override
            public void onTextChanged(CharSequence nomeNovo, int i, int i1, int i2) {
                //Faz nada
            }

            /*
            Apos o texto da hora ser editado, a hora da instancia da Atividade eh alterado. Caso a hora da
             Atividade seja vazio, ele altera para o valor minimo de hora para uma Atividade.
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String horaNova = editable.toString();
                if ( horaNova.length() > 0 && horaNova.length() <= 2 ) {
                    atividade.setHora(Double.parseDouble(horaNova));
                } else{
                    atividade.setHora(0);
                    if (atividade.getMinuto() == 0) {
                        editTextMinuto.setText( (int)Atividade.MINUTO_MINIMO+"");
                    }
                }
                listener.onAtividadeUpdated();
            }
        };
        editTextHora.addTextChangedListener(textWatcherHora);

        /*
        Define TextWatcher para o campo de Minuto da Atividade
        */
        TextWatcher textWatcherMinuto = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Faz nada
            }

            @Override
            public void onTextChanged(CharSequence nomeNovo, int i, int i1, int i2) {
                //Faz nada
            }

            /*
            Apos o texto do Minuto ser editado, o minuto da instancia da Atividade eh alterado.
            Caso o minuto da Atividade seja vazio, ele altera para o valor minimo de hora para
            uma Atividade.
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String minutoNovo = editable.toString();
                if (minutoNovo.length() > 0 && minutoNovo.length() <= 2) {
                    atividade.setMinuto(Double.parseDouble(minutoNovo));
                } else{
                    atividade.setMinuto(Atividade.MINUTO_MINIMO);
                }
                listener.onAtividadeUpdated();
            }
        };
        editTextMinuto.addTextChangedListener(textWatcherMinuto);

        return viewRecycled;
    }
}
