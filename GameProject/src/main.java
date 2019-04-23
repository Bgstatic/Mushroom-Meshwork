/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
class pipeImages extends ImageView{
    public String type;
    public String direction;
    //no arg cons ekle
    pipeImages(Image image, String type, String direction){
        super(image);
        this.type = type;
        this.direction = direction;
    }
}

public class main extends Application {
    
    //ImageView[][] images = new ImageView[4][4];
    pipeImages[][] images = new pipeImages[4][4];
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
        Pane firstLevel = new Pane();
        //firstLevel.getChildren().add(images[0][0]);
        Scene Lvl1_Scene = new Scene(firstLevel, 400, 400);
        firstStage.setScene(Lvl1_Scene);
        firstStage.setTitle("Level 1");
        readInput("src/level1.txt");
        
        //Adding images to pane
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                firstLevel.getChildren().add(images[i][j]);
                images[i][j].setX((j*100));
                images[i][j].setY((i*100));
            }
        }
        //
        
        //start game end //
        
        btn.setOnAction(e -> {
            primaryStage.close();
            firstStage.show();
            
        });
        if(images[0][0] == null)
            System.out.println("null");
        else
            System.out.println("null değil");
        //firstLevel.getChildren().add(images[0][0]);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void readInput(String file){

        try{
            Scanner input = new Scanner(new File(file));

            while(input.hasNextLine()){
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
                images[(int)(position / 4.1)][((int)Math.ceil(position % 4.1))-1] = imageView;
            }
            
        }
        catch(Exception e){
            
        }
    }
    
    public String findImageType(String type, String direction){
        if(type.equals("Starter")){
            if(direction.equals("Vertical")){
                return "images/starter1.jpg";
            }
            else if(direction.equals("Horizontal")){
                return "images/starter2.jpg";
            }
        }
        
        else if(type.equals("Empty")){
            if(direction.equals("none"))
                return "images/empty3.jpg";
            else if(direction.equals("Free"))
                return "images/empty.jpg";
        }
        
        else if(type.equals("Pipe")){
            if(direction.equals("Vertical")){
                return "images/pipe1.jpg";
            }
            else if(direction.equals("Horizontal")){
                return "images/pipe2.jpg";
            }
            else if(direction.equals("00")){
                return "images/00.jpg";
            }
            else if(direction.equals("01")){
                return "images/01.jpg";
            }
            else if(direction.equals("10")){
                return "images/10.jpg";
            }
            else if(direction.equals("11")){
                return "images/11.jpg";
            }
        }
        
        else if(type.equals("PipeStatic")){
            if(direction.equals("Vertical")){
                return "images/pipestatic1.jpg";
            }
            else if(direction.equals("Horizontal")){
                return "images/pipestatic2.jpg";
            }
        }
        else if(type.equals("End")){
            if(direction.equals("Vertical")){
                return "images/end1.jpg";
            }
            else if(direction.equals("Horizontal")){
                return "images/end2.jpg";
            }
        }
        return null;
    }
}
