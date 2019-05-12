
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

//Bilgehan Geçici 150117072
//Anıl Şenay 150117023

//This class creates a leader board screen on the game. It keeps the number of moves and user's name and it sorts the total number of moves in descending order

class LeaderBoard extends Stage {
    String scoresText = "";
    String nicksText = "";
    
    public LeaderBoard() {

        sortLeaderBoard(); //to update leaderboard sort it again.
        
        StackPane root = new StackPane();
        Scene leaderboardScene = new Scene(root, 1138, 480);
        Image backgroundImg = new Image("images/bg.gif");
        ImageView background = new ImageView(backgroundImg);
        
        //Go back button to go back main menu.
        Image homeImage = new Image("images/goback.png");
        ImageView homeBtn = new ImageView(homeImage);
        homeBtn.setFitHeight(75);
        homeBtn.setFitWidth(250);
        
        VBox mainVBox = new VBox();
        HBox hbox = new HBox();
        VBox scores = new VBox();
        VBox nicks = new VBox();
        
        //"Nickname" title label
        Label nickname = new Label("Nickname\n\n");
        nickname.setFont(new Font("Arial", 24));
        nickname.setUnderline(true);
        nickname.setTextFill(Color.web("#ffffff"));
        nickname.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        //"Score" title label
        Label score = new Label("Score\n\n");
        score.setFont(new Font("Arial", 24));
        score.setUnderline(true);
        score.setTextFill(Color.web("#ffffff"));
        score.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        //scores from scoresText variable
        Label scoreList = new Label(scoresText);
        scoreList.setFont(new Font("Arial", 20));
        scoreList.setTextFill(Color.web("#ffffff"));
        scoreList.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0.8,0,0)");
        
        //nicks from nicksText variable
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
        
        this.setScene(leaderboardScene);
        this.setResizable(false);
        this.sizeToScene(); // to fit scene to stage
        //when click home button it shows main menu.
        homeBtn.setOnMouseClicked(e -> {
            this.close();
            main.mainStage.show();
            
        });
    }
    
    //sort the leaderboard.txt 
    private void sortLeaderBoard() {
        ArrayList<String> leaderboard = new ArrayList<>();

        Scanner sc = null;
        try {
            File leaderboardTxt = new File("leaderboard.txt");
            if(!leaderboardTxt.exists()){
            	leaderboardTxt = new File("src/leaderboard.txt");
            }
            sc = new Scanner(leaderboardTxt);
        } catch (Exception e) {
            System.out.println("leaderboard.txt could not loaded. Please fix your txt file location");
        }
        while (sc.hasNextLine()) {
            leaderboard.add(sc.nextLine());
        }

        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<String> nicks = new ArrayList<>();
        
        for (int i = 0; i < leaderboard.size(); i++) {
            scores.add(Integer.parseInt(leaderboard.get(i).split(" ")[0])); //add scores to scores array list from leadeboard.
        }

        Collections.sort(scores); //sorting array.
        
        //add nicks the correct position as same as their scores of nicks array.
        for (int i = 0; i < leaderboard.size(); i++) {
            for (int j = 0; j < leaderboard.size(); j++) {
                if (Integer.parseInt(leaderboard.get(j).split(" ")[0]) == scores.get(i)) {
                    if (!nicks.contains(leaderboard.get(j).split(" ")[1])) {
                        nicks.add(leaderboard.get(j).split(" ")[1]);
                    }
                }
            }
        }
        
        //add scores and nick to scoreText and nicksText variables.
        for(int i = 0; i < scores.size(); i++){
            scoresText += scores.get(i) + "\n";
            nicksText += (i+1) + ". " + nicks.get(i) + "\n";
        }
    }
}
