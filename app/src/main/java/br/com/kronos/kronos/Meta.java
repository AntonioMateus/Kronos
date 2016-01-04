package br.com.kronos.kronos;

import java.util.List;

import br.com.kronos.exceptions.DescricaoDeMetaInvalidaException;

public class Meta {
    public static final double PRAZO_MINIMO = 0.25;

    private String descricao;
    private double prazo;
    private double tempoAcumulado = 0.0;
    private double tempoEstipulado = 0.0;
    private double tempoExcedido;
    private boolean repetir; //diz se a Meta irá ser definida com Meta novamente depois do prazo acabar
    private String categoria;
    private boolean metaTerminada;

    private int diaInicio;
    private int mesInicio;
    private int anoInicio;

    private int diaTermino;
    private int mesTermino;
    private int anoTermino;
    private List<Atividade> atividadesAssociadas;

    public Meta (String descricao, double prazo, boolean repetir, String categoria, int diaInicio, int mesInicio, int anoInicio) throws DescricaoDeMetaInvalidaException {
        setDescricao(descricao);
        setPrazo(prazo);
        this.repetir = repetir;
        this.categoria = categoria;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.anoInicio = anoInicio;
        this.metaTerminada = false;
    }

    public void setTerminoMeta (int diaTermino, int mesTermino, int anoTermino) {
        this.diaTermino = diaTermino;
        this.mesTermino = mesTermino;
        this.anoTermino = anoTermino;
        this.metaTerminada = true;
    }

    private void setPrazo (double prazo) {
        if (prazo < PRAZO_MINIMO) {
            this.prazo = PRAZO_MINIMO;
        }
        else {
            this.prazo = prazo;
        }
    }

    public void setDescricao(String descricao) throws DescricaoDeMetaInvalidaException {
        if (descricao.isEmpty()) {
            throw new DescricaoDeMetaInvalidaException();
        }
        this.descricao = descricao;
    }

    public String getDescricao () {
        return this.descricao;
    }

    public String getCategoria () {
        return this.categoria;
    }

    public double getPrazo () {
        return this.prazo;
    }

    public void setTempoAcumulado (double tempoAcumulado) {
        this.tempoAcumulado = tempoAcumulado;
        this.tempoExcedido = this.tempoAcumulado - this.tempoEstipulado;
    }

    public void setTempoEstipulado (double tempoEstipulado) {
        this.tempoEstipulado = tempoEstipulado;
        this.tempoExcedido = this.tempoAcumulado - this.tempoEstipulado;
    }

    public double getTempoAcumulado() {
        return this.tempoAcumulado;
    }

    public double getTempoEstipulado() {
        return this.tempoEstipulado;
    }

    public double getTempoExcedido() {
        return this.tempoExcedido;
    }

    public boolean getRepetir() {
        return this.repetir;
    }

    public boolean getMetaTerminada() {
        return this.metaTerminada;
    }

    public int getDiaInicio() {
        return this.diaInicio;
    }

    public int getMesInicio() {
        return this.mesInicio;
    }

    public int getAnoInicio() {
        return this.anoInicio;
    }

    public int getDiaTermino() {
        return this.diaTermino;
    }

    public int getMesTermino() {
        return this.mesTermino;
    }

    public int getAnoTermino() {
        return this.anoTermino;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) { return true; }
        if (o == null){
            return false;
        } else if (o.getClass()!=this.getClass()){
                return false;
        }

        Meta meta = (Meta) o;
        boolean descricaoIgual = !(getDescricao() != null ? !getDescricao().equals(meta.getDescricao()) : meta.getDescricao() != null);
        boolean categoriaIgual = !(getCategoria() != null ? !getCategoria().equals(meta.getCategoria()) : meta.getCategoria() != null);
        return descricaoIgual & categoriaIgual;
    }

    @Override
    public String toString() {
        return "Meta - descricao:"+getDescricao()+"; categoria:"+getCategoria()+"; data de inicio:"+getDiaInicio()+"/"+getMesInicio()+"/"+getAnoInicio()+" ("+(getMetaTerminada()?"ja":"nao")+" finalizada)";
    }

    public void setAtividadesAssociadas(List<Atividade> atividadesAssociadas) {
        this.atividadesAssociadas = atividadesAssociadas;
    }
}
