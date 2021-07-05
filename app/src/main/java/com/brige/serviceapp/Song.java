package com.brige.serviceapp;

public class Song {

   public long id;
    public String name;

    public Song() {
    }

    public long getLong() {
        return id;
    }

    public void setLong(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
