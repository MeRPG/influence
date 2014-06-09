package com.teremok.influence.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Alexx on 08.02.14
 */
@SuppressWarnings("unchecked")
public abstract class Checkbox extends Actor implements UIElement {
    String code;
    boolean checked;

    UIGroup group;

    public void check() {
        if (group != null) {
            group.select(this);
        }
        checked = true;
    }

    public void unCheck() {
        checked = false;
    }

    public void addToGroup(UIGroup uiGroup) {
        if (group != null)
            group.remove(this);
        group = uiGroup;
        group.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Checkbox)) return false;

        Checkbox checkbox = (Checkbox) o;

        if (!code.equals(checkbox.code)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
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

    public UIGroup getGroup() {
        return group;
    }
}
