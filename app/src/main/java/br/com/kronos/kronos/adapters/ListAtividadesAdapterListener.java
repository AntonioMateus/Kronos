package br.com.kronos.kronos.adapters;

import br.com.kronos.kronos.Atividade;

/*
A classe que implementar essa classe deve implementar o metodo que define
o que deve ser feito quando o ListAtividadesAdapter for atualizado.
 */
public interface ListAtividadesAdapterListener {

    /*
    Metodo que define o que deve ser feito quando o ListAtividadeAdapter for atualizado
     */
    void onAtividadeAdicionada(Atividade atividade);
    /*
    Define o que deve ser feito quando o checkBox de uma atividade for 'checado'
     */
    void onCheckedAtividade(Atividade atividade);
    /*
    Define o que deve ser feito quando há unchecked da Atividade
     */
    void onUncheckedAtividade(Atividade atividade);

    /*
    Define o que deve ser feito quando algum atributo da Atividade é alterado
     */
    void onAtividadeUpdated(Atividade atividadeAlterada);
}
