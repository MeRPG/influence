package com.teremok.framework.ui;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexx on 10.02.14
 */
public class RadioGroup implements UIGroup<Checkbox>  {

    int code;
    Checkbox checked;
    HashSet<Checkbox> set;

    public RadioGroup(int code) {
        this.code = code;
    }

    public RadioGroup(int code, Checkbox checked) {
        this.code = code;
        this.checked = checked;
        checked.addToGroup(this);
        checked.check();
    }

    @Override
    public void select(Checkbox element) {
        checked = element;
        if (set.contains(element)) {
            for (Checkbox cb : set) {
                if (! cb.equals(element)) {
                    cb.unCheck();
                }
            }
        }
    }

    @Override
    public void add(Checkbox element) {
        if (set == null)
            set = new HashSet<>();
        set.add(element);
    }

    @Override
    public void remove(Checkbox element) {
        if (set != null)
            set.remove(element);
    }

    @Override
    public List<Checkbox> getElements() {
        return new LinkedList<>(set);
    }

    // Auto-generated

    public int getCode() {
        return code;
    }

    public Checkbox getChecked() {
        return checked;
    }
}
