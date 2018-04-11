package vivacity.com.br.sanbotcafe;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class TicketActivity extends TopBaseActivity implements
        CancelarPedidoDialogFragment.CancelarPedidoListener, MyTextToSpeech.DoneListener,
        MySpeechToText.ResultsListener {

    private final int REQUEST_CODE_CHECK_TTS = 1;
    private final String TAG = TicketActivity.class.getSimpleName();

    private final Locale PT_BR = new Locale("pt", "BR");
    private final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(PT_BR);
    private final DecimalFormat MOEDA_BR = new DecimalFormat("¤ ###,###,##0.00",
            DECIMAL_FORMAT_SYMBOLS);

    private TextView contador;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView tvTotal;
    private Pedido pedido;
    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private CountDownTimer countDownTimer;
    private MySpeechToText mySpeechToText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Log.i(TAG, "onCreate invoked.");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.contador = (TextView) findViewById(R.id.tv_contador);
        this.tableLayout = (TableLayout) findViewById(R.id.pedido_tbl);
        this.tvTotal = (TextView) findViewById(R.id.tv_total);

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
            this.myTextToSpeech.speak("Pedido finalizado, mas você ainda tem 30 segundos para " +
                    "cancelar.");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }

        this.printOrder();

        this.countDownTimer = new CountDownTimer(31000, 1000) {
            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                contador.setText(String.valueOf(millisUntilFinished / 1000));
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                QuantidadeActivity.fecharPedido();
                startActivity(new Intent(TicketActivity.this, MainActivity.class));
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
    }

    private void printOrder() {

        if (this.pedido == null) {

            this.pedido = QuantidadeActivity.getPedido();
        }

        for (int i = 1; i <= this.pedido.getItensDePedidos().size(); i++) {

            this.tableRow = new TableRow(getApplicationContext());// New TableRow
            this.tableRow.setGravity(Gravity.CENTER);// Setup gravity to center
            this.tableRow.setOrientation(LinearLayout.HORIZONTAL);// Setup orientation

            TextView nomeItem = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            nomeItem.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            nomeItem.setTextColor(Color.BLACK);
            nomeItem.setTextSize(27);
            nomeItem.setText(this.pedido.getItensDePedidos().get(i - 1).getNome());
            nomeItem.setPaddingRelative(0, 0, 45, 0);

            this.tableRow.addView(nomeItem);// Add the TextView to TableRow created

            TextView quantidadeItem = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            quantidadeItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            quantidadeItem.setTextColor(Color.BLACK);
            quantidadeItem.setTextSize(27);
            quantidadeItem.setText(String.valueOf(this.pedido.getItensDePedidos().get(i - 1).getQuantidade()));
            quantidadeItem.setPaddingRelative(0, 0, 45, 0);

            this.tableRow.addView(quantidadeItem);// Add the TextView to TableRow created

            TextView precoItens = new TextView(getApplicationContext());// Instance a new TextView
            /*Setup the TextView*/
            precoItens.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            precoItens.setTextColor(Color.BLACK);
            precoItens.setTextSize(27);
            precoItens.setText(MOEDA_BR.format(
                    this.pedido.getItensDePedidos().get(i - 1).getQuantidade()
                            * this.pedido.getItensDePedidos().get(i - 1).getPrecoUnit()));

            // Add the TextView to TableRow created
            this.tableRow.addView(precoItens);

            this.tableLayout.addView(this.tableRow, i);// Add the TableRow created to TableLayout
        }

        this.pedido.calcularTotal();
        this.tvTotal.setText(String.format(getResources().getString(R.string.tv_total),
                MOEDA_BR.format(this.pedido.getPrecoFinal())));

    }

    public void cancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_novo_pedido:

                // Constrói um DialogFragment
                DialogFragment dialogFragment = new CancelarPedidoDialogFragment();
                // Mostra o fragment
                dialogFragment.show(getFragmentManager(), "Cancelar Pedido?");
                break;
        }
    }

    @Override
    public void onDialogCancelOrder(DialogInterface dialog, int which) {
        // Cancela o pedido e vai para a MainActivity
        this.pedido.fecharPedido();
        this.countDownTimer.cancel();
        startActivity(new Intent(this, MainActivity.class));
        Log.e(TAG, "Method finish() will invoke.");
        TicketActivity.this.finish();
        Log.e(TAG, "Method finish() was invoked.");
    }

    @Override
    public void onDialogContinueOrder(DialogInterface dialog, int which) {
        // Não faz nada. Só esperar o contador terminar
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Pedido finalizado, mas você ainda tem 30 segundos para " +
                                        "cancelar.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Pedido finalizado, mas você ainda tem 30 " +
                                "segundos para cancelar.");
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
                    mySpeechToText = new MySpeechToText(TicketActivity.this,
                            TicketActivity.this);
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
        checkSpeech(resultados);
    }

    private void checkSpeech(ArrayList<String> resultados) {
        for (String resultado : resultados) {
            // do something
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

        if (this.mySpeechToText != null) this.mySpeechToText.destroy();
        this.myTextToSpeech.destroyTextToSpeech();
    }
}
