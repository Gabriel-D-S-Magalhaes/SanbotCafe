package vivacity.com.br.sanbotcafe;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by mac on 06/04/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */
public class MyTextToSpeech extends UtteranceProgressListener implements TextToSpeech.OnInitListener {

    private final String TAG = MyTextToSpeech.class.getSimpleName();
    private final Locale PT_BR = new Locale("pt", "BR");
    private String text;
    private Activity activity;
    private TextToSpeech textToSpeech;
    private Context context;

    private final Resources RES = Resources.getSystem();

    public TextToSpeech getTextToSpeech() {
        return this.textToSpeech;
    }

    private Context getContext() {
        return this.context;
    }

    private Activity getActivity() {
        return this.activity;
    }

    public String getText() {
        return this.text;
    }

    MyTextToSpeech(@NonNull Activity activity, @NonNull Context context, @NonNull String text) {

        this.activity = activity;
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

                this.textToSpeech.setOnUtteranceProgressListener(this);

                this.textToSpeech.setLanguage(this.PT_BR);

                if (!TextUtils.isEmpty(this.getText())) {

                    if (this.getText().length() <= TextToSpeech.getMaxSpeechInputLength()) {

                        Log.i(TAG, "Text length = " + this.getText().length());

                        final HashMap<String, String> params = new HashMap<>();
                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "TTS_UTTERANCE_ID");
                        int i = this.textToSpeech.speak(this.getText(), TextToSpeech.QUEUE_FLUSH,
                                params);

                        checkSpeak(i);

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

    private void checkSpeak(int status) {
        switch (status) {
            case TextToSpeech.SUCCESS:
                Log.i(TAG, "Speaked successfully.");
                break;
            case TextToSpeech.ERROR:
                Log.i(TAG, "Speaked with failure.");
                break;
            default:
                Log.i(TAG, "Unknown error while speaking.");
                break;
        }
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

    public interface DoneListener {
        void onDone(boolean done);
    }

    /**
     * Called when an utterance "starts" as perceived by the caller. This will
     * be soon before audio is played back in the case of a {@link TextToSpeech#speak}
     * or before the first bytes of a file are written to the file system in the case
     * of {@link TextToSpeech#synthesizeToFile}.
     *
     * @param utteranceId The utterance ID of the utterance.
     */
    @Override
    public void onStart(String utteranceId) {
        Log.i(TAG, utteranceId.concat(" start!"));
    }

    /**
     * Called when an utterance has successfully completed processing.
     * All audio will have been played back by this point for audible output, and all
     * output will have been written to disk for file synthesis requests.
     * <p>
     * This request is guaranteed to be called after {@link #onStart(String)}.
     *
     * @param utteranceId The utterance ID of the utterance.
     */
    @Override
    public void onDone(String utteranceId) {
        Log.i(TAG, utteranceId.concat(" done!"));

        final DoneListener listener;

        // Verify that the host activity implements the callback interface
        try {

            // Instantiate the DoneListener so we can send events to the host
            listener = (DoneListener) getActivity();
            listener.onDone(true);

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement DoneListener");
        }
    }

    /**
     * Called when an error has occurred during processing. This can be called
     * at any point in the synthesis process. Note that there might be calls
     * to {@link #onStart(String)} for specified utteranceId but there will never
     * be a call to both {@link #onDone(String)} and {@link #onError(String)} for
     * the same utterance.
     *
     * @param utteranceId The utterance ID of the utterance.
     * @deprecated Use {@link #onError(String, int)} instead
     */
    @Override
    public void onError(String utteranceId) {
        Log.e(TAG, utteranceId.concat(" error!"));
    }
}
