package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class BebidasSemAlcoolActivity extends AppCompatActivity {


    public static final String TAG = BebidasSemAlcoolActivity.class.getSimpleName();
    public static final String BEBIDA_ESCOLHIDA = "br.com.vivacity.BEBIDA_ESCOLHIDA";

    private Bebidas bebida_escolhida = new Bebidas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_sem_alcool);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Keep screen ON
    }

    /**
     * Inicia a tela para dizer a quantidade da bebidas sem álcool escolhida
     */
    public void escolherQtdBebidasSemAlcool(View view) {

        Intent quantificar = new Intent(getApplicationContext(), QuantidadeActivity.class);

        switch (view.getId()) {

            case R.id.iv_cha:
                //bebida_escolhida.setNome("Chá");
                //bebida_escolhida.setPrecoUnit(3.50);
                quantificar.putExtra(BEBIDA_ESCOLHIDA, "Chá");
                break;

            case R.id.iv_cafe:

                //bebida_escolhida.setNome("Café");
                //bebida_escolhida.setPrecoUnit(3.50);
                quantificar.putExtra(BEBIDA_ESCOLHIDA, "Café");
                break;

            case R.id.iv_suco:

                //bebida_escolhida.setNome("Suco");
                //bebida_escolhida.setPrecoUnit(3.50);
                quantificar.putExtra(BEBIDA_ESCOLHIDA, "Suco");
                break;

            case R.id.iv_refri:

                //bebida_escolhida.setNome("Refrigerante");
                //bebida_escolhida.setPrecoUnit(3.50);
                quantificar.putExtra(BEBIDA_ESCOLHIDA, "Refrigerante");
                break;

            case R.id.iv_agua:

                //bebida_escolhida.setNome("Água");
                //bebida_escolhida.setPrecoUnit(3.50);
                quantificar.putExtra(BEBIDA_ESCOLHIDA, "Água");
                break;
        }

        quantificar.putExtra(BEBIDA_ESCOLHIDA, "");
        //ConfirmarPedidoActivity.getPedido().addItem(bebida_escolhida);
        startActivity(quantificar);
    }
}
