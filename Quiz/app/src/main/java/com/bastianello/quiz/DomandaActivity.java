package com.bastianello.quiz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;


public class DomandaActivity extends AppCompatActivity{

    SQLiteDatabase DB;
    Vector<Domanda> Domande;
    int[] QuattroDomande= new int[4];

    int index_domanda = 0;

    float ris = 0;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnessioneDB con  = new ConnessioneDB(this);
        try {
            con.createDataBase();
            con.openDataBase();
            this.DB = con.getDB();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        setContentView(R.layout.activity_domande);
        // recupero il dato passato
        String datopassato = getIntent().getExtras().getString("Genere");
        make(datopassato);
    }
    private void make(String S){
        CaricaDomande(S);
        if(S.equals("S"))Toast.makeText(this,"HO CARICATO LE DOMANDE DI CULTURA",Toast.LENGTH_SHORT).show();// Debug
        else Toast.makeText(this,"HO CARICATO LE DOMANDE DI SPORT",Toast.LENGTH_SHORT).show(); // Debug
        setContentView(R.layout.activity_domande);
        int a = this.QuattroDomande[index_domanda];
        ((TextView) findViewById(R.id.textdomanda)).setText(this.Domande.get(a).getTesto());
    }

    private void CaricaDomande(String genere) {
        Vector<Domanda> Domande = new Vector<>();
        String s= new String ();
        Cursor c = this.DB.rawQuery("SELECT * FROM Domande WHERE genere = ? ", new String[]{genere});
        //c.moveToFirst();
        while(c.moveToNext()){
            s= (c.getString(c.getColumnIndex("Risposta")));
            Domande.add(
                    new Domanda(
                            ( s.equals("true"))? true: false,
                            c.getString(c.getColumnIndex("Domanda"))
                    ) //Domande
            ); // add
            //System.out.println("DOMANDE: " + Domande.get(Domande.size()-1).toString()); //DEBUG
        }//while

        this.Domande = Domande;
        selezionaDomande();
    }//CaricaDomande


    private void selezionaDomande() {
        int[] QuattroDomande = new int[4];
        for(int l=0; l<4;l++) QuattroDomande[l]=100;
        int miavar=0;
        int i=0;
        if(Domande.size() != 4) {
            Random random = new Random();
            while (i != 4) {

                miavar = random.nextInt(Domande.size());
                System.out.println("hello: "+ miavar);//DEBUG
                System.out.println(this.Domande.get(miavar).getTesto());
                if(giaTrovato(miavar)){
                    System.out.println("gia trovato ");//DEBUG
                    do{

                        miavar = random.nextInt(Domande.size());
                        System.out.println("hello2: "+ miavar);//DEBUG
                    }while(giaTrovato(miavar));
                }
                this.QuattroDomande[i] = miavar;
                i++;
            }
        }
        else {
            while(miavar!=4) {
                this.QuattroDomande[miavar]=miavar;
                miavar++;
            }
        }
    }



    private boolean giaTrovato(int r) {
        for(int i=0; i<4; i++)
        {
            if(r == this.QuattroDomande[i]) return true;
        }
        return false;
    }







    public void onClickRadio(View v){
        int a = this.QuattroDomande[index_domanda];
        if(v.getId() == R.id.id_true){

            if(this.Domande.get(a).getRisposta()) {
                this.ris += 0.25;
                System.out.println(" Punteggio assegnato per il vero ");
            }
            //imposto la risposta a true
        }
        else {
            if(!this.Domande.get(a).getRisposta()) { this.ris += 0.25; System.out.println(" Punteggio assegnato per il falso ");}
            //imposto risposta a false


        }
        passaDomandaSucc();
    }

    //creazione secondo intent
    private void passaDomandaSucc() {
        this.index_domanda++;
        if(index_domanda == 4) {
            Intent ActivityMain = new Intent(this, MainActivity.class);
            String S = new String();
            ris = ris*100;
            S=ris+"%";
            ris = 0;
            index_domanda = 0;
            ActivityMain.putExtra("punteggio", S);
            startActivity(ActivityMain);
            finish();// chiude l'activity
        }
        else{
            int a = this.QuattroDomande[index_domanda];
            ((TextView) findViewById(R.id.textdomanda)).setText(this.Domande.get(a).getTesto());
        }

    }
}
