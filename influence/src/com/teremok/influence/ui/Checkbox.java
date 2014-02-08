package com.teremok.influence.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashSet;

/**
 * Created by Alexx on 08.02.14
 */
public abstract class Checkbox extends Actor {
    String code;
    boolean checked;

    HashSet<Checkbox> group;

    public void check() {
        if (group != null) {
            for (Checkbox checkbox : group) {
                checkbox.unCheck();
            }
        }
        checked = true;
    }

    public void unCheck() {
        checked = false;
    }

    public void addToGroup(Checkbox checkbox) {
        if (group == null)
            group = new HashSet<Checkbox>();
        group.add(checkbox);
    }


    // Auto-generated

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCode() {
        return code;
    }

}
