package br.com.kronos.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;

public class QualidadeDialogFragment extends DialogFragment implements RatingBar.OnRatingBarChangeListener {
    private Atividade atividade;
    private Context context;
    private Button buttonRating;

    public QualidadeDialogFragment(Context context, Atividade atividade, Button buttonRating) {
        this.context = context;
        this.atividade = atividade;
        this.buttonRating = buttonRating;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View viewQualidade = inflater.inflate(R.layout.dialog_qualidade, null);

        RatingBar ratingBarQualidade = (RatingBar) viewQualidade.findViewById(R.id.ratingBar_qualidade);
        ratingBarQualidade.setOnRatingBarChangeListener(this);
        ratingBarQualidade.setRating((float) atividade.getQualidade());

        Button buttonOk = (Button) viewQualidade.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(viewQualidade);
        return builder.create();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        atividade.setQualidade((double) rating);
        buttonRating.setText((int) atividade.getQualidade() + "");
    }
}
