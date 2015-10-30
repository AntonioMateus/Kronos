package br.com.kronos.kronos.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class TextWatcherAtividadeMinuto implements TextWatcher {

    private ListAtividadesAdapterListener listener;

    private CheckBox checkBox;

    private Atividade atividade;

    public TextWatcherAtividadeMinuto(Atividade atividade,
                                    ListAtividadesAdapterListener listener,
                                    CheckBox checkBox) {
        this.listener = listener;
        this.checkBox = checkBox;
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
    Apos o texto do Minuto ser editado, o minuto da instancia da Atividade eh alterado.
    Caso o minuto da Atividade seja vazio:
        e a Hora da Atividade = 0, ele altera para o valor minimo de duração para uma Atividade.
        e a Hora da Atividade > 0, ele altera para o valor 0
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String minutoNovo = editable.toString();
        if (minutoNovo.length() > 0 && minutoNovo.length() <= 2) {
            atividade.setMinuto(Double.parseDouble(minutoNovo));
        } else{
            if(atividade.getHora() == 0) {
                atividade.setMinuto(Atividade.MINUTO_MINIMO);
            }else{
                atividade.setMinuto(0);
            }
        }
                /* Se a Atividade estiver checada:
                metodo que define o que deve ser feito na Activity quando o Minuto da Atividade mudar
                 */
        if (checkBox.isChecked()) {
            try {
                listener.onAtividadeUpdated(atividade);
            } catch (HorasDiaExcedidoException e) {
                Toast.makeText(checkBox.getContext(), R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
                checkBox.setChecked(false);
            }
        }
    }
}
