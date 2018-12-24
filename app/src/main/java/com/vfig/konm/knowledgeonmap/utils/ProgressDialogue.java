package com.vfig.konm.knowledgeonmap.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vfig.konm.knowledgeonmap.R;

import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;

public class ProgressDialogue {


    private static final ProgressDialogue ourInstance = new ProgressDialogue();
    private static ICancelProgress cancelProgress;
    private Dialog pd = null;


    private ProgressDialogue() {
    }

    public static void setCancelProgress(ICancelProgress cancelProgress) {
        ProgressDialogue.cancelProgress = cancelProgress;
    }

    private static ProgressDialogue getInstance() {
        return ourInstance;
    }

    /**
     * Show progress dialog.
     *
     * @param context the context
     */
    public static void showProgressDialog(Context context) {
        try {
            ProgressDialogue.getInstance().showDialog(context, "Loading" + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide progress dialog.
     */
    public static void hideProgressDialog() {
        try {
            ProgressDialogue.getInstance().hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(Context context) {
        if (pd == null)
            pd = createProgressDialog(context, "");
        if (!pd.isShowing())
            pd.show();
    }

    /**
     * Show dialog.
     *
     * @param context the context
     * @param message the message
     */
    private void showDialog(Context context, String message) {
        try {
            if (pd == null)
                pd = createProgressDialog(context, message);
            if (!pd.isShowing())
                pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dialog createProgressDialog(Context context, String message) {
        Dialog progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.customprogressdialog);
        int themeColor = R.color.colorPrimary;
        GradientDrawable drawable = createNewGradientDrawable(1, themeColor, themeColor, 8);
        LinearLayout llMainLayout = (LinearLayout) progressDialog.findViewById(R.id.llMainLayout);
        llMainLayout.setBackground(drawable);
        TextView textView = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        textView.setText(message);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // This code
            // works when the dialog is canceled.
            public void onCancel(DialogInterface dialog) {
                try {
                 //   Log.e("cancel called", "0");
                    cancelProgress.progressCanceled();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();

                }
            }
        });
        try {
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    /**
     * Hide dialog.
     */
    private void hideDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();

            }
            pd = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GradientDrawable createNewGradientDrawable(int strokeWidth, int strokeColor,
                                                       int filledColor, int radius) {
        try {
            GradientDrawable gd = new GradientDrawable(TOP_BOTTOM, new int[]{filledColor,
                    filledColor});
            gd.setStroke(strokeWidth, strokeColor);
            gd.setCornerRadius(radius);
            return gd;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
