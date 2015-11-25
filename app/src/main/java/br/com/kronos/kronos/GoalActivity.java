package br.com.kronos.kronos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import br.com.kronos.kronos.R;

public class GoalActivity extends Activity implements View.OnClickListener {
    private ImageView _image;
    private float _newAngle, _oldAngle;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ImageButton botao = (ImageButton) findViewById(R.id.imageButton_spinner);
        botao.setOnClickListener(this);
        _image = (ImageView) findViewById(R.id.imageButton_spinner);
        mProgress = (ProgressBar) findViewById(R.id.progressBar_goal);
        mProgress.setProgress(80);
        mProgress.setIndeterminate(false);
    }

    public void onClick(View view) {
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
    }
}