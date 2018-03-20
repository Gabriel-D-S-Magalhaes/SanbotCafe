package vivacity.com.br.sanbotcafe;

import android.support.annotation.NonNull;

/**
 * Created by mac on 02/03/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */

public class ItensDePedido {

    private final double VINHO_PRECO = 3.00;
    private final double CERVEJA_PRECO = 3.00;
    private final double CHA_PRECO = 3.00;
    private final double CAFE_PRECO = 3.00;
    private final double SUCO_PRECO = 3.00;
    private final double AGUA_PRECO = 3.00;
    private final double REFRI_PRECO = 3.00;

    private String nome;
    private double precoUnit;
    private int quantidade;

    public ItensDePedido() {
    }

    public ItensDePedido(@NonNull String nome) {
        this.nome = nome;
    }

    public ItensDePedido(@NonNull String nome, double precoUnit) {
        this.nome = nome;
        this.precoUnit = precoUnit;
    }

    public ItensDePedido(@NonNull String nome, double precoUnit, int quantidade) {
        this.nome = nome;
        this.precoUnit = precoUnit;
        this.quantidade = quantidade;
    }

    /**
     * Constrói um objeto {@link ItensDePedido} com o preço tabelado
     */
    public ItensDePedido(@NonNull String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;

        switch (this.nome) {

            case "Vinho":
                this.setPrecoUnit(7.00);
                break;

            case "Cerveja":
                this.setPrecoUnit(5.00);
                break;

            case "Chá":
                this.setPrecoUnit(3.00);
                break;

            case "Café":
                this.setPrecoUnit(3.00);
                break;

            case "Suco":
                this.setPrecoUnit(7.00);
                break;

            case "Água":
                this.setPrecoUnit(2.00);
                break;

            case "Refrigerante":
                this.setPrecoUnit(3.50);
                break;

            default:
                this.setPrecoUnit(5.00);
                break;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoUnit() {
        return precoUnit;
    }

    public void setPrecoUnit(double precoUnit) {
        this.precoUnit = precoUnit;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void adicionarQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade += quantidade;
        } else {
            this.quantidade += 0;
        }
    }

    public void removerQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade -= quantidade;
        } else {
            this.quantidade -= 0;
        }
    }
}
