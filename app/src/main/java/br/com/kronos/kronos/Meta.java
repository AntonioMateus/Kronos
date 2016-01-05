package br.com.kronos.kronos;

import java.util.List;

import br.com.kronos.exceptions.DescricaoDeMetaInvalidaException;
import br.com.kronos.exceptions.TempoEstipuladoInvalidoException;

public class Meta {
    public static final double PRAZO_MINIMO = 0.25;

    /*
    Descreve qual explicitamente ou implicitamente (vai da vontade do Usuario do Kronos) qual é a Meta
    Serve como um titulo para a Meta e é o que a diferencia das outras.
     */
    private String descricao;
    private String categoria; //maneira de categorizar uma Meta, agrupando com outras Metas

    /*
    Lista de Atividades Associadas a Meta. Significa que para essa meta ser atingida
    o tempo gasto nessas Atividades deve ultrapassar o Tempo Estipulado para essa Meta
     */
    private List<Atividade> atividadesAssociadas;
    private double tempoEstipulado = 0.0; //tempo em horas que o usuário deve "Alcançar" (com as Atividades) para que essa Meta seja atingida
    private double tempoAcumulado = 0.0; //quanto tempo jah foi gasto nas Atividades que associadas a Meta
    private double prazo; //tempo em horas que define em quanto tempo o usuario deve terminar a Meta

    private boolean repetir; //diz se a Meta irá ser definida com Meta novamente depois que o Prazo acabar

    private int diaInicio; //Dia do mes em que essa Meta foi estipulada
    private int mesInicio; //Mes em que essa Meta foi estipulada (Janeiro = 1 ... Dezembro = 2)
    private int anoInicio; //Ano em que essa Meta foi estipulada

    private int diaTermino; //Dia do mes em que essa Meta foi atingida
    private int mesTermino; //Mes em que essa Meta foi atingida (Janeiro = 1 ... Dezembro = 2)
    private int anoTermino; //Ano em que essa Meta foi atingida

    public Meta(String descricao, double tempoEstipulado, int diaInicio, int mesInicio, int anoInicio) throws DescricaoDeMetaInvalidaException, TempoEstipuladoInvalidoException {
        setDescricao(descricao);
        setTempoEstipulado(tempoEstipulado);
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.anoInicio = anoInicio;
    }

    public Meta (String descricao, double prazo, boolean repetir, String categoria, int diaInicio, int mesInicio, int anoInicio) throws DescricaoDeMetaInvalidaException, TempoEstipuladoInvalidoException {
        this(descricao, Double.MAX_VALUE, diaInicio, mesInicio, anoInicio);
        setPrazo(prazo);
        this.repetir = repetir;
        this.categoria = categoria;
    }

    //Retorna um booleano que diz se a Meta foi Atingida ou não
    public boolean getMetaAtingida() {
        return tempoAcumulado >= tempoEstipulado;
    }

    public void setTerminoMeta (int diaTermino, int mesTermino, int anoTermino) {
        this.diaTermino = diaTermino;
        this.mesTermino = mesTermino;
        this.anoTermino = anoTermino;
    }

    public void setPrazo(double prazo) {
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
    }

    public void setTempoEstipulado(double tempoEstipulado) throws TempoEstipuladoInvalidoException {
        //O Tempo Estipulado não pode ser menor que a duração de uma Atividade mínima
        if (tempoEstipulado >= Atividade.DURACAO_MINIMA) {
            this.tempoEstipulado = tempoEstipulado;
        }else {
            throw new TempoEstipuladoInvalidoException();
        }
    }

    public double getTempoAcumulado() {
        return this.tempoAcumulado;
    }

    public double getTempoEstipulado() {
        return this.tempoEstipulado;
    }

    public double getTempoExcedido() {
        return this.tempoAcumulado - this.tempoEstipulado;
    }

    public boolean getRepetir() {
        return this.repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
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
        return "Meta - descricao:"+getDescricao()+"; categoria:"+getCategoria()+"; data de inicio:"+getDiaInicio()+"/"+getMesInicio()+"/"+getAnoInicio()+" ("+(getMetaAtingida()?"ja":"nao")+" finalizada)";
    }

    public void setAtividadesAssociadas(List<Atividade> atividadesAssociadas) {
        this.atividadesAssociadas = atividadesAssociadas;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
