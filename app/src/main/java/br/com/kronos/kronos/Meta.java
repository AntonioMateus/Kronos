package br.com.kronos.kronos;

import java.util.List;

/**
 * Created on 23/09/15.
 */
public class Meta {
    private String descricao;
    private List<Atividade> atividadesAssociadas;
    private String categoria;

    private int prazo; //
    public static final int SEM_LIMITES = 0;
    public static final int DIARIA = 1;
    public static final int SEMANAL = 7;
    public static final int MENSAL = 30;
    public static final int[] PRAZOS_PADRAO = {SEM_LIMITES, DIARIA, SEMANAL, MENSAL};

    /*
    se repetir==true,
        depois que o prazo for satisfeito, o check da Meta sera feito mais de uma vez continuamente
   se nao
        o check da Meta na sera mais feito mais de uma vez
     */
    private boolean repetir;

    private double tempoAcumulado; //Tempo acumulado em horas de todas as atividades associadas dentro do prazo
    private double tempoEstipulado; //Tempo estipulado pelo usuario em horas que o usuario define para considerar que a Meta foi cumprida

    private String dataInicio; //ddmmyyyy

    private boolean notificar;

    public boolean setDescricao(String descricao){
        this.descricao = descricao;
        return true;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public boolean addAtividadeAssociada(Atividade atividade){
        atividadesAssociadas.add(atividade);
        return true;
    }

    public List<Atividade> getAtividadesAssociadas(){
        return atividadesAssociadas;
    }

    public boolean addTempoAcumulado( double tempo ){
        this.tempoAcumulado += tempo;
        return true;
    }

    public double getTempoAcumulado(){
        return this.tempoAcumulado;
    }

    public boolean setPrazo( int prazo ) {
        this.prazo = prazo;
        return true;
    }

    public int getPrazo( ){
        return this.prazo;
    }

    public boolean isRepetir() {
        return repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public boolean isNotificar() {
        return notificar;
    }

    public void setNotificar(boolean notificar) {
        this.notificar = notificar;
    }
}
