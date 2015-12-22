package br.com.kronos.chartmakers;

import android.graphics.Color;
import android.widget.AbsListView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.utils.Pintor;

public class DiarioPieChartMaker implements ChartMaker<Atividade>{

    private static final int ATIVIDADE_NEUTRA_COR = Color.GRAY;

    @Override
    public void getChart(Chart chart, List<Atividade> atividades, int pieChartWidth, int pieChartHeight) throws HorasDiaExcedidoException {
        //pieChart.setDescription(getString(R.string.pieChart_description));
        PieChart pieChart = (PieChart) chart;
        AbsListView.LayoutParams pieChartLayoutParams = new AbsListView.LayoutParams(pieChartWidth, pieChartHeight);
        pieChart.setLayoutParams(pieChartLayoutParams);

        pieChart.setDescription("");
        pieChart.setDrawSliceText(true);
        pieChart.setRotationEnabled(true);

        //Texto que fica ao centro do grafico no espaco vazio
        pieChart.setDrawCenterText(false);
        /*
        pieChart.setCenterText(getString(R.string.pieChart_title));
        pieChart.setCenterTextColor(getResources().getColor(R.color.pieChart_title));
        pieChart.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.pieChart_title));
        */

        pieChart.highlightValues(null);
        pieChart.invalidate();

        //Legenda do grafico
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

        PieData data = getData(atividades); //Dados para colocar no grafico
        pieChart.setData(data); //insere os dados no grafico

        //return pieChart;
    }

    private PieData getData(List<Atividade> atividades) throws HorasDiaExcedidoException {
        ArrayList<Entry> atividadesDuracao = new ArrayList<>(); //lista com as duracoes de cada Atividade
        ArrayList<String> atividadesNomes = new ArrayList<>(); //lista com os nomes de cada Atividade
        ArrayList<Integer> cores = new ArrayList<>(); //lista com as cores de cada atividade

        //Confere se a lista de atividades completa as 24h
        double somaDuracoes = 0; //soma das duracoes das atividades na lista carregadas

        Iterator<Atividade> atividadesIterator = atividades.iterator();
        Pintor pintor = new Pintor();
        for (int atividadeIndice = 0; atividadesIterator.hasNext(); atividadeIndice++) {
            Atividade atividade = atividadesIterator.next();
            //Se atividade estiver checada, ela eh incluida no grafico
            if(atividade.isChecked()) {
                //Adicionar nome a lista de nome que sera exibido
                String atividadeNome = atividade.getNome();
                atividadesNomes.add(atividadeNome);

                //Adicionar duracao da atividade da lista de duracoes que sera exibido
                atividadesDuracao.add(new Entry((float) atividade.getDuracao(), atividadeIndice));

                somaDuracoes += atividade.getDuracao();

                //Adiciona as cores às atividades caso não tenha sido escolhido uma cor para tal ainda.
                if (atividade.getCor() == Color.WHITE) {
                    int randomColor = Color.WHITE;
                    //Random random = new Random();
                    while ( randomColor == Color.BLACK || randomColor == ATIVIDADE_NEUTRA_COR || randomColor == Color.WHITE || cores.contains(randomColor)) {
                        /*
                        int redRandom = random.nextInt(256);
                        int greenRandom = random.nextInt(256);
                        int blueRandom = random.nextInt(256);
                        randomColor = Color.rgb(redRandom, greenRandom, blueRandom);
                        */
                        randomColor = pintor.randomTinta();
                    }
                    atividade.setCor(randomColor);
                    cores.add(randomColor);
                }else {
                    int atividadeCor = atividade.getCor();
                    cores.add(atividadeCor);
                }
            }
        }

        /*
        Adiciona uma atividade "Neutra" que completa o dia na ultima posicao da lista de atividades
        caso a soma das duracoes nao complete o dia. A ideia eh que uma atividade neutra
        não represente uma Atividade específica e, ao mesmo tempo, todas as atividades que o usuário não inseriu do seu dia
         */
        if (somaDuracoes < 24.0) {
            double duracaoAtividadeNeutra = 24 - somaDuracoes;
            //Adicionando Atividade Neutra ao conjunto de Atividades
            atividadesNomes.add("");
            atividadesDuracao.add(new Entry((float) duracaoAtividadeNeutra, atividadesDuracao.size()));
            cores.add(ATIVIDADE_NEUTRA_COR);
        }else if(somaDuracoes > 24.0) {
            throw new HorasDiaExcedidoException();
        }

        PieDataSet dataSet = new PieDataSet(atividadesDuracao, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(5);

        dataSet.setColors(cores);

        PieData data = new PieData(atividadesNomes, dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //Define o formato dos valores exibidos no grafico
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int hora = (int) value;
                int minuto = (int) (value * 60) % 60;
                if (minuto > 0) {
                    return hora + "h" + minuto + "min";
                }else{
                    return hora + "h";
                }
            }
        });

        return data;
    }
}
