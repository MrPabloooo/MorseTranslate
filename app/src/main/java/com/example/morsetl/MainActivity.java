package com.example.morsetl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private EditText texteditor;

    private Button Cancelbtn;

    private Button Submitbtn;

    private int LongTime;

    private int ShortTime;

    private int DelayTimer;

    private int LongDelayTimer;

    private int CanRun;

    private CameraManager cameraManager;

    private String cameraId;

    private Button flshbtn;

    private TextView output;


    private Boolean Canceled;

    private Button copybtn;

    private Button settingsbtn;
    public String Translation;

    public TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        SettingsDao settingsDao = db.SettingsDao();



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




        List<Settings> allsettings = settingsDao.getAll();



       if(allsettings.size() == 0) {
           Settings settings = new Settings(0, LongTime, ShortTime, LongDelayTimer);
           settingsDao.insert(settings);


       }



        texteditor = findViewById(R.id.txteditor);
        Submitbtn = findViewById(R.id.Sub);
        flshbtn = findViewById(R.id.flasher);
        Cancelbtn = findViewById(R.id.Cncl);
        output = findViewById(R.id.outp);
        copybtn = findViewById(R.id.cpy);
        settingsbtn = findViewById(R.id.finish);





        CanRun = 1;
        Submitbtn.setClickable(true);
        flshbtn.setClickable(true);




        Submitbtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {

         /// //////////////////////////////////////////////////////////////////




                if (CanRun != 1) {
                    Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    return;
                }

                CanRun = 0;
                Submitbtn.setClickable(false);
                flshbtn.setClickable(false);


                String text = texteditor.getText().toString();
                text = text.toUpperCase();

                Canceled = false;

                Translation = "";
                output.setText("Output: ");

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
                flshbtn.setClickable(false);
                Submitbtn.setClickable(false);


                String text = texteditor.getText().toString();
                text = text.toUpperCase();

                Canceled = false;

                Translation = "";
                output.setText("Output: ");

                flashText(text, 0);










            }

        });


        Cancelbtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {


                Canceled = true;

                handler.removeCallbacksAndMessages(null);
                turnOffFlash();
                Translation = "";
                output.setText("Output: ");

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.cancel();

                CanRun = 1;
                Submitbtn.setClickable(true);
                flshbtn.setClickable(true);

                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();





            }
        });



        copybtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(Translation != null) {


                    if(CanRun == 1) {


                        CanRun = 0;
                        Submitbtn.setClickable(false);
                        flshbtn.setClickable(false);

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Coped Output", Translation.toString());
                        clipboard.setPrimaryClip(clip);

                        playShort();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            CanRun = 1;
                            Submitbtn.setClickable(true);
                            flshbtn.setClickable(true);
                        }, ShortTime);




                    }

                    else {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if(CanRun == 0) {
                                Toast.makeText(MainActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                            }
                        }, ShortTime);

                    }

                }




            }
        });


        settingsbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (CanRun != 1) {
                    Toast.makeText(MainActivity.this, "Cancel first", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);

                }
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

        if (Canceled) {
            CanRun = 1;
            Submitbtn.setClickable(true);
            flshbtn.setClickable(true);
            return;
        }


        if (morse == null) {
            onFinished.run();
            return;
        }




        int delay = 0;

        if (Translation == null) {
            Translation = "   ";
        }
        else {
            Translation = Translation + "   ";
        }

            output.setText("Output: " + Translation);



        for (char c : morse.toCharArray()) {

            if (c == '.') {

                handler.postDelayed(() -> {
                    playShort();
                    Translation = Translation + ".";
                    output.setText("Output: " + Translation);

                }, delay);

                delay += ShortTime + DelayTimer;

            } else if (c == '-') {

                handler.postDelayed(() -> {
                    playLong();
                    Translation = Translation + "-";
                    output.setText("Output: " + Translation);

                }, delay);

                delay += LongTime + DelayTimer;
            }
        }

        handler.postDelayed(onFinished, delay+LongDelayTimer);

    }










    public void flashMorse(String morse, Runnable onFinished) {

        if (Canceled) {
            CanRun = 1;
            Submitbtn.setClickable(true);
            flshbtn.setClickable(true);
            return;
        }



        if (morse == null) {
            onFinished.run();
            return;
        }




        int delay = 0;


            Translation = Translation + "   ";
            output.setText("Output: " + Translation);


        for (char c : morse.toCharArray()) {

            if (c == '.') {

                handler.postDelayed(() -> {
                    flashShort();
                    Translation = Translation + ".";
                    output.setText("Output: " + Translation);

                }, delay);

                delay += ShortTime + DelayTimer;

            } else if (c == '-') {

                handler.postDelayed(() -> {
                    flashLong();
                    Translation = Translation + "-";
                    output.setText("Output: " + Translation);

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
                Submitbtn.setClickable(true);
                flshbtn.setClickable(true);
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
            Submitbtn.setClickable(true);
            flshbtn.setClickable(true);
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







    @Override
    protected void onResume() {
        super.onResume();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        SettingsDao settingsDao = db.SettingsDao();


        List<Settings> allsettings = settingsDao.getAll();

        try{
            allsettings.get(0);
            LongTime = allsettings.get(0).LongDelayTimer;
            ShortTime = allsettings.get(0).ShortDelayTimer;
            LongDelayTimer = allsettings.get(0).Spacing;
        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

}
