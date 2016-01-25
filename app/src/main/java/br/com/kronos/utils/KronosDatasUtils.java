package br.com.kronos.utils;

public class KronosDatasUtils {

    public static String getDataFormatada(int dia, int mes, int ano) {
        String diaBD;
        if (dia < 10)
            diaBD = "0" +Integer.toString(dia);
        else
            diaBD = Integer.toString(dia);
        String mesBD;
        if (mes < 10)
            mesBD = "0" +Integer.toString(mes);
        else
            mesBD = Integer.toString(mes);
        return diaBD +"_" +mesBD +"_" +ano;
    }

    /*
    Compara duas datas e retorna:
    0, se as duas datas forem as mesmas
    maior que 0, se data1 for mais recente
    menor que 0, se a data1 for mais antiga
    As datas devem ser passadas na formatacao onde dia, mes e ano sao separados por "_"
     */
    public static int compararDatas(String data1, String data2) {
        if (data1.equals(data2)) {
            return 0;
        }

        int dia1 = Integer.parseInt(data1.split("_")[0]);
        int mes1 = Integer.parseInt(data1.split("_")[1]);
        int ano1 = Integer.parseInt(data1.split("_")[2]);

        int dia2 = Integer.parseInt(data2.split("_")[0]);
        int mes2 = Integer.parseInt(data2.split("_")[1]);
        int ano2 = Integer.parseInt(data2.split("_")[2]);

        if (ano1 < ano2) { //Se o ano da atividade eh anterior ao da Meta
            return -1;
        }else if(ano1 == ano2){ //Se o ano da atividade eh o mesmo do que da Meta
            if (mes1 < mes2) { //Se o mes for anterior ao da meta
                return -1;
            } else if (mes1 == mes2){ //Se o mes eh o mesmo do que o da Meta
                if(dia1 < dia2){ //Se o dia for anterior ao da Meta
                    return -1;
                }
            }
        }

        return 1; //as datas sao iguais
    }
}
