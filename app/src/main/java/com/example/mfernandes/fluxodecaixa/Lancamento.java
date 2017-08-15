package com.example.mfernandes.fluxodecaixa;

/**
 * Created by mfernandes on 11/08/17.
 */

public class Lancamento {

    int id;
    String origem;
    boolean naturezaSaida;
    float valor;
    int[] data;

    public Lancamento(int id, String origem, boolean naturezaSaida, float valor, int[] data) {
        this.id = id;
        this.origem = origem;
        this.naturezaSaida = naturezaSaida;
        this.valor = valor;
        this.data = data;
    }

    public Lancamento(String row) {
        String[] spl = row.split(";");
        String[] data = spl[4].split("/");
        int[] dt = new int[]{Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])};
        this.id =  Integer.valueOf(spl[0]);
        this.origem =        spl[1];
        this.naturezaSaida = ("S".equalsIgnoreCase(spl[2]));
        this.valor =  Float.valueOf(spl[3] + "F");
        this.data =  dt;
    }

    public int getId() {
        return id;
    }

    public String getOrigem() {
        return origem;
    }

    public boolean isNaturezaSaida() {
        return naturezaSaida;
    }

    public boolean isSaida() {
        return naturezaSaida;
    }

    public boolean isEntrada() {
        return !naturezaSaida;
    }

    public float getValor() {
        return valor;
    }

    public int[] getData() {
        return data;
    }

    public String getDataString() {
        return (data[0]  < 10 ? "0" + data[0] : data[0]) + "/" + (data[1]  < 10 ? "0" + data[1] : data[1])  + "/" + data[2];
    }


    @Override
    public String toString() {
        return id + ";" + origem + ";" + (naturezaSaida ? "S" : "E") + ";" + valor + ";"
                + getDataString();
    }
}
