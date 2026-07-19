package com.example.morsetl;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.morsetl.Settings;


import java.util.List;

@Dao
public interface SettingsDao {

    @Insert
    void insert(Settings Settings);

    @Query("SELECT * FROM Settings")
    List<Settings> getAll();


    @Query("DELETE FROM Settings WHERE id = :id")
    void deleteById(int id);

    @Query("Select * FROM Settings WHERE id = :id")
    Settings getById(int id);

    @Query("Select LongDelayTimer FROM Settings WHERE id = :id")
    int getLongDelayTimer(int id);


    @Query("Delete FROM Settings")
    void deleteAll();

}
