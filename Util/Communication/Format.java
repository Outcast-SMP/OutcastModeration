package me.illusion.outcastmoderation.Util.Communication;

import org.bukkit.entity.Player;

public class Format {
    public static String formatString(String text, Player ply) {
        if (text.contains("#IGN")) {
            text =  text.replace("#IGN", ply.getName());
        }

        return text;
    }

    public static String formatString(String text, String findValue, String replaceValue) {
        if (text.contains(findValue)) {
            text = text.replace(findValue, replaceValue);
        }

        return text;
    }

    public static String formatString(String text, String findValue, String replaceValue,
                                      String findValue2, String replaceValue2) {
        if (text.contains(findValue)) {
            text = text.replace(findValue, replaceValue);
        }

        if (text.contains(findValue2)) {
            text = text.replace(findValue2, replaceValue2);
        }

        return text;
    }

    public static String formatString(String text, String findValue, String replaceValue,
                                      String findValue2, String replaceValue2,
                                      String findValue3, String replaceValue3) {
        if (text.contains(findValue)) {
            text = text.replace(findValue, replaceValue);
        }

        if (text.contains(findValue2)) {
            text = text.replace(findValue2, replaceValue2);
        }

        if (text.contains(findValue3)) {
            text = text.replace(findValue3, replaceValue3);
        }

        return text;
    }

    public static String formatString(String text, String findValue, String replaceValue,
                                      String findValue2, String replaceValue2,
                                      String findValue3, String replaceValue3,
                                      String findValue4, String replaceValue4) {
        if (text.contains(findValue)) {
            text = text.replace(findValue, replaceValue);
        }

        if (text.contains(findValue2)) {
            text = text.replace(findValue2, replaceValue2);
        }

        if (text.contains(findValue3)) {
            text = text.replace(findValue3, replaceValue3);
        }

        if (text.contains(findValue4)) {
            text = text.replace(findValue4, replaceValue4);
        }

        return text;
    }
}
