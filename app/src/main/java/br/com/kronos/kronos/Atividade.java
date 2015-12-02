package br.com.kronos.kronos;

import android.graphics.Color;
import android.util.Log;

import java.util.List;

public class Atividade {
    public static final double MINUTO_MINIMO = 15;
    private static final double DURACAO_MINIMA = 0.25;

    private String nome;
    private double duracao; //em horas

    private double qualidade; //qualidade do tempo gasto para realizar a tarefa
    private static final double QUALIDADE_MAXIMA = 5; //qualidade máxima que uma atividade pode ter

    private int dia;
    private int mes;
    private int ano;

    /*
    Indica se a atividade foi inclusa no Historico do dia em questao (check = true), ou
    se ela so esta na lista de atividades mas nao foi cehcada para entrar no Historico (check = false)
     */
    private boolean check;
    private int cor;

    public Atividade(String nome, double duracao, double qualidade, int dia, int mes, int ano) {
        this.nome = nome;
        setDuracao(duracao);
        this.qualidade = qualidade;

        this.dia = dia;
        this.mes = mes;
        this.ano = ano;

        this.check = false;

        this.cor = Color.WHITE;
    }

    public void setDuracao(double duracao) {
        if (duracao > 0.0) {
            this.duracao = duracao;
        }else{
            this.duracao = DURACAO_MINIMA;
        }
    }

    public String getNome() {
        return nome;
    }

    public int getHora() {
        return (int) duracao;
    }

    public int getMinuto() {
        return (int) (duracao*60)%60;
    }

    /*
    Adiciona 1 ao indice de qualidade da atividade. Caso a qualidade ultrapasse o numero maximo de
    qualidade, ele volta a 0 (qualidade minima).
     */
    public boolean switchQualidade() {
        this.qualidade = (this.qualidade + 1) % (QUALIDADE_MAXIMA + 1);
        return true;
    }

    public void setQualidade(double qualidade) {
        this.qualidade = qualidade;
    }

    public double getQualidade() {
        return qualidade;
    }

    public double getDuracao() {
        return duracao;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setHora(double hora) {
        double minuto = getMinuto()/60.0;
        this.duracao = hora + minuto;
    }

    /*
    Recebe o valor
     */
    public void setMinuto(double minuto) {
        minuto = (minuto/60.0);
        this.duracao = getHora() + minuto;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atividade atividade = (Atividade) o;

        return !(getNome() != null ? !getNome().equals(atividade.getNome()) : atividade.getNome() != null);

    }

    @Override
    public int hashCode() {
        return getNome() != null ? getNome().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getNome() + "<duracao =" + getDuracao() + ";data=" + getDia() + "/" + getMes() + "/" + getAno() + ">";
    }

    public void setChecked(boolean check) {
        this.check = check;
    }

    public boolean isChecked() {
        return check;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }
}
