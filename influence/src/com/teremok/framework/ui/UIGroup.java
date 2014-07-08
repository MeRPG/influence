package com.teremok.framework.ui;

import java.util.List;

/**
 * Created by Alexx on 10.02.14
 */
public interface UIGroup<E extends UIElement> {
    void add(E element);
    void remove(E element);
    void select(E element);
    List<E> getElements();
}
