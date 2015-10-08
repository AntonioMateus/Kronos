package br.com.kronos.kronos.adapters;

import android.widget.EditText;

public class ViewAtividadeHolder {

    private EditText editTextNome;
    private EditText editTextHora;
    private EditText editTextMinuto;
    public int rowNumber;

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
}
