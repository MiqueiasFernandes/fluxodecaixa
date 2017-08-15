package com.example.mfernandes.fluxodecaixa;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        int[] res = null;
        try {
          res =  FileUtil.popular(this);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Erro ao popular base de dados. " + e,
                    Toast.LENGTH_LONG).show();
        } finally {
            if (res != null) {
                Toast.makeText(this,
                        "Foram importados " + res[0] + " lançamentos e " + res[1] + " falharam.",
                        Toast.LENGTH_LONG).show();

                Iterator<Lancamento> it = FileUtil.getLancamentos();

                TableLayout tableLayout = (TableLayout)findViewById(R.id.tab);
                tableLayout.removeAllViews();
                addHeader(tableLayout);
                while(it.hasNext()) {
                    addRow(it.next(), tableLayout);
                }


            }else {
                Toast.makeText(this,
                        "Erro crítico, nada importado.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }




    void addRow(Lancamento lancamento, TableLayout tableLayout) {
        TableRow tableRow = new TableRow(getApplicationContext());
        int color = lancamento.naturezaSaida ? Color.parseColor("#FF0000") : Color.parseColor("#0FCF0F");
        TextView columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s", lancamento.getId()));
        columsView.setTextColor(color);
        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s","  " + lancamento.origem));
        columsView.setTextColor(color);
        tableRow.addView(columsView);

//        columsView = new TextView(getApplicationContext());
//        columsView.setText(String.format("%7s", lancamento.naturezaSaida ? "S" : "E"));
//        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s","  " + lancamento.valor));
        columsView.setTextColor(color);
        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText( String.format("%7s", "  " + lancamento.getDataString()));
        columsView.setTextColor(color);
        tableRow.addView(columsView);

        tableLayout.addView(tableRow);
    }

    void addHeader(TableLayout tableLayout) {
        TableRow tableRow = new TableRow(getApplicationContext());

        TextView columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s", "ID"));
        columsView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s", "Origem"));
        columsView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tableRow.addView(columsView);

//        columsView = new TextView(getApplicationContext());
//        columsView.setText(String.format("%7s", "Natureza"));
//        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s","Valor"));
        columsView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tableRow.addView(columsView);

        columsView = new TextView(getApplicationContext());
        columsView.setText(String.format("%7s", "Data"));
        columsView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tableRow.addView(columsView);

        tableLayout.addView(tableRow);
    }



}
