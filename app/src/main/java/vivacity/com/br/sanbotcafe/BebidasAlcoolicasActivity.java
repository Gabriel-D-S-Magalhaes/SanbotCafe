package vivacity.com.br.sanbotcafe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.util.ArrayList;

public class BebidasAlcoolicasActivity extends TopBaseActivity implements MyTextToSpeech.DoneListener,
        MySpeechToText.ResultsListener {

    private final String TAG = BebidasAlcoolicasActivity.class.getSimpleName();
    public static final String EXTRA_ESCOLHIDA = BebidasAlcoolicasActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");
    private final int REQUEST_CODE_CHECK_TTS = 1;
    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private MySpeechToText mySpeechToText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas_alcoolicas);
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
            this.myTextToSpeech.speak("Essas são nossas opções de bebidas alcoólicas.");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
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
        Log.e(TAG, "Method finish() will invoke.");
        finish();
        Log.e(TAG, "Method finish() was invoked.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Essas são nossas opções de bebidas alcoólicas.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Essas são nossas opções de bebidas alcoólicas.");
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
                    mySpeechToText = new MySpeechToText(BebidasAlcoolicasActivity.this,
                            BebidasAlcoolicasActivity.this);
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
