package br.com.kronos.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.adapters.ListAtividadesAdapterListener;

public class SpinnerMinutosItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private ListAtividadesAdapterListener listener;
    private CheckBox checkBox;
    private Atividade atividade;

    public SpinnerMinutosItemSelectedListener(Atividade atividade, ListAtividadesAdapterListener listener, CheckBox checkBox) {
        this.listener = listener;
        this.checkBox = checkBox;
        this.atividade = atividade;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Double minutoNovo = Double.parseDouble((String) parent.getItemAtPosition(position));

        if(minutoNovo == 0 && atividade.getHora() == 0){
            minutoNovo = Atividade.MINUTO_MINIMO;
            parent.setSelection( (int)Atividade.MINUTO_MINIMO/15 );
        }
        atividade.setMinuto(minutoNovo);

        try {
            listener.onAtividadeUpdated(atividade, atividade.getNome());
        } catch (HorasDiaExcedidoException e) {
            Toast.makeText(checkBox.getContext(), R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
            atividade.setChecked(false);
            checkBox.setChecked(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(atividade.getHora() == 0){
            atividade.setMinuto(Atividade.MINUTO_MINIMO);
        }else{
            atividade.setMinuto(0);
        }

    }
}
