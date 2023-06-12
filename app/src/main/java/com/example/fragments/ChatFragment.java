package com.example.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;


public class ChatFragment extends Fragment {
    TextView phone_num;
    TextInputLayout message;
    Button send_btn;

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
         phone_num = view.findViewById(R.id.phone_tv);
         message = view.findViewById(R.id.txt_message);
         send_btn = view.findViewById(R.id.send_sms);

         send_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (ContextCompat.checkSelfPermission(getActivity(),
                         Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                 {
                      sendMessage();
                 }
                 else
                 {
                     ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},100);
                 }
             }
         });
        return view;
    }
    //method to send message

    private void sendMessage() {
        String number = phone_num.getText().toString().trim();
        String messages = message.getEditText().getText().toString().trim();

        if (!messages.isEmpty())
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,messages,null, null);
            Toast.makeText(getActivity(),"SMS sent successfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Type your message first ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
