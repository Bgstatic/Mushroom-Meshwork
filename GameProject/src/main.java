
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

    public static pipeImages[][] images = new pipeImages[4][4];
    public static mushroomImage mushroom;
    public static int totalMove;
    public static int level = 0;
    public static String[] levels = {"src/level1.txt", "src/level2.txt", "src/level3.txt", "src/level4.txt", "src/level5.txt"};
    public static gameStage lvlStage;
    public static MediaPlayer mainSound;
    public static MediaPlayer buttonSound;
    public static Stage mainStage;
    
    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        // Main scene //
        try {
            mainSound = new MediaPlayer(new Media(this.getClass().getResource("musics/mainScene.mp3").toExternalForm()));
            mainSound.setVolume(0.2);
            mainSound.play();

            buttonSound = new MediaPlayer(new Media(this.getClass().getResource("musics/menuNavigate.wav").toExternalForm()));
            buttonSound.setVolume(0.2);

        } catch (Exception e) {
            System.out.println("error");
        }
        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, 1138, 480);
        
        Image bgImage = new Image("images/bg.gif");
        ImageView background = new ImageView(bgImage);

        VBox vbox = new VBox();

        Image btnImage = new Image("images/start.png");
        ImageView startButton = new ImageView(btnImage);
        startButton.setFitHeight(75);
        startButton.setFitWidth(300);

        Image btnImage2 = new Image("images/settings.png");
        ImageView button2 = new ImageView(btnImage2);
        button2.setFitHeight(75);
        button2.setFitWidth(300);

        HBox bottomHBox = new HBox();
        HBox leaderboardHbox = new HBox();
        Image leaderImg = new Image("images/cup.png");
        ImageView leaderBtn = new ImageView(leaderImg);
        leaderBtn.setFitHeight(75);
        leaderBtn.setFitWidth(75);
        leaderboardHbox.getChildren().add(leaderBtn);
        
        Image creditsImg = new Image("images/credits.png");
        ImageView creditsBtn = new ImageView(creditsImg);
        creditsBtn.setFitHeight(75);
        creditsBtn.setFitWidth(300);
        
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPadding(new Insets(0,10,5,0));
        bottomHBox.getChildren().addAll(leaderboardHbox);
        
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.getChildren().addAll(startButton, button2, creditsBtn, bottomHBox);

        root.getChildren().addAll(background, vbox);

        primaryStage.setTitle("Game Project");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        // Main scene end //

        // start game //
        lvlStage = new gameStage();
        
        //button actions
        startButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(1000));
            fade.setNode(root);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(ef -> {
                primaryStage.close();
                lvlStage.show();
                mainSound.stop();
                lvlStage.startMusic();
                root.setOpacity(1);
            });
        });

        button2.setOnMouseClicked(e -> {
            main.buttonPlay();
            settingsStage settings = new settingsStage();
            settings.show();
        });
        
        creditsBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            creditsStage credits = new creditsStage();
            credits.show();
            primaryStage.close();
        });
        
        
        leaderBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.show();
            primaryStage.close();
        });
    }

    public static void buttonPlay() {
        main.buttonSound.stop();
        main.buttonSound.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void readInput(String file) {

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
