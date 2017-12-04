package com.nexgen.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AbsentScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_screen);
    }

    public void submitAbsence(View view){
        EditText reasonEdit = (EditText) findViewById(R.id.editText);
        String reason = String.valueOf(reasonEdit.getText());
        MailService m = new MailService("vicky261091@gmail.com", "***********");

        String[] toArr = {"vicky261091@gmail.com"};
        m.setTo(toArr);
        m.setFrom("vicky261091@gmail.com");
        m.setSubject("Absence-Course:Operating System");
        m.setBody("Vignesh-K00405290"+"Reason: "+reason);

        try {

            if(m.send()) {
                Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }
        Intent nextpage=new Intent(this,Logout.class);
        nextpage.putExtra("message","Your absence has been notified to your instrucetor");
        startActivity(nextpage);

    }


}
