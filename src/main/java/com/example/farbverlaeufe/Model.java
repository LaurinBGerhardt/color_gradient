package com.example.farbverlaeufe;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

/**
 * Model which does all the calculations for the View
 */
public class Model {

    Controller controller;
    View view;
    Timeline ticks;
    Random rand = new Random();

    /**
     * Number of pixels to be colored each second
     */
    double TICKSPEED = 0.0001;

    /**
     * The most important datastructure in the model. The possibleNextPixels HashSet contains all pixel objects
     * which represent pixels on the image which are not yet colored, but which have a colored neighbor - and hence
     * have the possibility of being the next ones colored.
     */
    Set<Pixel> possibleNextPixels = new HashSet<>();

    public Model(Controller controller) {
        this.controller = controller;
    }

    /**
     * From the code provided to us on Uni2Work - only the actionEvent is custom made by me
     * Starts the timeline of the model - i.e. coloring the pixels of the empty image
     */
    void initTimeline() {
        EventHandler<ActionEvent> handler = actionEvent -> {
            Pixel newPixel = null;
            if (possibleNextPixels.size() > 0) newPixel = findNewPixel();
            if (newPixel != null) newPixel = calculateNewColor(newPixel);
            if (newPixel != null) {
                view.fillInNewPixel(newPixel);
                view.findNewPixels(newPixel);
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(TICKSPEED), handler);
        this.ticks = new Timeline();
        this.ticks.getKeyFrames().add(keyFrame);
        ticks.setCycleCount(Timeline.INDEFINITE);
        ticks.play();
    }

    /**
     * Each empty pixel randomly gets a color similar, but not identical to a surrounding colored pixel
     * @param newPixel a transparent pixel to be colored
     * @return the exact pixel which was transparent, but not it has a color
     */
    Pixel calculateNewColor(Pixel newPixel) {
        if (this.view == null) throw new NullPointerException("View ist hier noch Null!");
        Pixel basePixel = view.selectRandomColoredSurroundingPixel(newPixel); //the colored pixel to yield its color
        if (basePixel != null) {
            //Tweak the color of the basePixel a bit:
            Color baseColor = basePixel.color;
            double redOffset = ColorRandomizer.adjust();
            double greenOffset = ColorRandomizer.adjust();
            double blueOffset = ColorRandomizer.adjust();
            Color newColor = Color.color(
                    baseColor.getRed() * redOffset,
                    baseColor.getGreen() * greenOffset,
                    baseColor.getBlue() * blueOffset
            );
            newPixel.setColor(newColor);
            return newPixel;
        }
        return null;
    }

    /**
     * Of all possible pixels which could be colored next, one is selected randomly
     * @return the pixel to be colored next
     */
    Pixel findNewPixel() {
        Pixel newPixel = (Pixel) possibleNextPixels.toArray()[rand.nextInt(possibleNextPixels.size())];
        possibleNextPixels.remove(newPixel);
        return newPixel;
    }

    /**
     * Needed, because the model and the view need to contain each other. So one is created first (here the model),
     * which then gets the other (here the view) assigned to it once that has been initialized.
     */
    public void setView(View view) {
        this.view = view;
    }
}
