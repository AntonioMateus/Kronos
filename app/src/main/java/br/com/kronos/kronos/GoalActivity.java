package br.com.kronos.kronos;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.adapters.ExpandableAdapter;

public class GoalActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_CODE_ADD_GOAL = 0;
    public static final int RESULT_CODE_ADD_GOAL = 0;

    private ExpandableListView expandableListView; //Lista de categorias e suas respectivas metas
    private KronosDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_metas);
        Button buttonAdicionarMeta = (Button) findViewById(R.id.button_adicionarMeta);
        buttonAdicionarMeta.setOnClickListener(this);

        this.database = new KronosDatabase(GoalActivity.this);
        setExpandableList();
    }

    private void setExpandableList() {
        List<Meta> metas = database.getMetasComTempoAcumulado(); //lista de metas trazidas do banco de dados, com as atividades associadas

        ExpandableAdapter expandableAdapter = new ExpandableAdapter(GoalActivity.this, metas);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_GOAL && resultCode == RESULT_CODE_ADD_GOAL) {
            setExpandableList();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onClick(View view) {
        if (view.getId() == R.id.button_adicionarMeta) {
            Intent intentAdicionarMeta = new Intent(this, AddGoalActivity.class);
            startActivityForResult(intentAdicionarMeta, REQUEST_CODE_ADD_GOAL);
        }
    }
}