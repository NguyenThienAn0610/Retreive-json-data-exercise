package com.iot.admin.exercise2_midterm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Executors;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public class Constants {
        public static final int NUM_BUTTONS = 1;
        public static final int NUM_TEXTS = 6;
    }

    EditText index;
    EditText[] data = new EditText[Constants.NUM_TEXTS];
    Button[] buttons = new Button[Constants.NUM_BUTTONS];
    String dataReceived;
    String ID, TEMP, HUMI, PM1_0, PM2_5, PM10, CO2, CO, HCHO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index = findViewById(R.id.index);
        data[0] = findViewById(R.id.editText0);
        data[1] = findViewById(R.id.editText1);
        data[2] = findViewById(R.id.editText2);
        data[3] = findViewById(R.id.editText3);
        data[4] = findViewById(R.id.editText4);
        data[5] = findViewById(R.id.editText5);

        buttons[0] = findViewById(R.id.send);
        buttons[0].setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Do something in response to button click

//        String[] dataFromServer = new String[Constants.NUM_TEXTS];
        try {
            switch (view.getId()) {
                case R.id.send:
                    sendDataToServer();
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void sendDataToServer(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
//        String apiURL = "https://api.thingspeak.com/update?api_key=0324U6WNIBX28W4G&field" + ID + "=" + value;
//        String apiURL = "https://api.thingspeak.com/update?api_key=0324U6WNIBX28W4G&field1=" + ID + "&field2=" + value;
        String apiURL = "https://ubc.sgp1.cdn.digitaloceanspaces.com/BK_AIR/calib_air_sensor.txt";
        Request request = builder.url(apiURL).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                dataReceived = response.body().string();
                try {
                    JSONArray jArray = new JSONArray(dataReceived);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Log.d("i", Integer.toString(i+1));
                        Log.d("index", index.getText().toString());
                        ID = jObject.getString("ID");
                        if (Integer.parseInt(ID) == Integer.parseInt(index.getText().toString())) {
                            TEMP = jObject.getString("TEMP");
                            HUMI = jObject.getString("HUMI");
                            PM1_0 = jObject.getString("PM1_0");
                            PM2_5 = jObject.getString("PM2_5");
                            PM10 = jObject.getString("PM10");
                            CO2 = jObject.getString("CO2");
                            CO = jObject.getString("CO");
                            HCHO = jObject.getString("HCHO");
                            data[0].setText(PM1_0);
                            data[1].setText(CO2);
                            data[2].setText(PM2_5);
                            data[3].setText(CO);
                            data[4].setText(PM10);
                            data[5].setText(HCHO);
                            Log.d("ID", ID);
                            Log.d("TEMP", TEMP);
                            Log.d("HUMI", HUMI);
                            Log.d("PM1_0", PM1_0);
                            Log.d("PM2_5", PM2_5);
                            Log.d("PM10", PM10);
                            Log.d("CO2", CO2);
                            Log.d("CO", CO);
                            Log.d("HCHO", CO);
                            break;
                        } else {
                            Log.d("Failed", "not equal");
                        }
                    } // End Loop
                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }

            }
        });
    }
}
