package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class BebidasAlcoolicasActivity extends AppCompatActivity {

    public static final String TAG = BebidasAlcoolicasActivity.class.getSimpleName();
    public static final String EXTRA_ESCOLHIDA = BebidasAlcoolicasActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_alcoolicas);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
    }

    /**
     * Inicia a tela para dizer a quantidade da bebidas com álcool escolhida
     */
    public void escolherQtdBebidasAlcoolicas(View view) {

        Intent quantificar = new Intent(getApplicationContext(), QuantidadeActivity.class);

        switch (view.getId()) {

            case R.id.iv_vinho:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Vinho");
                break;

            case R.id.iv_cerveja:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Cerveja");
                break;
        }

        startActivity(quantificar);
    }
}
