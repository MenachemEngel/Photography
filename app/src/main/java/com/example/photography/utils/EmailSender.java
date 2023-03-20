package com.example.photography.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {


    public static void sendMail(Activity activity, String subject, String body) {
        BackgroundMail.newBuilder(activity)
                .withUsername("photogarphy.app.progect@gmail.com")
                .withPassword("photography2020")
                .withMailto("menachemeng@edu.hac.ac.il")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(subject)
                .withBody(body)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(activity, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(activity, "ההודעה נכשלה", Toast.LENGTH_SHORT).show();
                    }
                })
                .send();
    }
}
