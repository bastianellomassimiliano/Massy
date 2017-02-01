package com.bastianello.quiz;


public class Domanda {
    public Domanda(boolean risposta, String testo) {
        this.risposta = risposta;
        this.testo = testo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public boolean getRisposta() {
        return risposta;
    }

    public void setRisposta(boolean risposta) {
        this.risposta = risposta;
    }

    @Override
    public String toString(){
        return this.getTesto() + ": " + (this.risposta? "true": "false");
    }

    private String testo;
    private boolean risposta;
}//Domande
