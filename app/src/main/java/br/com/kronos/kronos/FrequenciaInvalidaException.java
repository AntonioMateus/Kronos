package br.com.kronos.kronos;

/**
 * Created on 23/09/15.
 */
public class FrequenciaInvalidaException extends Throwable {

    @Override
    public String getMessage() {
        return "Frequência não existente. " + super.getMessage();
    }
}
