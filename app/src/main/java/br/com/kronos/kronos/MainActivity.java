package br.com.kronos.kronos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Random;

import br.com.kronos.database.KronosDatabase;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonMyDay;
    private Button buttonHistory;
    private Button buttonGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMyDay = (Button) findViewById(R.id.button_myDay);
        buttonHistory = (Button) findViewById(R.id.button_history);
        buttonGoal = (Button) findViewById(R.id.button_goal);

        Button buttonGenerateData = (Button) findViewById(R.id.button_generateData);
        buttonGenerateData.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        buttonMyDay.setOnClickListener(this);
        buttonHistory.setOnClickListener(this);
        buttonGoal.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_myDay) {
            Intent intentMyDay = new Intent(this, MyDayActivity.class);
            startActivity(intentMyDay);
        }else if(v.getId() == R.id.button_history){
            Intent intentHistory = new Intent(this, HistoryActivity.class);
            startActivity(intentHistory);
        }else if(v.getId() == R.id.button_goal){
            Intent intentHistory = new Intent(this, GoalActivity.class);
            startActivity(intentHistory);
        }else if (v.getId() == R.id.button_generateData) {
            gerarDadosAleatorios();
        }
    }

    private void gerarDadosAleatorios() {

        String[] atividadesNome = {"Dormir", "Transporte", "Alimentacao", "Lazer", "Esporte", "LoLzin", "DarkSouls", "Boliche"};

        KronosDatabase kronosDatabase = new KronosDatabase(this);

        int[] meses = {31, //janeiro
                28, //fevereiro
                31, //marco
                30, //abril
                31, //maio
                30, //junho
                31, //julho
                31, //agosto
                30, //setembro
                31, //outubro
                30, //novembro
                31}; //dezembro

        Calendar calendar = Calendar.getInstance();
        int diaCorrente = calendar.get(Calendar.DAY_OF_MONTH); //1, 2, ... 31
        int mesCorrente = calendar.get(Calendar.MONTH) + 1; //1, 2, ... 12
        int anoCorrente = calendar.get(Calendar.YEAR);

        for(int diasPassados = 0; diasPassados < 365; diasPassados++ ){
            diaCorrente--;
            if( diaCorrente == 0 ) {
                mesCorrente--;
                if(mesCorrente == 0){
                    mesCorrente = meses.length;
                    anoCorrente--;
                }
                diaCorrente = meses[mesCorrente - 1];
            }

            Random random = new Random();
            int pRandomico = random.nextInt(10);
            int probabilidadeDeUsarOKronos = 4;

            if(pRandomico < probabilidadeDeUsarOKronos) {
                for (String atividadeNome : atividadesNome) {
                    double duracao = random.nextDouble() * (24/ atividadesNome.length);
                    if (duracao != 0) {
                        double qualidade = random.nextInt(5);
                        Atividade atividade = new Atividade(atividadeNome, duracao, qualidade, diaCorrente, mesCorrente, anoCorrente);
                        kronosDatabase.addAtividadeHistorico(atividade);
                    }
                }
            }
        }
    }
}
