package br.com.kronos.kronos;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.adapters.ExpandableAdapter;

public class GoalActivity extends Activity implements View.OnClickListener {
    private ImageView _image;
    private float _newAngle, _oldAngle;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    private List<String> listGroup;
    private HashMap<String,List<Meta>> mapaCategoriaMetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button buttonAdicionarMeta = (Button) findViewById(R.id.button_adicionarMeta);
        buttonAdicionarMeta.setOnClickListener(this);

        KronosDatabase database = new KronosDatabase(GoalActivity.this);

        /*
        try {
            //Criacao de metas para teste
            database.removeTodasMetas();
            Meta meta1 = new Meta("projeto verao 2020", 3, false, "saude", 7, 12, 2015);
            meta1.setTempoAcumulado(0);
            meta1.setTempoEstipulado(130.0);
            Meta meta2 = new Meta("assistir star wars", 2, false, "diversao", 28, 11, 2015);
            meta2.setTempoAcumulado(15);
            meta2.setTempoEstipulado(18);
            meta2.setTerminoMeta(29, 11, 2015);
            Meta meta3 = new Meta("terminar de ler a guerra civil", 10, false, "diversao", 9, 12, 2015);
            meta3.setTempoAcumulado(0);
            meta3.setTempoEstipulado(100.0);
            database.addMeta(meta1);
            database.addMeta(meta2);
            database.addMeta(meta3);
        } catch (DescricaoDeMetaInvalidaException | TempoEstipuladoInvalidoException | MetaRepetidaException e) {
            e.printStackTrace();
        }
        */
        //---------------------------------------------

        //listGroup = database.getCategorias(); //Armazena na lista as categorias já existentes
        //mapaCategoriaMetas = database.devolveRelacaoCategoriaMeta();

        //Mapeia Meta em relacao a Categoria
        List<Meta> metas = database.getMetas();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_metas);
        //expandableListView.setAdapter(new ExpandableAdapter(GoalActivity.this, mapaCategoriaMetas, listGroup));
        ExpandableAdapter expandableAdapter = new ExpandableAdapter(GoalActivity.this, metas);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(GoalActivity.this, "item " + childPosition + " do grupo " + groupPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(GoalActivity.this, "grupo " + groupPosition + " expandindo", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(GoalActivity.this, "grupo " + groupPosition + " retraindo", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));
        /*
        ImageButton botao = (ImageButton) findViewById(R.id.imageButton_spinner);
        botao.setOnClickListener(this);
        _image = (ImageView) findViewById(R.id.imageButton_spinner);
        mProgress = (ProgressBar) findViewById(R.id.progressBar_goal);
        mProgress.setProgress(80);
        mProgress.setIndeterminate(false);
        */
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button_adicionarMeta) {
            Intent intentAdicionarMeta = new Intent(this, AddGoalActivity.class);
            startActivity(intentAdicionarMeta);
        }

        /*
        if (view.getId() == R.id.imageButton_spinner) {
            _newAngle = _oldAngle + 180;
            //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _image.getLayoutParams();
            //LayoutParams layoutParams = (LinearLayout.LayoutParams)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) _image.getLayoutParams();
            int centerX = layoutParams.leftMargin + (_image.getWidth()/2);
            int centerY = layoutParams.topMargin + (_image.getHeight()/2);
            RotateAnimation animation = new RotateAnimation(_oldAngle, _newAngle, centerX, centerY);
            animation.setDuration(0);
            animation.setRepeatCount(0);
            animation.setFillAfter(true);
            _image.startAnimation(animation);
            _oldAngle = _newAngle;
        }
        */
    }

    /*public void setGroupParents() {
        parentItems.add("Android");
        parentItems.add("Core Java");
        parentItems.add("Desktop Java");
        parentItems.add("Enterprise Java");
    }

    public void setChildData() {
        // Android
        ArrayList<String> child = new ArrayList<String>();
        child.add("Core");
        child.add("Games");
        childItems.add(child);

        // Core Java
        child = new ArrayList<String>();
        child.add("Apache");
        child.add("Applet");
        child.add("AspectJ");
        child.add("Beans");
        child.add("Crypto");
        childItems.add(child);

        // Desktop Java
        child = new ArrayList<String>();
        child.add("Accessibility");
        child.add("AWT");
        child.add("ImageIO");
        child.add("Print");
        childItems.add(child);

        // Enterprise Java
        child = new ArrayList<String>();
        child.add("EJB3");
        child.add("GWT");
        child.add("Hibernate");
        child.add("JSP");
        childItems.add(child);
    }*/
}