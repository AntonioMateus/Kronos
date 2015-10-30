package br.com.kronos.kronos.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;

import java.util.List;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class TextWatcherAtividadeNome implements TextWatcher {
    private Context context;
    private ListAtividadesAdapterListener listener;
    private CheckBox checkBox;

    private Atividade atividade;
    private List<Atividade> atividadesChecadas;

    public TextWatcherAtividadeNome(Atividade atividade, List<Atividade> atividadesChecadas,
                                    ListAtividadesAdapterListener listener, CheckBox checkBox) {
        this.context = checkBox.getContext();
        this.listener = listener;
        this.checkBox = checkBox;

        this.atividade = atividade;
        this.atividadesChecadas = atividadesChecadas;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Faz nada antes do texto ser modificado
    }

    @Override
    public void onTextChanged(CharSequence nomeNovo, int i, int i1, int i2) {
        //Faz nada com o trecho que mudou em particular
    }

    /*
    Apos o texto ser editado, o nome da instancia da Atividade eh alterado. Caso o nome da
     Atividade seja vazio, ele altera para o nome default de Atividade.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String nomeNovo = editable.toString();
        /*
        Se esta atividade estiver checada e ja houver uma Atividade com esse nome,
        essa ativiade deixa de estar checada.
         */
        if(checkBox.isChecked()) {
            int checadasComMesmoNome = Atividade.atividadesChecadasComMesmoNome(atividade, atividadesChecadas);
            if (checadasComMesmoNome > 0) {
                checkBox.setChecked(false);
            }
        }

        //Muda o nome na instancia da Atividade
        if (!nomeNovo.equals("")) {
            atividade.setNome(nomeNovo);
        } else {
            atividade.setNome(context.getString(R.string.activityDefaultName));
        }

        /*Se a atividade estiver checada:
            metodo que define o que deve ser feito na Activity quando o nome da Atividade mudar
        */
        if (checkBox.isChecked()) {
            listener.onAtividadeUpdated(atividade);
        }
    }
}
