package com.teremok.influence.net;

/**
 * Created by Алексей on 03.06.2014
 */
public class Record {

    int place;
    String id;
    String name;
    int influence;

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    @Override
    public String toString() {
        return "Record{" +
                "place=" + place +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", influence=" + influence +
                '}';
    }
}
