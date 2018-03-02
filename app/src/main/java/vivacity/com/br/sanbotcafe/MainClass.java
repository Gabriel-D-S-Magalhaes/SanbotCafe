package vivacity.com.br.sanbotcafe;

/**
 * Created by mac on 02/03/18.
 *
 * @author
 */

public class MainClass {

    public static void main(String[] args) {


        Bebidas agua = new Bebidas("Água", 2.00);
        Bebidas vinho = new Bebidas("Vinho", 5.00);
        Bebidas suco = new Bebidas("Suco", 3.00);

        agua.setQtd(4);
        vinho.setQtd(1);
        suco.setQtd(2);

        agua.setQtd(1);
        agua.remQtd(2);


        Pedido pedido = new Pedido();
        pedido.addItem(agua);
        pedido.addItem(vinho);
        pedido.addItem(suco);

        pedido.imprimirTicket();
        System.out.println("Total = " + pedido.calcularTotal());

    }
}
