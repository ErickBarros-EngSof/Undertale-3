package com.mycompany.undertale_3.model;

public class Jogador {
    private int id;
    private String nome;
    private int pontosTotais;
    private int moedas;
    private int runs;

    public Jogador() {}
    public Jogador(int id, String nome, int pontosTotais, int moedas, int runs) {
        this.id = id; this.nome = nome; this.pontosTotais = pontosTotais; this.moedas = moedas; this.runs = runs;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getPontosTotais() { return pontosTotais; }
    public void setPontosTotais(int pontosTotais) { this.pontosTotais = pontosTotais; }
    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }
    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; }
}
