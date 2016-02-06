package com.github.noobymatze.lambdawicket.model;

import java.io.Serializable;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class ReadOnlyModelTest {

    @Test
    public void testMapNull() {
        Hero hero = null;
        String actual = ReadOnlyModel.of(hero).
            map(Hero::getName).
            getObject();

        assertThat(actual, nullValue());
    }

    @Test
    public void testMap() {
        Hero hero = new Hero("Barry Allen");
        String actual = ReadOnlyModel.of(hero).
            map(Hero::getName).
            getObject();

        assertThat(actual, is("Barry Allen"));
    }

    @Test
    public void testOrElse() {
        Hero hero = null;

        String expected = "Oliver Queen";
        String actual = ReadOnlyModel.of(hero).
            map(Hero::getName).
            orElse(expected).
            getObject();

        assertThat(actual, is(expected));
    }
    
    public static final class Hero implements Serializable {

        private final String name;

        public Hero(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
    
}
