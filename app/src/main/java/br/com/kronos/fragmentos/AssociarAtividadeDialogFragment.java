package br.com.kronos.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import br.com.kronos.adapters.AssociarAtividadesListAdapter;
import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;

public class AssociarAtividadeDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private List<Atividade> atividades;
    private AssociarAtividadeDialogFragmentListener listener;
    private EditText editTextAssociarNovaAtividade;
    private CheckBox checkBoxAssociarNovaAtividade;
    private ListView listViewAtividades;

    public AssociarAtividadeDialogFragment(List<Atividade> atividadesAssociadas) {
        atividades = new LinkedList<>(atividadesAssociadas);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        KronosDatabase kronosDatabase = new KronosDatabase(getActivity());
        List<Atividade> atividadesDistintas = kronosDatabase.getAtividadesDistintas();
        for (Atividade atividade : atividadesDistintas) {
            if (!atividades.contains(atividade)) {
                atividades.add(atividade);
            }
        }


        View viewAssociarAtividade = getActivity().getLayoutInflater().inflate(R.layout.layout_associar_atividade, null);
        editTextAssociarNovaAtividade = (EditText) viewAssociarAtividade.findViewById(R.id.editText_associarNovaAtividade);

        listViewAtividades = (ListView) viewAssociarAtividade.findViewById(R.id.listView_atividades);
        atualizarListView();

        Button buttonAssociarAtividade = (Button) viewAssociarAtividade.findViewById(R.id.button_associarAtividades);
        buttonAssociarAtividade.setOnClickListener(this);

        checkBoxAssociarNovaAtividade = (CheckBox) viewAssociarAtividade.findViewById(R.id.checkBox_associarNovaAtividade);
        checkBoxAssociarNovaAtividade.setOnCheckedChangeListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(viewAssociarAtividade);

        return builder.create();
    }

    private void atualizarListView() {
        AssociarAtividadesListAdapter listViewAtividadesAdapter = new AssociarAtividadesListAdapter(getActivity(), R.layout.layout_associar_atividade_item, atividades);
        listViewAtividades.setAdapter(listViewAtividadesAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_associarAtividades) {
            List<Atividade> atividadesAssociadas = new LinkedList<>();
            for (Atividade atividade : atividades) {
                if (atividade.isChecked()) {
                    atividadesAssociadas.add(atividade);
                }
            }

            listener.onAtividadesAssociadas(atividadesAssociadas);
            dismiss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        this.listener = (AssociarAtividadeDialogFragmentListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String nome = editTextAssociarNovaAtividade.getText().toString();
        if (isChecked && !nome.isEmpty()) {
            Atividade atividade = new Atividade(nome, Atividade.DURACAO_MINIMA, 0, 1, 1, 0);
            atividade.setChecked(true);

            if(!atividades.contains(atividade)) {
                atividades.add(atividade);
                atualizarListView();
            }
        }

        checkBoxAssociarNovaAtividade.setChecked(false);
        editTextAssociarNovaAtividade.setText("");
    }
}
