package com.example.maledetta_treest;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM user")
    List<User> getUsers();

    @Query("SELECT * FROM user WHERE uid = :myUid")
    User findByUid(int myUid);

    @Insert
    void insertUsers(User... users);

}
