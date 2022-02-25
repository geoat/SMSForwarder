package com.geoat.smsforwarder.config;

import java.util.Objects;

public class Configuration {

    private String recipientEmailId = "";
    private String smtpGmailId = "";
    private String smtpGmailPassword = "";

    public Configuration() {

    }

    public Configuration(String recipientEmailID, String smtpGmailId, String smtpGmailPassword) {
        this.recipientEmailId = recipientEmailID;
        this.smtpGmailId = smtpGmailId;
        this.smtpGmailPassword = smtpGmailPassword;
    }


    public Configuration clone() {
        return new Configuration(this.recipientEmailId,
                this.smtpGmailId,
                this.smtpGmailPassword);
    }

    public boolean isValid() {
        return recipientEmailId != null && !recipientEmailId.isEmpty()
                && smtpGmailId != null && !smtpGmailId.isEmpty()
                && smtpGmailPassword != null && !smtpGmailPassword.isEmpty();
    }

    public String getRecipientEmailId() {
        return recipientEmailId;
    }

    public String getSmtpGmailId() {
        return smtpGmailId;
    }

    public String getSmtpGmailPassword() {
        return smtpGmailPassword;
    }

    public void setRecipientEmailId(String recipientEmailId) {
        Objects.requireNonNull(recipientEmailId);
        this.recipientEmailId = recipientEmailId;
    }

    public void setSmtpGmailId(String smtpGmailId) {
        Objects.requireNonNull(smtpGmailId);
        this.smtpGmailId = smtpGmailId;
    }

    public void setSmtpGmailPassword(String smtpGmailPassword) {
        Objects.requireNonNull(smtpGmailPassword);
        this.smtpGmailPassword = smtpGmailPassword;
    }
}
