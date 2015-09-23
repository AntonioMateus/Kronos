package br.com.kronos.kronos;

/**
 * Created on 23/09/15.
 */
public class Atividade {
    private String nome;
    private double tempo; //em horas

    private double qualidade;

    public boolean setNome( String nome ){
        this.nome = nome;
        return true;
    }

    public String getNome(){
        return nome;
    }

    public boolean setTempo(double tempo){
        this.tempo = tempo;
        return true;
    }

    public double getTempo(){
        return this.tempo;
    }

    public boolean setQualidade(double qualidade) {
        this.qualidade = qualidade;
        return true;
    }

    public double getQualidade(){
        return qualidade;
    }

}
