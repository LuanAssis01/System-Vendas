package model.entities;

import java.text.DecimalFormat;

public class ItemVenda {
    private final Integer numero;
    private final String nome;
    private final Double valor;
    private final Integer quantidade;

    public ItemVenda(Integer numero, String nome, Double valor, Integer quantidade) {
        this.numero = numero;
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public Double getValor() {
        return valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double subTotal() {
        return valor * quantidade;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("%.2f");
        return "Número do produto: " + numero + "\nNome do produto: " + nome +
               "\nValor do produto: " + df.format(valor) +
               "\nQuantidade de produto: " + quantidade +
               "\nSubtotal da compra: " + df.format(subTotal());
    }
}
