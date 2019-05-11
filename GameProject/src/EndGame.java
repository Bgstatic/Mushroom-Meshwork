
import java.io.FileWriter;
import java.io.PrintWriter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//Bilgehan Geçici 150117072
//Anıl Şenay 150117023

//This class creates a screen at the end of the game and it prints the total number of moves done by user and it has a name input section for the leader board section.

class EndGame extends Stage {

    EndGame() {

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/endOfGame.png");
        ImageView endOfGame = new ImageView(bg);

        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Image continueImg = new Image("images/continue.png");
        ImageView continueBtn = new ImageView(continueImg);
        continueBtn.setFitHeight(75);
        continueBtn.setFitWidth(250);

        Label nick = new Label("Nickname: ");
        nick.setFont(new Font("Arial", 20));
        nick.setTextFill(Color.web("#ffffff"));
        nick.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        TextField text = new TextField();

        hbox.setSpacing(20);
        hbox.getChildren().addAll(nick, text);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        Label moveText = new Label("You made : " + GameStage.moveInLevel + " moves." + "(Total:" + main.totalMove + ")");
        moveText.setFont(new Font("Arial", 22));
        moveText.setTextFill(Color.web("#ffffff"));
        moveText.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");

        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(18);
        vbox.setPadding(new Insets(30, 30, 60, 30));
        vbox.getChildren().addAll(moveText, hbox, continueBtn);

        pane.getChildren().addAll(endOfGame, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        continueBtn.setOnMouseClicked(e -> {
            writeLeaderBoard(text.getText(), main.totalMove);
            CreditsStage credits = new CreditsStage();
            credits.show();
            this.close();
        });
    }

    public void writeLeaderBoard(String nick, int score) {
        try {
            PrintWriter file = new PrintWriter(new FileWriter("src/leaderboard.txt", true));

            file.println(score + " " + nick);

            file.close();
        } catch (Exception e) {

        }
    }
}
