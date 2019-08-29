package com.example.gymmyapplication.view;

//import android.app.FragmentTransaction;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gymmyapplication.R;
import com.example.gymmyapplication.model.DBHelper;
import com.example.gymmyapplication.model.Esercizio;

import java.util.ArrayList;
public class InsertActivity extends AppCompatActivity {
    DBHelper db;
    FloatingActionButton F_A_B;
    FloatingActionButton remova_frag;
    Button buttonAnnulla;
    TextView viewIns;
    Button btnsave;
    public ArrayList<Exercise_Fragment> listafragment;
    int ripetizioni, recupero;
    String esercizio;
    TextView write;
    boolean flag;
    int flagPunti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        final Intent intent = getIntent();
        final String s = intent.getStringExtra("message");
        setContentView(R.layout.activity_insert);
        listafragment = new ArrayList<>();
        if(savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Exercise_Fragment f = new Exercise_Fragment();
            transaction.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
            transaction.add(R.id.Linear, f, "tag");// give your fragment container id in first parameter
            transaction.commit();
            listafragment.add(f);
        }
        db= new DBHelper(this);
        flag=true;
        flagPunti=0;
        buttonAnnulla=(Button)findViewById(R.id.annulla);
        btnsave=(Button)findViewById(R.id.save_exercise);
        viewIns=(TextView)findViewById(R.id.Work_out);
        F_A_B =(FloatingActionButton) findViewById(R.id.fabscheda);
        remova_frag =(FloatingActionButton) findViewById(R.id.remove_frag);
        viewIns.setText(s);
        assert buttonAnnulla !=null;
        buttonAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        F_A_B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FloatingActionButton remove = findViewById(R.id.remove_frag);
                remove.show();
                remove.setEnabled(true);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Exercise_Fragment f = new Exercise_Fragment();
                transaction.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
                transaction.add(R.id.Linear, f, "tag");// give your fragment container id in first parameter
                transaction.commit();
                listafragment.add(f);
            }
        });
        remova_frag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                FragmentManager managers = getSupportFragmentManager();
                Fragment f = managers.findFragmentByTag("tag");
                FragmentTransaction ft = managers.beginTransaction();
                ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
                if (listafragment.size() > 2){
                    ft.remove(f);
                    ft.commit();
                    listafragment.remove(listafragment.size()-1);
                }
                else if (listafragment.size()==2){
                    ft.remove(f);
                    ft.commit();
                    listafragment.remove(listafragment.size()-1);
                    remova_frag.hide();
                    remova_frag.setEnabled(false);

                }
                else {
                    remova_frag.hide();
                    remova_frag.setEnabled(false);
                }
            }
        });

       /* public int Contr(int i){
            int contrRec=convertInt(listafragment.get(i).rec);

        }*/
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                ArrayList<Esercizio> ListaEsercizi = new ArrayList<>();
                for (int i=0;i<listafragment.size();i++) {
                    if (listafragment.get(i).rip ==null||listafragment.get(i).rec.isEmpty()) {//isEmpty non mi va
                        errore(3);
                        flag = false;
                        break;
                    }
                    if (listafragment.get(i).rec ==null|| listafragment.get(i).rec.isEmpty()) {
                        errore(3);
                        flag=false;
                        break;
                    }

                    else if(controllaPunti(i)==1)//ho messo ;
                    {
                        errore(flagPunti);
                        break;
                    }
                    else if(controllaPunti(i)==2)//ho messo ,
                    {
                        errore(flagPunti);
                        break;
                    }
                    else
                    {
                        if (listafragment.get(i).eserc == null|| listafragment.get(i).eserc.isEmpty())
                            listafragment.get(i).eserc="Esercizio Generico";
                        ripetizioni = convertInt(listafragment.get(i).rip);
                        recupero = convertInt(listafragment.get(i).rec);
                        esercizio = listafragment.get(i).eserc;
                        Esercizio new_esercizio = new Esercizio(esercizio, ripetizioni, recupero);
                        ListaEsercizi.add(new_esercizio);
                    }

                }
                if(flag==true){
                    db.insertScheda(s, ListaEsercizi);
                    SharedPrefesSAVE(s);
                    launchTimer(v,s);
                }


            }
        });
    }
    public void errore(int a){
        {//DIALOG ERRORE
            AlertDialog.Builder myallert = new AlertDialog.Builder(InsertActivity.this)
                    .setTitle(R.string.title_errore);
            if (a==1)
            {
                myallert.setMessage(R.string.insert_punti);
            }
            else if(a==2)
            {
                myallert.setMessage(R.string.insert_virgola);
            }
            else if(a==3)
            {
                myallert.setMessage((R.string.completa));
            }
            myallert.show();
            flagPunti=0;
            flag=false;
        }
    }
    private int convertInt(String a) {
        int b= new Integer(a).intValue();
        return b;
    }
    public void launchTimer(View v,String s){
        Intent i=new Intent(this,TimerActivity.class);
        i.putExtra("nomescheda" , s);
        startActivity(i);
    }
    public void SharedPrefesSAVE(String Name){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }
    public int controllaPunti(int i){
        if (listafragment.get(i).eserc ==null || listafragment.get(i).eserc.isEmpty())
            return flagPunti=0;
        for(int indice=0;indice<listafragment.get(i).eserc.length();indice++){
            if (listafragment.get(i).eserc.charAt(indice)==';'){
                return flagPunti=1;
            }
            if(listafragment.get(i).eserc.charAt(indice)==','){
                return flagPunti=2;
            }
        }
        return flagPunti=0;
    }
}