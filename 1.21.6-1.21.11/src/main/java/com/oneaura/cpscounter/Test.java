package com.oneaura.cpscounter;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        for (Method m : ConfigEntryBuilder.class.getMethods()) {
            if (m.getName().startsWith("start")) {
                System.out.println(m.getName());
            }
        }
    }
}
