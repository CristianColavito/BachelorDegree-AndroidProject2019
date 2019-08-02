package com.example.gymmyapplication.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gymmyapplication.R;
import com.example.gymmyapplication.model.DBHelper;
import com.example.gymmyapplication.model.Esercizio;
import com.example.gymmyapplication.model.Scheda;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends Activity implements OnClickListener {

    private boolean pause;
    ProgressBar mProgressBarFinta, mProgressBar1;
    private ImageButton buttonOvale;
    private ImageButton buttonStopTime;
    private TextView textViewShowTime,txtscheda,txteserc,txtrip;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;

    private DBHelper db;

    int rec,rip,contatore;
    boolean flag;
    String NomeEserc;
    List<Esercizio> ListaEsercizi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        db=new DBHelper(this);
        final Intent intent = getIntent();
        final String c = intent.getStringExtra("nomescheda");
        Scheda scheda= (Scheda) db.getScheda(c);

        buttonOvale=(ImageButton) findViewById(R.id.buttonOvale);
        buttonStopTime = (ImageButton) findViewById(R.id.buttonstop);
        txtscheda=(TextView)findViewById(R.id.textView_scheda);
        txteserc=(TextView)findViewById(R.id.textView_eserc);
        txtrip=(TextView)findViewById(R.id.textView_rip);
        textViewShowTime = (TextView) findViewById(R.id.textView_timerview_time);
        buttonStopTime.setOnClickListener(this);
        buttonOvale.setOnClickListener(this);
        mProgressBarFinta = (ProgressBar) findViewById(R.id.progressbar_finta);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBarFinta.setVisibility(View.VISIBLE);
        mProgressBar1.setVisibility(View.INVISIBLE);
        contatore=0;
        flag=true;
        pause=true;

        ListaEsercizi = scheda.getEsercizi();
        rec=ListaEsercizi.get(0).Recupero;
        rip=ListaEsercizi.get(0).Ripetizioni;
        NomeEserc=ListaEsercizi.get(0).NomeEsercizio;

        setTimer(rec);
        updateCountDownText();
        setTitle(c);
        txtscheda.setText("Scheda: " + c);
        txteserc.setText("Esercizio: "+NomeEserc);
        txtrip.setText("Ripetizioni: " + rip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonOvale:
                if(pause==true)
                {
                    mProgressBarFinta.setVisibility(View.INVISIBLE);
                    pause=false;
                    startTimer();
                    mProgressBar1.setVisibility(View.VISIBLE);
                    break;
                }
                else
                {
                    countDownTimer.cancel();
                    pause=true;
                    break;
                }
            case R.id.buttonstop:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
    private void startTimer() {

        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                totalTimeCountInMilliseconds=leftTimeInMilliseconds;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                updateCountDownText();
                textViewShowTime.setVisibility(View.VISIBLE);
                mProgressBarFinta.setVisibility(View.VISIBLE);
                mProgressBar1.setVisibility(View.GONE);
                if (controlla(rip)==false){
                    flag = false;
                    contatore++;
                }
                if(flag==false)
                {
                    if(contatore == ListaEsercizi.size()) {
                        {//DIALOG ERRORE ,
                            new AlertDialog.Builder (TimerActivity.this)
                                    .setTitle(R.string.timer_fine_title)
                                    .setMessage(R.string.timer_fine_message)
                                    .setNeutralButton (R.string.ok , new AlertDialog.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog , int which){
                                            cambiactivity();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                    else{
                        flag=true;
                        rip=ListaEsercizi.get(contatore).Ripetizioni;
                        rec=ListaEsercizi.get(contatore).Recupero;
                        txteserc.setText("Esercizio: "+ ListaEsercizi.get(contatore).NomeEsercizio);
                        txtrip.setText("Ripetizioni: "+ rip);
                        setTimer(rec);
                        pause = true;
                        updateCountDownText();
                    }
                }
                else{
                    setTimer(ListaEsercizi.get(contatore).Recupero);
                    txtrip.setText("Ripetizioni: " + rip);
                    pause = true;
                    updateCountDownText();
                }
            }
        }.start();
    }
    public boolean controlla (int  var) {
        if (var > 1)
        {
            rip--;
            return true;
        }
        return false;
    }
    private void updateCountDownText(){
        int minutes=(int)(totalTimeCountInMilliseconds/1000)/60;
        int seconds=(int)(totalTimeCountInMilliseconds/1000)%60;
        mProgressBar1.setProgress((int) (totalTimeCountInMilliseconds));
        String timeleftFormatted= String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewShowTime.setText(timeleftFormatted);
    }
    private void setTimer(int time){
        totalTimeCountInMilliseconds =  time * 1000;
        mProgressBar1.setMax( time * 1000);
    }

    private void cambiactivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}