package br.com.brunocs.expandabletags;

/**
 * Created by Pc on 25/06/2016.
 */
public class Tag {
    private String name;
    private String description;
    private Integer backgroundColor;
    private Integer textColor;
    private boolean visible;

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
        this.visible = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tag)) return false;

        Tag t = (Tag) object;

        if (t.getName() == this.name && t.getDescription() == this.description) {
            return true;
        } else {
            return false;
        }
    }

}
