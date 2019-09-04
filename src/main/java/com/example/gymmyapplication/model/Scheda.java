package com.example.gymmyapplication.model;

import java.util.List;

public class Scheda {
    public static final String TABLE_NAME="scheda";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_NOMES="nome_scheda";
    public static final String COLUMN_ESERC="nome_esercizio";
    private int id;
    private String nome_scheda;
    private List<Esercizio> esercizi;
    private int rec;
    private int rip;

    //Create Table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOMES + " TEXT,"
                    + COLUMN_ESERC + " TEXT"
                    + ")";
    public Scheda(){

    }
    public void AggiungiEsercizio(Esercizio esercizio)
    {
        this.esercizi.add(esercizio);
    }
    public Scheda( String nome_scheda, List<Esercizio> nome_esercizio){
        this.nome_scheda=nome_scheda;
        this.esercizi=nome_esercizio;
    }
    public String getNome_scheda(){
        return nome_scheda;
    }

    public List<Esercizio> getEsercizi() {
        return esercizi;
    }
    public void setId(int id){
        this.id= id;
    }
    public void setRec(int rec) {
        this.rec = rec;
    }
    public void setRip(int rip) {
        this.rip = rip;
    }
    public void setNome_scheda(String nome_scheda) {
        this.nome_scheda = nome_scheda;
    }
    public void setNome_esercizi(List nome_esercizio) {
        this.esercizi = nome_esercizio;
    }
}