package br.com.kronos.kronos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.chartmakers.DiarioPieChartMaker;
import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.adapters.ListAtividadesAdapterListener;
import br.com.kronos.database.KronosDatabase;
import br.com.kronos.listener.RatingFragmentListener;

public class MyDayActivity extends Activity implements View.OnClickListener, ListAtividadesAdapterListener,
                                                        View.OnTouchListener, RatingFragmentListener{

    private KronosDatabase kronosDatabase;
    private List<Atividade> atividades; //lista de todas as atividades dentro do listView

    private ListView listViewAtividades;
    private ListAtividadesAdapter listAtividadesAdapter;

    private PieChart pieChart;
    private boolean pieChartHeader = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        kronosDatabase = new KronosDatabase(this);

        atividades = new LinkedList<>();

        Button buttonAddAtividade = (Button) findViewById(R.id.button_adicionar_atividdade);
        buttonAddAtividade.setOnClickListener(this);

        //ListView com as Atividades
        listViewAtividades = (ListView) findViewById(R.id.listView_atividades);
        //Infla PieChart com os atributos do layout especificado
        pieChart = (PieChart) getLayoutInflater().inflate(R.layout.layout_piechart_atividades, listViewAtividades, false);

        //lista de atividades é carregada com as atividades que jah constavam na lista
        atividades = kronosDatabase.getAtividadesLista();

        //Definir dia, mes e ano atual
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int ano = calendar.get(Calendar.YEAR);
        //Checar as atividades que ja foram checadas no dia de hoje
        List<Atividade> atividadesChecadasAnteriormente = kronosDatabase.getAtividadesHistorico(dia, mes, ano);
        for (Atividade atividadeIterada : atividades) {
            if (atividadesChecadasAnteriormente.contains(atividadeIterada)) {
                atividadeIterada.setChecked(true);
            }
        }

        //Listar atividades carregadas na ListView
        setListViewAtividades();
        listViewAtividades.addHeaderView(pieChart, null, false);
        pieChartHeader = true;

        //Plotar grafico com as atividades carregadas e que estao checadas
        try {
            plotar();
        } catch (HorasDiaExcedidoException e) {
            e.printStackTrace();
        }

        //Animar grafico
        pieChart.animateXY(2000, 2000);
    }

    private void setListViewAtividades() {
        //Apos carregadas as atividades em formato de lista, criar um Adapter para alimentar uma ListView
        listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, this);

        if (listAtividadesAdapter.getCount() > 0) {
            listViewAtividades.setAdapter(listAtividadesAdapter);//Alimentar a lista de atividades com o Adapter
            listViewAtividades.setSelection(atividades.size() - 1);
        } else{
            String[] listaVazia = {getString(R.string.listaAtividadesVazia)};
            ArrayAdapter<String> listAdapterVazio = new ArrayAdapter<>(this, R.layout.list_activity_empty_item_layout, listaVazia);
            listViewAtividades.setAdapter(listAdapterVazio);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_setVisibilidadeGrafico) {
            setVisibilidadeGrafico();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVisibilidadeGrafico() {
        if ( pieChartHeader ) {
            listViewAtividades.removeHeaderView(pieChart);
        }else{
            listViewAtividades.addHeaderView(pieChart, null, false);
        }
        pieChartHeader = !pieChartHeader;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_adicionar_atividdade) {
            //Cria uma atividade Default para ser adicionada a lista
            String actividadeNome = getString(R.string.activityDefaultName);
            Atividade atividade = new Atividade(actividadeNome, 0, 0, 0, 0, 0);

            //Se ja houver uma atividade com esse nome, eh acrescentado um numero para diferencia-las
            for (int atividadeAleatoria = 0; atividades.contains(atividade); atividadeAleatoria++) {
                atividade.setNome( actividadeNome + ""+ atividadeAleatoria );
            }

            //Adiciona a Atividade na Lista de Atividades
            atividades.add(atividade);

            //Adiciona a nova atividade ao Adapter
            listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, this);

            onAtividadeAdicionada(atividade);
        }
    }

    /*
    Define tamanho e os dados do grafico de atividades em formato de pizza.
     */
    private void plotar() throws HorasDiaExcedidoException {
        boolean haAtividadesChecadas = false; //indica se ha atividades checadas
        for (Atividade atividade : atividades) {
            if (atividade.isChecked()) {
                haAtividadesChecadas = true;
                break;
            }
        }

        int pieChartWidth = 0;
        int pieChartHeight = 0;

        if (haAtividadesChecadas) {
            //Define o tamamho do Grafico, tornando ele visivel
            pieChartWidth = AbsListView.LayoutParams.MATCH_PARENT;
            pieChartHeight = getResources().getDimensionPixelSize(R.dimen.pieChart_activities_height);

            DiarioPieChartMaker chartMaker = new DiarioPieChartMaker();
            chartMaker.getChart(pieChart, atividades, pieChartWidth, pieChartHeight);
        }

        AbsListView.LayoutParams pieChartLayoutParams = new AbsListView.LayoutParams(pieChartWidth, pieChartHeight);
        pieChart.setLayoutParams(pieChartLayoutParams);
    }

    /*
    Metodo que define o que deve ser feito quando o ListAtividadeAdapter for atualizado
     */
    @Override
    public void onAtividadeAdicionada(Atividade atividade) {
        kronosDatabase.addAtividadeLista(atividade);

        setListViewAtividades();
    }

    /*
    Define o que deve ser feito quando o checkBox de uma atividade for 'checado'
     */
    @Override
    public void onCheckedAtividade(Atividade atividade) throws HorasDiaExcedidoException {
        //Setar data da Atividade para adiciona-la
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int ano = calendar.get(Calendar.YEAR);
        atividade.setDia(dia);
        atividade.setMes(mes);
        atividade.setAno(ano);

        //Adiciona Atividade no Historico de Atividades
        kronosDatabase.addAtividadeHistorico(atividade);

        plotar();
        esconderTeclado();
    }

    /*
    Define o que deve ser feito quando há unchecked da Atividade
     */
    @Override
    public void onUncheckedAtividade(Atividade atividade) {
        //Remover Atividade do Banco de Dados
        kronosDatabase.removeAtividadeHistorico(atividade);

        try {
            plotar();
        } catch (HorasDiaExcedidoException e) {
            //Caso inválido
            e.printStackTrace();
        }
    }

    /*
    Define que quando a Atividade é alterada, o gráfico é utilizado
     */
    @Override
    public void onAtividadeUpdated(Atividade atividadeAlterada, String atividadeNomeAntigo) throws HorasDiaExcedidoException {
        kronosDatabase.updateLista(atividadeAlterada, atividadeNomeAntigo);
        if (atividadeAlterada.isChecked()) {
            kronosDatabase.updateHistorico(atividadeAlterada, atividadeNomeAntigo);
            plotar();
        }
    }

    @Override
    public void onAtividadeRemovida(Atividade atividade) {
        //Se a Atividade estava checada, entao eh efetuada o uncheck desta atividade
        if (atividade.isChecked()) {
            onUncheckedAtividade(atividade);
        }

        //Remove Atividade do Banco de Dados da Lista
        kronosDatabase.removeAtividadeLista(atividade);

        //Atualiza ListView das Atividades
        setListViewAtividades();

        esconderTeclado();
    }

    @Override
    public List<Atividade> getAtividades() {
        return atividades;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        esconderTeclado();
        return true;
    }

    private void esconderTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null){
            inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0);
        }
    }
}
