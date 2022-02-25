package com.geoat.smsforwarder.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.geoat.smsforwarder.config.Configuration;

public class ConfigurationRepository {

    public static final String SHARED_PREF_NAME = "ConfigurationPref";
    public static final String RECIPIENT_EMAIL_ID = "recipientEmailId";
    public static final String SMTP_GMAIL_ID = "smtpGmailId";
    public static final String SMTP_GMAIL_PASSWORD = "smtpGmailPassword";

    public static void saveConfiguration(Context context, Configuration configuration) {

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor edit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        edit.putString(RECIPIENT_EMAIL_ID, configuration.getRecipientEmailId());
        edit.putString(SMTP_GMAIL_ID, configuration.getSmtpGmailId());
        edit.putString(SMTP_GMAIL_PASSWORD, configuration.getRecipientEmailId());

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        edit.commit();
    }

    public static Configuration loadConfiguration(Context context) {

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Configuration configuration = new Configuration();

        configuration.setRecipientEmailId(sharedPreferences.getString(RECIPIENT_EMAIL_ID, ""));
        configuration.setSmtpGmailId(sharedPreferences.getString(SMTP_GMAIL_ID, ""));
        configuration.setSmtpGmailPassword(sharedPreferences.getString(SMTP_GMAIL_PASSWORD, ""));

        return configuration;
    }
}
