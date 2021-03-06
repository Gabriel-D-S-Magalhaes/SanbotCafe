package vivacity.com.br.sanbotcafe;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SystemManager;

import java.util.ArrayList;

public class QuantidadeActivity extends TopBaseActivity implements FecharPedidoDialogFragment
        .FecharPedidoListener, MyTextToSpeech.DoneListener, MySpeechToText.ResultsListener {

    private final String TAG = QuantidadeActivity.class.getSimpleName();
    private final int REQUEST_CODE_CHECK_TTS = 1;

    private static Pedido pedido;

    private Intent confirmarPedido;

    private LinearLayout linearLayout;// Linear layout que agrupa os botões 1, 2, 3, 4 ou 5 quantidades
    private TextView textView;// Texto será modificado de "Quantidade:" para "Algo mais?"
    private Button btnNext;// Começa invisible, mas, após escolhida a qtd. fica "visible"

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
    private MyTextToSpeech myTextToSpeech;
    private MySpeechToText mySpeechToText;

    public static Pedido getPedido() {
        return pedido;
    }

    public static void fecharPedido() {
        QuantidadeActivity.pedido.fecharPedido();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        register(QuantidadeActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantidade);
        Log.i(TAG, "onCreate invoked.");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.linearLayout = findViewById(R.id.escolha_quantidade_liner_layout);
        this.textView = findViewById(R.id.tv_quantidade);
        this.btnNext = findViewById(R.id.btn_next);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked.");

        // Get the Intent that started this activity and extract the string
        // Intent intent = getIntent();

        // Pega a bebida escolhida na tela que chamou essa activity
        this.bebida = getIntent().getStringExtra(BebidasSemAlcoolActivity.EXTRA_ESCOLHIDA);

        // Inicializa a Intent para ir para a próxima activity
        this.confirmarPedido = new Intent(getApplicationContext(), ConfirmarPedidoActivity.class);

        if (this.myTextToSpeech == null) {

            Intent checkTTS = new Intent();
            checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTS, this.REQUEST_CODE_CHECK_TTS);

        } else {

            // Esse blobo será iniciado se o usuário da MainActivity3 voltar para cá com o botão
            // VOLTAR. Será que funciona com o FloatButton do Sanbot???
            this.myTextToSpeech.speak("Escolha a quantidade de itens.");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
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
        Log.e(TAG, "Method finish() will invoke.");
        QuantidadeActivity.this.finish();
        Log.e(TAG, "Method finish() was invoked.");
    }

    /**
     * O negative button do fragment foi clicado isso significa que o usuário não quer finalizar o
     * pedido
     */
    @Override
    public void onDialogNegativeClick(int dialog) {

        startActivity(new Intent(getApplicationContext(), CategoriaBebidasActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Escolha a quantidade de itens.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Escolha a quantidade de itens.");
                    }
                }

                break;
        }
    }

    @Override
    public void onDone(boolean done) {
        final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");// PT-BR
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");// English US

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                5000);

        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Should be on the main thread
                    mySpeechToText = new MySpeechToText(QuantidadeActivity.this,
                            QuantidadeActivity.this);
                    mySpeechToText.startListening(recognizerIntent);
                }
            });

        } catch (ActivityNotFoundException e) {

            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void results(ArrayList<String> resultados) {
        for (String resultado : resultados) {
            Log.i(TAG, resultado);
        }

        Log.i(TAG, "Resultado mais confiável: ".concat(resultados.get(0)));
        this.checkSpeech(resultados);
    }

    private void checkSpeech(ArrayList<String> resultados) {
        for (String resultado : resultados) {

            switch (resultado) {

                case "um":
                case "Um":
                case "Uma":
                case "uma":
                case "1":

                    QuantidadeActivity.this.quantidadeEscolhida(
                            findViewById(R.id.btn_uma_quantidade));
                    return;

                case "dois":
                case "Dois":
                case "duas":
                case "Duas":
                case "2":

                    QuantidadeActivity.this.quantidadeEscolhida(
                            findViewById(R.id.btn_duas_quantidades));
                    return;

                case "três":
                case "Três":
                case "3":

                    QuantidadeActivity.this.quantidadeEscolhida(
                            findViewById(R.id.btn_tres_quantidades));
                    return;

                case "quatro":
                case "Quatro":
                case "4":

                    QuantidadeActivity.this.quantidadeEscolhida(
                            findViewById(R.id.btn_quatro_quantidades));
                    return;

                case "cinco":
                case "Cinco":
                case "5":

                    QuantidadeActivity.this.quantidadeEscolhida(
                            findViewById(R.id.btn_cinco_quantidades));
                    return;
            }
        }

        this.myTextToSpeech.speak("Desculpa, mas não entendi. Escolha uma das opções tocando" +
                " na tela.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause invoked.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop invoked.");

        if (this.myTextToSpeech != null && this.myTextToSpeech.getTextToSpeech().isSpeaking()) {

            int status = this.myTextToSpeech.getTextToSpeech().stop();
            switch (status) {
                case TextToSpeech.SUCCESS:
                    Log.i(TAG, "Stopped successfully.");
                    break;
                case TextToSpeech.ERROR:
                    Log.e(TAG, "Stopped with failure.");
                    break;
                default:
                    Log.e(TAG, "Unknown error while stopping");
                    break;
            }
        }

        Log.e(TAG, "Method finish() will invoke.");
        finish();
        Log.e(TAG, "Method finish() was invoked.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart invoked.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy invoked.");

        if (this.mySpeechToText != null) this.mySpeechToText.destroy();
        this.myTextToSpeech.destroyTextToSpeech();
    }
}
