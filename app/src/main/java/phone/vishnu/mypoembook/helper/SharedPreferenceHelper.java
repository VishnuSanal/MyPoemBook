package phone.vishnu.mypoembook.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceHelper {

    private final String FIRST_RUN_BOOLEAN = "firstRunBoolean";

    private final String FONT_ARRAY_STRING = "fontArrayString";
    private final String PRESET_ARRAY_STRING = "presetArrayString";
    private final String CREATE_OPTIONS_ARRAY_STRING = "createOptionArrayString";

    private final String CARD_COLOR = "colorString";

    private final String BG_PATH = "backgroundPath";
    private final String FONT_PATH = "fontPath";

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean(FIRST_RUN_BOOLEAN, true);
    }

    public void setFirstRunBoolean(boolean firstRunBoolean) {
        sharedPreferences.edit().putBoolean(FIRST_RUN_BOOLEAN, firstRunBoolean).apply();
    }

    public String getFontArrayString() {
        return sharedPreferences.getString(FONT_ARRAY_STRING, null);
    }

    public void setFontArrayString(String fontArrayString) {
        sharedPreferences.edit().putString(FONT_ARRAY_STRING, fontArrayString).apply();
    }

    public String getPresetArrayString() {
        return sharedPreferences.getString(PRESET_ARRAY_STRING, null);
    }

    public void setPresetArrayString(String presetArrayString) {
        sharedPreferences.edit().putString(PRESET_ARRAY_STRING, presetArrayString).apply();
    }

    public String getCreateOptionsArrayString() {
        return sharedPreferences.getString(CREATE_OPTIONS_ARRAY_STRING, null);
    }

    public void setCreateOptionsArrayString(String createOptionsArrayString) {
        sharedPreferences.edit().putString(CREATE_OPTIONS_ARRAY_STRING, createOptionsArrayString).apply();
    }

    public String getBackgroundPath() {
        return sharedPreferences.getString(BG_PATH, null);
    }

    public void setBackgroundPath(String backgroundPath) {
        sharedPreferences.edit().putString(BG_PATH, backgroundPath).apply();
    }

    public String getFontPath() {
        return sharedPreferences.getString(FONT_PATH, null);
    }

    public void setFontPath(String fontPath) {
        sharedPreferences.edit().putString(FONT_PATH, fontPath).apply();
    }

    public String getCardColorPreference() {
        return sharedPreferences.getString(CARD_COLOR, null);
    }

    public void setCardColorPreference(String colorPreference) {
        sharedPreferences.edit().putString(CARD_COLOR, colorPreference).apply();
    }

    public void resetSharedPreferences() {
        setCardColorPreference(null);
        setBackgroundPath(null);
        setFontPath(null);
    }
}
