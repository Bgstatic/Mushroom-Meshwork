
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


class gameStage extends Stage {

    public static ImagePane firstLevel;
    public static int moveInLevel;
    public static MediaPlayer levelSound;
    public static MediaPlayer switchEffect;
    public static MediaPlayer wrongMove;
    public static Scene scene;
    gameStage() {

        firstLevel = new ImagePane();
        scene = new Scene(firstLevel, 400, 400);
        this.setScene(scene);
        this.setTitle("Level " + (main.level + 1));
        main.readInput(main.levels[main.level]);
        firstLevel.print();
        scene.setCursor(Cursor.OPEN_HAND);
        
        try {
            levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
            wrongMove = new MediaPlayer(new Media(this.getClass().getResource("musics/wrongMove.wav").toExternalForm()));
            switchEffect = new MediaPlayer(new Media(this.getClass().getResource("musics/switch.wav").toExternalForm()));
            levelSound.setVolume(main.mainSound.getVolume());
        } catch (Exception e) {
            System.out.println("error");
        }
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                pauseStage pause = new pauseStage();
                pause.show();
            }
        });
    }

    public void changeTitle() {
        this.setTitle("Level " + (main.level + 1));
    }

    public void startMusic() {
        levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
        levelSound.setVolume(main.mainSound.getVolume());
        levelSound.play();
    }

    public void stopMusic() {
        levelSound.stop();
    }
}