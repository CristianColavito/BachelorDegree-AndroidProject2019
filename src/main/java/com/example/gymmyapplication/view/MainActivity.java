package com.example.gymmyapplication.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymmyapplication.R;
import com.example.gymmyapplication.model.DBHelper;
import com.example.gymmyapplication.model.Scheda;
import com.example.gymmyapplication.utils.MyDividerItemDecoration;
import com.example.gymmyapplication.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SchedeAdapter mAdapter;
    private List<Scheda> schedaList =new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noSchedeView;
    private DBHelper db;
    private CoordinatorLayout coordinatorLayout;
    private Dialog ThisDialog;
    private TextView Myname;
    //Toast.makeText(this,"Tap per iniziare l'allenamento \nTenere premuto per cancellare",Toast.LENGTH_LONG).show();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout=findViewById(R.id.coordinator_layout);
        recyclerView=findViewById(R.id.recycler_view);
        noSchedeView =findViewById(R.id.empty_schede_view);
        db=new DBHelper(this);
        schedaList.addAll(db.getAllSchede());
        FloatingActionButton fabaddscheda=(FloatingActionButton) findViewById(R.id.fabscheda);
        Myname = (TextView)findViewById(R.id.Work_out);
        ImageButton btinfo=(ImageButton)findViewById(R.id.btinfo);
        btinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoToast();
            }
        });
        fabaddscheda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisDialog = new Dialog(MainActivity.this);
                ThisDialog.setTitle("Save Your Name");
                ThisDialog.setContentView(R.layout.dialog_template);
                final EditText Write = (EditText)ThisDialog.findViewById(R.id.write);
                Button SaveMyWorkOut = (Button)ThisDialog.findViewById(R.id.SaveNow);
                Button CancelMyWorkOut = (Button)ThisDialog.findViewById(R.id.Cancel);
                Write.setEnabled(true);
                SaveMyWorkOut.setEnabled(true);
                SaveMyWorkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = Write.getText().toString();
                        if(s.isEmpty())
                        {
                            {//DIALOG COMPILA CAMPI
                                new AlertDialog.Builder (MainActivity.this)
                                        .setTitle(R.string.title_errore)
                                        .setMessage(R.string.completa)
                                        .create()
                                        .show();
                            }
                        }
                        else {
                            SharedPrefesSAVE(s);
                            ThisDialog.cancel();
                            launchInsert(v, s);
                        }
                    }

                });
                CancelMyWorkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThisDialog.dismiss();
                    }
                });
                ThisDialog.show();
            }
        });
        mAdapter = new SchedeAdapter(this,schedaList);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL,16));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String f= caricaScheda(position);
                launchTimer(view,f);
            }
            @Override
            public void onLongClick(View view, int position) {
                cancelSchedaDialog(position);
            }
        }));
        toggleEmptySchede();
    }
    private void cancelSchedaDialog(final int position){
        CharSequence color[] =new CharSequence[]{
                "Conferma", "Annulla"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Vuoi eliminare la scheda?");
        builder.setItems(color, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    deleteScheda(position);
                }
            }
        });
        builder.show();
    }
    public void launchInsert (View v, String s){
        Intent i = new Intent(this, InsertActivity.class);
        i.putExtra("message" , s);
        startActivity(i);
    }
    public void launchTimer(View v, String c){
        Intent i=new Intent(this,TimerActivity.class);
        i.putExtra("nomescheda" , c);
        startActivity(i);
    }
    public void SharedPrefesSAVE(String Name){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }
    private String caricaScheda(int position){
        String c;
        c =schedaList.get(position).getNome_scheda();
        return c;
    }
    private void deleteScheda(int position){
        db.deleteScheda(schedaList.get(position));
        schedaList.remove(position);
        mAdapter.notifyItemRemoved(position);
        toggleEmptySchede();
    }
    private void toggleEmptySchede(){
        if(db.getSchedeCount()>0){
            noSchedeView.setVisibility(View.GONE);
        }
        else{
            noSchedeView.setVisibility(View.VISIBLE);
        }
    }
    public void infoToast(){
        Toast.makeText(this,"Tap per iniziare l'allenamento \nTenere premuto per cancellare",Toast.LENGTH_LONG).show();
    }
}