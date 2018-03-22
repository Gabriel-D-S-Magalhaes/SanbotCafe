package vivacity.com.br.sanbotcafe;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by mac on 02/03/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */

public class Pedido {

    private ArrayList<ItensDePedido> itensDePedidos = new ArrayList<>();

    private double precoFinal;

    public Pedido() {
        this.precoFinal = 0.0;
    }

    public ArrayList<ItensDePedido> getItensDePedidos() {
        return itensDePedidos;
    }

    public double getPrecoFinal() {
        return precoFinal;
    }

    private void setPrecoFinal(double precoFinal) {
        this.precoFinal = precoFinal;
    }

    public void addItem(@NonNull ItensDePedido itensDePedido) {

        // Percorro a minha lista de itens para ver se nela já existe o item passado como parâmetro
        for (int i = 0; i < getItensDePedidos().size(); i++) {

            // Vejo se o item "i" tem o mesmo código que o item passado como parâmetro.
            // Ex.: Uma pessoa pediu 2 águas e 1 café. Quando perguntado se deseja mais alguma coisa
            // ela responde - Sim, quero mais uma água. O item água já está listado, logo só preciso
            // adicionar a quantidade pedida ao item do mesmo tipo (i. e. código) já listado
            // anteriormente
            if (getItensDePedidos().get(i).getNome().equals(itensDePedido.getNome())) {

                // O item que será adicionado já está na lista. Pego a quantidade atual desse item
                // e incremento com a quantidade que seria marcada se o mesmo não houvesse sido
                // adicionado
                getItensDePedidos().get(i).adicionarQuantidade(itensDePedido.getQuantidade());
                return;
            }
        }

        // Aqui significa que aquele item não foi adicionado aos itens de pedido
        getItensDePedidos().add(itensDePedido);
    }

    public void calcularTotal() {

        double total = 0.0;

        for (ItensDePedido itemDePedido : this.itensDePedidos) {
            total += itemDePedido.getPrecoUnit() * itemDePedido.getQuantidade();
        }

        setPrecoFinal(total);
    }

    public void imprimirTicket() {

        for (ItensDePedido itemDePedido : this.itensDePedidos) {
            System.out.println("Items: " + itemDePedido.getNome()
                    + " | Quantidade: " + itemDePedido.getQuantidade()
                    + " | Preço unitário: R$ " + itemDePedido.getPrecoUnit());
        }
    }

    public void fecharPedido() {

        this.itensDePedidos.clear();

        this.precoFinal = 0.0;
    }
}
