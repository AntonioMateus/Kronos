package br.com.kronos.kronos;

import java.util.List;

/**
 * Created on 23/09/15.
 */
public class Meta {
    private String descricao;
    private List<Atividade> atividadesAssociadas;
    private double tempoAcumulado; //em horas
    private int prazo;
    private boolean notificar; //ponderado somente se categoria == CUSTOM

    private int frequencia;
    public static final int SEM_LIMITES = 0;
    public static final int DIARIA = 1;
    public static final int SEMANAL = 2;
    public static final int MENSAL = 3;
    public static final int CUSTOM = 4;
    public static final int[] FREQUENCIAS = {SEM_LIMITES, DIARIA, SEMANAL, MENSAL, CUSTOM};

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

    public boolean setFrequencia( int frequencia ) throws FrequenciaInvalidaException {
        for (int FREQUENCIA : FREQUENCIAS) {
            if (frequencia == FREQUENCIA) {
                this.frequencia = frequencia;
                return true;
            }
        }
        throw new FrequenciaInvalidaException();
    }

    public int getFrequencia( ){
        return this.frequencia;
    }

    public boolean setPrazo( int prazo ){
        this.prazo = prazo;
        return true;
    }

    public int getPrazo(){
        return this.prazo;
    }
}
