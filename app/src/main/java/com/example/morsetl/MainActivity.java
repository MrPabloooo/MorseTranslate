package com.example.morsetl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import android.os.VibrationEffect;
import android.os.Vibrator;

import android.hardware.camera2.CameraManager;
import android.content.Context;


import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {


    private EditText texteditor;

    private Button Submitbtn;

    private int LongTime;

    private int ShortTime;

    private int DelayTimer;

    private int LongDelayTimer;

    private int CanRun;

    private CameraManager cameraManager;

    private String cameraId;

    private Button flshbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }






        LongTime = 400;
        ShortTime = 100;
        DelayTimer = 300;
        LongDelayTimer = 1250;

        texteditor = findViewById(R.id.txteditor);
        Submitbtn = findViewById(R.id.Sub);
        flshbtn = findViewById(R.id.flasher);


        CanRun = 1;




        Submitbtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {

         /// //////////////////////////////////////////////////////////////////




                if (CanRun != 1) {
                    Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    return;
                }

                CanRun = 0;


                String text = texteditor.getText().toString();
                text = text.toUpperCase();

                playText(text, 0);










            }

        });


        flshbtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {

                /// //////////////////////////////////////////////////////////////////




                if (CanRun != 1) {
                    Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    return;
                }

                CanRun = 0;


                String text = texteditor.getText().toString();
                text = text.toUpperCase();

                flashText(text, 0);










            }

        });









    }


    public void playLong() {


        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                    VibrationEffect.createOneShot(
                            LongTime,
                            VibrationEffect.DEFAULT_AMPLITUDE
                    )
            );
        }


    }

    public void playShort() {


        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                    VibrationEffect.createOneShot(
                            ShortTime,
                            VibrationEffect.DEFAULT_AMPLITUDE
                    )
            );
        }


    }

    public void flashLong() {

        turnOnFlash();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            turnOffFlash();
        }, LongTime);



    }

    public void flashShort() {

        turnOnFlash();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            turnOffFlash();
        }, ShortTime);



    }




    public void shortDelay(Runnable action) {
        new Handler(Looper.getMainLooper()).postDelayed(action, ShortTime + DelayTimer);
    }

    public void longDelay(Runnable action) {
        new Handler(Looper.getMainLooper()).postDelayed(action, LongTime + DelayTimer);
    }





    private Handler handler = new Handler(Looper.getMainLooper());


    public void playMorse(String morse, Runnable onFinished) {

        if (morse == null) {
            onFinished.run();
            return;
        }




        int delay = 0;

        for (char c : morse.toCharArray()) {

            if (c == '.') {

                handler.postDelayed(() -> {
                    playShort();
                }, delay);

                delay += ShortTime + DelayTimer;

            } else if (c == '-') {

                handler.postDelayed(() -> {
                    playLong();
                }, delay);

                delay += LongTime + DelayTimer;
            }
        }

        handler.postDelayed(onFinished, delay+LongDelayTimer);

    }










    public void flashMorse(String morse, Runnable onFinished) {

        if (morse == null) {
            onFinished.run();
            return;
        }




        int delay = 0;

        for (char c : morse.toCharArray()) {

            if (c == '.') {

                handler.postDelayed(() -> {
                    flashShort();
                }, delay);

                delay += ShortTime + DelayTimer;

            } else if (c == '-') {

                handler.postDelayed(() -> {
                    flashLong();
                }, delay);

                delay += LongTime + DelayTimer;
            }
        }

        handler.postDelayed(onFinished, delay+LongDelayTimer);

    }











    HashMap<Character, String> morse = new HashMap<>();

    {
        morse.put('A', ".-");
        morse.put('B', "-...");
        morse.put('C', "-.-.");
        morse.put('D', "-..");
        morse.put('E', ".");
        morse.put('F', "..-.");
        morse.put('G', "--.");
        morse.put('H', "....");
        morse.put('I', "..");
        morse.put('J', ".---");
        morse.put('K', "-.-");
        morse.put('L', ".-..");
        morse.put('M', "--");
        morse.put('N', "-.");
        morse.put('O', "---");
        morse.put('P', ".--.");
        morse.put('Q', "--.-");
        morse.put('R', ".-.");
        morse.put('S', "...");
        morse.put('T', "-");
        morse.put('U', "..-");
        morse.put('V', "...-");
        morse.put('W', ".--");
        morse.put('X', "-..-");
        morse.put('Y', "-.--");
        morse.put('Z', "--..");
    }


    private void playText(String text, int index) {




            if (index >= text.length()) {
                CanRun = 1;
                return;
            }

            char znak = text.charAt(index);

            playMorse(morse.get(znak), () -> {
                playText(text, index + 1);

            });
        }


    private void flashText(String text, int index) {




        if (index >= text.length()) {
            CanRun = 1;
            return;
        }

        char znak = text.charAt(index);

        flashMorse(morse.get(znak), () -> {
            flashText(text, index + 1);

        });
    }


    private void turnOnFlash() {
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlash() {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
