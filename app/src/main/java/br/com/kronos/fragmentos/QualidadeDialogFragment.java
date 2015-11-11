package br.com.kronos.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.R;

public class QualidadeDialogFragment extends DialogFragment{
    private Atividade atividade;
    private Context context;

    public QualidadeDialogFragment(Context context, Atividade atividade) {
        this.context = context;
        this.atividade = atividade;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View viewQualidade = inflater.inflate(R.layout.dialog_qualidade, null);
        viewQualidade.findViewById(R.id.ratingBar_qualidade);

        builder.setView(viewQualidade);
        return builder.create();
    }
}
