package vivacity.com.br.sanbotcafe;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mac on 02/03/18.
 *
 * @author
 */

public class Pedido {

    private ArrayList<Bebidas> bebidas = new ArrayList<>();

    private double total;

    public void addItem(Bebidas bebidas) {
        this.bebidas.add(bebidas);
    }

    public double calcularTotal() {

        double total = 0.0;

        for (Bebidas bebida : this.bebidas) {
            total += bebida.getPrecoUnit() * bebida.getQtd();
        }

        return total;
    }

    public void imprimirTicket() {

        for (Bebidas bebida : this.bebidas) {
            System.out.println("Items: " + bebida.getNome() + " QTD: " + bebida.getQtd()
                    + " Preço unitário: " + bebida.getPrecoUnit());
        }
    }
}
