package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class ConfirmarPedidoActivity extends TopBaseActivity implements NumberPicker.OnValueChangeListener {

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

            int i = 0;

            for (ItensDePedido itensDePedido : this.pedido.getItensDePedidos()) {

                TextView nomeItem = this.setUpTextViewNomeItem();
                NumberPicker qtdItem = this.setUpNumberPickerQuantidadeItens();
                TextView precoItem = this.setUpTextViewPrecoItem();

                nomeItem.setText(itensDePedido.getNome());
                qtdItem.setValue(itensDePedido.getQuantidade());
                precoItem.setText(String.valueOf(itensDePedido.getPrecoUnit() *
                        itensDePedido.getQuantidade()));

                TableRow row = new TableRow(getApplicationContext());
                row.setGravity(Gravity.CENTER);
                row.addView(nomeItem);
                qtdItem.setId(i);
                row.addView(qtdItem);
                row.addView(precoItem);

                this.itensTableLayout.addView(row);
                i++;
            }

            this.pedido.calcularTotal();
            this.tvConfirmarTotal.setText("Total: R$" + this.pedido.getPrecoFinal());
        }
    }

    private NumberPicker setUpNumberPickerQuantidadeItens() {
        NumberPicker quantidadeItens = new NumberPicker(getApplicationContext());
        quantidadeItens.setMaxValue(99);
        quantidadeItens.setMinValue(1);
        quantidadeItens.setBackgroundColor(Color.GRAY);
        quantidadeItens.setOnValueChangedListener(this);
        return quantidadeItens;
    }

    private TextView setUpTextViewPrecoItem() {
        TextView precoItens = new TextView(getApplicationContext());
        precoItens.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        precoItens.setTextColor(Color.BLACK);
        precoItens.setTextSize(27);

        return precoItens;
    }

    private TextView setUpTextViewNomeItem() {

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

    /**
     * Called upon a change of the current value.
     *
     * @param picker The NumberPicker associated with this listener.
     * @param oldVal The previous value.
     * @param newVal The new value.
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        this.pedido.getItensDePedidos().get(picker.getId()).setQuantidade(newVal);
    }
}
