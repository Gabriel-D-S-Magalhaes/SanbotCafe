package vivacity.com.br.sanbotcafe;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class QuantidadeActivity extends TopBaseActivity implements FecharPedidoDialogFragment
        .FecharPedidoListener {

    private static final String TAG = QuantidadeActivity.class.getSimpleName();

    private static Pedido pedido;

    private Intent confirmarPedido;

    private LinearLayout linearLayout;// Linear layout que agrupa os botões 1, 2, 3, 4 ou 5 quantidade
    private TextView textView;// Texto será modificado de "Quantidade:" para "Algo mais?"
    private Button btnNext;// Começa invisible, mas após escolhida a qtd. fica visible

    private String bebida;// Armazena a bebida escolhida na tela anterior

    // Armazena a quantidade escolhida nessa tela da bebida escolhida na tela anterior
    private int quantidade;

    public static final String EXTRA_BEBIDA = QuantidadeActivity.class.getPackage().getName()
            .concat(".EXTRA_BEBIDA");

    public static final String EXTRA_QUANTIDADE = QuantidadeActivity.class.getPackage().getName()
            .concat(".EXTRA_QUANTIDADE");

    public static final String EXTRA_PEDIDO = QuantidadeActivity.class.getPackage().getName()
            .concat(".EXTRA_PEDIDO");

    private SystemManager systemManager;

    public static Pedido getPedido() {
        return pedido;
    }

    public static void fecharPedido() {
        QuantidadeActivity.pedido.fecharPedido();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantidade);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.linearLayout = (LinearLayout) findViewById(R.id.escolha_quantidade_liner_layout);
        this.textView = (TextView) findViewById(R.id.tv_quantidade);
        this.btnNext = (Button) findViewById(R.id.btn_next);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked");

        // Get the Intent that started this activity and extract the string
        // Intent intent = getIntent();

        // Pega a bebida escolhida na tela que chamou essa activity
        this.bebida = getIntent().getStringExtra(BebidasSemAlcoolActivity.EXTRA_ESCOLHIDA);

        // Inicializa a Intent para ir para a próxima activity
        this.confirmarPedido = new Intent(getApplicationContext(), ConfirmarPedidoActivity.class);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }

    /**
     * Usuário escolhe uma quantidade de 1 a 5 da bebida escolhida na activity anterior
     *
     * @param view - Botão apertado
     */
    public void quantidadeEscolhida(View view) {

        switch (view.getId()) {

            case R.id.btn_uma_quantidade:

                this.quantidade = 1;
                break;

            case R.id.btn_duas_quantidades:

                this.quantidade = 2;
                break;

            case R.id.btn_tres_quantidades:

                this.quantidade = 3;
                break;

            case R.id.btn_quatro_quantidades:

                this.quantidade = 4;
                break;

            case R.id.btn_cinco_quantidades:

                this.quantidade = 5;
                break;
        }

        this.linearLayout.setVisibility(View.INVISIBLE);// Deixa o layout dos botões invisîvel
        this.btnNext.setVisibility(View.VISIBLE);// Deixa o botão "Próximo" visível

        // Se nenhum pedido foi aberto
        if (pedido == null) {
            pedido = new Pedido();// Abre um pedido
        }

        pedido.addItem(new ItensDePedido(this.bebida, this.quantidade));// Adiciona um item

        textView.setText(getString(R.string.tv_algo_mais));// Muda o TextView para "Algo mais?"
    }

    /**
     * Exibi um {@link DialogFragment} para o usuário escolher entre finalizar ou não o pedido
     */
    public void confirmarPedido(View view) {

        switch (view.getId()) {
            case R.id.btn_next:

                // Constrói um DialogFragment
                DialogFragment fecharPedido = new FecharPedidoDialogFragment();
                fecharPedido.show(getFragmentManager(), "Fechar pedido?");// Mostra o fragment
                break;
        }
    }

    /**
     * O positive button do fragment foi clicado isso significa que o usuário quer finalizar o
     * pedido
     */
    @Override
    public void onDialogPositiveClick(int dialog) {

        startActivity(confirmarPedido);// Inicia a próxima activity
    }

    /**
     * O negative button do fragment foi clicado isso significa que o usuário não quer finalizar o
     * pedido
     */
    @Override
    public void onDialogNegativeClick(int dialog) {

        startActivity(new Intent(getApplicationContext(), CategoriaBebidasActivity.class));
    }
}
