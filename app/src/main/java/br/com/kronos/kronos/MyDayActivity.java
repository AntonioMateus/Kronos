package br.com.kronos.kronos;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.kronos.adapters.ListAtividadesAdapterListener;

public class MyDayActivity extends Activity implements View.OnClickListener, ListAtividadesAdapterListener {

    private ListView listViewAtividades;
    private List<Atividade> atividades;
    private ListAtividadesAdapter listAtividadesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        listViewAtividades = (ListView) findViewById(R.id.listView_activities);
        setListaAtividades(); //lista de atividades é carregada com as atividades do dia anterior

        Button buttonAddAtividade = (Button) findViewById(R.id.button_activityAdd);
        buttonAddAtividade.setOnClickListener(this);
    }

    private void setListaAtividades() {
        atividades = new LinkedList<>();
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

        //Apos carregadas as atividades em formato de lista:
        listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, this); //criar um Adapter para alimentar uma ListView
        listViewAtividades.setAdapter(listAtividadesAdapter);//Alimentar a lista de atividades com o Adapter
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_activityAdd) {
            //Cria uma atividade Default para ser adicionada a lista
            String activityName = getString(R.string.activityDefaultName);
            Atividade atividade = new Atividade(activityName, 0, 0, 0, 0, 0);
            atividades.add(atividade);

            //Adiciona a nova atividade ao Adapter
            listAtividadesAdapter = new ListAtividadesAdapter(this, R.layout.list_activity_item_layout, atividades, this);
            //Atualiza a lista de Atividades com o adapter que está com uma nova atividade para ser editada
            listViewAtividades.setAdapter(listAtividadesAdapter);
        }
    }

    /*
    Metodo que define o que deve ser feito quando o ListAtividadeAdapter for atualizado
     */
    @Override
    public void onAdapterUpdate() {
        listViewAtividades.setAdapter(listAtividadesAdapter);
    }
}
