
import javafx.animation.PathTransition;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

class PipeImages extends ImageView {

    public String type;
    public String direction;
    public int initial_index_X, initial_index_Y, final_index_X, final_index_Y;
    public boolean isLevelFinished;
    public Path path = new Path();
    public static PathTransition pt;
    public String previousMove = "toDown";
    
    PipeImages() {

    }

    PipeImages(Image image, String type, String direction) {
        super(image); // send the image to it's super class ImageView.
        this.type = type;
        this.direction = direction;
        
        //mouse pressed action
        setOnMousePressed(e -> {
            GameStage.scene.setCursor(Cursor.CLOSED_HAND);
            
            //get the initial X and Y porperties and devided by 100 to find image's indexes in array.
            initial_index_X = (int) (e.getSceneY() / 100);
            initial_index_Y = (int) (e.getSceneX() / 100);

        });
        
        //mouse dragged action
        setOnMouseDragged(e -> {
            //if level is not finished yet and it is movable pipe then set it's X and Y properties based on mouse's coordinates.  
            if(isLevelFinished == false)
                if (!this.direction.equals("PipeStatic")
                    && !this.direction.equals("Starter")
                    && !this.direction.equals("End")
                    && !this.type.equals("Free")) {

                this.setX(e.getX()-this.getFitWidth()/2);
                this.setY(e.getY()-this.getFitHeight()/2);
                this.toFront(); //after drag the image, set it top of all images.
            }
            
        });

        //mouse release action
        setOnMouseReleased(e -> {
            
            GameStage.scene.setCursor(Cursor.OPEN_HAND);
            //getting second image's indexes in array from it's coordinates.
            final_index_X = (int) (e.getSceneY() / 100); 
            final_index_Y = (int) (e.getSceneX() / 100);
            
            //to control if second coordinates is not out of board(if user did not drag it to out of stage)
            //to control if initial and final images can swicth with canMove method.
            //to control if level finished or not with isFinished method.
            if (final_index_X < 4 && final_index_Y < 4 && !(final_index_X < 0) && !(final_index_Y < 0)
                    && canMove(initial_index_X, initial_index_Y, final_index_X, final_index_Y) && !isFinished()) {

                //Change coordinates of 2 images
                main.images[initial_index_X][initial_index_Y].setX(main.images[final_index_X][final_index_Y].getX());
                main.images[initial_index_X][initial_index_Y].setY(main.images[final_index_X][final_index_Y].getY());
                main.images[final_index_X][final_index_Y].setX(initial_index_Y*100);
                main.images[final_index_X][final_index_Y].setY(initial_index_X * 100);
                
                switchSound(); // switch pipes sound effect
                
                //After change images's locations, we have to change their indexes in array either.
                PipeImages temp = main.images[initial_index_X][initial_index_Y];
                main.images[initial_index_X][initial_index_Y] = main.images[final_index_X][final_index_Y];
                main.images[final_index_X][final_index_Y] = temp;

                main.totalMove++; //increament the total move by 1.
                GameStage.moveInLevel++; //increament the move in current level by 1.

                
                path = new Path(); //creates new path.

                //control if level finished or not.
                if (isFinished()) {

                    pt = new PathTransition(); //creating the path transition.
                    pt.setDuration(Duration.millis(4000));
                    pt.setPath(path);
                    pt.setNode(main.mushroom);
                    main.mushroom.toFront(); //set the mushroom top of all images.
                    pt.setAutoReverse(false);
                    pt.play();
                    pt.setOnFinished(eh -> { //after movement, level will be finished and show next level stage.
                        
                        if (main.level < main.levels.length - 1) { //this controls if there any remaining level.
                            NextLevelStage nextLvl = new NextLevelStage(); //is there any remaining level, show next level stage.
                            nextLvl.show();
                        } else { //if there is not remaining level, show end game stage.
                            EndGame end = new EndGame();
                            end.show();
                        }
                    });
                }
            } else {
                wrongMove(); //wrong move sound effect.
                //if pipes can not switch, set first image to it's old positions.
                this.setX(initial_index_Y*100); 
                this.setY(initial_index_X*100);
            }
        });
    }

    //It checks whether the moveable blocks can switch.
    public boolean canMove(int initial_X, int initial_Y, int final_X, int final_Y) {

        if (!main.images[final_X][final_Y].type.equals("Free")) {
            return false;
        }
        if (main.images[initial_X][initial_Y].direction.equals("PipeStatic")
                || main.images[initial_X][initial_Y].direction.equals("Starter")
                || main.images[initial_X][initial_Y].direction.equals("End")) {
            return false;
        }

        //Burada ilginç bir trick yaptım: hareket ettirdiğimiz kutu empty olan kutunun sağ-sol-üst-alt'ında olup olmadığını kontrol etmek için
        // bulundukları konumların sayı karşılığını aldım mesela images[2][3] == 23, images[1][1] == 11; konumlarının indexlerini birleştirip sayı olarak aldım kısacası
        // sonrasında if içindeki işlemler sol-sağ-üst-alt olup olmadığını veriyor.
        
        //convert indexes as integer numbers. (For example: [1][2] == 12, [2][0] == 20)
        int initialAsNumber = initial_X * 10 + initial_Y;
        int finalAsNumber = final_X * 10 + final_Y;
        
        //it limits blocks movement as only 1 block vertical and horizontal but not diagonally.
        if (finalAsNumber - initialAsNumber == 1
                || finalAsNumber - initialAsNumber == 10
                || initialAsNumber - finalAsNumber == 1
                || initialAsNumber - finalAsNumber == 10) {
            return true;

        } else {
            return false;
        }
        //
        //
    }

    //Checks the level is finisihed or not.
    public boolean isFinished() {

        //finds starter's indexes.
        int x = (whereIsStarter().yProperty().intValue()) / 100;
        int y = (whereIsStarter().xProperty().intValue()) / 100;

        //by checking starter's type(vertical or horizontal), it also add its path.
        if (main.images[x][y].type.equals("Vertical")) {
            path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
            previousMove = "toDown";
            checkNext(x + 1, y); //declare the checkNext method recursively.
        } else if (main.images[x][y].type.equals("Horizontal")) {
            path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
            previousMove = "toLeft";
            checkNext(x, y - 1);
        }
        
        //after the recursion if isLevelFinished variable is true, return true for this method.
        if (isLevelFinished) {
            return true; 
        } else {
            return false;
        }
    }
    
    // it finds the location of the starter image than it adds the path to the starter image where it is located
    public PipeImages whereIsStarter() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (main.images[i][j].direction.equals("Starter")) {
                    path.getElements().add(new MoveTo(main.images[i][j].getX() + 50.0f, main.images[i][j].getY() + 50.0f)); // başlangıç noktası
                    return main.images[i][j];
                }
            }
        }
        return null;
    }

    public void checkNext(int x, int y) {
        //
        //Checking the images which located in x. and y. indexes of array until find "End" pipe recursively.
        //First, it controls previousMove's type. Based on previousMove it determine next move.
        //By pipe's type, it updates previousMove.
        //This method also add pathes with pipe's type and  direction.
        if (main.images[x][y].direction.equals("Pipe") && main.images[x][y].type.equals("Vertical")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toDown";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toUp";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY()));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].direction.equals("Pipe") && main.images[x][y].type.equals("Horizontal")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toRight";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 100.0f, main.images[x][y].getY() + 50.0f));
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toLeft";
                path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].type.equals("00")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toLeft";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX(), main.images[x][y].getY() + 50, false, false));
                checkNext(x, y - 1);

            } else if (previousMove.equals("toRight")) {
                previousMove = "toUp";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY(), false, false));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].type.equals("01")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toRight";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 100, main.images[x][y].getY() + 50, false, false));//radiusX, radiusY, xAxisRotation, X, Y
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toUp";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY(), false, false));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].type.equals("11")) {
            if (previousMove.equals("toLeft")) {
                previousMove = "toDown";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 100, main.images[x][y].getY() + 50, false, false));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toRight";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY() + 100, false, false));
                checkNext(x, y + 1);
            }
        }
        if (main.images[x][y].type.equals("10")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toDown";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY() + 100, false, false));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toLeft";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX(), main.images[x][y].getY() + 50, false, false));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].direction.equals("PipeStatic") && main.images[x][y].type.equals("Vertical")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toDown";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toUp";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY()));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].direction.equals("PipeStatic") && main.images[x][y].type.equals("Horizontal")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toRight";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 100.0f, main.images[x][y].getY() + 50.0f));
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toLeft";
                path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].direction.equals("End")) {
            if (main.images[x][y].type.equals("Horizontal")) {
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 50.0f));
            } else {
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 50.0f));
            }
            isLevelFinished = true; // if next checkNext method find "End" so level complated successfuly and isLevelFinished become true.
        }
    }
    
    // It plays the swapping sound effect
    public void switchSound() {
        GameStage.switchEffect.stop();
        GameStage.switchEffect.play();
    }
    // It plays the wrong move sound effect
    public void wrongMove() {
        GameStage.wrongMove.stop();
        GameStage.wrongMove.play();
    }
}