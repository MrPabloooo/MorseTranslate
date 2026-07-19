package com.example.morsetl;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button backbtn;

    private EditText LongTime;

    private EditText ShortTime;

    private EditText LongDelayTimer;

    private Button savebtn;

    public Boolean saved;






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
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sett), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Settings> allsettings = settingsDao.getAll();

        ShortTime = findViewById(R.id.a);
        LongTime = findViewById(R.id.b);
        LongDelayTimer = findViewById(R.id.c);
        savebtn =findViewById(R.id.Sve);




        Settings settings = allsettings.get(0);
        LongTime.setText(String.valueOf(settings.LongDelayTimer));
        ShortTime.setText(String.valueOf(settings.ShortDelayTimer));
        LongDelayTimer.setText(String.valueOf(settings.Spacing));



        if(saved == null) {
            saved = false;
        }


        backbtn = findViewById(R.id.finish);

        backbtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {

                if(Integer.parseInt(LongTime.getText().toString()) != settings.LongDelayTimer || Integer.parseInt(ShortTime.getText().toString()) != settings.ShortDelayTimer || Integer.parseInt(LongDelayTimer.getText().toString()) != settings.Spacing) {
                    if (saved == true) {
                        finish();
                    }
                    else {


                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setMessage("Save changes?");
                        builder.setPositiveButton("Yes", (dialog, which) -> {

                            Save();
                            if(saved == true) {
                                finish();
                            }

                                }
                        );
                        builder.setNegativeButton("No", (dialog, which) -> {
                            finish();
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();





                }

                }
                else{

                    finish();
                }


            }
        });

        savebtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                    150,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                            )
                    );
                }

                Save();


            }
        });








    }

    public void Save() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        SettingsDao settingsDao = db.SettingsDao();





        if (Integer.parseInt(LongTime.getText().toString())
                <= Integer.parseInt(ShortTime.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Long signals length cannot be smaller than short", Toast.LENGTH_SHORT).show();
            saved = false;
            return;
        }

        if(Integer.parseInt(LongDelayTimer.getText().toString()) < Integer.parseInt(LongTime.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Spacing cannot be smaller than Long signals length", Toast.LENGTH_SHORT).show();
            saved = false;
            return;
        }

        if(Integer.parseInt(ShortTime.getText().toString()) < 50) {
            Toast.makeText(SettingsActivity.this, "Short signals length cannot be smaller than 50ms", Toast.LENGTH_SHORT).show();
            saved = false;
            return;
        }



        settingsDao.deleteAll();

        Settings settings = new Settings(0, Integer.parseInt(LongTime.getText().toString()), Integer.parseInt(ShortTime.getText().toString()), Integer.parseInt(LongDelayTimer.getText().toString()));
        settingsDao.insert(settings);
        saved = true;

    }

}
