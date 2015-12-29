package br.com.kronos.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.adapters.ListAtividadesAdapterListener;

public class TextWatcherAtividadeNome implements TextWatcher {
    private Context context;
    private ListAtividadesAdapterListener listener;
    private CheckBox checkBox;
    private Atividade atividade;
    private String atividadeNomeAntigo;

    public TextWatcherAtividadeNome(Atividade atividade, ListAtividadesAdapterListener listener, CheckBox checkBox) {
        this.context = checkBox.getContext();
        this.listener = listener;
        this.checkBox = checkBox;
        this.atividade = atividade;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Armazenar nome antigo da Atividade para realizar update do Banco de Dados
        this.atividadeNomeAntigo = atividade.getNome();
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
        List<Atividade> atividades = listener.getAtividades();

        //Muda o nome na instancia da Atividade. Se o nome for vazio, escolhe-se o nome padrao para uma Atividade
        if (nomeNovo.equals("")) {
            nomeNovo = context.getString(R.string.activityDefaultName);

            Iterator<Atividade> iteradorAtividades = atividades.iterator();
            for (int indiceAtividadeAleatoria = 0; iteradorAtividades.hasNext(); indiceAtividadeAleatoria++) {
                Atividade atividadeIterada = iteradorAtividades.next();
                if(atividadeIterada.getNome().equals(nomeNovo)){
                    nomeNovo+=indiceAtividadeAleatoria;
                    iteradorAtividades = atividades.iterator();
                }
            }
        }
        atividade.setNome(nomeNovo);

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
        if (atividadesComEsseNome > 1) { //Se houver essa atividade e mais uma com o nome em questão
            Toast.makeText(context, R.string.atividadeChecadaComMesmoNome, Toast.LENGTH_SHORT).show();
            atividade.setChecked(false);
            checkBox.setChecked(false);
        }else {
            try {
                listener.onAtividadeUpdated(atividade, atividadeNomeAntigo);
            } catch (HorasDiaExcedidoException e) {
                //Caso impossível
                Toast.makeText(context, R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
            }
        }
    }
}