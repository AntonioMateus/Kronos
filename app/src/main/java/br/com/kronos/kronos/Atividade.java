package br.com.kronos.kronos;

import android.util.Log;

import java.util.List;

public class Atividade {
    public static final double MINUTO_MINIMO = 15;
    private static final double DURACAO_MINIMA = 0.25;

    private String nome;
    private double duracao; //em horas

    private double qualidade; //qualidade do tempo gasto para realizar a tarefa
    private static final double QUALIDADE_MAXIMA = 5; //qualidade máxima que uma atividade pode ter
    private String data; //ddmmyyyy

    public Atividade(String nome, double duracao, int qualidade, int dia, int mes, int ano) {
        this.nome = nome;
        setDuracao(duracao);
        this.qualidade = qualidade;
        this.data = setData(dia, mes, ano);
    }

    public Atividade(String nome, double duracao, int qualidade, String data) {
        this.nome = nome;
        setDuracao(duracao);
        this.qualidade = qualidade;
        this.data = data;
    }

    private void setDuracao(double duracao) {
        if (duracao > 0.0) {
            this.duracao = duracao;
        }else{
            this.duracao = DURACAO_MINIMA;
        }
    }

    /*
    Esse método formata a data da maneira certa para salvar no banco de dados: ddmmaaaa
     */
    private String setData(int dia, int mes, int ano) {
        String data = "";

        //Caso a dia seja de apenas um digito (menor que 10), acrescentar dígito '0'
        if (dia < 10) {
            data += 0 + ""+dia;
        }else{
            data += ""+dia;
        }

        //Caso a mes seja de apenas um digito (menor que 10), acrescentar dígito '0'
        if (mes < 10) {
            data += 0 + ""+mes;
        }else{
            data += ""+mes;
        }

        data += ""+ano;

        return data;
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

    public double getQualidade() {
        return qualidade;
    }

    public String getData() { return data;}

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

    public static int atividadesChecadasComMesmoNome(Atividade atividade, List<Atividade> atividades) {
        int atividadesComMemsmoNome = 0;
        String atividadeNome = atividade.getNome();
        for (Atividade atividadeIterada : atividades) {
            String atividadeIteradaNome = atividadeIterada.getNome();
            if (atividadeNome.equals(atividadeIteradaNome)) {
                atividadesComMemsmoNome++;
            }
        }
        return atividadesComMemsmoNome;
    }
}
