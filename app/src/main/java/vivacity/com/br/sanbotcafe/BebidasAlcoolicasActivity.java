package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class BebidasAlcoolicasActivity extends TopBaseActivity {

    public static final String TAG = BebidasAlcoolicasActivity.class.getSimpleName();
    public static final String EXTRA_ESCOLHIDA = BebidasAlcoolicasActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");
    private SystemManager systemManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_alcoolicas);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON
        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
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
