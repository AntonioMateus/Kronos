package br.com.kronos.kronos.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class TextWatcherAtividadeHora implements TextWatcher {
    private ListAtividadesAdapterListener listener;

    private CheckBox checkBox;
    private EditText editTextMinuto;

    private Atividade atividade;

    public TextWatcherAtividadeHora(Atividade atividade,
                                    ListAtividadesAdapterListener listener,
                                    CheckBox checkBox, EditText editTextMinuto) {
        this.listener = listener;

        this.checkBox = checkBox;
        this.editTextMinuto = editTextMinuto;

        this.atividade = atividade;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Faz nada
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        //Faz nada com o trecho que mudou em particular
    }

    /*
    Apos o texto da hora ser editado, a hora da instancia da Atividade eh alterado.
    Caso a hora da Atividade seja vazio:
        minuto > 0 ele altera para o valor minimo de hora para uma Atividade.
        minuto = 0, é definido a duração mínima que uma Atividade pode ter
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String horaNova = editable.toString();
        if ( horaNova.length() > 0 && horaNova.length() <= 2 ) {
            atividade.setHora(Double.parseDouble(horaNova));
        } else{
            atividade.setHora(0);
            if ( atividade.getMinuto() == 0 ) {
                editTextMinuto.setText( (int) Atividade.MINUTO_MINIMO+"");
            }
        }
                /* Se a Atividade estiver checada:
                metodo que define o que deve ser feito na Activity quando o Hora da Atividade mudar
                 */
        if (checkBox.isChecked()) {
            listener.onAtividadeUpdated(atividade);
        }
    }
}
