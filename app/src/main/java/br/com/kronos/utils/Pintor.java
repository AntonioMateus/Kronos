package br.com.kronos.utils;

import android.graphics.Color;

import java.util.Random;

public class Pintor {

    private int indiceTintaCorrente;
    //Conjunto de tintas disponiveis
    private int[] tintas = {
            Color.parseColor("#f44336"), //vermelho
            Color.parseColor("#b71c1c"), //vermelho
            Color.parseColor("#e53935"), //vermelho

            Color.parseColor("#e91e63"), //rosa
            Color.parseColor("#f50057"), //rosa
            Color.parseColor("#c51162"), //rosa
            
            Color.parseColor("#9c27b0"), //roxo
            Color.parseColor("#aa00ff"), //roxo
            Color.parseColor("#d500f9"), //roxo

            Color.parseColor("#673ab7"), //roxo escuro
            Color.parseColor("#b388ff"), //roxo escuro
            Color.parseColor("#6200ea"), //roxo escuro

            Color.parseColor("#3f51b5"), //indigo
            Color.parseColor("#304ffe"), //indigo
            Color.parseColor("#8c9eff"), //indigo

            Color.parseColor("#2196f3"), //azul
            Color.parseColor("#82b1ff"), //azul
            Color.parseColor("#2962ff"), //azul

            Color.parseColor("#03a9f4"), //azul claro
            Color.parseColor("#00b0ff"), //azul claro
            Color.parseColor("#80d8ff"), //azul claro

            Color.parseColor("#00bcd4"), //ciano
            Color.parseColor("#84ffff"), //ciano
            Color.parseColor("#00e5ff"), //ciano

            Color.parseColor("#009688"), //teal
            Color.parseColor("#a7ffeb"), //teal
            Color.parseColor("#1de9b6"), //teal

            Color.parseColor("#4caf50"), //verde
            Color.parseColor("#b9f6ca"), //verde
            Color.parseColor("#00c853"), //verde

            Color.parseColor("#8bc34a"), //verde claro
            Color.parseColor("#ccff90"), //verde claro
            Color.parseColor("#64dd17"), //verde claro

            Color.parseColor("#cddc39"), //limao
            Color.parseColor("#f4ff81"), //limao
            Color.parseColor("#c6ff00"), //limao

            Color.parseColor("#ffeb3b"), //amarelo
            Color.parseColor("#ffff8d"), //amarelo
            Color.parseColor("#ffd600"), //amarelo
    };

    public Pintor() {
        this.indiceTintaCorrente = 0;
    }

    public int nextTinta() {
        int tinta = tintas[indiceTintaCorrente];
        indiceTintaCorrente = (indiceTintaCorrente + 1)%tintas.length;
        return tinta;
    }

    public int randomTinta() {
        Random random = new Random();
        return tintas[(int)(random.nextDouble() * tintas.length)];
    }
}
