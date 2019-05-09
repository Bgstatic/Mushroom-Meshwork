
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class LeaderBoard extends Stage {
    String scoresText = "";
    String nicksText = "";
    
    public LeaderBoard() {

        sortLeaderBoard();
        StackPane root = new StackPane();
        Scene creditsScene = new Scene(root, 1138, 480);
        Image backgroundImg = new Image("images/bg.gif");
        ImageView background = new ImageView(backgroundImg);
        
        Image homeImage = new Image("images/goback.png");
        ImageView homeBtn = new ImageView(homeImage);
        homeBtn.setFitHeight(75);
        homeBtn.setFitWidth(250);
        
        VBox mainVBox = new VBox();
        HBox hbox = new HBox();
        VBox scores = new VBox();
        VBox nicks = new VBox();
        
        Label nickname = new Label("Nickname\n\n");
        nickname.setFont(new Font("Arial", 24));
        nickname.setUnderline(true);
        nickname.setTextFill(Color.web("#ffffff"));
        nickname.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        Label score = new Label("Score\n\n");
        score.setFont(new Font("Arial", 24));
        score.setUnderline(true);
        score.setTextFill(Color.web("#ffffff"));
        score.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        Label scoreList = new Label(scoresText);
        scoreList.setFont(new Font("Arial", 20));
        scoreList.setTextFill(Color.web("#ffffff"));
        scoreList.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0.8,0,0)");
        
        Label nickList = new Label(nicksText);
        nickList.setFont(new Font("Arial", 20));
        nickList.setTextFill(Color.web("#ffffff"));
        nickList.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0.8,0,0)");
        
        scores.setAlignment(Pos.TOP_CENTER);
        nicks.setAlignment(Pos.TOP_CENTER);
        hbox.setAlignment(Pos.TOP_CENTER);
        mainVBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        hbox.setSpacing(30);
        
        scores.getChildren().addAll(score, scoreList);
        nicks.getChildren().addAll(nickname, nickList);
        hbox.getChildren().addAll(nicks, scores);
        mainVBox.getChildren().addAll(hbox, homeBtn);
        root.getChildren().addAll(background, mainVBox);
        
        this.setScene(creditsScene);
        
        homeBtn.setOnMouseClicked(e -> {
            this.close();
            main.mainStage.show();
            
        });
    }

    private void sortLeaderBoard() {
        ArrayList<String> leaderboard = new ArrayList<>();

        Scanner sc = null;
        try {
            sc = new Scanner(new File("src/leaderboard.txt"));
        } catch (Exception e) {

        }
        while (sc.hasNextLine()) {
            leaderboard.add(sc.nextLine());
        }

        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<String> nicks = new ArrayList<>();
        
        for (int i = 0; i < leaderboard.size(); i++) {
            scores.add(Integer.parseInt(leaderboard.get(i).split(" ")[0]));
        }

        Collections.sort(scores);

        for (int i = 0; i < leaderboard.size(); i++) {
            for (int j = 0; j < leaderboard.size(); j++) {
                if (Integer.parseInt(leaderboard.get(j).split(" ")[0]) == scores.get(i)) {
                    if (!nicks.contains(leaderboard.get(j).split(" ")[1])) {
                        nicks.add(leaderboard.get(j).split(" ")[1]);
                    }
                }
            }
        }
        
        for(int i = 0; i < scores.size(); i++){
            scoresText += scores.get(i) + "\n";
            nicksText += (i+1) + ". " + nicks.get(i) + "\n";
        }
    }
}
