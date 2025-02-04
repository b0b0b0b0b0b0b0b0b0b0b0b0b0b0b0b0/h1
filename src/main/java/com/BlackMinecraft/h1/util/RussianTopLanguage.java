package com.BlackMinecraft.h1.util;
public class RussianTopLanguage {
    public static String formatHearts(int count) {
        int mod10 = count % 10;
        int mod100 = count % 100;
        if (mod10 == 1 && mod100 != 11) {
            return count + " сердце";
        } else if (mod10 >= 2 && mod10 <= 4 && !(mod100 >= 12 && mod100 <= 14)) {
            return count + " сердца";
        } else {
            return count + " сердец";
        }
    }
}
