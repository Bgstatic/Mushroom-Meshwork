

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author anil
 */
class pipeImages extends ImageView {

    public String type;
    public String direction;
    public int initial_index_X, initial_index_Y, final_index_X, final_index_Y;

    //no arg cons ekle
    pipeImages() {

    }

    pipeImages(Image image, String type, String direction) {
        super(image);
        this.type = type;
        this.direction = direction;

        setOnMousePressed(e -> {

            initial_index_X = (int) (e.getSceneY() / 100);
            initial_index_Y = (int) (e.getSceneX() / 100);

        });
        
        setOnMouseReleased(e -> {
            
            final_index_X = (int) (e.getSceneY() / 100);
            final_index_Y = (int) (e.getSceneX() / 100);
            if(canMove(initial_index_X, initial_index_Y, final_index_X, final_index_Y)){

                    int temp_X = (int)(main.images[initial_index_X][initial_index_Y].getX());
                    int temp_Y = (int)(main.images[initial_index_X][initial_index_Y].getY());

                    //Kordinatlarının yer değiştirmesi
                    main.images[initial_index_X][initial_index_Y].setX(main.images[final_index_X][final_index_Y].getX());
                    main.images[initial_index_X][initial_index_Y].setY(main.images[final_index_X][final_index_Y].getY());
                    main.images[final_index_X][final_index_Y].setX(temp_X);
                    main.images[final_index_X][final_index_Y].setY(temp_Y);
                    
                    //Array içinde de değişiklik yapmamız gerekiyor
                    pipeImages temp = main.images[initial_index_X][initial_index_Y];
                    main.images[initial_index_X][initial_index_Y] = main.images[final_index_X][final_index_Y];
                    main.images[final_index_X][final_index_Y] = temp;
                
            }
        });
    }
    //Yaptığımız hareketin mümkün olup olmadığının kontrolü
    public boolean canMove(int initial_X, int initial_Y, int final_X, int final_Y){
        
        if(!main.images[final_X][final_Y].type.equals("Free")){
            return false;
        }
        if(main.images[initial_X][initial_Y].direction.equals("PipeStatic") 
               || main.images[initial_X][initial_Y].direction.equals("Starter")
               ||  main.images[initial_X][initial_Y].direction.equals("End")){
            return false;
        }
        
        //Burada ilginç bir trick yaptım: hareket ettirdiğimiz kutu empty olan kutunun sağ-sol-üst-alt'ında olup olmadığını kontrol etmek için
        // bulundukları konumların sayı karşılığını aldım mesela images[2][3] == 23, images[1][1] == 11; konumlarının indexlerini birleştirip sayı olarak aldım kısacası
        // sonrasında if içindeki işlemler sol-sağ-üst-alt olup olmadığını veriyor.
        int initialAsNumber = initial_X * 10 + initial_Y;
        int finalAsNumber = final_X * 10 + final_Y;
        
        if(finalAsNumber - initialAsNumber == 1 
        || finalAsNumber - initialAsNumber == 10
        || initialAsNumber - finalAsNumber == 1
        || initialAsNumber - finalAsNumber == 10){
            return true;
            
        }
        else{
            return false;
        }
        //
        //
    }
    
}

class ImagePane extends Pane {

    ImagePane() {
    }

    public void print() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.getChildren().add(main.images[i][j]);
                main.images[i][j].setX((j * 100));
                main.images[i][j].setY((i * 100));
            }
        }
    }
}

public class main extends Application {

    public static pipeImages[][] images = new pipeImages[4][4];

    @Override
    public void start(Stage primaryStage) {

        // Main scene //
        Button btn = new Button();
        btn.setText("Start");

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene mainScene = new Scene(root, 400, 400);

        primaryStage.setTitle("Game Project");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        // Main scene end //

        // start game //
        Stage firstStage = new Stage();
        ImagePane firstLevel = new ImagePane();
        Scene Lvl1_Scene = new Scene(firstLevel, 400, 400);
        firstStage.setScene(Lvl1_Scene);
        firstStage.setTitle("Level 1");
        readInput("src/level1.txt");
        firstLevel.print();

        
        //start game end //
        btn.setOnAction(e -> {
            primaryStage.close();
            firstStage.show();

        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void readInput(String file) {

        try {
            Scanner input = new Scanner(new File(file));

            while (input.hasNextLine()) {
                String read = input.nextLine();
                String[] parsed = read.split(",");

                int position = Integer.parseInt(parsed[0]);
                String type = parsed[1];
                String direction = parsed[2];
                String imageType = findImageType(type, direction);
                Image image = new Image(imageType);
                pipeImages imageView = new pipeImages(image, direction, type);

                //burda özellik veririm imageviewe
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                //
                images[(int) (position / 4.1)][((int) Math.ceil(position % 4.1)) - 1] = imageView;
            }

        } catch (Exception e) {

        }
    }

    public String findImageType(String type, String direction) {
        if (type.equals("Starter")) {
            if (direction.equals("Vertical")) {
                return "images/starter1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/starter2.jpg";
            }
        } else if (type.equals("Empty")) {
            if (direction.equals("none")) {
                return "images/empty3.jpg";
            } else if (direction.equals("Free")) {
                return "images/empty.jpg";
            }
        } else if (type.equals("Pipe")) {
            if (direction.equals("Vertical")) {
                return "images/pipe1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/pipe2.jpg";
            } else if (direction.equals("00")) {
                return "images/00.jpg";
            } else if (direction.equals("01")) {
                return "images/01.jpg";
            } else if (direction.equals("10")) {
                return "images/10.jpg";
            } else if (direction.equals("11")) {
                return "images/11.jpg";
            }
        } else if (type.equals("PipeStatic")) {
            if (direction.equals("Vertical")) {
                return "images/pipestatic1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/pipestatic2.jpg";
            }
        } else if (type.equals("End")) {
            if (direction.equals("Vertical")) {
                return "images/end1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/end2.jpg";
            }
        }
        return null;
    }
}
