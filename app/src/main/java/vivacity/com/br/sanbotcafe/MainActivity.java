package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class MainActivity extends TopBaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.systemManager.switchFloatBar(false, this.getClass().getName());
    }

    /**
     * Método que concentra todos as Views clicáveis e seus respectivos métodos invocados.
     *
     * @param view - View (i. e. TextView, Button, etc) que foi clicada
     */
    public void onClickedViews(View view) {

        // Escolha o id da View e case alguma view então faça alguma coisa
        switch (view.getId()) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
