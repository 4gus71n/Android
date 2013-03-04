package ar.com.kimboo.r2d2.db;

import com.j256.ormlite.field.DatabaseField;

public class Entity {
    @DatabaseField(generatedId=true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
