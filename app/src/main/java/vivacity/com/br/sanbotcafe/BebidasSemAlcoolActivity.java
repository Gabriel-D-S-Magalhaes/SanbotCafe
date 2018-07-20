package vivacity.com.br.sanbotcafe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SystemManager;

import java.util.ArrayList;

public class BebidasSemAlcoolActivity extends TopBaseActivity implements MyTextToSpeech.DoneListener,
        MySpeechToText.ResultsListener {


    private final String TAG = BebidasSemAlcoolActivity.class.getSimpleName();

    public static final String EXTRA_ESCOLHIDA = BebidasSemAlcoolActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");

    private final int REQUEST_CODE_CHECK_TTS = 1;

    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private MySpeechToText mySpeechToText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        register(BebidasSemAlcoolActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_sem_alcool);
        Log.i(TAG, "onCreate invoked.");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Keep screen ON

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

        if (this.myTextToSpeech == null) {

            Intent checkTTS = new Intent();
            checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTS, this.REQUEST_CODE_CHECK_TTS);

        } else {

            // Esse blobo será iniciado se o usuário da MainActivity3 voltar para cá com o botão
            // VOLTAR. Será que funciona com o FloatButton do Sanbot???
            this.myTextToSpeech.speak("Essas são nossas opções de bebidas sem álcool.");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
    }

    /**
     * Inicia a tela para dizer a quantidade da bebidas sem álcool escolhida
     */
    public void escolherQtdBebidasSemAlcool(View view) {

        Intent quantificar = new Intent(getApplicationContext(), QuantidadeActivity.class);

        switch (view.getId()) {

            case R.id.tv_cha:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Chá");
                break;

            case R.id.tv_cafe:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Café");
                break;

            case R.id.tv_suco:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Suco");
                break;

            case R.id.tv_refri:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Refrigerante");
                break;

            case R.id.tv_agua:

                quantificar.putExtra(EXTRA_ESCOLHIDA, "Água");
                break;
        }

        startActivity(quantificar);
        Log.e(TAG, "Method finish() will invoke.");
        finish();
        Log.e(TAG, "Method finish() was invoked.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Essas são nossas opções de bebidas sem álcool.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Essas são nossas opções de bebidas sem álcool.");
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
                    mySpeechToText = new MySpeechToText(BebidasSemAlcoolActivity.this,
                            BebidasSemAlcoolActivity.this);
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

        final Intent quantificar = new Intent(BebidasSemAlcoolActivity.this.getApplicationContext(),
                QuantidadeActivity.class);

        for (String resultado : resultados) {

            switch (resultado) {

                case "chá":

                    quantificar.putExtra(EXTRA_ESCOLHIDA, "Chá");
                    this.startActivity(quantificar);
                    this.finish();
                    return;

                case "café":

                    quantificar.putExtra(EXTRA_ESCOLHIDA, "Café");
                    this.startActivity(quantificar);
                    this.finish();
                    return;

                case "sucos":

                    quantificar.putExtra(EXTRA_ESCOLHIDA, "Suco");
                    this.startActivity(quantificar);
                    this.finish();
                    return;

                case "água":

                    quantificar.putExtra(EXTRA_ESCOLHIDA, "Água");
                    this.startActivity(quantificar);
                    this.finish();
                    return;

                case "refrigerante":

                    quantificar.putExtra(EXTRA_ESCOLHIDA, "Refrigerante");
                    this.startActivity(quantificar);
                    this.finish();
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
