package vivacity.com.br.sanbotcafe;

import java.util.ArrayList;

/**
 * Created by mac on 02/03/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */

public class Bebidas {


    private String nome;
    private double precoUnit;
    private int qtd;

    public Bebidas() {
    }

    public Bebidas(String nome, double precoUnit) {
        this.nome = nome;
        this.precoUnit = precoUnit;
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

    public int getQtd() {
        return qtd;
    }

    /**
     * Adiciona a quantidade, informada no parâmetro, para esse produto
     *
     * @param qtd - Quantidade a ser adicionada
     */
    public void setQtd(int qtd) {
        this.qtd += qtd;
    }

    /**
     * Remove a quantidade, informada no parâmetro, para esse produto
     *
     * @param i - Quantidade a ser removida
     */
    public void remQtd(int i) {
        this.qtd -= i;
    }
}
