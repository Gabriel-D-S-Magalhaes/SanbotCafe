package vivacity.com.br.sanbotcafe;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConfirmarPedidoActivity extends TopBaseActivity implements
        NumberPicker.OnValueChangeListener, CancelarPedidoDialogFragment.CancelarPedidoListener,
        MyTextToSpeech.DoneListener {

    private final String TAG = ConfirmarPedidoActivity.class.getSimpleName();

    private final Locale PT_BR = new Locale("pt", "BR");
    private final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(PT_BR);
    private final DecimalFormat MOEDA_BR = new DecimalFormat("¤ ###,###,##0.00",
            DECIMAL_FORMAT_SYMBOLS);

    private Pedido pedido;
    private TableLayout itensTableLayout;

    private TextView tvConfirmarTotal;
    private SystemManager systemManager;
    private MyTextToSpeech myTextToSpeech;
    private final int REQUEST_CODE_CHECK_TTS = 1;
    private MySpeechToText mySpeechToText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
        Log.i(TAG, "onCreate invoked");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// Keep screen ON

        this.itensTableLayout = (TableLayout) findViewById(R.id.itens_table_layout);
        this.tvConfirmarTotal = (TextView) findViewById(R.id.tv_confirmar_total);

        this.systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
    }

    @Override
    protected void onMainServiceConnected() {
        this.systemManager.switchFloatBar(false, getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart invoked");

        this.pedido = QuantidadeActivity.getPedido();

        if (this.pedido != null) {

            this.popularTableLayout();

            this.pedido.calcularTotal();
            this.tvConfirmarTotal.setText(String.format(getResources().getString(R.string.tv_total),
                    MOEDA_BR.format(this.pedido.getPrecoFinal())));
        }

        if (this.myTextToSpeech == null) {

            Intent checkTTS = new Intent();
            checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTS, this.REQUEST_CODE_CHECK_TTS);

        } else {

            // Esse blobo será iniciado se o usuário da MainActivity3 voltar para cá com o botão
            // VOLTAR. Será que funciona com o FloatButton do Sanbot???
            this.myTextToSpeech.speak("Confirme seu pedido.");
            //this.myTextToSpeech.getTextToSpeech().speak(getString(R.string.you_are_in_main_activity_2), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume invoked.");
    }

    private void popularTableLayout() {
        int i = 0;

        for (ItensDePedido itensDePedido : this.pedido.getItensDePedidos()) {

            TextView nomeItem = this.setUpTextViewNomeItem();
            NumberPicker qtdItem = this.setUpNumberPickerQuantidadeItens();
            TextView precoItem = this.setUpTextViewPrecoItem();

            nomeItem.setText(itensDePedido.getNome());
            qtdItem.setValue(itensDePedido.getQuantidade());
            precoItem.setText(MOEDA_BR.format(
                    itensDePedido.getPrecoUnit() * itensDePedido.getQuantidade()));

            TableRow row = new TableRow(getApplicationContext());
            row.setGravity(Gravity.CENTER);
            row.addView(nomeItem);
            qtdItem.setId(i);
            row.addView(qtdItem);
            row.addView(precoItem);

            this.itensTableLayout.addView(row);
            i++;
        }
    }

    private NumberPicker setUpNumberPickerQuantidadeItens() {
        NumberPicker quantidadeItens = new NumberPicker(getApplicationContext());
        quantidadeItens.setMaxValue(99);
        quantidadeItens.setMinValue(0);
        quantidadeItens.setBackgroundColor(Color.GRAY);
        quantidadeItens.setOnValueChangedListener(this);
        return quantidadeItens;
    }

    private TextView setUpTextViewPrecoItem() {
        TextView precoItens = new TextView(getApplicationContext());
        precoItens.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        precoItens.setTextColor(Color.BLACK);
        precoItens.setTextSize(27);

        return precoItens;
    }

    private TextView setUpTextViewNomeItem() {

        TextView nomeItem = new TextView(getApplicationContext());
        nomeItem.setTextSize(27);
        nomeItem.setTextColor(Color.BLACK);
        nomeItem.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        return nomeItem;
    }

    public void confirmCancelOrder(View view) {

        switch (view.getId()) {

            case R.id.btn_cancel:
                // Constrói um DialogFragment
                DialogFragment dialogFragment = new CancelarPedidoDialogFragment();
                dialogFragment.show(getFragmentManager(), "Cancelar Pedido?");// Mostra o fragment
                break;

            case R.id.btn_confirm:

                Intent fecharPedido = new Intent(getApplicationContext(), TicketActivity.class);
                startActivity(fecharPedido);
                break;
        }
    }

    /**
     * Called upon a change of the current value.
     *
     * @param picker The NumberPicker associated with this listener.
     * @param oldVal The previous value.
     * @param newVal The new value.
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        this.pedido.getItensDePedidos().get(picker.getId()).setQuantidade(newVal);// Muda a quantidade do item em questão
        this.pedido.calcularTotal();// Calcula o total
        this.tvConfirmarTotal.setText(String.format(getResources().getString(R.string.tv_total),
                MOEDA_BR.format(this.pedido.getPrecoFinal())));// Mostra o total atualizado

        TableRow row = (TableRow) this.itensTableLayout.getChildAt(picker.getId());// Linha do NumberPicker clicado
        TextView precoDoItem = (TextView) row.getVirtualChildAt(2);// Terceira View da linha do NumberPicker em questão

        /* Pegando o preço e quantidade do item cujo picker refere-se */
        double precoUnitItem = this.pedido.getItensDePedidos().get(picker.getId()).getPrecoUnit();
        double quantidadeItens = this.pedido.getItensDePedidos().get(picker.getId()).getQuantidade();

        precoDoItem.setText(MOEDA_BR.format(precoUnitItem * quantidadeItens));// Atualiza o TextView
    }

    @Override
    public void onDialogCancelOrder(DialogInterface dialog, int which) {
        // Cancela o pedido e vai para a MainActivity
        this.pedido.fecharPedido();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onDialogContinueOrder(DialogInterface dialog, int which) {
        // Não faz nada
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_CHECK_TTS:

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    if (this.myTextToSpeech == null) {

                        this.myTextToSpeech = new MyTextToSpeech(this, this,
                                "Confirme seu pedido.");
                    } else {

                        // TALVEZ esse trecho nunca será executado;
                        this.myTextToSpeech.speak("Confirme seu pedido.");
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
                    mySpeechToText = new MySpeechToText(ConfirmarPedidoActivity.this,
                            ConfirmarPedidoActivity.this);
                    mySpeechToText.startListening(recognizerIntent);
                }
            });

        } catch (ActivityNotFoundException e) {

            Log.e(TAG, e.getMessage());
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
