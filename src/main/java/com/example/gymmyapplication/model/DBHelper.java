package com.example.gymmyapplication.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    String array;
    private static final int DATABASE_VERSION= 1;
    private static final String DATABASE_NAME= "scheda_db";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Scheda.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Scheda.TABLE_NAME);
        onCreate(db);
    }
    public long insertScheda(String nomes, List arrayEserc){
        StringBuilder sb = new StringBuilder();
        for (Object ese : arrayEserc) {
            ese=(Esercizio)ese;
            sb.append(((Esercizio) ese).NomeEsercizio).append(",");
            sb.append(((Esercizio) ese).Recupero).append(",");
            sb.append(((Esercizio) ese).Ripetizioni).append(";");
        }
        String plus = sb.toString();

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Scheda.COLUMN_NOMES,nomes);
        values.put(Scheda.COLUMN_ESERC,plus);
        long id=db.insert(Scheda.TABLE_NAME,null,values);
        db.close();
        return id;
    }
    public List<Scheda> getAllSchede() {
        List<Scheda> schede = new ArrayList<>();
        List<Esercizio> Eserc_array=new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ Scheda.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do{
                array=cursor.getString(cursor.getColumnIndex(Scheda.COLUMN_ESERC));
                Scheda scheda =new Scheda();
                scheda.setId(cursor.getInt(cursor.getColumnIndex(scheda.COLUMN_ID)));
                scheda.setNome_scheda(cursor.getString(cursor.getColumnIndex(scheda.COLUMN_NOMES)));
                Eserc_array=FunzioneMadre(array);
                scheda.setNome_esercizi(Eserc_array);
                schede.add(scheda);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return schede;
    }
    public void deleteScheda(Scheda scheda){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Scheda.TABLE_NAME,Scheda.COLUMN_NOMES + " = ?",
                new String[]{String.valueOf(scheda.getNome_scheda())});
        db.close();
    }
    public int getSchedeCount(){
        String countQuery= "SELECT  * FROM " + Scheda.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        int count= cursor.getCount();
        cursor.close();
        return count;
    }
    public Scheda getScheda(String nomes){
        List<Esercizio> Eserc_array=new ArrayList<>();
        String query="SELECT * FROM " + Scheda.TABLE_NAME+ " WHERE " + Scheda.COLUMN_NOMES +" = "+ "'"+ nomes+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        Scheda scheda=new Scheda();
        if(cursor.moveToFirst()){
            array=cursor.getString(cursor.getColumnIndex(Scheda.COLUMN_ESERC));
            Eserc_array=FunzioneMadre(array);
            scheda.setId(cursor.getInt(cursor.getColumnIndex(scheda.COLUMN_ID)));
            scheda.setNome_scheda(cursor.getString(cursor.getColumnIndex(scheda.COLUMN_NOMES)));
            scheda.setNome_esercizi(Eserc_array);
        }
        db.close();
        return scheda;
    }
    public List<Esercizio> FunzioneMadre(String array){
        List<Esercizio> arrayEserc=new ArrayList<>();
        String str[] = array.split(";"); //mi restituisce tutto fino al ; ESERC ha 3 cose dentro
        for(int i=0;i<str.length;i++)
        {
            String []   strEserc=str[i].split(",");
            Esercizio es= new Esercizio(strEserc[0],Integer.parseInt(strEserc[2]),Integer.parseInt(strEserc[1]));
            arrayEserc.add(es);
        }
        return arrayEserc;
    }
}