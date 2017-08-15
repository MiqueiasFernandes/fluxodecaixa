package com.example.mfernandes.fluxodecaixa;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.util.Formatter;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onPostResume() {
        super.onPostResume();
        calcular();
    }

    void calcular() {
        try {
            FileUtil.popular(this);


            Iterator<Lancamento> it = FileUtil.getLancamentos();
            int cont = 0;
            int hoje = FileUtil.getDiaAtual();
            Float saldo = 0F;
            Float entD = 0F;
            Float entM = 0F;
            Float saiD = 0F;
            Float saiM = 0F;

            while (it.hasNext()) {
                Lancamento lancamento = it.next();
                cont++;
                if (lancamento.isEntrada()) {
                    saldo += lancamento.getValor();
                    entM += lancamento.getValor();
                }
                else {
                    saldo -= lancamento.getValor();
                    saiM += lancamento.getValor();
                }

                if (lancamento.getData()[0] == hoje) {
                    if (lancamento.isEntrada()) {
                        entD += lancamento.getValor();
                    }else {
                        saiD += lancamento.getValor();
                    }
                }
            }

//            saldo = new BigDecimal(saldo.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//            entD = new BigDecimal(entD.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//            entM = new BigDecimal(entM.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//            saiD = new BigDecimal(saiD.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//            saiM = new BigDecimal(saiM.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();





            ((TextView) findViewById(R.id.saldo)).setText("R$ " + handleFloat(saldo));
            ((TextView) findViewById(R.id.saldo)).setTextColor(saldo < 0 ? Color.parseColor("#FF0000") : Color.parseColor("#00C000"));
            ((TextView) findViewById(R.id.entradaDiaria)).setText("R$ " + handleFloat(entD));
            ((TextView) findViewById(R.id.entrada)).setText("R$ " + handleFloat(entM));
            ((TextView) findViewById(R.id.saidaDiaria)).setText("R$ " + handleFloat(saiD));
            ((TextView) findViewById(R.id.saida)).setText("R$ " + handleFloat(saiM));

            Toast.makeText(this.getApplicationContext(), "Total de " + cont+ " Lançamentos calculados!", Toast.LENGTH_LONG);


        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Falha ao computar Lançamentos! " + e, Toast.LENGTH_LONG);
        }
    }


    String handleFloat(Float f) {

//        String  ft =  Float.toString(
//                new BigDecimal(f.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());





//       String[] part = ft.split(".");
//        String res = part.length > 1 ? ("," + part[1]) : "";
//        int tam = part[0].length() - 1;
//        for (int i = tam; i >= 0; i-=3) {
//            if (i < tam)
//                res = "." + part[0].substring(i, )
//        }
return    String.format("%,.2f", f).replace(",", ";").replace(".", ",").replace(";", ".");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FileUtil.getConfiguracao(this, "referencia") == null) {
            FileUtil.setConfiguracao(this, "referencia",FileUtil.getNomeByNum(FileUtil.getMesAtual()));
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent intent = new Intent(this, Main2Activity.class);
        fab.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, 0);
            }
        })
        ;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Congfigurar o APP")
                    .setTitle("Altere o mes de referencia");
            // Add the buttons
            builder.setPositiveButton("Feito", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, Main3Activity.class));

        } else if (id == R.id.nav_gallery) {
            ///graficos
        } else if (id == R.id.nav_slideshow) {
            showDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showDialog() {
        final Activity a = this;
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.excluir);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        int[] range = null;
        try {
            range = FileUtil.getRangeId(this);
        } catch (Exception e) {
            Snackbar.make(np, "Houve um erro: " + e, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        np.setMinValue(range[0]);
        np.setMaxValue(range[1]);
        np.setValue(range[1]);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    FileUtil.removeLancamento(np.getValue(), a);
                    calcular();
                }catch (Exception e) {
                    Snackbar.make(np, "Falhou: " + e, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }


}
