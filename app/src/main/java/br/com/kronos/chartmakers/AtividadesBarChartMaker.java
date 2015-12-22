package br.com.kronos.chartmakers;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import br.com.kronos.exceptions.HorasDiaExcedidoException;
import br.com.kronos.kronos.Atividade;

public class AtividadesBarChartMaker implements ChartMaker<Atividade>{
    @Override
    public void getChart(Chart view, List<Atividade> dados, int pieChartWidth, int pieChartHeight) throws HorasDiaExcedidoException {
        BarChart barChart = (BarChart) view;

        barChart.setMaxVisibleValueCount(60);

        //Posicionando o eixo X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(2);

        //Esconder eixo Y da direita
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setEnabled(false);

        //Esconder legenda
        Legend legenda = barChart.getLegend();
        legenda.setEnabled(false);

        barChart.setDescription(""); //sem descricao

        BarData barData = getData(dados);
        barChart.setData(barData);
    }

    private BarData getData(List<Atividade> atividades) {
        ArrayList<BarEntry> atividadesDuracao = new ArrayList<>(); //lista com as duracoes de cada Atividade
        ArrayList<String> atividadesNomes = new ArrayList<>(); //lista com os nomes de cada Atividade
        ArrayList<Integer> cores = new ArrayList<>(); //lista com as cores de cada atividade

        Iterator<Atividade> atividadesIterator = atividades.iterator();
        for (int atividadeIndice = 0; atividadesIterator.hasNext(); atividadeIndice++) {
            Atividade atividade = atividadesIterator.next();
            //Se atividade estiver checada, ela eh incluida no grafico
            if(atividade.isChecked()) {
                //Adicionar nome a lista de nome que sera exibido
                String atividadeNome = atividade.getNome();
                atividadesNomes.add(atividadeNome);

                //Adicionar duracao da atividade da lista de duracoes que sera exibido
                atividadesDuracao.add(new BarEntry((float) atividade.getDuracao(), atividadeIndice));

                //Adiciona as cores às atividades caso não tenha sido escolhido uma cor para tal ainda.
                if (atividade.getCor() == Color.WHITE) {
                    int randomColor = Color.WHITE;
                    Random random = new Random();
                    while ( randomColor == Color.BLACK || randomColor == Color.WHITE || cores.contains(randomColor)) {
                        int redRandom = random.nextInt(256);
                        int greenRandom = random.nextInt(256);
                        int blueRandom = random.nextInt(256);
                        randomColor = Color.rgb(redRandom, greenRandom, blueRandom);
                    }
                    atividade.setCor(randomColor);
                    cores.add(randomColor);
                }else {
                    int atividadeCor = atividade.getCor();
                    cores.add(atividadeCor);
                }
            }
        }

        BarDataSet set = new BarDataSet(atividadesDuracao, "");
        set.setBarSpacePercent(35f);

        set.setColors(cores);

        BarData barData = new BarData(atividadesNomes, set);
        barData.setValueTextSize(10f);

        return barData;
    }
}
