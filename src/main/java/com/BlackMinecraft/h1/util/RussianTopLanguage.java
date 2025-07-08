package com.BlackMinecraft.h1.util;
public class RussianTopLanguage {
    public static String formatHearts(int count, String lang) {
        if ("ru".equalsIgnoreCase(lang)) {
            int m10 = count % 10, m100 = count % 100;
            if (m10 == 1 && m100 != 11)        return count + " сердце";
            else if (m10 >= 2 && m10 <= 4 && !(m100 >= 12 && m100 <= 14))
                return count + " сердца";
            else                                return count + " сердец";
        }
        return count + (count == 1 ? " heart" : " hearts");
    }

}
