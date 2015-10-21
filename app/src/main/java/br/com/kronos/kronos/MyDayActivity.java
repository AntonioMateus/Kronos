package br.com.kronos.kronos;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

import br.com.kronos.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class MyDayActivity extends Activity implements View.OnClickListener, ListAtividadesAdapterListener {

    private static final int NUMERO_ATIVIDADES_DIA_MAXIMO = 96; // 24h / 15min
    private ListView listViewAtividades;
    private List<Atividade> atividades;
    private ListAtividadesAdapter listAtividadesAdapter;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        listViewAtividades = (ListView) findViewById(R.id.listView_activities);
        setListaAtividades(); //lista de atividades é carregada com as atividades do dia anterior

        Button buttonAddAtividade = (Button) findViewById(R.id.button_activityAdd);
        buttonAddAtividade.setOnClickListener(this);

        pieChart = (PieChart) findViewById(R.id.pieChart_activities);
    }

    private void setListaAtividades() {
        atividades = new LinkedList<>();
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
        listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, this); //criar um Adapter para alimentar uma ListView
        listViewAtividades.setAdapter(listAtividadesAdapter);//Alimentar a lista de atividades com o Adapter
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
            listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, this);
            //Atualiza a lista de Atividades com o adapter que está com uma nova atividade para ser editada
            listViewAtividades.setAdapter(listAtividadesAdapter);
        }
    }

    /*
    Metodo que define o que deve ser feito quando o ListAtividadeAdapter for atualizado
     */
    @Override
    public void onAdapterUpdate() {
        listViewAtividades.setAdapter(listAtividadesAdapter);
    }

    @Override
    public void onCheckedAtividade(Atividade atividade) {
        plotar();
    }

    private void plotar() {
        pieChart.setDescription(getString(R.string.pieChart_description));
        pieChart.setDrawSliceText(true);
        pieChart.setRotationEnabled(true);

        pieChart.setCenterText(getString(R.string.pieChart_title));
        pieChart.setCenterTextColor(getResources().getColor(R.color.pieChart_title));
        pieChart.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.pieChart_title));

        //PieChart Legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //Add data
        PieData data = getData(atividades);
        //Define o formato dos valores exibidos no grafico
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int hora = (int)value;
                int minuto = (int) (value*60)%60;
                return hora + "h" + minuto + "min";
            }
        });
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    private PieData getData(List<Atividade> atividades) {
        ArrayList<Entry> atividadesDuracao = new ArrayList<>();
        ArrayList<String> atividadesNomes = new ArrayList<>();

        Iterator<Atividade> atividadesIterator = atividades.iterator();
        for (int atividadeIndice = 0; atividadesIterator.hasNext(); atividadeIndice++) {
            Atividade atividade = atividadesIterator.next();
            atividadesNomes.add(atividade.getNome());
            atividadesDuracao.add(new Entry((float) atividade.getDuracao(), atividadeIndice));
        }

        PieDataSet dataSet = new PieDataSet(atividadesDuracao, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        //Adiciona as cores possivei no grafico. Aleatoriamente
        ArrayList<Integer> colors = new ArrayList<>();
        for (int indiceCor = 0; indiceCor < NUMERO_ATIVIDADES_DIA_MAXIMO; indiceCor++) {
            Random random = new Random();
            int redRandom = random.nextInt(256);
            int greenRandom = random.nextInt(256);
            int blueRandom = random.nextInt(256);
            int randomColor = Color.rgb(redRandom, greenRandom, blueRandom);
            if (!(randomColor == Color.BLACK)) {
                colors.add(randomColor);
            }else{
                indiceCor--;
            }
        }
        dataSet.setColors(colors);

        PieData data = new PieData(atividadesNomes, dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        return data;
    }

    @Override
    public void onUncheckedAtividade(Atividade atividade) {

    }
}
