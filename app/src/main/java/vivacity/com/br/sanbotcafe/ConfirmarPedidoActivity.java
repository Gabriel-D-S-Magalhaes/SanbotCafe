package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class ConfirmarPedidoActivity extends TopBaseActivity {

    private static final String TAG = ConfirmarPedidoActivity.class.getSimpleName();

    private Pedido pedido;
    private ListView itensListView;
    private TextView tvConfirmarTotal;
    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
        this.itensListView = (ListView) findViewById(R.id.itens_list_view);
        this.tvConfirmarTotal = (TextView) findViewById(R.id.tv_confirmar_total);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked");

        this.pedido = QuantidadeActivity.getPedido();

        if (this.pedido != null) {

            String[] fromColums = {"_id", "nome", "preco"};
            int[] toViews = {0, R.id.nome_item_tv, R.id.preco_item_tv};

            MatrixCursor cursor = new MatrixCursor(fromColums);
            MatrixCursor.RowBuilder rowBuilder;

            for (ItensDePedido itensDePedido : this.pedido.getItensDePedidos()) {
                rowBuilder = cursor.newRow();
                rowBuilder.add(fromColums[1], itensDePedido.getNome());
                rowBuilder.add(fromColums[2],
                        (itensDePedido.getPrecoUnit() * itensDePedido.getQuantidade()));
            }

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.line_layout, cursor, fromColums, toViews, 0);

            this.itensListView.setAdapter(adapter);

            this.pedido.calcularTotal();
            this.tvConfirmarTotal.setText("Total: R$" + this.pedido.getPrecoFinal());
        }
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
