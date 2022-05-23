package com.example.farbverlaeufe;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class View {

    Model model;
    int WIDTH = 300;
    int HEIGHT = 300;
    WritableImage writableImage;
    PixelReader pixelReader;
    PixelWriter pixelWriter;
    Pixel currPixel;

    /**
     * Usually when using an fxml no extra view class is needed. However, the fxml is very bad at dynamically drawing,
     * so this extra View class handles that.
     * @param controller the controller binding to the fxml
     * @param model the model doing the calculations
     */
    public View (Controller controller, Model model) {
        this.model = model;
        //WritableImage is better than Canvas, because a Canvas updates in bursts. That can lead to the problem
        //that when writing and reading a pixel in short succession, the PixelReader doesn't see what the PixelWriter
        //has done before
        this.writableImage = new WritableImage(WIDTH, HEIGHT);
        controller.imageView.setImage(writableImage); //<- because the fxml doesn't do WritableImages for some reason

        this.pixelReader = writableImage.getPixelReader();
        this.pixelWriter = writableImage.getPixelWriter();

        //Set middle pixel to grey:
        pixelWriter.setColor(WIDTH /2, HEIGHT /2, Color.GREY);
        currPixel = new Pixel(
                WIDTH /2,
                HEIGHT /2,
                pixelReader.getColor(WIDTH /2, HEIGHT /2)
        );
        findNewPixels(currPixel);
    }

    /**
     * Used to sync image pixel with custom Pixel object
     * @param pixel the pixel to be colored
     */
    public void fillInNewPixel(Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();
        Color color = pixel.getColor();
        pixelWriter.setColor(x,y,color);
    }

    /**
     * Searches the 8 surrounding Pixels of the Pixel object in the image and marks them 'to be colored some time in
     * the future' if they are withing bounds and still transparent
     * @param pixel the pixel which's surrounding ones are to be searched
     */
    public void findNewPixels(Pixel pixel) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = pixel.getX() +i;
                int y = pixel.getY() +j;
                if (x < 0 || y < 0 || x > WIDTH || y > WIDTH || x > HEIGHT || y > HEIGHT) continue;
                if (x == pixel.getX() && y == pixel.getY()) continue;
                Color color;
                try {
                    color = pixelReader.getColor(x, y);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
                if (!Color.TRANSPARENT.equals(color)) continue;
                model.possibleNextPixels.add(new Pixel(x,y,color));
            }
        }
    }

    /**
     * Randomly selects a single transparent pixel 'to be colored next' which is surrounding an already colored pixel.
     * @param pixel Colored pixel of which a random transparent surrounding pixel is selected to be colored next.
     * @return the new transparent pixel to be colored
     */
    public Pixel selectRandomColoredSurroundingPixel(Pixel pixel) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = pixel.getX() +i;
                int y = pixel.getY() +j;
                if (x < 0 || y < 0 || x > WIDTH || y > WIDTH || x > HEIGHT || y > HEIGHT) continue;
                if (x == pixel.getX() && y == pixel.getY()) continue;
                Color color;
                try {
                    color = pixelReader.getColor(x, y);
                } catch (IndexOutOfBoundsException e) {
                    continue; //if pixel is out of bounds I don't care
                }
                if (!Color.TRANSPARENT.equals(color)) {
                    return new Pixel(x, y, color);
                }
            }
        }
        return null;
    }
}
