package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class TicketActivity extends TopBaseActivity {

    private TextView contador;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView tvTotal;
    private Pedido pedido;
    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.contador = (TextView) findViewById(R.id.tv_contador);
        this.tableLayout = (TableLayout) findViewById(R.id.pedido_tbl);
        this.tvTotal = (TextView) findViewById(R.id.tv_total);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
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

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }

    private void printOrder() {

        if (this.pedido == null) {

            this.pedido = QuantidadeActivity.getPedido();
        }

        for (int i = 1; i <= this.pedido.getItensDePedidos().size(); i++) {

            this.tableRow = new TableRow(getApplicationContext());// New TableRow
            this.tableRow.setGravity(Gravity.CENTER);// Setup gravity to center
            this.tableRow.setOrientation(LinearLayout.HORIZONTAL);// Setup orientation

            TextView nomeItem = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            nomeItem.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            nomeItem.setTextColor(Color.BLACK);
            nomeItem.setTextSize(27);
            nomeItem.setText(this.pedido.getItensDePedidos().get(i - 1).getNome());
            nomeItem.setPaddingRelative(0, 0, 45, 0);

            this.tableRow.addView(nomeItem);// Add the TextView to TableRow created

            TextView quantidadeItem = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            quantidadeItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            quantidadeItem.setTextColor(Color.BLACK);
            quantidadeItem.setTextSize(27);
            quantidadeItem.setText(String.valueOf(this.pedido.getItensDePedidos().get(i - 1).getQuantidade()));
            quantidadeItem.setPaddingRelative(0, 0, 45, 0);

            this.tableRow.addView(quantidadeItem);// Add the TextView to TableRow created

            TextView precoItens = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            precoItens.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            precoItens.setTextColor(Color.BLACK);
            precoItens.setTextSize(27);
            precoItens.setText(String.valueOf(this.pedido.getItensDePedidos().get(i - 1).getQuantidade()
                    * this.pedido.getItensDePedidos().get(i - 1).getPrecoUnit()));

            // Add the TextView to TableRow created
            this.tableRow.addView(precoItens);

            this.tableLayout.addView(this.tableRow, i);// Add the TableRow created to TableLayout
        }

        this.pedido.calcularTotal();
        this.tvTotal.setText("Total: R$" + this.pedido.getPrecoFinal());
    }

    public void cancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_novo_pedido:

                Toast.makeText(getApplicationContext(), "Novo pedido", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
