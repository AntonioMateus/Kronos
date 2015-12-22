package br.com.kronos.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;
import br.com.kronos.adapters.ListAtividadesAdapterListener;
import br.com.kronos.adapters.ViewAtividadeHolder;

public class SpinnerHorasItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private final ViewAtividadeHolder viewAtividadeHolder;
    private ListAtividadesAdapterListener listener;
    private Atividade atividade;

    public SpinnerHorasItemSelectedListener(Atividade atividade, ListAtividadesAdapterListener listener, ViewAtividadeHolder viewAtividadeHolder) {
        this.listener = listener;
        this.viewAtividadeHolder = viewAtividadeHolder;
        this.atividade = atividade;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Double horaNova = Double.parseDouble((String) parent.getItemAtPosition(position));

        if(horaNova == 0 && atividade.getMinuto() == 0){
            Spinner spinnerMinuto = viewAtividadeHolder.getSpinnerMinuto();
            spinnerMinuto.setSelection((int)Atividade.MINUTO_MINIMO/15);
        }
        atividade.setHora(horaNova);

        /* Se a Atividade estiver checada:
        metodo que define o que deve ser feito na Activity quando a Hora da Atividade mudar
         */
        try {
            listener.onAtividadeUpdated(atividade, atividade.getNome());
        } catch (HorasDiaExcedidoException e) {
            CheckBox checkBox = viewAtividadeHolder.getCheckBox();
            Toast.makeText(checkBox.getContext(), R.string.horasDoDiaExcedidas, Toast.LENGTH_SHORT).show();
            atividade.setChecked(false);
            checkBox.setChecked(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(atividade.getMinuto() == 0){
            atividade.setMinuto(Atividade.MINUTO_MINIMO);
        }else{
            atividade.setMinuto(0);
        }

    }
}
