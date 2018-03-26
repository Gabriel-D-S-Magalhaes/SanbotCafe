package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class ConfirmarPedidoActivity extends TopBaseActivity {

    private static final String TAG = ConfirmarPedidoActivity.class.getSimpleName();

    private Pedido pedido;
    private TableLayout itensTableLayout;

    private TextView tvConfirmarTotal;
    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.itensTableLayout = (TableLayout) findViewById(R.id.itens_table_layout);
        this.tvConfirmarTotal = (TextView) findViewById(R.id.tv_confirmar_total);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked");

        this.pedido = QuantidadeActivity.getPedido();

        if (this.pedido != null) {

            TextView nomeItem = this.setUpTextView();
            NumberPicker qtdItem = new NumberPicker(getApplicationContext());
            qtdItem.setMaxValue(99);
            qtdItem.setMinValue(1);
            TextView precoItem = new TextView(getApplicationContext());

            for (ItensDePedido itensDePedido : this.pedido.getItensDePedidos()) {

                nomeItem.setText(itensDePedido.getNome());
                qtdItem.setValue(itensDePedido.getQuantidade());
                precoItem.setText(String.valueOf(itensDePedido.getPrecoUnit() *
                        itensDePedido.getQuantidade()));

                TableRow row = new TableRow(getApplicationContext());
                row.addView(nomeItem);
                row.addView(qtdItem);
                row.addView(precoItem);

                this.itensTableLayout.addView(row);

            }

            this.pedido.calcularTotal();
            this.tvConfirmarTotal.setText("Total: R$" + this.pedido.getPrecoFinal());
        }
    }

    private TextView setUpTextView() {

        TextView nomeItem = new TextView(getApplicationContext());
        nomeItem.setTextSize(27);
        nomeItem.setTextColor(Color.BLACK);
        nomeItem.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        return nomeItem;
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
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
