package ar.com.kimboo.r2d2.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ar.com.kimboo.r2d2.db.Entity;

@DatabaseTable
public class User extends Entity{
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String PASSWORD_FIELD_NAME = "password";
    
    @DatabaseField(unique = true, canBeNull = false, columnName = EMAIL_FIELD_NAME)
    private String email;
    @DatabaseField(canBeNull = false, columnName = PASSWORD_FIELD_NAME)
    private String password;
    
    public User(){}
    
    public User(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
