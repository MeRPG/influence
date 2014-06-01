package com.teremok.influence.model;

/**
 * Created by Алексей on 01.05.2014
 */
public class Route {

    public int key;

    public int from;
    public int to;

    public boolean enabled;

    public Route(int from, int to) {
        this.from = from < to ? from : to;
        this.to = from < to ? to : from;
        this.key = Router.calculateKey(this.from, this.to);
        this.enabled = false;
    }

    public Route(int from, int to, boolean enabled) {
        this(from, to);
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Route{" +
                "key=" + key +
                ", from=" + from +
                ", to=" + to +
                ", enabled=" + enabled +
                '}';
    }
}
