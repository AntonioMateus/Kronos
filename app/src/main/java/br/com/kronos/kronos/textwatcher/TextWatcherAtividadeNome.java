package br.com.kronos.kronos.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class TextWatcherAtividadeNome implements TextWatcher {
    private Context context;
    private ListAtividadesAdapterListener listener;
    private CheckBox checkBox;

    private Atividade atividade;
    //private String atividadeNomeAntigo;

    public TextWatcherAtividadeNome(Atividade atividade,
                                    ListAtividadesAdapterListener listener, CheckBox checkBox) {
        this.context = checkBox.getContext();
        this.listener = listener;
        this.checkBox = checkBox;

        this.atividade = atividade;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Armazenar nome antigo da Atividade para realizar update do Banco de Dados
        //this.atividadeNomeAntigo = charSequence.toString();
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
        List<Atividade> atividadesChecadas = listener.getAtividadesChecadas();
        List<Atividade> atividades = listener.getAtividades();

        /*
        Se esta atividade estiver checada e ja houver uma Atividade com esse nome,
        essa atividade deixa de estar checada.
         */
        if(checkBox.isChecked()) {
            if (atividadesChecadas.contains(atividade)) {
                Toast.makeText(context, R.string.atividadeChecadaComMesmoNome, Toast.LENGTH_SHORT).show();
                checkBox.setChecked(false);
            }
        }

        //Muda o nome na instancia da Atividade. Se o nome for vazio, escolhe-se o nome padrao para uma Atividade
        if (!nomeNovo.equals("")) {
            atividade.setNome(nomeNovo);
        } else {
            String atividadeAleatoriaNome = context.getString(R.string.activityDefaultName);
            atividade.setNome(atividadeAleatoriaNome);
            for (int atividadeAleatoria = 0; atividades.contains(atividade); atividadeAleatoria++) {
                atividade.setNome(atividadeAleatoriaNome + atividadeAleatoria);
            }
        }

        /*
        Se a atividade estiver checada depois que o nome for alterado: chama-se o metodo que
        define o que deve ser feito na Activity listener quando o nome da Atividade mudar
        */
        if (checkBox.isChecked()) {
            try {
                listener.onAtividadeUpdated(atividade);
            } catch (HorasDiaExcedidoException e) {
                //Caso impossível
                Toast.makeText(context, R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
