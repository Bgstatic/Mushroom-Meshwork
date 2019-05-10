
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


class GameStage extends Stage {

    public static ImagePane firstLevel;
    public static int moveInLevel;
    public static MediaPlayer levelSound;
    public static MediaPlayer switchEffect;
    public static MediaPlayer wrongMove;
    public static Scene scene;
    GameStage() {

        firstLevel = new ImagePane(); // Creating new Image pane. 
	scene = new Scene(firstLevel, 400, 400); // Set the properties of the scene.
	this.setScene(scene);
        this.setTitle("Level " + (main.level + 1)); // Set title based on given level number
	main.readInput(main.levels[main.level]);  //Crate the game board with readInput method based on given file
	firstLevel.print(); // adding images to board.
        scene.setCursor(Cursor.OPEN_HAND);
        
        //adding musics and sound effects.
        try {
            levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
            wrongMove = new MediaPlayer(new Media(this.getClass().getResource("musics/wrongMove.wav").toExternalForm()));
            switchEffect = new MediaPlayer(new Media(this.getClass().getResource("musics/switch.wav").toExternalForm()));
            levelSound.setVolume(main.mainSound.getVolume());
        } catch (Exception e) {
            System.out.println("error");
        }
        //To show pause menu after click ESC.
        scene.setOnKeyPressed(e -> { 
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                PauseStage pause = new PauseStage();
                pause.show();
            }
        });
    }
    //Change Stage's title with level information. (Level 1, Level 2, ..)
    public void changeTitle() {
        this.setTitle("Level " + (main.level + 1));
    }
    //start level music method.
    public void startMusic() {
        levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
        levelSound.setVolume(main.mainSound.getVolume());
        levelSound.play();
    }
    //to stop level music.
    public void stopMusic() {
        levelSound.stop();
    }
}