package com.zenlabs.sevenminuteworkout.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sbstrm.appirater.Appirater;
import com.zenlabs.sevenminuteworkout.R;

/**
 * Created by madarashunor on 10/12/15.
 */
public class UtilsRateMethods {

    static boolean showAds = false;


    public interface RateDialogManager {
        void dialogDismissed();
    }

    public static void createRateDialog(final Activity context, final RateDialogManager manager) {

        showAds = true;

        final Dialog dialog = new Dialog(context, R.style.RateMeDialog);
        dialog.setContentView(R.layout.rate_custom_dialog);

        TextView textViewRateQuestion = (TextView) dialog.findViewById(R.id.textViewRateQuestion);

        TextView textViewClose = (TextView) dialog
                .findViewById(R.id.textViewClose);
        SpannableString closeText = new SpannableString(
                context.getString(R.string.close));
        closeText.setSpan(new UnderlineSpan(), 0, closeText.length(), 0);
        textViewClose.setText(closeText);

        TextView textViewAskMeLater = (TextView) dialog
                .findViewById(R.id.textViewAskMeLater);
        SpannableString askMeLaterText = new SpannableString(
                context.getString(R.string.ask_me_later));
        askMeLaterText.setSpan(new UnderlineSpan(), 0, askMeLaterText.length(),
                0);
        textViewAskMeLater.setText(askMeLaterText);

        textViewClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Refuse
                Appirater.refuse(context);
                dialog.dismiss();
            }
        });

        textViewAskMeLater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageButton buttonLike = (ImageButton) dialog
                .findViewById(R.id.buttonLike);
        ImageButton buttonDislike = (ImageButton) dialog
                .findViewById(R.id.buttonDislike);

        buttonLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showAds = false;
                // Show positive dialog
                createRateDialogPositive(context, manager);
                dialog.dismiss();
            }
        });

        buttonDislike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showAds = false;

                // Create negative dialog
                createRateDialogNegative(context, manager);
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (showAds)
                    manager.dialogDismissed();
            }
        });

        dialog.show();
    }

    public static void createRateDialogPositive(final Context context, final RateDialogManager manager) {

        final Dialog dialog = new Dialog(context, R.style.RateMeDialog);
        dialog.setContentView(R.layout.rate_custom_dialog_page_2);

        View viewLane = (View) dialog.findViewById(R.id.lane);
        TextView textViewTitle = (TextView) dialog
                .findViewById(R.id.textViewTitle);
        TextView textViewQuestion = (TextView) dialog
                .findViewById(R.id.textViewQuestion);
        Button buttonPositive = (Button) dialog
                .findViewById(R.id.buttonPositive);
        Button buttonNegative = (Button) dialog
                .findViewById(R.id.buttonNegative);

        viewLane.setBackgroundColor(context.getResources().getColor(
                R.color.green));
        textViewTitle.setText(context.getString(R.string.thank_you));
        textViewQuestion.setText(context
                .getString(R.string.rate_positive_question));

        buttonPositive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Rate
                Appirater.marketLink = "market://details?id=%s";
                Appirater.rate(context);

                dialog.dismiss();
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                manager.dialogDismissed();
            }
        });

        dialog.show();
    }

    public static void createRateDialogNegative(final Context context, final RateDialogManager manager) {

        final Dialog dialog = new Dialog(context, R.style.RateMeDialog);
        dialog.setContentView(R.layout.rate_custom_dialog_page_2);

        View viewLane = (View) dialog.findViewById(R.id.lane);
        TextView textViewTitle = (TextView) dialog
                .findViewById(R.id.textViewTitle);
        TextView textViewQuestion = (TextView) dialog
                .findViewById(R.id.textViewQuestion);
        Button buttonPositive = (Button) dialog
                .findViewById(R.id.buttonPositive);
        Button buttonNegative = (Button) dialog
                .findViewById(R.id.buttonNegative);

        viewLane.setBackgroundColor(context.getResources()
                .getColor(R.color.red));
        textViewTitle.setText(context
                .getString(R.string.we_are_sorry_to_hear_that));
        textViewQuestion.setText(context
                .getString(R.string.rate_negative_question));

        buttonPositive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // Send feedback
                Appirater.sendFeedback(context);
                dialog.dismiss();
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                manager.dialogDismissed();
            }
        });

        dialog.show();
    }

}
