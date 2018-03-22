package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

public class TicketActivity extends AppCompatActivity {

    private TextView contador;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.contador = (TextView) findViewById(R.id.tv_contador);
        this.tableLayout = (TableLayout) findViewById(R.id.pedido_tbl);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.printOrder();

        new CountDownTimer(31000, 1000) {
            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                contador.setText(String.valueOf(millisUntilFinished / 1000));
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                QuantidadeActivity.fecharPedido();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }.start();
    }

    private void printOrder() {

        if (this.pedido == null) {

            this.pedido = QuantidadeActivity.getPedido();
        }

        for (int i = 1; i <= this.pedido.getItensDePedidos().size(); i++) {

            this.tableRow = new TableRow(getApplicationContext());
            this.tableRow.setGravity(Gravity.CENTER);

            TextView nomeItem = new TextView(getApplicationContext());
            nomeItem.setText(this.pedido.getItensDePedidos().get(i - 1).getNome());
            nomeItem.setTextColor(Color.BLACK);
            nomeItem.setTextSize(27);
            nomeItem.setPadding(0, 0, 45, 0);

            this.tableRow.addView(nomeItem);

            TextView quantidadeItem = new TextView(getApplicationContext());
            quantidadeItem.setText(String.valueOf(this.pedido.getItensDePedidos().get(i - 1).getQuantidade()));
            quantidadeItem.setTextColor(Color.BLACK);
            quantidadeItem.setTextSize(27);
            quantidadeItem.setPadding(0, 0, 45, 0);

            this.tableRow.addView(quantidadeItem);

            TextView precoItens = new TextView(getApplicationContext());
            precoItens.setText(String.valueOf(this.pedido.getItensDePedidos().get(i - 1).getQuantidade()
                    * this.pedido.getItensDePedidos().get(i - 1).getPrecoUnit()));
            precoItens.setTextColor(Color.BLACK);
            precoItens.setTextSize(27);

            this.tableRow.addView(precoItens);

            this.tableLayout.addView(this.tableRow, i);
        }
    }

    public void cancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_novo_pedido:

                Toast.makeText(getApplicationContext(), "Novo pedido", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
