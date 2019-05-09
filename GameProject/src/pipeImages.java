
import javafx.animation.PathTransition;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

class pipeImages extends ImageView {

    public String type;
    public String direction;
    public int initial_index_X, initial_index_Y, final_index_X, final_index_Y;
    public boolean isLevelFinished;
    public Path path = new Path();
    public int pathIndex;
    public static PathTransition pt;
    
    //no arg cons ekle
    pipeImages() {

    }

    pipeImages(Image image, String type, String direction) {
        super(image);
        this.type = type;
        this.direction = direction;
        setOnMousePressed(e -> {
            gameStage.scene.setCursor(Cursor.CLOSED_HAND);
            initial_index_X = (int) (e.getSceneY() / 100);
            initial_index_Y = (int) (e.getSceneX() / 100);

        });
        
        setOnMouseDragged(e -> {
            if(isLevelFinished == false)
                if (!this.direction.equals("PipeStatic")
                    && !this.direction.equals("Starter")
                    && !this.direction.equals("End")
                    && !this.type.equals("Free")) {

                this.setX(e.getX()-this.getFitWidth()/2);
                this.setY(e.getY()-this.getFitHeight()/2);
                this.toFront();
            }
            
        });

        setOnMouseReleased(e -> {
            
            gameStage.scene.setCursor(Cursor.OPEN_HAND);
            final_index_X = (int) (e.getSceneY() / 100);
            final_index_Y = (int) (e.getSceneX() / 100);
            if (final_index_X < 4 && final_index_Y < 4 && !(final_index_X < 0) && !(final_index_Y < 0)
                    && canMove(initial_index_X, initial_index_Y, final_index_X, final_index_Y) && !isFinished()) {

                //Kordinatlarının yer değiştirmesi
                main.images[initial_index_X][initial_index_Y].setX(main.images[final_index_X][final_index_Y].getX());
                main.images[initial_index_X][initial_index_Y].setY(main.images[final_index_X][final_index_Y].getY());
                main.images[final_index_X][final_index_Y].setX(initial_index_Y*100);
                main.images[final_index_X][final_index_Y].setY(initial_index_X * 100);
                
                switchSound();
                //Array içinde de değişiklik yapmamız gerekiyor
                pipeImages temp = main.images[initial_index_X][initial_index_Y];
                main.images[initial_index_X][initial_index_Y] = main.images[final_index_X][final_index_Y];
                main.images[final_index_X][final_index_Y] = temp;

                main.totalMove++;
                gameStage.moveInLevel++;

                path = new Path(); //bir üstteki if içinde kontrol yaparken yanlışlıkla path'i de çiziyoruz. Bu yüzden alttaki if i çalıştırdığında path'i 2. kez çiziyor.
                //Bunu önlemek için path'i yeniliyoruz burada.

                if (isFinished()) {

                    pt = new PathTransition();
                    pt.setDuration(Duration.millis(4000));
                    pt.setPath(path);
                    pt.setNode(main.mushroom);
                    main.mushroom.toFront();
                    pt.setAutoReverse(false);
                    pt.play();
                    pt.setOnFinished(eh -> {
                        if (main.level < main.levels.length - 1) {
                            nextLevelStage nextLvl = new nextLevelStage();
                            nextLvl.show();
                        } else {
                            endGame end = new endGame();
                            end.show();
                        }
                    });
                }
            } else {
                wrongMove();
                this.setX(initial_index_Y*100);
                this.setY(initial_index_X*100);
            }
        });
    }

    //Yaptığımız hareketin mümkün olup olmadığının kontrolü
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
        int initialAsNumber = initial_X * 10 + initial_Y;
        int finalAsNumber = final_X * 10 + final_Y;

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

    public boolean isFinished() {

        int x = (whereIsStarter().yProperty().intValue()) / 100;
        int y = (whereIsStarter().xProperty().intValue()) / 100;

        if (main.images[x][y].type.equals("Vertical")) {
            path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
            previousMove = "toDown";
            checkNext(x + 1, y);
        } else if (main.images[x][y].type.equals("Horizontal")) {
            path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
            previousMove = "toLeft";
            checkNext(x, y - 1);
        }
        if (isLevelFinished) {
            return true;
        } else {
            return false;
        }
    }

    public pipeImages whereIsStarter() {
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

    String previousMove = "toDown";

    public void checkNext(int x, int y) {
        //BU METHOD AYNI ZAMANDA PATH DE ÇİZİYOR.

        //if(x < 4 && x >= 0 && y < 4 && y < 4) ekle sonradan array dışına çıkmış mı çıkmamış mı diye
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
            isLevelFinished = true;
        }
    }

    public void switchSound() {
        gameStage.switchEffect.stop();
        gameStage.switchEffect.play();
    }

    public void wrongMove() {
        gameStage.wrongMove.stop();
        gameStage.wrongMove.play();
    }
}