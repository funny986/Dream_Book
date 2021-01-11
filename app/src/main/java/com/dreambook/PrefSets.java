package com.dreambook;

public interface PrefSets {
    void genderSet(int gender);
    void themeSet(boolean theme);
    int getAutorGender();
    boolean getCustomTheme();
}
