

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
            System.out.println("İlk image kordinat : " + e.getSceneX() + " " + e.getSceneY());
            initial_index_X = (int) (e.getSceneY() / 100);
            initial_index_Y = (int) (e.getSceneX() / 100);
            System.out.println("press kordinat: " + initial_index_X + " " + initial_index_Y);

        });
        /* setOnMouseReleased(e -> {
            System.out.println("İkinci image kordinat : " + e.getSceneX()+ " " + e.getSceneY());
            final_index_X = (int)(e.getSceneY() / 100);
            final_index_Y = (int)(e.getSceneX() / 100);
            System.out.println("release kordinat: " + final_index_X + " " + final_index_Y);
        });*/
        /*setOnMouseDragged(e -> {
            final_index_X = (int) (e.getSceneY() / 100);
            final_index_Y = (int) (e.getSceneX() / 100);
            //System.out.println(main.images[final_index_X][final_index_Y].getX() + " " + main.images[final_index_X][final_index_Y].getY());
            //System.out.println(main.images[initial_index_X][initial_index_Y].getX() + " " + main.images[initial_index_X][initial_index_Y].getY()); 
            
        });*/
        setOnMouseReleased(e -> {
            final_index_X = (int) (e.getSceneY() / 100);
            final_index_Y = (int) (e.getSceneX() / 100);
                        System.out.println("released initial kordinat: " + main.images[initial_index_X][initial_index_Y].getX() + " " + main.images[initial_index_X][initial_index_Y].getY());
                                    System.out.println("released final kordinat: " + main.images[final_index_X][final_index_Y].getX() + " " + main.images[final_index_X][final_index_Y].getY());
                                    
             /*                       
            main.images[initial_index_X][initial_index_Y].setTranslateX(main.images[final_index_X][final_index_Y].getX() - main.images[initial_index_X][initial_index_Y].getX());
            main.images[initial_index_X][initial_index_Y].setTranslateY(main.images[final_index_X][final_index_Y].getY() - main.images[initial_index_X][initial_index_Y].getY());
            main.images[final_index_X][final_index_Y].setTranslateX(main.images[initial_index_X][initial_index_Y].getX() - main.images[final_index_X][final_index_Y].getX());
            main.images[final_index_X][final_index_Y].setTranslateY(main.images[initial_index_X][initial_index_Y].getY() - main.images[final_index_X][final_index_Y].getY()); 
                                    */
            //main.images[3][1].setTranslateX(100);
            //main.images[3][1].setTranslateY(250);
            int temp_X = (int)(main.images[initial_index_X][initial_index_Y].getX());
            int temp_Y = (int)(main.images[initial_index_X][initial_index_Y].getY());
            //main.images[initial_index_X][initial_index_Y].relocate(main.images[final_index_X][final_index_Y].getX(), main.images[final_index_X][final_index_Y].getY());
            //main.images[final_index_X][final_index_Y].relocate(temp_X, temp_Y);
            
            ///////////////////////////////////////* BURALAR ÇOK KARIŞTI BİR ÇEKİ DÜZEN VERECEM *////////////////

            main.images[initial_index_X][initial_index_Y].setX(main.images[final_index_X][final_index_Y].getX());
            main.images[initial_index_X][initial_index_Y].setY(main.images[final_index_X][final_index_Y].getY());
            main.images[final_index_X][final_index_Y].setX(temp_X);
            main.images[final_index_X][final_index_Y].setY(temp_Y);
            pipeImages temp = main.images[initial_index_X][initial_index_Y];
            main.images[initial_index_X][initial_index_Y] = main.images[final_index_X][final_index_Y];
            main.images[final_index_X][final_index_Y] = temp;
            
            System.out.println("" + main.images[2][2].direction);

        });
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
        //firstLevel.getChildren().add(images[0][0]);
        Scene Lvl1_Scene = new Scene(firstLevel, 400, 400);
        firstStage.setScene(Lvl1_Scene);
        firstStage.setTitle("Level 1");
        readInput("src/level1.txt");
        firstLevel.print();

        System.out.println(images[3][1].getX());
        System.out.println(images[3][1].getY());
        System.out.println(images[3][1].getScaleX());
        System.out.println(images[3][1]);

        //Adding images to pane
        /* for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                firstLevel.getChildren().add(images[i][j]);
                images[i][j].setX((j*100));
                images[i][j].setY((i*100));
            }
        }*/
        //
        //start game end //
        btn.setOnAction(e -> {
            primaryStage.close();
            firstStage.show();

        });

    }

    public static void print() {

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
