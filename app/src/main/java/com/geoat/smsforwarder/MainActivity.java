package com.geoat.smsforwarder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.geoat.smsforwarder.config.Configuration;
import com.geoat.smsforwarder.mail.GMailSender;
import com.geoat.smsforwarder.repository.ConfigurationRepository;

import javax.mail.MessagingException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "autoStartServiceChannel";
    public static final String CHANNEL_NAME = "Auto Start Service Channel";

    private static Configuration configuration;

    public static Configuration getConfiguration(Context context) {
        if (configuration == null) {
            configuration = ConfigurationRepository.loadConfiguration(context);
        }
        return configuration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configuration = ConfigurationRepository.loadConfiguration(getApplicationContext());

        EditText editRecipientEmail = findViewById(R.id.editRecipientEmail);
        EditText editSmtpGmail = findViewById(R.id.editSmtpGmail);
        EditText editSmtpGmailPassword = findViewById(R.id.editSmtpGmailPassword);

        editRecipientEmail.setText(configuration.getRecipientEmailId());
        editSmtpGmail.setText(configuration.getSmtpGmailId());
        editSmtpGmailPassword.setText(configuration.getSmtpGmailPassword());

        Button buttonTestConfiguration = findViewById(R.id.buttonTestConfiguration);

        buttonTestConfiguration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    Configuration configuration = new Configuration(
                            editRecipientEmail.getText().toString(),
                            editSmtpGmail.getText().toString(),
                            editSmtpGmailPassword.getText().toString());

                    if (!configuration.isValid()) {
                        Toast.makeText(MainActivity.this, "The configuration is invalid.",
                                Toast.LENGTH_SHORT).show();
                    }
                    GMailSender sender = new GMailSender(configuration.getSmtpGmailId(),
                            configuration.getSmtpGmailPassword());
                    sender.sendMail("Test Subject",
                            "Test Body",
                            configuration.getSmtpGmailId(),
                            configuration.getRecipientEmailId());

                    Toast.makeText(MainActivity.this, "Mail Send", Toast.LENGTH_SHORT).show();

                    System.out.println("Done");

                } catch (MessagingException e) {
                    Log.i("abcd", "Could not send mail");
                } catch (Exception e) {
                    Log.i("abcd", "Could not send mail");
                }
            }

        });

        Button buttonSaveConfiguration = (Button) findViewById(R.id.buttonSaveConfiguration);

        buttonSaveConfiguration.setOnClickListener(view -> {
            configuration = new Configuration(
                    editRecipientEmail.getText().toString(),
                    editSmtpGmail.getText().toString(),
                    editSmtpGmailPassword.getText().toString());

            if (!configuration.isValid()) {
                Toast.makeText(MainActivity.this, "The configuration is invalid.",
                        Toast.LENGTH_SHORT).show();
            }

            ConfigurationRepository.saveConfiguration(getApplicationContext(), configuration);
        });
    }

    String message = "";

    public void getAllSms(Context context) {
        message = "";
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    message += "\n" + body;
                    Date dateFormat = new Date(Long.valueOf(smsDate));
                    String type;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }


                    c.moveToNext();
                }
            }

            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }

    public class Sms {
        private String _id;
        private String _address;
        private String _msg;
        private String _readState; //"0" for have not read sms and "1" for have read sms
        private String _time;
        private String _folderName;

        public String getId() {
            return _id;
        }

        public String getAddress() {
            return _address;
        }

        public String getMsg() {
            return _msg;
        }

        public String getReadState() {
            return _readState;
        }

        public String getTime() {
            return _time;
        }

        public String getFolderName() {
            return _folderName;
        }


        public void setId(String id) {
            _id = id;
        }

        public void setAddress(String address) {
            _address = address;
        }

        public void setMsg(String msg) {
            _msg = msg;
        }

        public void setReadState(String readState) {
            _readState = readState;
        }

        public void setTime(String time) {
            _time = time;
        }

        public void setFolderName(String folderName) {
            _folderName = folderName;
        }

    }
}