package com.example.morsetl;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.morsetl.Settings;
import com.example.morsetl.SettingsDao;

@Database(entities = {Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SettingsDao SettingsDao();
}
