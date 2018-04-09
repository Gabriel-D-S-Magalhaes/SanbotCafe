package vivacity.com.br.sanbotcafe;

import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by mac on 06/04/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */
public class MyTextToSpeech implements TextToSpeech.OnInitListener {

    private static final String TAG = MyTextToSpeech.class.getSimpleName();
    private final Locale PT_BR = new Locale("pt", "BR");
    private String text;
    private TextToSpeech textToSpeech;
    private Context context;

    private final Resources RES = Resources.getSystem();

    public TextToSpeech getTextToSpeech() {
        return this.textToSpeech;
    }

    public Context getContext() {
        return this.context;
    }

    public String getText() {
        return this.text;
    }

    public MyTextToSpeech(@NonNull Context context, @NonNull String text) {

        this.context = context;
        this.text = text;
        this.textToSpeech = new TextToSpeech(context, this);
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            if (this.textToSpeech.isLanguageAvailable(PT_BR) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {

                this.textToSpeech.setLanguage(this.PT_BR);

                if (!TextUtils.isEmpty(this.getText())) {

                    if (this.getText().length() <= TextToSpeech.getMaxSpeechInputLength()) {

                        Log.i(TAG, "Text length = " + this.getText().length());

                        this.textToSpeech.speak(this.getText(), TextToSpeech.QUEUE_FLUSH,
                                null);

                    } else {

                        System.out.println("Limit of length of input string passed to speak and " +
                                "synthesizeToFile = " + TextToSpeech.getMaxSpeechInputLength());

                        Toast.makeText(this.getContext(), this.RES.getString(R.string.text_limit),
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(this.getContext(), this.RES.getString(R.string.no_text),
                            Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(this.getContext(), this.RES.getString(R.string.lang_unavailable),
                        Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this.getContext(), this.RES.getString(R.string.tts_not_initialized),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public int speak(@NonNull String text) {

        int status = 0;

        try {

            status = this.textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }

        return status;
    }

    public void destroyTextToSpeech() {

        try {

            this.textToSpeech.stop();
            this.textToSpeech.shutdown();
            Log.i(TAG, "Text To Speech stop and shutdown.");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }
    }
}
