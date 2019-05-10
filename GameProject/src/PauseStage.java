
import javafx.animation.Animation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class PauseStage extends Stage {

    public PauseStage() {
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        //paused mario gif
        Image bg = new Image("images/pause.gif");
        ImageView pauseBg = new ImageView(bg);
        pauseBg.setFitHeight(475);
        pauseBg.setFitWidth(660);

        VBox vbox = new VBox();
        
        StackPane closePane = new StackPane();
        
        //close button
        Image close = new Image("images/closeButton.png");
        ImageView closeButton = new ImageView(close);
        closeButton.setFitHeight(25);
        closeButton.setFitWidth(25);
        
        closePane.getChildren().add(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(60, 50, 0, 0));

        HBox hbox = new HBox();
        
        //main menu button
        Image homeImage = new Image("images/mainMenu.png");
        ImageView homeBtn = new ImageView(homeImage);
        homeBtn.setFitHeight(75);
        homeBtn.setFitWidth(250);
        //settings button
        Image settingsImage = new Image("images/settings-2.png");
        ImageView settingsBtn = new ImageView(settingsImage);
        settingsBtn.setFitHeight(75);
        settingsBtn.setFitWidth(250);
        
        hbox.setSpacing(20);
        hbox.getChildren().addAll(homeBtn, settingsBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(200);
        vbox.getChildren().addAll(closePane, hbox);

        pane.getChildren().addAll(pauseBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);//remove stage's background and window

        //close button action
        closeButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });
        //settings button action
        settingsBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            SettingsStage settings = new SettingsStage();
            settings.show();
        });
        //home button action
        homeBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            if(PipeImages.pt != null){
                PipeImages.pt.stop();
            }
            main.level = 0;
            main.totalMove = 0;
            GameStage.moveInLevel = 0;
            main.lvlStage.stopMusic();
            main.mainSound.play();
            main.lvlStage.close();
            this.close();
            main.lvlStage = new GameStage();
            main.mainStage.show();

        });
        //when press ESC, close the screen.
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                this.close();
            }
        });
    }
}