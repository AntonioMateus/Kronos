package br.com.kronos.chartmakers;

import com.github.mikephil.charting.charts.Chart;

import java.util.List;

import br.com.kronos.exceptions.HorasDiaExcedidoException;


public interface ChartMaker<T> {
    void getChart(Chart chart, List<T> dados, int pieChartWidth, int pieChartHeight) throws HorasDiaExcedidoException;
}
