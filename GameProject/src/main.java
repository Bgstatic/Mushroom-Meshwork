
import java.io.File;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author anil
 */

public class main extends Application {

    public static PipeImages[][] images = new PipeImages[4][4];
    public static MushroomImage mushroom;
    public static int totalMove;
    public static int level = 0;
    public static String[] levels = {"src/level1.txt", "src/level2.txt", "src/level3.txt", "src/level4.txt", "src/level5.txt"};
    public static GameStage lvlStage;
    public static MediaPlayer mainSound;
    public static MediaPlayer buttonSound;
    public static Stage mainStage;
    
    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        // Main scene //
        
        //This try catch to add music to main menu and add sound effect for buttons.
        try {
            mainSound = new MediaPlayer(new Media(this.getClass().getResource("musics/mainScene.mp3").toExternalForm()));
            mainSound.setVolume(0.2);
            mainSound.play();

            buttonSound = new MediaPlayer(new Media(this.getClass().getResource("musics/menuNavigate.wav").toExternalForm()));
            buttonSound.setVolume(0.2);

        } catch (Exception e) {
            System.out.println("error");
        }
        
        //Main Scene start
        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, 1138, 480);
        
        Image bgImage = new Image("images/bg.gif"); //background gif
        ImageView background = new ImageView(bgImage);

        VBox vbox = new VBox();
        //start button
        Image btnImage = new Image("images/start.png"); 
        ImageView startButton = new ImageView(btnImage);
        startButton.setFitHeight(75);
        startButton.setFitWidth(300);
        
        //settings button
        Image settingsImage = new Image("images/settings.png"); 
        ImageView settingsBtn = new ImageView(settingsImage);
        settingsBtn.setFitHeight(75);
        settingsBtn.setFitWidth(300);

        HBox bottomHBox = new HBox();
        HBox leaderboardHbox = new HBox();
        
        //leaderboard icon 
        Image leaderImg = new Image("images/cup.png");
        ImageView leaderBtn = new ImageView(leaderImg);
        leaderBtn.setFitHeight(75);
        leaderBtn.setFitWidth(75);
        leaderboardHbox.getChildren().add(leaderBtn);
        
        //credits button
        Image creditsImg = new Image("images/credits.png");
        ImageView creditsBtn = new ImageView(creditsImg);
        creditsBtn.setFitHeight(75);
        creditsBtn.setFitWidth(300);
        
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT); // set the leaderboard icon to the right side of the bottom.
        bottomHBox.setPadding(new Insets(0,10,5,0));
        bottomHBox.getChildren().addAll(leaderboardHbox);
        
        vbox.setAlignment(Pos.BOTTOM_CENTER); //Set buttons to center of the scene. 
        vbox.getChildren().addAll(startButton, settingsBtn, creditsBtn, bottomHBox); //add all nodes to pane.

        root.getChildren().addAll(background, vbox); //add background and vbox pane to main pane

        primaryStage.setTitle("Mushroom Mashwork");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        // Main scene end //
        
        lvlStage = new GameStage(); //create new gameStage class to start game
        
        // ** button actions **//
        //start button action
        startButton.setOnMouseClicked(e -> {

            main.buttonPlay(); //button click sound effect
            //After click the start button it starts the game with fade effect.
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(1000));
            fade.setNode(root);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(ef -> { //when fade effect finished, program keeps with these steps.
                primaryStage.close(); //main screen close.
                lvlStage.show(); // game board comes up.
                mainSound.stop(); // main menu music stops.
                lvlStage.startMusic(); //game music starts.
                root.setOpacity(1); // after fade transition set root pane's opactiy to 1.
            });
        });
        //settings button action
        settingsBtn.setOnMouseClicked(e -> { //when the button clicked, settings menu shows up.
            main.buttonPlay(); //button click sound effect
            SettingsStage settings = new SettingsStage();
            settings.show();
        });
        //credits button action
        creditsBtn.setOnMouseClicked(e -> {  //when the button clicked, credits shows up. 
            main.buttonPlay(); //button click sound effect
            CreditsStage credits = new CreditsStage();
            credits.show();
            primaryStage.close();
        });
        //leaderboard icon action
        leaderBtn.setOnMouseClicked(e -> {  //when the icon clicked, leaderboard shows up.
            main.buttonPlay(); //button click sound effect
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.show();
            primaryStage.close();
        });
        //button actions end
    }
    
    //sound effect starter method.
    public static void buttonPlay() {
        main.buttonSound.stop();
        main.buttonSound.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    //get level informations from the file which is given as argument.
    //After that informations, creates pipe images based on text's contents.
    public static void readInput(String file) {

        try {
            Scanner input = new Scanner(new File(file));

            while (input.hasNextLine()) {
                String read = input.nextLine();
                String[] parsed = read.split(",");

                int position = Integer.parseInt(parsed[0]); //position of pipe on board.
                String type = parsed[1]; // type of pipe (Starter, Pipe, etc.)
                String direction = parsed[2]; // direction of pipe (Vertical, horizontal, etc.)
                String imageType = findImageType(type, direction); //get image url via findImageType method.
                Image image = new Image(imageType);
                PipeImages imageView = new PipeImages(image, direction, type); //creates the image based on the given image, direction and type.

                //set heigt and width properties of the images.
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                
                //To add exact position of the images to images array.
                //After getting the position from input, it finds indexes's of image with some mathematical operations.
                //To find first index of image, davide position with 4.1 (For example: if position 15, 15/4.1 = 3 => its first index = 3.
                //To find second index of image, take mod of position from 4.1 and ceiling it and subtract 1. 
                //(For example: 15 % 4.1 = 2.7, to cail => 3, substract => 2. So its indexes are => [3,2]
                images[(int) (position / 4.1)][((int) Math.ceil(position % 4.1)) - 1] = imageView;
            }

        } catch (Exception e) {

        }
    }
    
    //It returns the exact url of the images based on the given characteristic features.
    public static String findImageType(String type, String direction) {
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
            } else if (direction.equals("00")) {
                return "images/00_static.jpg";
            } else if (direction.equals("01")) {
                return "images/01_static.jpg";
            } else if (direction.equals("10")) {
                return "images/10_static.jpg";
            } else if (direction.equals("11")) {
                return "images/11_static.jpg";
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
