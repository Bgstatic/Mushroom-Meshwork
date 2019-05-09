
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


class creditsStage extends Stage {

    public static Timeline animation;
    public static Scene creditsScene;
    public static ImageView credits_text;
    creditsStage() {

        Pane root = new Pane();
        creditsScene = new Scene(root, 1138, 480);
        Image backgroundImg = new Image("images/bg.gif");
        ImageView background = new ImageView(backgroundImg);
        Image creditsImage = new Image("images/credits_text.png");
        credits_text = new ImageView(creditsImage);
        credits_text.setY(creditsScene.getHeight());

        animation = new Timeline(
                new KeyFrame(Duration.millis(20), e -> {
                    if (credits_text.getY() == -creditsImage.getHeight()) {
                        animation.stop();

                        finished();
                    }
                    credits_text.setY(credits_text.getY() - 1);
                }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        animation.setOnFinished(e -> {
            credits_text.setY(creditsScene.getHeight());
        });

        root.getChildren().addAll(background, credits_text);
        this.setScene(creditsScene);

        creditsScene.setOnKeyPressed(e -> {
            animation.stop();
            credits_text.setY(creditsScene.getHeight());
            finished();
        });
    }

    private void finished() {

        main.level = 0;
        main.totalMove = 0;
        gameStage.moveInLevel = 0;
        main.lvlStage.close();
        this.close();

        Stage backMain = new Stage();
        StackPane stackPane = new StackPane();

        VBox vbox = new VBox();

        Label text = new Label("Do you want to go back main menu?");
        Button yes = new Button("Yes");
        Button no = new Button("No");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(yes, no);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(text, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);
        stackPane.getChildren().add(vbox);
        Scene backToMain = new Scene(stackPane, 300, 200);
        backMain.setScene(backToMain);

        yes.setOnAction(e -> {
            animation.stop();
            credits_text.setY(creditsScene.getHeight());
            backMain.close();
            main.lvlStage = new gameStage();
            main.mainStage.show();
        });
        no.setOnAction(e -> {
            System.exit(0);
        });

        backMain.show();
    }
}