package com.oneaura.cpscounter;

import java.util.ArrayList;
import java.util.List;

public class CPSManager {

    // Her tıklamanın milisaniye cinsinden zamanını saklayacağımız liste.
    private static final List<Long> clicks = new ArrayList<>();

    /**
     * Her tıklandığında bu metot çağrılır ve o anın zamanını listeye ekler.
     */
    public static void recordClick() {
        clicks.add(System.currentTimeMillis());
    }

    /**
     * Bu metot oyunun her anında (tick) çalıştırılır.
     * Listeyi kontrol eder ve 1000 milisaniyeden (1 saniye) eski olan tıklamaları temizler.
     */
    public static void tick() {
        long currentTime = System.currentTimeMillis();
        clicks.removeIf(time -> time < currentTime - 1000);
    }

    /**
     * O an listede kaç tane tıklama kaydı varsa onun sayısını döndürür.
     * Bu bizim anlık CPS değerimizdir.
     * @return Anlık saniye başına tıklama sayısı.
     */
    public static int getCPS() {
        return clicks.size();
    }
}

