package com.example.farbverlaeufe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Controller {

    @FXML Label title;

    @FXML Button startButton;

    @FXML ImageView imageView;
    int WIDTH;
    int HEIGHT;

    int CLEAR_RADIUS = 30;

    Model model;
    View view;

    /**
     * This method is automatically called right after the FXMLLoader from the Main class is done doing its thing.
     * It is vital that the connection to model and view is initialized here, not in a constructor (because then #
     * it'd always be null)
     */
    public void initialize() {
        //Because of the fxml the model and the view need to contain each other
        this.model = new Model(this);
        this.view = new View(this,this.model);
        model.setView(view);
        this.WIDTH = (int)imageView.getImage().getWidth();
        this.HEIGHT = (int)imageView.getImage().getHeight();
    }

    /**
     * When start button press, the farbvelaeufe progress
     */
    @FXML
    public void onStartButtonClick() {
        model.initTimeline();
    }

    /**
     * When on color the mousebutton click, the farbe goes home cos it's sick
     * (meaning there's no color afterwards).
     * Caveat: The click needs to be on a colored pixel for some reason
     */
    @FXML
    public void onClick(MouseEvent mouseEvent) {
        //Circle object handles math for me - so I don't need to think which points are in it or not
        Circle circle = new Circle();
        circle.setCenterX(mouseEvent.getX());
        circle.setCenterY(mouseEvent.getY());
        circle.setRadius(CLEAR_RADIUS);

        //As to not iterate over the entire picture, because most of it doesn't have the circle anyway:
        double startOfRelevantAreaX = mouseEvent.getX() - CLEAR_RADIUS;
        double startOfRelevantAreaY = mouseEvent.getY() - CLEAR_RADIUS;
        double endOfRelevantAreaX = mouseEvent.getX() + CLEAR_RADIUS;
        double endOfRelevantAreaY = mouseEvent.getY() + CLEAR_RADIUS;

        //Iterate over sqare which contains the circle, and eradicate all color which actually is in the circle
        for (int x = (int)startOfRelevantAreaX; x <= endOfRelevantAreaX; x++) {
            for (int y = (int)startOfRelevantAreaY; y <= endOfRelevantAreaY; y++) {
                if (circle.contains(x,y)) {
                    try {
                        Color color = view.pixelReader.getColor(x,y);
                        view.pixelWriter.setColor(x, y, Color.TRANSPARENT);
                        model.possibleNextPixels.add(new Pixel(x,y,color));
                    } catch (IndexOutOfBoundsException e) {
                        continue; //When circle goes out of bounds I don't care
                    }
                }
            }
        }
    }
}