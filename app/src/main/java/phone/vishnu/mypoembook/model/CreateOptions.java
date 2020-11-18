package phone.vishnu.mypoembook.model;

public class CreateOptions {
    private String name, fontPath, bgPath, cardColor;

    public CreateOptions() {
    }

    public CreateOptions(String fontPath, String bgPath, String cardColor) {
        this.fontPath = fontPath;
        this.bgPath = bgPath;
        this.cardColor = cardColor;
    }

    public CreateOptions(String name, String fontPath, String bgPath, String cardColor) {
        this.name = name;
        this.fontPath = fontPath;
        this.bgPath = bgPath;
        this.cardColor = cardColor;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public String getBgPath() {
        return bgPath;
    }

    public void setBgPath(String bgPath) {
        this.bgPath = bgPath;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
