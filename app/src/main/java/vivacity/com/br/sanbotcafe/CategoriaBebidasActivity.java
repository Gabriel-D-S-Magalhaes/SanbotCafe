package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class CategoriaBebidasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_bebidas);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
    }

    public void categoriaEscolhida(View view) {

        Intent escolherBebidas = new Intent();

        switch (view.getId()) {

            case R.id.iv_bebidas_com_alcool:

                //escolherBebidas.setClass(getApplicationContext(), BebidasAlcoolicasActivity.class);
                escolherBebidas.setClass(getApplicationContext(), IdadeAlertaActivity.class);
                break;

            case R.id.iv_bebidas_sem_alcool:

                escolherBebidas.setClass(getApplicationContext(), BebidasSemAlcoolActivity.class);
                break;
        }

        startActivity(escolherBebidas);
    }
}
