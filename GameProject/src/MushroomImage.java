
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class MushroomImage extends ImageView {

    MushroomImage(Image image, double x, double y) { //constructor
        super(image); // Get the image from it's superclass.
        //Set all properties of the mushroom image.
        this.setFitHeight(50);
        this.setFitWidth(50);
        this.setX(x);
        this.setY(y);

    }
}