package com.android.nitelights.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

/**
 * A set of GUI helper method.
 * @author Андрей
 *
 */
abstract public class GUIHelper {
	
	public interface DialogResult {
		void result(boolean res, String text);
	}
	
	public static void showError(Context context, String msg) {
		showMessage(context, msg, "Error", null);
	}
	
	public static void showMessage(Context context, String msg, String title) {
		showMessage(context, msg, title, null);
	}
	
	/**
	 * Show message.
	 * @param context context
	 * @param msg message
	 * @param title message title
	 * @param listener listener to get result
	 */
	public static void showMessage(Context context, String msg, String title, final DialogResult listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (listener != null)
					listener.result(true, null);
			}
		});
		
		AlertDialog dlg = builder.create();
		dlg.show();
	}
	
	public static void showYesNo(Context context, String msg, final DialogResult listener) {
		DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
			@Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	listener.result(true, null);
		        	break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	listener.result(false, null);
		            break;
		        }
		    }
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg).setPositiveButton("Yes", clickListener)
		    .setNegativeButton("No", clickListener).show();
	}
	
	/**
	 * Shows dialog asking user to enter text.
	 * @param context context
	 * @param msg message
	 * @param listener dialog result listener
	 */
	public static void showEnterText(Context context, String msg, final DialogResult listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final EditText edit = new EditText(context);
		
		DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
			@Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	listener.result(true, edit.getText().toString());
		        	break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	listener.result(false, edit.getText().toString());
		            break;
		        }
		    }
		};
		    
		builder.setView(edit);
		builder.setMessage(msg)
			.setPositiveButton("Yes", clickListener)
	    	.setNegativeButton("No", clickListener)
	    	.show();		
	}
}
