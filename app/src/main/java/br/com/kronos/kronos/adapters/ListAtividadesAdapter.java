package br.com.kronos.kronos.adapters;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.fragmentos.QualidadeDialogFragment;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.textwatcher.SpinnerHorasItemSelectedListener;
import br.com.kronos.kronos.textwatcher.SpinnerMinutosItemSelectedListener;
import br.com.kronos.kronos.textwatcher.TextWatcherAtividadeNome;

public class ListAtividadesAdapter extends ArrayAdapter<Atividade>{
    private final int resource;
    private final List<Atividade> atividades;
    private LayoutInflater mLayoutInflater;
    private ListAtividadesAdapterListener listener;

    public ListAtividadesAdapter(Context context, int resource, ListAtividadesAdapterListener listener) {
        super(context, resource, listener.getAtividades());
        this.atividades = listener.getAtividades();
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
        view = mLayoutInflater.inflate(resource, parent, false);
        ViewAtividadeHolder holder = new ViewAtividadeHolder();

        holder.setEditTextNome((EditText) view.findViewById(R.id.editText_atividade_nome));
        //holder.setEditTextHora((EditText) view.findViewById(R.id.spinner_atividade_horas));
        holder.setSpinnerHoras((Spinner) view.findViewById(R.id.spinner_atividade_horas));
        holder.setSpinnerMinuto((Spinner) view.findViewById(R.id.spinner_atividade_minuto));

        holder.setButtonDelete((ImageButton) view.findViewById(R.id.imageButton_cancelar));
        holder.setButtonRating((Button) view.findViewById(R.id.button_atividade_qualidade));
        holder.setCheckBox((CheckBox) view.findViewById(R.id.checkBox_inserir_grafico));

        final Atividade atividade = getItem(position);

        //View do item da Atividade
        EditText editTextNome = holder.getEditTextNome();
        //EditText editTextHora = holder.getEditTextHora();
        Spinner spinnerHoras = holder.getSpinnerHoras();
        Spinner spinnerMinuto = holder.getSpinnerMinuto();
        final CheckBox checkBox = holder.getCheckBox();

        /*
        Preenchendo EditTexts com os dados da Atividade
         */
        editTextNome.setText(atividade.getNome());
        //editTextHora.setText("" + atividade.getHora());
        //Determina Spinner com as horas com as opcoes possiveis e com os dados da Atividade
        ArrayAdapter<CharSequence> adapterHoras =
                ArrayAdapter.createFromResource(spinnerHoras.getContext(), R.array.spinner_horas_opcoes, android.R.layout.simple_spinner_item);
        adapterHoras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHoras.setAdapter(adapterHoras);
        spinnerHoras.setSelection(atividade.getHora());
        //Determina Spinner com os minutos com as opcoes possiveis e com os dados da Atividade
        ArrayAdapter<CharSequence> adapterMinutos =
                ArrayAdapter.createFromResource(spinnerMinuto.getContext(), R.array.spinner_minutos_opcoes, android.R.layout.simple_spinner_item);
        adapterMinutos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinuto.setAdapter(adapterMinutos);
        spinnerMinuto.setSelection(atividade.getMinuto()/15);

        /*
        Se a atividade estava checada, esta continua checada.
         */
        if (atividade.isChecked()) {
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
                atividades.remove(atividade);
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
        buttonRating.setText(qualidade + "");
        buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atividade.switchQualidade();
                int qualidade = ((int) atividade.getQualidade());
                buttonRating.setText(qualidade + "");
                try {
                    listener.onAtividadeUpdated(atividade, atividade.getNome());
                } catch (HorasDiaExcedidoException e) {
                    //e.printStackTrace();
                }
            }
        });
        buttonRating.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                QualidadeDialogFragment qualidadeDialog = new QualidadeDialogFragment(getContext(), atividade, buttonRating);
                qualidadeDialog.show(((Activity)buttonRating.getContext()).getFragmentManager() , "QualidadeDialog" );
                return true;
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
                    /*
                    Se esta atividade estiver checada e ja houver uma Atividade com esse nome,
                    essa atividade deixa de estar checada.
                     */
                    int atividadesComEsseNome = 0;
                    for (Atividade atividadeIterada : atividades) {
                        if (atividadeIterada.isChecked() && atividadeIterada.equals(atividade)) {
                            atividadesComEsseNome++;
                        }
                    }
                    if (atividadesComEsseNome < 1) {
                        try {
                            atividade.setChecked(true);
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
                    atividade.setChecked(false);
                    listener.onUncheckedAtividade(atividade);
                }
            }
        });

        // TextWatcher que define o que deve acontecer quando o campo Nome da Atividade mudar
        TextWatcher textWatcherNome = new TextWatcherAtividadeNome(atividade, listener, checkBox);
        editTextNome.addTextChangedListener(textWatcherNome);

        //Define o que deve acontecer quando o Minuto da Atividade eh escolhido
        SpinnerHorasItemSelectedListener spinnerHorasItemSelectedListener = new SpinnerHorasItemSelectedListener(atividade, listener, holder);
        spinnerHoras.setOnItemSelectedListener(spinnerHorasItemSelectedListener);

        //Define o que deve acontecer quando o Minuto da Atividade eh escolhido
        SpinnerMinutosItemSelectedListener spinnerMinutosItemSelectedListener = new SpinnerMinutosItemSelectedListener(atividade, listener, checkBox);
        spinnerMinuto.setOnItemSelectedListener(spinnerMinutosItemSelectedListener);

        //return viewRecycled;
        return view;
    }
}
