package br.com.kronos.kronos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class MyDayActivity extends Activity implements View.OnClickListener, ListAtividadesAdapterListener, View.OnTouchListener {

    private static final int ATIVIDADE_NEUTRA_COR = Color.GRAY; // 24h / 15min

    private ListView listViewAtividades;

    private List<Atividade> atividades; //lista de todas as atividades dentro do listView
    private List<Atividade> atividadesChecadas; //lista das atividades com checkbox acionado

    private ListAtividadesAdapter listAtividadesAdapter;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        atividades = new LinkedList<>();
        atividadesChecadas = new LinkedList<>();

        listViewAtividades = (ListView) findViewById(R.id.listView_activities);
        setListaAtividades(); //lista de atividades é carregada com as atividades do dia anterior

        Button buttonAddAtividade = (Button) findViewById(R.id.button_activityAdd);
        buttonAddAtividade.setOnClickListener(this);

        pieChart = (PieChart) findViewById(R.id.pieChart_activities);
        //pieChart.setOnTouchListener(this);
    }

    private void setListaAtividades(){
        /* Traz as atividades do banco de dados local
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
        atividades = databaseOpenHelper.getAtividades();
        */
        //Caso de teste sem banco de dados
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        Atividade atividade = new Atividade("Dormir", 8, 0, dia, mes, ano);
        atividades.add(atividade);

        Atividade atividade2 = new Atividade("Estudar", 2.5, 0, dia, mes, ano);
        atividades.add(atividade2);

        //Apos carregadas as atividades em formato de lista:
        listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, atividadesChecadas, this); //criar um Adapter para alimentar uma ListView

        if (listAtividadesAdapter.getCount() > 0) {
            listViewAtividades.setAdapter(listAtividadesAdapter);//Alimentar a lista de atividades com o Adapter
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
        //int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_activityAdd) {
            //Cria uma atividade Default para ser adicionada a lista
            String activityName = getString(R.string.activityDefaultName);
            Atividade atividade = new Atividade(activityName, 0, 0, 0, 0, 0);
            atividades.add(atividade);

            //Adiciona a nova atividade ao Adapter
            listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, atividadesChecadas, this);
            //Atualiza a lista de Atividades com o adapter que está com uma nova atividade para ser editada
            listViewAtividades.setAdapter(listAtividadesAdapter);

            //Posiciona a Lista de forma que o ultimo item (o adicionado) seja adicionado.
            listViewAtividades.setSelection(atividades.size() - 1);
        }
    }

    /*
    Metodo que define o que deve ser feito quando o ListAtividadeAdapter for atualizado
     */
    @Override
    public void onAtividadeAdicionada(Atividade atividade) {
        listViewAtividades.setAdapter(listAtividadesAdapter);
        listViewAtividades.setSelection(listAtividadesAdapter.getPosition(atividade));
    }

    /*
    Define o que deve ser feito quando o checkBox de uma atividade for 'checado'
     */
    @Override
    public void onCheckedAtividade(Atividade atividade) throws HorasDiaExcedidoException {
        atividadesChecadas.add(atividade);
        plotar(atividadesChecadas);
        esconderTeclado();
    }

    /*
    Define tamanho e os dados do grafico de atividades em formato de pizza.
     */
    private void plotar(List<Atividade> atividades) throws HorasDiaExcedidoException {
        int pieChartWidth = 0; //largura da View do grafico
        int pieChartHeight = 0; //altura da View do grafico
        if (atividades.size() > 0) {
            pieChartWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
            pieChartHeight = getResources().getDimensionPixelOffset(R.dimen.pieChart_activities_height);

            pieChart.setDescription(getString(R.string.pieChart_description));
            pieChart.setDrawSliceText(true);
            pieChart.setRotationEnabled(true);

            //Texto que fica ao centro do grafico no espaco vazio
            pieChart.setDrawCenterText(false);
            /*
            pieChart.setCenterText(getString(R.string.pieChart_title));
            pieChart.setCenterTextColor(getResources().getColor(R.color.pieChart_title));
            pieChart.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.pieChart_title));
            */

            //Legenda do grafico
            Legend legend = pieChart.getLegend();
            legend.setEnabled(true);
            legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

            pieChart.highlightValues(null);
            pieChart.invalidate();

            PieData data = getData(atividades); //Dados para colocar no grafico
            pieChart.setData(data); //insere os dados no grafico
        }

        //Define as dimensoes do grafico
        RelativeLayout.LayoutParams pieChartLayoutParams =
                new RelativeLayout.LayoutParams(pieChartWidth, pieChartHeight);
        pieChart.setLayoutParams(pieChartLayoutParams);
    }

    private PieData getData(List<Atividade> atividades) throws HorasDiaExcedidoException {
        ArrayList<Entry> atividadesDuracao = new ArrayList<>(); //lista com as duracoes de cada Atividade
        ArrayList<String> atividadesNomes = new ArrayList<>(); //lista com os nomes de cada Atividade

        Iterator<Atividade> atividadesIterator = atividades.iterator();
        for (int atividadeIndice = 0; atividadesIterator.hasNext(); atividadeIndice++) {
            Atividade atividade = atividadesIterator.next();

            //Adicionar nome a lista de nome que sera exibido
            String atividadeNome = atividade.getNome();
            atividadesNomes.add(atividadeNome);

            //Adicionar duracao da atividade da lista de duracoes que sera exibido
            atividadesDuracao.add(new Entry((float) atividade.getDuracao(), atividadeIndice));
        }

        //Confere se a lista de atividades completa as 24h
        double somaDuracoes = 0; //soma das duracoes das atividades na lista carregadas
        for (Atividade atividadeIterada : atividades) {
            somaDuracoes += atividadeIterada.getDuracao();
        }

        /*
        Adiciona uma atividade "Neutra" que completa o dia na ultima posicao da lista de atividades
        caso a soma das duracoes nao complete o dia. A ideia eh que uma atividade neutra
        não represente uma Atividade específica e, ao mesmo tempo, todas as atividades que o usuário não inseriu do seu dia
         */
        if (somaDuracoes < 24.0) {
            double duracaoAtividadeNeutra = 24 - somaDuracoes;
            atividadesNomes.add("");
            atividadesDuracao.add(new Entry((float) duracaoAtividadeNeutra, atividadesDuracao.size()));
        }else if(somaDuracoes > 24.0) {
            throw new HorasDiaExcedidoException();
        }

        PieDataSet dataSet = new PieDataSet(atividadesDuracao, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        //Adiciona as cores possiveis no grafico. Aleatoriamente
        ArrayList<Integer> colors = new ArrayList<>();
        int numeroCores = atividadesNomes.size();
        if (somaDuracoes < 24.0) {
            numeroCores--;
        }
        for (int indiceCor = 0; indiceCor < numeroCores; indiceCor++) {
            Random random = new Random();
            int redRandom = random.nextInt(256);
            int greenRandom = random.nextInt(256);
            int blueRandom = random.nextInt(256);
            int randomColor = Color.rgb(redRandom, greenRandom, blueRandom);
            if (! (randomColor == Color.BLACK) && !(randomColor == ATIVIDADE_NEUTRA_COR)) {
                colors.add(randomColor);
            }else{
                indiceCor--;
            }
        }
        //Se houve adição da Atividade Neutra, adicionar a cor para ela
        if(somaDuracoes < 24.0) {
            colors.add( ATIVIDADE_NEUTRA_COR );
        }

        dataSet.setColors(colors);

        PieData data = new PieData(atividadesNomes, dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        //Define o formato dos valores exibidos no grafico
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int hora = (int) value;
                int minuto = (int) (value * 60) % 60;
                return hora + "h" + minuto + "min";
            }
        });

        return data;
    }

    /*
    Define o que deve ser feito quando há unchecked da Atividade
     */
    @Override
    public void onUncheckedAtividade(Atividade atividade) {
        boolean removed = atividadesChecadas.remove(atividade);
        if(removed) {
            try {
                plotar(atividadesChecadas);
            } catch (HorasDiaExcedidoException e) {
                //Caso inválido
                e.printStackTrace();
            }
        }
    }

    /*
    Define que quando a Atividade é alterada, o gráfico é utilizado
     */
    @Override
    public void onAtividadeUpdated(Atividade atividadeAlterada) throws HorasDiaExcedidoException {
        plotar(atividadesChecadas);
    }

    @Override
    public void onAtividadeRemovida(Atividade atividade) {
        atividades.remove(atividade);
        if (listAtividadesAdapter.getCount() > 0) {
            listViewAtividades.setAdapter(listAtividadesAdapter);
        } else{
            String[] listaVazia = {getString(R.string.listaAtividadesVazia)};
            ArrayAdapter<String> listAdapterVazio = new ArrayAdapter< >(this, R.layout.list_activity_empty_item_layout, listaVazia);
            listViewAtividades.setAdapter(listAdapterVazio);
        }
        onUncheckedAtividade(atividade);
        esconderTeclado();
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
