
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class ImagePane extends Pane {

    ImagePane() {
    }
    //it sets the locations of the images which are in the images array.
    public void print() {
        ImageView bg = new ImageView(new Image("images/emptyBg.png"));
        bg.setFitHeight(400);
        bg.setFitWidth(400);
        this.getChildren().add(bg);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.getChildren().add(main.images[i][j]); //add image to pane.
                //set image's X and Y as its index values * 100.
                main.images[i][j].setX((j * 100));
                main.images[i][j].setY((i * 100));
                
                //it places the mushroom image to the center of starter image.
                if (main.images[i][j].direction.equals("Starter")) {
                    main.mushroom = new MushroomImage(new Image("images/mushroom.png"), (j * 100) + 25, (i * 100) + 25);
                }
            }
        }
        this.getChildren().add(main.mushroom); // to be top of pipes as image, this should be added in pane later.
    }
}