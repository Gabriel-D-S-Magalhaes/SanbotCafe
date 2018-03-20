package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.pedido = QuantidadeActivity.getPedido();

        if (this.pedido != null) {
            this.pedido.imprimirTicket();
        }
    }

    public void confirmCancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_cancel:

                break;

            case R.id.btn_confirm:

                Intent fecharPedido = new Intent(getApplicationContext(), TicketActivity.class);
                startActivity(fecharPedido);
                break;
        }
    }
}
