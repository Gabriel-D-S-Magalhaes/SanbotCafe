package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuantidadeActivity extends AppCompatActivity {

    private static final String TAG = QuantidadeActivity.class.getSimpleName();

    private Intent confirmarPedido;
    private LinearLayout linearLayout;
    private TextView textView;

    private String bebida;
    private int quantidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantidade);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        linearLayout = (LinearLayout) findViewById(R.id.escolha_quantidade_liner_layout);
        textView = (TextView) findViewById(R.id.tv_quantidade);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked");

        this.confirmarPedido = new Intent(getApplicationContext(), ConfirmarPedidoActivity.class);
        this.bebida = getIntent().getStringExtra(BebidasSemAlcoolActivity.BEBIDA_ESCOLHIDA);
    }

    public void quantidadeEscolhida(View view) {

        switch (view.getId()) {

            case R.id.btn_uma_quantidade:

                this.quantidade = 1;
                break;

            case R.id.btn_duas_quantidades:

                this.quantidade = 2;
                break;

            case R.id.btn_tres_quantidades:

                this.quantidade = 3;
                break;

            case R.id.btn_quatro_quantidades:

                this.quantidade = 4;
                break;

            case R.id.btn_cinco_quantidades:

                this.quantidade = 5;
                break;
        }

        textView.setText(getString(R.string.tv_algo_mais));
        linearLayout.setVisibility(View.INVISIBLE);
    }

    public void confirmarPedido(View view) {

        switch (view.getId()) {
            case R.id.btn_next:

                ConfirmarPedidoActivity.getPedido().addItem(new Bebidas(bebida, quantidade));
                startActivity(confirmarPedido);
                break;
        }
    }
}
