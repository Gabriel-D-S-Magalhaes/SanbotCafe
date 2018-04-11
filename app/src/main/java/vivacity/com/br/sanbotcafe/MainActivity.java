package vivacity.com.br.sanbotcafe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.util.ArrayList;

public class MainActivity extends TopBaseActivity implements MyTextToSpeech.DoneListener,
        MySpeechToText.ResultsListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private final int REQUEST_CODE_CHECK_TTS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate invoked.");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, this.getClass().getName());
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
                Log.e(TAG, "Method finish() will invoke.");
                finish();
                Log.e(TAG, "Method finish() was invoked.");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Bem-Vindo");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Bem-Vindo");
                    }
                }

                break;
        }
    }

    @Override
    public void onDone(boolean done) {
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

        this.myTextToSpeech.destroyTextToSpeech();
    }
}
