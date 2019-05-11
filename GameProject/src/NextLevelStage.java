
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

//Bilgehan Geçici 150117072
//Anıl Şenay 150117023

//This class creates next level screen on the game. When the user completes the level the next level screen shows up.

class NextLevelStage extends Stage { //NextLevelStage Class

    double x, y;
    public static MediaPlayer winSound; 

    public NextLevelStage() {
        GameStage.levelSound.stop();
        playMusic();

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/nextLevel.gif"); // Adding mario next level image
        ImageView nextLvlBg = new ImageView(bg);
        nextLvlBg.setFitHeight(475);
        nextLvlBg.setFitWidth(660);

        VBox vbox = new VBox();
		
        // Adding label for showing total moves  and set the font and color properties 
        Label moveText = new Label("You made : " + GameStage.moveInLevel + " moves." + "(Total:" + main.totalMove + ")");
        moveText.setFont(new Font("Arial", 26));
        moveText.setTextFill(Color.web("#ffffff"));
        moveText.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");

        HBox hbox = new HBox();

        Image btnImage = new Image("images/nextLvl.png"); // next level button
        ImageView nextBtn = new ImageView(btnImage);
        nextBtn.setFitHeight(75);
        nextBtn.setFitWidth(250);

        Image homeImage = new Image("images/mainMenu.png"); // Main menu image 
        ImageView homeButton = new ImageView(homeImage);
        homeButton.setFitHeight(75);
        homeButton.setFitWidth(250);

        hbox.setSpacing(20);
        hbox.getChildren().addAll(homeButton, nextBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(-10);
        vbox.getChildren().addAll(moveText, hbox);
        vbox.setAlignment(Pos.BOTTOM_CENTER);

        pane.getChildren().addAll(nextLvlBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);
         
		 // it provides drag the next level screen
        pane.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            this.getScene().getWindow().setX(e.getScreenX() - x);
            this.getScene().getWindow().setY(e.getScreenY() - y);
        });

        nextBtn.setOnMouseClicked(e -> { // when the button pressed it passes to the next level 
        	 GameStage.moveInLevel = 0;
             this.close();
             main.readInput(main.levels[++main.level]);
             main.lvlStage.changeTitle();
             GameStage.firstLevel.print();
             winSound.stop();
             main.lvlStage.startMusic();
            main.buttonPlay();

        });

        homeButton.setOnMouseClicked(e -> { // when the button pressed it resets the game 
            main.buttonPlay();
            main.level = 0;
            main.totalMove = 0;
            GameStage.moveInLevel = 0;
            main.mainSound.play();
            main.lvlStage.close();
            this.close();
            main.mainStage.show();
            main.lvlStage = new GameStage();

        });
    }

    public void playMusic() { // it plays the music when the level is completed
        try {
            winSound = new MediaPlayer(new Media(this.getClass().getResource("musics/levelCompleted.wav").toExternalForm()));
            winSound.setVolume(0.2);
            winSound.play();
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    
}
