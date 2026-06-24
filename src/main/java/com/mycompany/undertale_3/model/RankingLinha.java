package com.mycompany.undertale_3.model;

public class RankingLinha {
    private final String nome;
    private final int pontosTotais;
    private final int runs;
    public RankingLinha(String nome, int pontosTotais, int runs) {
        this.nome = nome; this.pontosTotais = pontosTotais; this.runs = runs;
    }
    public String getNome() { return nome; }
    public int getPontosTotais() { return pontosTotais; }
    public int getRuns() { return runs; }
}
