package com.example.maledetta_treest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM user")
    List<User> getUsers();

    @Query("SELECT * FROM user WHERE uid = :myUid")
    User findByUid(int myUid);

    @Query("SELECT * FROM user WHERE uid = :myUid AND pversion = :pversion")
    User findByUidAndPVersion(int myUid, int pversion);

    @Insert(onConflict = OnConflictStrategy.REPLACE) // farà il replace se gli user hanno uid uguale
    void insertUsers(User... users);

    @Update
    public void updateUsers(User... users); // di default fa l'update degli User con uid uguale

}
