package br.com.kronos.kronos.adapters;

import android.widget.EditText;
import android.widget.ImageButton;

public class ViewAtividadeHolder {

    private EditText editTextNome; //EditText do nome da atividade
    private EditText editTextHora; //EditText da duraçao da parte em horas da atividade
    private EditText editTextMinuto; //EditText da duraçao  da parte em minutos da atividade

    private ImageButton buttonDelete; //ImageButton que deleta a Atividade da lista

    public void setEditTextNome(EditText editTextNome) {
        this.editTextNome = editTextNome;
    }

    public void setEditTextHora(EditText editTextHora) {
        this.editTextHora = editTextHora;
    }

    public void setEditTextMinuto(EditText editTextMinuto) {
        this.editTextMinuto = editTextMinuto;
    }

    public EditText getEditTextNome() {
        return editTextNome;
    }

    public EditText getEditTextHora() {
        return editTextHora;
    }

    public EditText getEditTextMinuto() {
        return editTextMinuto;
    }

    public ImageButton getImageButtonDelete() {
        return buttonDelete;
    }

    public void setButtonDelete(ImageButton buttonDelete) {
        this.buttonDelete = buttonDelete;
    }

}
