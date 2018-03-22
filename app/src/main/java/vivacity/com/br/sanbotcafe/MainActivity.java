package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

    }

    /**
     * Método que concentra todos as Views clicáveis e seus respectivos métodos invocados.
     *
     * @param view - View (i. e. TextView, Button, etc) que foi clicada
     */
    public void onClickedViews(View view) {

        // Escolha o id da View e case alguma view então faça alguma coisa
        switch (view.getId()) {

            case R.id.btn_exit:
                break;

            case R.id.btn_start:

                Intent comecarPedido = new Intent(getApplicationContext(),
                        CategoriaBebidasActivity.class);
                startActivity(comecarPedido);

                break;
        }
    }

    public void exitApp(View view) {

        switch (view.getId()) {
            case R.id.btn_exit:
                // Sair do app
                //finish();
                this.finishAffinity();
                break;
        }
    }
}
