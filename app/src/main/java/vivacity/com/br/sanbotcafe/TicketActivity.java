package vivacity.com.br.sanbotcafe;

import android.app.DialogFragment;
import android.content.DialogInterface;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TicketActivity extends TopBaseActivity implements
        CancelarPedidoDialogFragment.CancelarPedidoListener {

    final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(
            new Locale("pt", "BR"));
    final DecimalFormat MOEDA_BR = new DecimalFormat("¤ ###,###,##0.00", DECIMAL_FORMAT_SYMBOLS);

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
            precoItens.setText(MOEDA_BR.format(
                    this.pedido.getItensDePedidos().get(i - 1).getQuantidade()
                            * this.pedido.getItensDePedidos().get(i - 1).getPrecoUnit()));

            // Add the TextView to TableRow created
            this.tableRow.addView(precoItens);

            this.tableLayout.addView(this.tableRow, i);// Add the TableRow created to TableLayout
        }

        this.pedido.calcularTotal();
        this.tvTotal.setText(String.format(getResources().getString(R.string.tv_total),
                MOEDA_BR.format(this.pedido.getPrecoFinal())));

    }

    public void cancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_novo_pedido:

                // Constrói um DialogFragment
                DialogFragment dialogFragment = new CancelarPedidoDialogFragment();
                dialogFragment.show(getFragmentManager(), "Cancelar Pedido?");// Mostra o fragment
                break;
        }
    }

    @Override
    public void onDialogCancelOrder(DialogInterface dialog, int which) {
        // Cancela o pedido e vai para a MainActivity
        this.pedido.fecharPedido();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onDialogContinueOrder(DialogInterface dialog, int which) {
        // Não faz nada. Só esperar o contador terminar
    }
}
