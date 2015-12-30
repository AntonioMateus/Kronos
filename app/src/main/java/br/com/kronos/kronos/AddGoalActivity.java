package br.com.kronos.kronos;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Calendar;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.exceptions.DescricaoDeMetaInvalidaException;

public class AddGoalActivity extends Activity implements View.OnClickListener {

    private Spinner spinnerPrazo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageButton buttonAdicionarMeta = (ImageButton) findViewById(R.id.imageButton_adicionarMeta);
        spinnerPrazo = (Spinner) findViewById(R.id.spinner_metaPrazo);

        buttonAdicionarMeta.setOnClickListener(this);//adicionando funcao do botao -> quando for acionado onClick() (definido em algum lugar dessa classe) ira ser chamado

        //Definir as opcoes disponiveis no Spinner do Prazo da Meta
        ArrayAdapter spinnerPrazoAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_metaPrazo_opcoes, android.R.layout.simple_spinner_item);
        spinnerPrazo.setAdapter(spinnerPrazoAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton_adicionarMeta) {
            adicionarMeta();
        }
    }

    private void adicionarMeta() {
        EditText editTextDescricao = (EditText) findViewById(R.id.editText_metaDescricao);
        String descricao = editTextDescricao.getText().toString();

        CheckBox checkBoxRepetir = (CheckBox) findViewById(R.id.chechBox_repetir);
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
}
