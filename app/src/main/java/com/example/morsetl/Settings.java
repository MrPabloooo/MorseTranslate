package com.example.morsetl;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Settings {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int LongDelayTimer;

    public int ShortDelayTimer;

    public int Spacing;



    public Settings(int id, int LongDelayTimer, int ShortDelayTimer, int Spacing) {
        this.id = id;
        this.LongDelayTimer = LongDelayTimer;
        this.ShortDelayTimer = ShortDelayTimer;
        this.Spacing = Spacing;

    }

}
