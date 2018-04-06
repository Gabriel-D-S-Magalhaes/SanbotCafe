package vivacity.com.br.sanbotcafe;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by mac on 06/04/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */
public class MyTextToSpeech implements TextToSpeech.OnInitListener {

    private boolean initialized = false;
    private TextToSpeech textToSpeech;
    private final Locale PT_BR = new Locale("pt", "BR");

    public MyTextToSpeech(@NonNull Context context) {

        this.textToSpeech = new TextToSpeech(context, this);
    }

    public TextToSpeech getTextToSpeech() {

        return this.textToSpeech;
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            // TTS engine initialized
            this.initialized = true;
        }
    }

    public void speak(@NonNull Context context, @NonNull String text) {

        if (this.textToSpeech == null) {

            this.textToSpeech = new TextToSpeech(context, this);
        }

        if (this.initialized) {

            if (textToSpeech.isLanguageAvailable(this.PT_BR) == TextToSpeech.LANG_COUNTRY_AVAILABLE
                    && text.length() <= TextToSpeech.getMaxSpeechInputLength()) {

                this.textToSpeech.setLanguage(PT_BR);
                this.textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {

            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
