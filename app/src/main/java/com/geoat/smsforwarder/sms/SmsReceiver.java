package com.geoat.smsforwarder.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.geoat.smsforwarder.MainActivity;
import com.geoat.smsforwarder.config.Configuration;
import com.geoat.smsforwarder.mail.GMailSender;

import javax.mail.MessagingException;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                    try {
                        Configuration configuration = MainActivity.getConfiguration(context);
                        if (configuration != null) {
                            GMailSender gMailSender = new GMailSender(configuration.getSmtpGmailId(),
                                    configuration.getSmtpGmailPassword());
                            String messageDate =
                                    new Date(messages[i].getTimestampMillis()).toGMTString();
                            String senderAddress = messages[i].getDisplayOriginatingAddress();
                            gMailSender.sendMail("Message @" + messageDate + " From: " + senderAddress,
                                    messages[i].getMessageBody(),
                                    configuration.getSmtpGmailId(),
                                    configuration.getRecipientEmailId());

                            Toast.makeText(context.getApplicationContext(), "Mail with SMS send.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        System.out.println("Done");

                    } catch (MessagingException e) {
                        Log.e(TAG, "Could not send mail");
                    } catch (Exception e) {
                        Log.e(TAG, "Could not send mail");
                    }
                }
                if (messages.length > -1) {
                    Log.i(TAG, "Message received.");
                }
            }
        }
    }
}
