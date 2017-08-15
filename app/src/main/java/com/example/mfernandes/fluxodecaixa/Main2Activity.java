package com.example.mfernandes.fluxodecaixa;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Calendar c = Calendar.getInstance();

        ((EditText) findViewById(R.id.data)).setText(c.get(Calendar.DAY_OF_MONTH) +"/"+(c.get(Calendar.MONTH)+1) +"/"+c.get(Calendar.YEAR));
    }

    public void salvar(View view) {
        String origem = ((EditText) findViewById(R.id.origem)).getText().toString();
        String valor = ((EditText) findViewById(R.id.valor)).getText().toString();
        String[] data = ((EditText) findViewById(R.id.data)).getText().toString().split("/");
        boolean natureza = ((Switch) findViewById(R.id.natureza)).isChecked();
        String num = FileUtil.getConfiguracao(this, "lancamento");

        int lanc = num == null ? 0 : Integer.parseInt(num);
        lanc++;
        FileUtil.setConfiguracao(this, "lancamento",String.valueOf(lanc));

        int[] dt = new int[]{Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])};


        try{
            FileUtil.writeLancamento(new Lancamento(lanc, origem, natureza, Float.valueOf(valor), dt), this);
            finish();
            Toast.makeText(this,
                    "O lançamento ID " + lanc + " foi adicionado com sucesso!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }


    }


}
