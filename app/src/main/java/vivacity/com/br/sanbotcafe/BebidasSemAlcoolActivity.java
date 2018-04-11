package vivacity.com.br.sanbotcafe;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

public class BebidasSemAlcoolActivity extends TopBaseActivity {


    private final String TAG = BebidasSemAlcoolActivity.class.getSimpleName();

    public static final String EXTRA_ESCOLHIDA = BebidasSemAlcoolActivity.class.getPackage()
            .getName().concat(".EXTRA_ESCOLHIDA");

    private final int REQUEST_CODE_CHECK_TTS = 1;

    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(BebidasSemAlcoolActivity.this,
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

        this.myTextToSpeech.destroyTextToSpeech();
    }
}
