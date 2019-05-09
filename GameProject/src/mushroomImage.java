
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class mushroomImage extends ImageView {

    mushroomImage(Image image, double x, double y) {
        super(image);
        this.setFitHeight(50);
        this.setFitWidth(50);
        this.setX(x);
        this.setY(y);

    }
}