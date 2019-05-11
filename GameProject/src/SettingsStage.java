
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//Bilgehan Geçici 150117072
//Anıl Şenay 150117023

//This class creates a screen on the game for some volume settings

class SettingsStage extends Stage { //SettingsStage class

    public SettingsStage() { // Constructor

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/settingsWindow.png"); // Adding settings Window image 

        // Set properties of the image 
        ImageView pauseBg = new ImageView(bg);
        pauseBg.setFitHeight(475);
        pauseBg.setFitWidth(660);

        VBox vbox = new VBox();

        StackPane closePane = new StackPane();
        Image close = new Image("images/closeButton.png"); // Adding close button image

        // Set properties of the image 
        ImageView closeButton = new ImageView(close);
        closeButton.setFitHeight(25);
        closeButton.setFitWidth(25);
        closePane.getChildren().add(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(60, 50, 0, 0));

        VBox volumes = new VBox();

        // MAIN MUSIC SLIDER START //
        HBox volumeHbox = new HBox();

        // Adding music slider pattern
        Slider musicVol = new Slider();

        //Setting music slider properties 
        musicVol.setPrefWidth(250);
        musicVol.setMaxWidth(Region.USE_PREF_SIZE);
        musicVol.setMinWidth(30);
        musicVol.setValue(main.mainSound.getVolume() * 200);
        main.mainSound.volumeProperty().bind(musicVol.valueProperty().divide(200));
        GameStage.levelSound.volumeProperty().bind(musicVol.valueProperty().divide(200));
        volumeHbox.setAlignment(Pos.CENTER);

        //Adding label for music volume
        Label music = new Label("Music volume: ");
        music.setFont(new Font("Arial", 20));
        music.setTextFill(Color.web("#ffffff"));
        music.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        volumeHbox.getChildren().addAll(music, musicVol);
        // MAIN MUSIC SLIDER END //

        // EFFECTS SLIDER START //
        HBox effectsHbox = new HBox();
        Slider effectsSlider = new Slider();
        effectsSlider.setPrefWidth(250);
        effectsSlider.setMaxWidth(Region.USE_PREF_SIZE);
        effectsSlider.setMinWidth(30);
        effectsSlider.setValue(GameStage.switchEffect.getVolume() * 100);
        GameStage.switchEffect.volumeProperty().bind(effectsSlider.valueProperty().divide(100));
        GameStage.wrongMove.volumeProperty().bind(effectsSlider.valueProperty().divide(100));
        effectsHbox.setAlignment(Pos.CENTER);

        Label effects = new Label("Effects volume: ");
        effects.setFont(new Font("Arial", 20));
        effects.setTextFill(Color.web("#ffffff"));
        effects.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        effectsHbox.getChildren().addAll(effects, effectsSlider);
        // EFFECTS SLIDER END //

        volumes.setPadding(new Insets(80, 0, 0, 0));
        volumes.setSpacing(20);
        volumes.getChildren().addAll(volumeHbox, effectsHbox);

        HBox hbox = new HBox();
        Image homeImage = new Image("images/goback.png"); //Adding goback button 
        ImageView goBackBtn = new ImageView(homeImage);
        goBackBtn.setFitHeight(75);
        goBackBtn.setFitWidth(250);

        hbox.setSpacing(20);
        hbox.getChildren().addAll(goBackBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(40);
        vbox.getChildren().addAll(closePane, volumes, hbox);

        pane.getChildren().addAll(pauseBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        closeButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });

        goBackBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                this.close();
            }
        });
    }
}
