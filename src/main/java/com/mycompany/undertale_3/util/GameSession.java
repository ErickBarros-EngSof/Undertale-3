package com.mycompany.undertale_3.util;

import com.mycompany.undertale_3.model.Item;
import com.mycompany.undertale_3.model.Jogador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameSession {
    public static Jogador jogador;
    public static int hp, maxHp, atk, def, moedasRun, pontosRun, inimigosDerrotados;
    public static boolean bossDesbloqueado;
    public static ObservableList<Item> inventario = FXCollections.observableArrayList();

    public static void novaRun(Jogador j, int upHp, int upAtk, int upDef) {
        jogador = j;
        maxHp = 100 + upHp * 20;
        hp = maxHp;
        atk = 15 + upAtk * 5;
        def = 5 + upDef * 3;
        moedasRun = 0;
        pontosRun = 0;
        inimigosDerrotados = 0;
        bossDesbloqueado = false;
        inventario.clear();
        inventario.add(new Item("Poção", 2));
    }

    public static void addItem(String nome, int qtd) {
        for (Item item : inventario) {
            if (item.getNome().equalsIgnoreCase(nome)) {
                item.setQuantidade(item.getQuantidade() + qtd);
                return;
            }
        }
        inventario.add(new Item(nome, qtd));
    }
}
