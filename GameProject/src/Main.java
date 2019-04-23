/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author anil
 */
class Pipe extends ImageView{
    double firstX, firstY;
    
    Pipe(Image image){
        super(image);
        
        setOnMouseDragged(e -> {
           this.setX(e.getX());
           this.setY(e.getY());
        });
    }
    
    
}
class MouseDragHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent e){
        
    }
}
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");

        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Button btn2 = new Button();
        btn2.setText("Stage2");
        Image image = new Image("Mario_pipe.png");
        
        ImageView imageView = new ImageView(image);
        Pipe[][] images = new Pipe[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                images[i][j] = new Pipe(image);
                images[i][j].setFitHeight(100);
                images[i][j].setFitWidth(100);
            }
        }
 
        GridPane newRoot = new GridPane();
        //newRoot.add(imageView, 0, 0);
        //newRoot.add(new ImageView(image), 0, 1);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(i == j && i == 2)
                    continue;
                else
                    newRoot.add(images[i][j], i, j);
            }
        }
        images[2][1].setOnMousePressed(e -> {
            int firstRow = (int)(images[2][1].getX() / 100);
            int firstColumn = (int)(images[2][1].getY() / 100);

            System.out.println(e.getSceneX()+" "+e.getSceneY());
            newRoot.getChildren().remove(images[2][1]);
        });
        images[2][1].setOnMouseReleased(e -> {

            int finalRow = (int)(e.getSceneX() / 100);
            int finalColumn = (int)(e.getSceneY() / 100);
                newRoot.add(images[2][1], finalRow, finalColumn);
        });
        
        images[1][2].setOnMouseDragged(e -> {
            int firstRow = (int)(images[1][2].getX() / 100);
            int firstColumn = (int)(images[1][2].getY() / 100);
            int finalRow = (int)(e.getX() / 100);
            int finalColumn = (int)(e.getY() / 100);
                        newRoot.getChildren().remove(images[1][2]);
            newRoot.add(images[1][2], finalRow, finalColumn);

            
        });
        
        imageView.setOnMouseClicked(e -> {
            newRoot.add(imageView, 1, 0); 
            newRoot.getChildren().remove(imageView);
        });
        //newRoot.getChildren().add(btn2);
        Scene scene2 = new Scene(newRoot, 300, 250);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scene2);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
