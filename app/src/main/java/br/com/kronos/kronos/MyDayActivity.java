package br.com.kronos.kronos;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.kronos.adapters.ListAtividadesAdapter;

public class MyDayActivity extends Activity {

    private ListView listViewAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        listViewAtividades = (ListView) findViewById(R.id.listView_activities);
        setListaAtividades();
    }

    private void setListaAtividades() {
        List<Atividade> atividades = new LinkedList<>();
        /* Traz as atividades do banco de dados local
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
        atividades = databaseOpenHelper.getAtividades();
        */
        //Caso de teste sem banco de dados
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        Atividade atividade = new Atividade("Dormir", 8, 5, dia, mes, ano);
        atividades.add(atividade);

        Atividade atividade2 = new Atividade("Estudar", 2.5 , 1, dia, mes, ano);
        atividades.add(atividade2);

        ListAtividadesAdapter listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades);
        listViewAtividades.setAdapter(listAtividadesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
