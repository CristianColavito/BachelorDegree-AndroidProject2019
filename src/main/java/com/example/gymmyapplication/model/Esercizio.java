package com.example.gymmyapplication.model;

public class Esercizio {
    public String NomeEsercizio;
    public int Ripetizioni, Recupero;
    public Esercizio(String Nomesercizio,int  Rip,int Rec){
        this.NomeEsercizio=Nomesercizio;
        this.Ripetizioni=Rip;
        this.Recupero=Rec;
    }
}
