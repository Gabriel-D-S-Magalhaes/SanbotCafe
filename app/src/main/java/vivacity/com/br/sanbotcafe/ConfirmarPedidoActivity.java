package vivacity.com.br.sanbotcafe;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Arrays;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    private static final String TAG = ConfirmarPedidoActivity.class.getSimpleName();

    private Pedido pedido;
    private ListView itensListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
        this.itensListView = (ListView) findViewById(R.id.itens_list_view);
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
