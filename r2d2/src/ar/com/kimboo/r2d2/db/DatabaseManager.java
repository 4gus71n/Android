package ar.com.kimboo.r2d2.db;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import ar.com.kimboo.r2d2.model.User;

/**
 * @author astinx
 * This singleton It's like a all-in-one DAO
 */
public class DatabaseManager {
    
    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public List<User> getAllUsers() {
        List<User> userList = null;
        try {
            userList = getHelper().getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean existUser(User currUser) {
        try {
            return getHelper().getUserDao().queryBuilder()
                    .where().eq(User.EMAIL_FIELD_NAME, currUser.getEmail())
                    .and().eq(User.PASSWORD_FIELD_NAME, currUser.getPassword()).countOf() != 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean existUserEmail(String email) {
        try {
            return getHelper().getUserDao().queryBuilder()
                    .where().eq(User.EMAIL_FIELD_NAME, email).countOf() != 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addUser(User currUser) {
        try {
            if (!existUser(currUser))
                getHelper().getUserDao().create(currUser);
        } catch (java.sql.SQLException e) {
            Log.d("Login","The user already was in the database");
            e.printStackTrace(); 
        }
        
    }
}
