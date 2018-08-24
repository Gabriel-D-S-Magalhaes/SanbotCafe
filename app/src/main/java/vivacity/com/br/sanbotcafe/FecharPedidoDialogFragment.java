package vivacity.com.br.sanbotcafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by mac on 20/03/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */

public class FecharPedidoDialogFragment extends DialogFragment {

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface FecharPedidoListener {

        void onDialogPositiveClick(int dialog);

        void onDialogNegativeClick(int dialog);
    }

    // Use this instance of the interface to deliver action events
    FecharPedidoListener fecharPedidoListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            fecharPedidoListener = (FecharPedidoListener) context;
        } catch (ClassCastException e) {

            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " deve implementar FecharPedidoDialogFragment");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Finalizar")
                .setMessage("Deseja fechar o pedido?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    /**
                     * This method will be invoked when a button in the dialog is clicked.
                     *
                     * @param dialog the dialog that received the click
                     * @param which  the button that was clicked (ex.
                     *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fecharPedidoListener.onDialogPositiveClick(which);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    /**
                     * This method will be invoked when a button in the dialog is clicked.
                     *
                     * @param dialog the dialog that received the click
                     * @param which  the button that was clicked (ex.
                     *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fecharPedidoListener.onDialogNegativeClick(which);
                    }
                });

        return builder.create();// 3. Get the AlertDialog from create()
    }
}
