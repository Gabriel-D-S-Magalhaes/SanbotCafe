package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class CategoriaBebidasActivity extends TopBaseActivity {

    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_bebidas);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }

    public void categoriaEscolhida(View view) {

        Intent escolherBebidas = new Intent();

        switch (view.getId()) {

            case R.id.iv_bebidas_com_alcool:

                escolherBebidas.setClass(getApplicationContext(), IdadeAlertaActivity.class);
                break;

            case R.id.iv_bebidas_sem_alcool:

                escolherBebidas.setClass(getApplicationContext(), BebidasSemAlcoolActivity.class);
                break;
        }

        startActivity(escolherBebidas);
    }
}
