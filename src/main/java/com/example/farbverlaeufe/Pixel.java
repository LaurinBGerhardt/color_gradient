package com.example.farbverlaeufe;

import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * A custom class to hold information of individual pixels of the image. Not exactly necessary, but handy
 */
public class Pixel {
    int x;
    int y;
    Color color;

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    //Generated methods:

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Deliberately, if a new pixel with a color is inserted into possibleNextPixels, but there's already one inside
     * the set without a color, the one without should be replaced with the colored one. That's why the equals the the
     * hash don't consider color
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pixel)) return false;
        Pixel pixel = (Pixel) o;
        return getX() == pixel.getX() && getY() == pixel.getY();
    }

    /**
     * See @equals
     */
    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
