package br.com.kronos.kronos;

/**
 * Created on 23/09/15.
 */
public class Atividade {
    private String nome;
    private double duracao; //em horas

    private double qualidade;
    private String data; //ddmmyyyy

    public boolean setNome( String nome ){
        this.nome = nome;
        return true;
    }

    public String getNome(){
        return nome;
    }

    public boolean setDuracao(double duracao){
        this.duracao = duracao;
        return true;
    }

    public double getTempo(){
        return this.duracao;
    }

    public boolean setQualidade(double qualidade) {
        this.qualidade = qualidade;
        return true;
    }

    public double getQualidade(){
        return qualidade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
