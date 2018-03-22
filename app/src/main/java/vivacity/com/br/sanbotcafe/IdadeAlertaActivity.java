package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class IdadeAlertaActivity extends TopBaseActivity {

    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idade_alerta);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }


    public void maiorDeIdade(View view) {

        Intent escolha = new Intent();

        switch (view.getId()) {

            case R.id.btn_maior_de_idade:

                escolha.setClass(getApplicationContext(), BebidasAlcoolicasActivity.class);
                break;

            case R.id.btn_menor_de_idade:

                escolha.setClass(getApplicationContext(), BebidasSemAlcoolActivity.class);
                break;
        }

        startActivity(escolha);
    }
}
