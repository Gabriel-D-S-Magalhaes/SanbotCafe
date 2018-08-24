package vivacity.com.br.sanbotcafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mac on 11/04/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */
public class MySpeechToText implements RecognitionListener {

    private final String TAG = MySpeechToText.class.getSimpleName();

    private Activity activity;
    private Context context;
    private SpeechRecognizer speechRecognizer;
    private boolean recognitionAvaiable = false;
    private ArrayList<String> resultados;

    MySpeechToText(@NonNull Activity activity, @NonNull Context context) {

        // Checks whether a speech recognition service is available on the system.
        // If this method returns false, createSpeechRecognizer(Context) will fail.
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            this.activity = activity;
            this.context = context;
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            this.speechRecognizer.setRecognitionListener(this);
            this.recognitionAvaiable = true;
        }
    }

    private Activity getActivity() {
        return this.activity;
    }

    public Context getContext() {
        return this.context;
    }

    public boolean isRecognitionAvaiable() {
        return this.recognitionAvaiable;
    }

    public ArrayList<String> getResultados() {
        return this.resultados;
    }

    /**
     * Called when the endpointer is ready for the user to start speaking.
     *
     * @param params parameters set by the recognition service. Reserved for future use.
     */
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech triggered");
    }

    /**
     * The user has started to speak.
     */
    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech triggered");
    }

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged triggered.");
    }

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     *               single channel audio stream. The sample rate is implementation dependent.
     */
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived triggered.");
    }

    /**
     * Called after the user stops speaking.
     */
    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech triggered.");
    }

    /**
     * A network or recognition error occurred.
     *
     * @param error code is defined in {@link android.speech.SpeechRecognizer}
     */
    @Override
    public void onError(int error) {

        switch (error) {

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

                Log.e(TAG, "Network operation timed out.");
                break;

            case SpeechRecognizer.ERROR_NETWORK:

                Log.e(TAG, "Other network related errors.");
                break;

            case SpeechRecognizer.ERROR_AUDIO:

                Log.e(TAG, "Audio recording error.");
                break;

            case SpeechRecognizer.ERROR_SERVER:

                Log.e(TAG, "Server sends error status.");
                break;

            case SpeechRecognizer.ERROR_CLIENT:

                Log.e(TAG, "Other client side errors.");
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

                Log.e(TAG, "No speech input.");
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:

                Log.e(TAG, "No recognition result matched.");
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

                Log.e(TAG, "RecognitionService busy.");
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

                Log.e(TAG, "Insufficient permissions.");
                break;

            default:

                Log.e(TAG, "Unknown error.");
                break;
        }
    }

    public interface ResultsListener {
        void results(ArrayList<String> resultados);
    }

    /**
     * Called when recognition results are ready.
     *
     * @param results the recognition results. To retrieve the results in {@code
     *                ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
     *                {@link android.speech.SpeechRecognizer#RESULTS_RECOGNITION} as a parameter.
     *                A float array of confidence values might also be given in {@link
     *                android.speech.SpeechRecognizer#CONFIDENCE_SCORES}.
     */
    @Override
    public void onResults(Bundle results) {
        Log.i(TAG, "onResults triggered: ");

        final ResultsListener listener;

        this.resultados = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (this.resultados != null && !this.resultados.isEmpty()) {

            // Verify that the host activity implements the callback interface
            try {

                // Instantiate the ResultsListener so we can send events to the host
                listener = (ResultsListener) getActivity();
                listener.results(this.resultados);

            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(getActivity().toString()
                        + " must implement ResultsListener");
            }
        }
    }

    /**
     * Called when partial recognition results are available. The callback might be called at any
     * time between {@link #onBeginningOfSpeech()} and {@link #onResults(Bundle)} when partial
     * results are ready. This method may be called zero, one or multiple times for each call to
     * {@link android.speech.SpeechRecognizer#startListening(Intent)}, depending on the speech
     * recognition service implementation.  To request partial results, use {@link
     * android.speech.RecognizerIntent#EXTRA_PARTIAL_RESULTS}
     *
     * @param partialResults the returned results. To retrieve the results in
     *                       ArrayList&lt;String&gt; format use {@link
     *                       Bundle#getStringArrayList(String)} with {@link
     *                       android.speech.SpeechRecognizer#RESULTS_RECOGNITION} as a parameter
     */
    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(TAG, "onPartialResults triggered.");
    }

    /**
     * Reserved for adding future events.
     *
     * @param eventType the type of the occurred event
     * @param params    a Bundle containing the passed parameters
     */
    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onEvent triggered.");
    }

    public void startListening(@NonNull Intent recognizerIntent) {

        if (this.speechRecognizer != null) {

            try {

                this.speechRecognizer.startListening(recognizerIntent);

            } catch (Exception e) {

                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Cancels the speech recognition.
     */
    public void cancel() {

        if (this.speechRecognizer != null) {

            try {

                this.speechRecognizer.cancel();

            } catch (Exception e) {

                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Stops listening for speech.
     */
    public void stopListening() {

        if (this.speechRecognizer != null) {

            try {

                this.speechRecognizer.stopListening();

            } catch (Exception e) {

                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Destroys the {@link SpeechRecognizer} object.
     */
    public void destroy() {

        if (this.speechRecognizer != null) {

            try {

                this.speechRecognizer.destroy();

            } catch (Exception e) {

                Log.e(TAG, e.getMessage());
            }
        }
    }
}
