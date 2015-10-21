package br.com.kronos.kronos.adapters;

import br.com.kronos.kronos.Atividade;

/*
A classe que implementar essa classe deve implementar o metodo que define
o que deve ser feito quando o ListAtividadesAdapter for atualizado.
 */
public interface ListAtividadesAdapterListener {
    void onAdapterUpdate();
    void onCheckedAtividade(Atividade atividade);
    void onUncheckedAtividade(Atividade atividade);
}
