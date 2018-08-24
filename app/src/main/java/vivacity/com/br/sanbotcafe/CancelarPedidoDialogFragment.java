package vivacity.com.br.sanbotcafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by mac on 03/04/18.
 *
 * @author Gabriel Dos Santos Magalhães
 */
public class CancelarPedidoDialogFragment extends DialogFragment {

    private static final String TAG = CancelarPedidoListener.class.getSimpleName();

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface CancelarPedidoListener {
        void onDialogCancelOrder(DialogInterface dialog, int which);

        void onDialogContinueOrder(DialogInterface dialog, int which);
    }

    // Use this instance of the interface to deliver action events
    CancelarPedidoListener cancelarPedidoListener;

    // Override the Fragment.onAttach() method to instantiate the CancelarPedidoListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {

            // Instantiate the CancelarPedidoListener so we can send events to the host
            cancelarPedidoListener = (CancelarPedidoListener) context;
        } catch (ClassCastException e) {

            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " deve implementar " + TAG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(getString(R.string.cancel_pedido_dialog_fragment_title))
                .setMessage(getString(R.string.cancel_pedido_dialog_fragment_message))
                .setPositiveButton(getString(R.string.cancel_pedido_dialog_fragment_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelarPedidoListener.onDialogCancelOrder(dialog, which);
                            }
                        })
                .setNegativeButton(getString(R.string.cancel_pedido_dialog_fragment_continue),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelarPedidoListener.onDialogContinueOrder(dialog, which);
                            }
                        });

        return builder.create();// 3. Get the AlertDialog from create()
    }
}
