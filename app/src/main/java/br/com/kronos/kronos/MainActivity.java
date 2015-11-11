package br.com.kronos.kronos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonMyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMyDay = (Button) findViewById(R.id.button_myDay);
    }

    @Override
    protected void onStart() {
        super.onStart();

        buttonMyDay.setOnClickListener(this);
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_myDay) {
            Intent intentMyDay = new Intent(this, MyDayActivity.class);
            startActivity(intentMyDay);
        }
        if(v.getId() == R.id.button_history){
            Intent intentHistory = new Intent(this, HistoryActivity.class);
            startActivity(intentHistory);
        }
    }
}
