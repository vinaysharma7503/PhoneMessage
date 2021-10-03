package com.example.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText Message,Phone;
    int MY_PERMISSION_REQUEST_SEND_SMS = 1;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS DELIVERED";
    PendingIntent senPI, deleveredPI;
    BroadcastReceiver smsSentReceiever, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Message = (EditText) findViewById(R.id.Message);
        Phone = (EditText) findViewById(R.id.Phone);
        
        senPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        deleveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        smsSentReceiever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Sent!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "NULL PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this, "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;
                }
                
            }
        };
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS Not Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
                
            }
        };
        
        registerReceiver(smsSentReceiever,new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver,new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiever);

    }

    public void btn_SendSMS_OnClick(View v)
    {
        String message = Message.getText().toString();
        String phone = Phone.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SEND_SMS);
        }else {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone,null,message,senPI,deleveredPI);
        }
    }
}