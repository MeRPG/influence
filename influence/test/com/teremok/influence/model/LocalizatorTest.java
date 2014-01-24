package com.teremok.influence.model;

import junit.framework.Assert;
import org.junit.Test;

import static com.teremok.influence.model.Localizator.*;

/**
 * Created by Alexx on 24.01.14
 */
public class LocalizatorTest {

    @Test
    public void testDefaultLanguageIsEnglish() {
        Localizator.setDefaultLanguage();
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());
    }

    @Test
    public void testLanguageIsEnglishWhenSetNull() {
        Localizator.setLanguage(null);
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());
    }

    @Test
    public void testLanguageIsEnglishWhenSetAbracadabra() {
        Localizator.setLanguage(" 4 as45d 45");
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());
    }

    @Test
    public void testSetConcreteLanguages() {

        Localizator.setEnglishLanguage();
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());

        Localizator.setRussianLanguage();
        Assert.assertEquals(LANGUAGE_RUSSIAN, Localizator.getLanguage());

    }

    @Test
    public void testGetSetLanguages() {

        Localizator.setLanguage(LANGUAGE_ENGLISH);
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());

        Localizator.setLanguage(LANGUAGE_RUSSIAN);
        Assert.assertEquals(LANGUAGE_RUSSIAN, Localizator.getLanguage());

    }

    @Test
    public void testSwitchLanguage() {
        Localizator.setDefaultLanguage();
        Localizator.switchLanguage();
        Assert.assertEquals(LANGUAGE_RUSSIAN, Localizator.getLanguage());

        Localizator.switchLanguage();
        Assert.assertEquals(LANGUAGE_ENGLISH, Localizator.getLanguage());
    }

}
