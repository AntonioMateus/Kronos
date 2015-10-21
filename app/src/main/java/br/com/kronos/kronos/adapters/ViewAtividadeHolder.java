package br.com.kronos.kronos.adapters;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class ViewAtividadeHolder {

    private EditText editTextNome; //EditText do nome da atividade
    private EditText editTextHora; //EditText da duraçao da parte em horas da atividade
    private EditText editTextMinuto; //EditText da duraçao  da parte em minutos da atividade

    private ImageButton buttonDelete; //ImageButton que deleta a Atividade da lista
    private Button buttonRating; //ImageButton que avalia a qualidade da Atividade
    private CheckBox checkBox;

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

    public Button getButtonRating() {
        return buttonRating;
    }

    public void setButtonRating(Button buttonRating) {
        this.buttonRating = buttonRating;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }

    public boolean setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
        return true;
    }
}
