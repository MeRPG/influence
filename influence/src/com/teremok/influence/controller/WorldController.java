package com.teremok.influence.controller;

import com.teremok.influence.model.World;

/**
 * Created by Alexx on 11.12.13.
 */
public class WorldController {

    private World world;

    public WorldController(World world) {
        this.world = world;
    }

    private void processInput() {

    }

    public void update(float delta) {
        processInput();

        /* ... */
    }

}
