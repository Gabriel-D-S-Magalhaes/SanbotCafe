package vivacity.com.br.sanbotcafe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SystemManager;

import java.util.ArrayList;

public class CategoriaBebidasActivity extends TopBaseActivity implements MyTextToSpeech.DoneListener,
        MySpeechToText.ResultsListener {

    private final String TAG = CategoriaBebidasActivity.class.getSimpleName();
    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private final int REQUEST_CODE_CHECK_TTS = 1;
    private MySpeechToText mySpeechToText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        register(CategoriaBebidasActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_bebidas);
        Log.i(TAG, "onCreate invoked.");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

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
            this.myTextToSpeech.speak("Bem-Vindo");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
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
                                "Escolha entre bebidas alcoólicas e sem álcool.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Escolha entre bebidas alcoólicas e sem álcool.");
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

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                5000);

        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Should be on the main thread
                    mySpeechToText = new MySpeechToText(CategoriaBebidasActivity.this,
                            CategoriaBebidasActivity.this);
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

    /**
     * Verify if the speech spoke is acceptable
     */
    private void checkSpeech(ArrayList<String> resultados) {

        for (String resultado : resultados) {

            switch (resultado) {

                case "bebidas sem álcool":
                case "sem álcool":

                    this.startActivity(new Intent(CategoriaBebidasActivity.this.getApplicationContext(),
                            BebidasSemAlcoolActivity.class));
                    this.finish();
                    return;

                case "bebidas alcoólicas":
                case "bebidas com álcool":
                case "alcoólicas":
                case "com álcool":
                    this.startActivity(new Intent(CategoriaBebidasActivity.this.getApplicationContext(),
                            IdadeAlertaActivity.class));
                    this.finish();
                    return;
            }
        }

        this.myTextToSpeech.speak("Desculpa, mas não entendi. Toque na tela para escolher qual " +
                "o tipo de bebida que deseja.");
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
