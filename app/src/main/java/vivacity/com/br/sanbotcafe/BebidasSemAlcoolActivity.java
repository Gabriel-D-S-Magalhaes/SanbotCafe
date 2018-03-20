package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class BebidasSemAlcoolActivity extends AppCompatActivity {


    public static final String TAG = BebidasSemAlcoolActivity.class.getSimpleName();
    public static final String EXTRA_ESCOLHIDA = BebidasSemAlcoolActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");


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

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Chá");
                break;

            case R.id.iv_cafe:


                quantificar.putExtra(EXTRA_ESCOLHIDA, "Café");
                break;

            case R.id.iv_suco:


                quantificar.putExtra(EXTRA_ESCOLHIDA, "Suco");
                break;

            case R.id.iv_refri:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Refrigerante");
                break;

            case R.id.iv_agua:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Água");
                break;
        }

        startActivity(quantificar);
    }
}
