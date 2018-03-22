package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class BebidasSemAlcoolActivity extends TopBaseActivity {


    public static final String TAG = BebidasSemAlcoolActivity.class.getSimpleName();

    public static final String EXTRA_ESCOLHIDA = BebidasSemAlcoolActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");

    private SystemManager systemManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_sem_alcool);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Keep screen ON
        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
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
