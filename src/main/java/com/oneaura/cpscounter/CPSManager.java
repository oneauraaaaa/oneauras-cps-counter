package com.oneaura.cpscounter;

import java.util.ArrayList;
import java.util.List;

public class CPSManager {

    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();

    // Sol tıklama anını listeye ekler.
    public static void recordLeftClick() {
        leftClicks.add(System.currentTimeMillis());
    }

    // Sağ tıklama anını listeye ekler.
    public static void recordRightClick() {
        rightClicks.add(System.currentTimeMillis());
    }

    // Listeleri güncel tutar, 1 saniyeden eski tıklamaları siler.
    public static void tick() {
        long currentTime = System.currentTimeMillis();
        leftClicks.removeIf(time -> time < currentTime - 1000);
        rightClicks.removeIf(time -> time < currentTime - 1000);
    }

    // Anlık sol tık CPS değerini döndürür.
    public static int getLeftCPS() {
        return leftClicks.size();
    }

    // Anlık sağ tık CPS değerini döndürür.
    public static int getRightCPS() {
        return rightClicks.size();
    }
}

