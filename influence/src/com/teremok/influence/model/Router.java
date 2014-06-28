package com.teremok.influence.model;

import com.badlogic.gdx.Gdx;

import java.util.*;

/**
 * Created by Алексей on 01.05.2014
 */
public class Router {

    private Map<Integer, Route> routeMap;

    public Router() {
        routeMap = new HashMap<>();
    }

    public Router(List<Route> routeList) {
        this();
        for (Route route : routeList) {
            add(route);
        }
    }

    public void add(int from, int to) {
        Route route = new Route(from, to, true);
        routeMap.put(route.key, route);
    }

    public void add(Route route) {
        routeMap.put(route.key, route);
    }

    public void clear() {
        routeMap.clear();
    }

    public void removeForNumber(int number) {
        HashSet<Integer> toDelete = new HashSet<>();
        for (Route route : routeMap.values()) {
            if (route.from == number || route.to == number) {
                toDelete.add(route.key);
            }
        }

        for (Integer key : toDelete) {
            routeMap.remove(key);
        }
    }

    public Collection<Route> getAsCollection(){
        return routeMap.values();
    }

    public void enable(Integer routeKey) {
        Route route = routeMap.get(routeKey);
        route.enabled = true;
    }

    public void enable(int from, int to) {
        enable(calculateKey(from, to));
    }

    public void disable(Integer routeKey) {
        Route route = routeMap.get(routeKey);
        route.enabled = false;
    }

    public void disableForNumber(int number) {
        for (Route route : routeMap.values()) {
            if (route.from == number || route.to == number) {
                disable(route.key);
            }
        }
    }

    public void disable(int from, int to) {
        disable(calculateKey(from, to));
    }

    public void toggle(int from, int to) {
        toggle(calculateKey(from, to));
    }

    public void toggle(Integer routeKey) {
        Route route = routeMap.get(routeKey);
        route.enabled = ! route.enabled;
    }

    public void print() {
        int enableCount = 0;
        Route route;
        for (Integer routeKey : routeMap.keySet()) {
            route = routeMap.get(routeKey);
            Gdx.app.debug(getClass().getSimpleName(), "route: " + route);
            if (route.enabled)
                enableCount++;
        }
        Gdx.app.debug(getClass().getSimpleName(), "route count: " + routeMap.size() +"; enabled: " + enableCount);

    }

    public boolean routeExist(Integer routeKey) {
        Route route = routeMap.get(routeKey);
        return route != null && route.enabled;
    }

    public boolean routeExist(int from, int to) {
        Integer routeKey = calculateKey(from, to);
        return routeExist(routeKey);
    }

    public boolean routePossible(Integer routeKey) {
        Route route = routeMap.get(routeKey);
        return route != null;
    }

    public boolean routePossible(int from, int to) {
        Integer routeKey = calculateKey(from, to);
        return routePossible(routeKey);
    }

    public static int calculateKey(int from, int to) {
        int min = from < to ? from : to;
        int max = to < from ? from : to;

        return min * 1000 + max;
    }

    public static int calculateKey(Route route) {
        return calculateKey(route.from, route.to);
    }

    public static void calculateAndSetKey(Route route) {
        route.key = calculateKey(route.from, route.to);
    }
}
