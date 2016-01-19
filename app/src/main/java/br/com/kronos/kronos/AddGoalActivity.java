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
import br.com.kronos.exceptions.MetaRepetidaException;
import br.com.kronos.exceptions.TempoEstipuladoInvalidoException;
import br.com.kronos.fragmentos.AssociarAtividadeDialogFragment;
import br.com.kronos.fragmentos.AssociarAtividadeDialogFragmentListener;

/*
Essa Activity representa a tela para adicionar uma Meta
 */
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

        ImageButton buttonAdicionarMeta = (ImageButton) findViewById(R.id.imageButton_adicionarMeta);
        ImageButton buttonCancelarAdicaoMeta = (ImageButton) findViewById(R.id.imageButton_cancelarAdicaoMeta);
        spinnerPrazo = (Spinner) findViewById(R.id.spinner_metaPrazo);
        Button buttonAssociarAtividade = (Button) findViewById(R.id.button_associarAtividades);
        textViewAtividadesAssociadas = (TextView) findViewById(R.id.textView_atividadesAssociadas);

        //Set do indicador da quantidade de atividades que foram associadas
        textViewAtividadesAssociadas.setText(getString(R.string.textView_atividadesAssociadas) + atividadesAssociadas.size());

        //adicionando funcao do botao -> quando for acionado onClick() (definido em algum lugar dessa classe) ira ser chamado
        buttonAdicionarMeta.setOnClickListener(this);
        buttonCancelarAdicaoMeta.setOnClickListener(this);
        buttonAssociarAtividade.setOnClickListener(this);

        //Definir as opcoes disponiveis no Spinner do Prazo da Meta
        ArrayAdapter spinnerPrazoAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_metaPrazo_opcoes, R.layout.spinner_metaprazo_item);
        spinnerPrazo.setAdapter(spinnerPrazoAdapter);
        spinnerPrazo.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageButton_adicionarMeta) {
            adicionarMeta();
        }else if (id == R.id.button_associarAtividades) {
            associarAtividades();
        }else if (id == R.id.imageButton_cancelarAdicaoMeta) {
            finish();
        }
    }

    private void associarAtividades() {
        AssociarAtividadeDialogFragment associarAtividadeDialogFragment = new AssociarAtividadeDialogFragment(atividadesAssociadas);
        associarAtividadeDialogFragment.show(getFragmentManager(), "AssociarAtividadeDialogFragment");
    }

    private void adicionarMeta() {
        EditText editTextDescricao = (EditText) findViewById(R.id.editText_metaDescricao);
        String descricao = editTextDescricao.getText().toString();

        EditText editTextHoras = (EditText) findViewById(R.id.editText_metaDuracaoHoras);
        String horasEstipuladaDigitada = editTextHoras.getText().toString();
        double horasEstipulada = 0;
        if(!horasEstipuladaDigitada.isEmpty()) {
            horasEstipulada = Double.parseDouble(horasEstipuladaDigitada);
        }

        EditText editTextMinutos = (EditText) findViewById(R.id.editText_metaDuracaoMinutos);
        String minutosEstipuladoDigitado = editTextMinutos.getText().toString();
        double minutosEstipulado = 0;
        if(!minutosEstipuladoDigitado.isEmpty()) {
            minutosEstipulado = Double.parseDouble(minutosEstipuladoDigitado) / 60;
        }
        double tempoEstipulado = horasEstipulada + minutosEstipulado;

        Calendar calendar = Calendar.getInstance();
        int diaInicio = calendar.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendar.get(Calendar.MONTH) + 1;
        int anoInicio = calendar.get(Calendar.YEAR);

        AlertDialog.Builder builderFeedback = new AlertDialog.Builder(this);
        try {
            Meta meta = new Meta(descricao, tempoEstipulado, diaInicio, mesInicio, anoInicio);
            meta.setAtividadesAssociadas(atividadesAssociadas);

            CheckBox checkBoxRepetir = (CheckBox) findViewById(R.id.chechBox_metaRepetir);
            boolean repetir = checkBoxRepetir.isChecked();
            meta.setRepetir(repetir);

            EditText editTextCategoria = (EditText) findViewById(R.id.editText_metaCategoria);
            String categoria = editTextCategoria.getText().toString();
            meta.setCategoria(categoria);

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
            meta.setPrazo(prazo);

            KronosDatabase kronosDatabase = new KronosDatabase(this);
            kronosDatabase.addMeta(meta);

            builderFeedback.setMessage(R.string.alertDialog_metaCriada);
            builderFeedback.setPositiveButton(R.string.alertDialog_metaCriada_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Faz o diálogo desaparecer e fecha a Activity
                    finish();
                }
            });
        } catch (DescricaoDeMetaInvalidaException e) {
            //Meta não é criada e o usuário é avisado
            builderFeedback.setMessage(R.string.alertDialog_descricaoMetaNaoPreenchida);
            builderFeedback.setPositiveButton(R.string.alertDialog_descricaoMetaNaoPreenchida_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Faz o diálogo desaparecer
                }
            });
        } catch (TempoEstipuladoInvalidoException e) {
            //Meta não é criada e o usuário é avisado
            builderFeedback.setMessage(R.string.alertDialog_duracaoMetaNaoPreenchida);
            builderFeedback.setPositiveButton(R.string.alertDialog_duracaoMetaNaoPreenchida_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //O dialogo desaparece
                }
            });
        } catch (MetaRepetidaException e) {
            //Meta não é criada e o usuário é avisado
            builderFeedback.setMessage(R.string.alertDialog_metaRepetida);
            builderFeedback.setPositiveButton(R.string.alertDialog_metaRepetida_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //O dialogo desaparece
                }
            });
        }

        builderFeedback.create().show(); //exibe a caixa de dialogo para mostrar o feedback
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
