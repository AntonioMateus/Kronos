package br.com.kronos.kronos;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.exceptions.DescricaoDeMetaInvalidaException;
import br.com.kronos.fragmentos.AssociarAtividadeDialogFragment;
import br.com.kronos.fragmentos.AssociarAtividadeDialogFragmentListener;

public class AddGoalActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AssociarAtividadeDialogFragmentListener {

    private Spinner spinnerPrazo; //opções para Prazo da Meta
    private List<Atividade> atividadesAssociadas; //Lista de atividades que foram associadas a essa meta
    private TextView textViewAtividadesAssociadas; //indica quantas atividades foram associadas até agora

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Instancia Lista de Atividades Associadas
        atividadesAssociadas = new LinkedList<>();

        ImageButton imageButtonAdicionarMeta = (ImageButton) findViewById(R.id.imageButton_adicionarMeta);
        spinnerPrazo = (Spinner) findViewById(R.id.spinner_metaPrazo);
        Button buttonAssociarAtividade = (Button) findViewById(R.id.button_associarAtividades);
        textViewAtividadesAssociadas = (TextView) findViewById(R.id.textView_atividadesAssociadas);

        //Set do indicador da quantidade de atividades que foram associadas
        textViewAtividadesAssociadas.setText(getString(R.string.textView_atividadesAssociadas) + atividadesAssociadas.size());

        //adicionando funcao do botao -> quando for acionado onClick() (definido em algum lugar dessa classe) ira ser chamado
        imageButtonAdicionarMeta.setOnClickListener(this);
        buttonAssociarAtividade.setOnClickListener(this);

        //Definir as opcoes disponiveis no Spinner do Prazo da Meta
        ArrayAdapter spinnerPrazoAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_metaPrazo_opcoes, R.layout.spinner_metaprazo_item);
        spinnerPrazo.setAdapter(spinnerPrazoAdapter);
        spinnerPrazo.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton_adicionarMeta) {
            adicionarMeta();
        }else if (v.getId() == R.id.button_associarAtividades) {
            associarAtividades();
        }
    }

    private void associarAtividades() {
        AssociarAtividadeDialogFragment associarAtividadeDialogFragment = new AssociarAtividadeDialogFragment(atividadesAssociadas);
        associarAtividadeDialogFragment.show(getFragmentManager(), "AssociarAtividadeDialogFragment");
    }

    private void adicionarMeta() {
        EditText editTextDescricao = (EditText) findViewById(R.id.editText_metaDescricao);
        String descricao = editTextDescricao.getText().toString();

        CheckBox checkBoxRepetir = (CheckBox) findViewById(R.id.chechBox_metaRepetir);
        boolean repetir = checkBoxRepetir.isChecked();

        EditText editTextCategoria = (EditText) findViewById(R.id.editText_metaCategoria);
        String categoria = editTextCategoria.getText().toString();

        String prazoSelecionado = (String) spinnerPrazo.getSelectedItem();
        double prazo;
        if (prazoSelecionado.equals(getString(R.string.semPrazo))) {
            prazo = Double.MAX_VALUE;
        }else if (prazoSelecionado.equals(getString(R.string.prazoUmaSemana))) {
            prazo = 7 * 24; //sete dias * 24h
        }else if (prazoSelecionado.equals(getString(R.string.prazoMeioMes))) {
            prazo = 15 * 24; //quinze dias * 24h
        }else if (prazoSelecionado.equals(getString(R.string.prazoUmMes))) {
            prazo = 30 * 24; //trinta dias * 24h
        }else if (prazoSelecionado.equals(getString(R.string.prazoUmSemestre))) {
            prazo = 6 * 30 * 24; //seis meses * trinta dias * 24h
        }else if (prazoSelecionado.equals(getString(R.string.prazoUmAno))) {
            prazo = 12 * 30 * 24; //doze meses * trinta dias * 24h
        } else { //periodo custom
            EditText editTextPrazoCustom = (EditText) findViewById(R.id.editText_metaPrazoCustom);
            String prazoCustoDigitado = editTextPrazoCustom.getText().toString();
            if (prazoCustoDigitado.isEmpty()) {
                prazo = Double.MAX_VALUE;
            } else {
                prazo = Double.parseDouble(prazoCustoDigitado) * 24; //dias digitados * 24h
            }
        }

        Calendar calendar = Calendar.getInstance();
        int diaInicio = calendar.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendar.get(Calendar.MONTH) + 1;
        int anoInicio = calendar.get(Calendar.YEAR);

        EditText editTextHoras = (EditText) findViewById(R.id.editText_metaDuracaoHoras);
        EditText editTextMinutos = (EditText) findViewById(R.id.editText_metaDuracaoMinutos);
        if (editTextHoras.getText().toString().isEmpty() && editTextMinutos.getText().toString().isEmpty()) { //Se o usuario não definiu uma duração
            //Meta não é criada e o usuário é avisado
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.alertDialog_duracaoMetaNaoPreenchida);
            builder.setPositiveButton(R.string.alertDialog_duracaoMetaNaoPreenchida_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //O dialogo desaparece
                }
            });
            builder.create().show();
            return;
        }

        String horasEstipuladaDigitada = editTextHoras.getText().toString();
        double horasEstipulada = 0;
        if(!horasEstipuladaDigitada.isEmpty()) {
            horasEstipulada = Double.parseDouble(horasEstipuladaDigitada);
        }
        String minutosEstipuladoDigitado = editTextMinutos.getText().toString();
        double minutosEstipulado = 0;
        if(!minutosEstipuladoDigitado.isEmpty()) {
            minutosEstipulado = Double.parseDouble(minutosEstipuladoDigitado) / 60;
        }else if (horasEstipulada == 0) {
            minutosEstipulado = Meta.PRAZO_MINIMO;
        }
        double tempoEstipulado = horasEstipulada + minutosEstipulado;

        Meta meta;
        try {
            meta = new Meta(descricao, prazo, repetir, categoria, diaInicio, mesInicio, anoInicio);
            meta.setTempoEstipulado(tempoEstipulado);
            meta.setAtividadesAssociadas(atividadesAssociadas);

            KronosDatabase kronosDatabase = new KronosDatabase(this);
            kronosDatabase.addMeta(meta);
        } catch (DescricaoDeMetaInvalidaException e) {
            //e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.alertDialog_descricaoMetaNaoPreenchida);
            builder.setPositiveButton(R.string.alertDialog_descricaoMetaNaoPreenchida_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Faz o diálogo desaparecer
                }
            });
            builder.create().show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_metaPrazo) {
            String prazoSelecionado = (String) parent.getItemAtPosition(position);
            EditText editTextPrazoCustom = (EditText) findViewById(R.id.editText_metaPrazoCustom);
            if (prazoSelecionado.equals(getString(R.string.prazoCustomizado))) {
                editTextPrazoCustom.setVisibility(View.VISIBLE);
            } else {
                editTextPrazoCustom.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAtividadesAssociadas(List<Atividade> atividadesAssociadas) {
        this.atividadesAssociadas = atividadesAssociadas;

        textViewAtividadesAssociadas.setText(getString(R.string.textView_atividadesAssociadas) + atividadesAssociadas.size());
    }
}
