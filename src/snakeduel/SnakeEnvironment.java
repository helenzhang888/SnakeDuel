/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeduel;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.Grid;
import image.ResourceTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author zhanglianghui
 */
class SnakeEnvironment extends Environment {
    private GameState gameState = GameState.STARTING;

    private Grid grid;
    private int score = 0;
    private Snake snake;
    private ArrayList<Point> apples;
    private ArrayList<Point> poisonBottles;
    private Point letters;

    //counter for snake
    private int delay = 4;
    private int moveCounter = delay;

    //counter for special object
    private int disappear = 1000;
    private int appear = 150;
    private int objectCounterDisappear = disappear;
    private int objectCounterAppear = appear;

    //level ups
    private final int SPEEDLEVELONE = 50;
    private final int SPEEDLEVELTWO = 100;
    private final int SPEEDLEVELTHREE = 300;
    private final int LEVELTWO = 500;
    private final int LEVELTHREE = 1000;

    //life
    private int life = 5;

    //instructions
    private int instructionCounter = 100;
    private String instruction = "Using arrow keys or w, a, s, d to control the direction. ";

    private Image letter;
    
    private Image starting;
    private Image ended;
    
    private Image heart;

    public SnakeEnvironment() {

    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(ResourceTools.loadImageFromResource("resources/background_snow.jpg"));

        this.letter = ResourceTools.loadImageFromResource("resources/diamond.jpg");
        
        this.heart = ResourceTools.loadImageFromResource("resources/heart.png");
        
        this.starting = ResourceTools.loadImageFromResource("resources/starting.jpg");
        this.ended = ResourceTools.loadImageFromResource("resources/ended.jpg");
        
//        this.logo = ResourceTools.loadImageFromResource("resources/logo.jpg");
        
        AudioPlayer.play("/resources/luv_letter.wav", AudioPlayer.LOOP_INFINITE);
        

        this.grid = new Grid();
        this.grid.setPosition(new Point(50, 100));
        this.grid.setColor(new Color(150, 150, 250));
        this.grid.setCellHeight(22);
        this.grid.setCellWidth(22);
        this.grid.setColumns(57);
        this.grid.setRows(26);

        this.apples = new ArrayList<Point>();
        this.poisonBottles = new ArrayList<Point>();

        this.poisonBottles.add(getRandomGridLocation());
        this.poisonBottles.add(getRandomGridLocation());

        this.apples.add(getRandomGridLocation());
        this.apples.add(getRandomGridLocation());
        this.apples.add(getRandomGridLocation());
        this.apples.add(getRandomGridLocation());


        this.letters = new Point(-1000, -1000);

        this.snake = new Snake();
        this.snake.getBody().add(new Point(5, 5));
        this.snake.getBody().add(new Point(5, 4));
        this.snake.getBody().add(new Point(5, 3));
        this.snake.getBody().add(new Point(4, 3));
    }

    private Point getRandomGridLocation() {
//        return new Point((int) (Math.random() * this.grid.getColumns()), (int) (Math.random() * this.grid.getRows()));
        boolean collision = true;
        Point myPoint = new Point();
        do {
               myPoint.setLocation(new Point((int) (Math.random() * this.grid.getColumns()), (int) (Math.random() * this.grid.getRows())));
               collision = checkIntersect(myPoint, this.poisonBottles)  || checkIntersect(myPoint, this.apples);
        } while (collision);
        return myPoint;
    }

    private boolean checkIntersect(Point location, ArrayList<Point> locations) {
        for (Point point : locations) {
            if (location.equals(point)) {
                return true;
            }
        }
        return false;
    }
    
    
    private void checkAppleHeadIntersect() {
        for (int i = 0; i < this.apples.size(); i++) {
            if (this.snake.getHead().equals(this.apples.get(i))) {
//                System.out.println("HITTTTTTTTTTTTTTTTTTTTTTTTT");
                //sound
                AudioPlayer.play("/resources/eat_apple_sound.wav");
                //snake grow
                snake.setGrowthCounter(1);
                //add points
                this.setScore(this.getScore() + 10);
                //set new apples
                this.apples.get(i).x = getRandomGridLocation().x;
                this.apples.get(i).y = getRandomGridLocation().y;
//                this.apples.remove(i);
//                this.apples.add(new Point (((int)(Math.random() * this.grid.getColumns())),((int)(Math.random() * this.grid.getRows()))));
                this.poisonBottles.add(getRandomGridLocation());
            }
        }
    }

    private void checkBottlesHeadIntersect() {
        for (int i = 0; i < this.poisonBottles.size(); i++) {
            if (this.snake.getHead().equals(this.poisonBottles.get(i))) {
                //sound
                AudioPlayer.play("/resources/glass_break_sound.wav");
                this.setScore(this.getScore() - 10);
                this.setLife(this.getLife() - 1);
                this.poisonBottles.get(i).x = getRandomGridLocation().x;
                this.poisonBottles.get(i).y = getRandomGridLocation().y;

            }
        }
    }

    private void checkLettersHeadIntersect() {

        if (this.snake.getHead().equals(this.letters)) {
            this.setScore(this.getScore() + 100);
            this.letters.move(-1000, -1000);
            objectCounterAppear = appear;
            AudioPlayer.play("/resources/rocket_sound.wav");
            for (int j = this.poisonBottles.size() - 5; j < this.poisonBottles.size(); j++) {
                this.poisonBottles.get(j).x = -1000;
                this.poisonBottles.get(j).y = -1000;
            }
        }

    }

    @Override
    public void timerTaskHandler() {
        if (this.gameState == GameState.RUNNING) {

            if (snake != null) {
                if (moveCounter <= 0) {
                    snake.move();
                    moveCounter = delay;
                    checkAppleHeadIntersect();
                    checkBottlesHeadIntersect();
                    checkLettersHeadIntersect();
                } else {
                    moveCounter--;
                }
            }

            if (letters != null) {
                if (this.letters.equals(new Point(-1000, -1000))) {
                    objectCounterDisappear--;
                } else {
                    objectCounterAppear--;
                }

                if (objectCounterDisappear <= 0) {
                    this.letters.move(getRandomGridLocation().x, getRandomGridLocation().y);
                    AudioPlayer.play("/resources/bell_sound.wav");
                    objectCounterDisappear = disappear;
                }

                if (objectCounterAppear <= 0) {
                    this.letters.move(-1000, -1000);
                    objectCounterAppear = appear;
                }
            }

            if (instructionCounter == 0) {
                this.instruction = "Collect apples and avoid poison bottles.";
                instructionCounter = 201;
            } else if (instructionCounter == 101) {
                this.instruction = "The snake will speed up as the game progress.";
                instructionCounter = 302;
            } else if (instructionCounter == 202) {
                this.instruction = "Catch the diamond to reduce the number of poison bottles.";
                instructionCounter = 403;
            } else if (instructionCounter == 303) {
                this.instruction = "Good luck <3";
                instructionCounter = 504;
            } else if (instructionCounter == 404) {
                this.instruction = "";
                instructionCounter = -1;
            } else if (instructionCounter == -1) {
                this.instruction = "";
            } else {
                instructionCounter--;
            }

            if (snake.getHead().x < 0) {
                snake.getHead().x = grid.getColumns() - 1;
            } else if (snake.getHead().y < 0) {
                snake.getHead().y = grid.getRows() - 1;
            } else if (snake.getHead().x > grid.getColumns() - 1) {
                snake.getHead().x = 0;
            } else if (snake.getHead().y > grid.getRows() - 1) {
                snake.getHead().y = 0;
            }
        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            //toggle the PAUSED/RUNNING state
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.RUNNING;
            } else if (gameState == GameState.LEVELUP) {
                gameState = GameState.RUNNING;
            } 
            
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (gameState == GameState.STARTING) {
                gameState = GameState.RUNNING;
            } else if (gameState == GameState.ENDED) {
                gameState = GameState.RUNNING;
                this.setBackground(ResourceTools.loadImageFromResource("resources/background_snow.jpg"));
                this.score = 0;
                this.life = 5;
                this.delay = 4;
                //initialize the snake
                
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            snake.setDirection(Direction.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            snake.setDirection(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            snake.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            snake.setDirection(Direction.DOWN);
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            gameState = GameState.ENDED;
        }
    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {

    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {

    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        if (this.grid != null) {
            this.grid.paintComponent(graphics);

            if (this.letters != null) {
                graphics.drawImage(letter, this.grid.getCellPosition(this.letters).x, this.grid.getCellPosition(this.letters).y, this.grid.getCellWidth(), this.grid.getCellHeight(), this);

            }

            if (this.apples != null) {
                for (int i = 0; i < this.apples.size(); i++) {
//                   System.out.printf("apple at position %d has location x = %d y= %d\n", i,  this.apples.get(i).x, this.apples.get(i).y );
                    GraphicsPalette.drawApple(graphics, this.grid.getCellPosition(this.apples.get(i)), this.grid.getCellSize());
                }
            }

            if (this.poisonBottles != null) {
                for (int i = 0; i < this.poisonBottles.size(); i++) {
                    GraphicsPalette.drawPoisonBottle(graphics, this.grid.getCellPosition(this.poisonBottles.get(i)), this.grid.getCellSize(), Color.green);
                }
            }
            if (this.life == 1) {
                graphics.drawImage(heart, 1240, 40, 60, 70, this);
            } else if (this.life == 2) {
                graphics.drawImage(heart, 1240, 40, 60, 70, this);
                graphics.drawImage(heart, 1225, 40, 60, 70, this);
            } else if (this.life == 3) {
                graphics.drawImage(heart, 1240, 40, 60, 70, this);
                graphics.drawImage(heart, 1225, 40, 60, 70, this);
                graphics.drawImage(heart, 1210, 40, 60, 70, this);
            } else if (this.life == 4) {
                graphics.drawImage(heart, 1240, 40, 60, 70, this);
                graphics.drawImage(heart, 1225, 40, 60, 70, this);
                graphics.drawImage(heart, 1210, 40, 60, 70, this);
                graphics.drawImage(heart, 1195, 40, 60, 70, this);
            } else if (this.life == 5) {
                graphics.drawImage(heart, 1240, 40, 60, 70, this);
                graphics.drawImage(heart, 1225, 40, 60, 70, this);
                graphics.drawImage(heart, 1210, 40, 60, 70, this);
                graphics.drawImage(heart, 1195, 40, 60, 70, this);
                graphics.drawImage(heart, 1180, 40, 60, 70, this);
            }
            
            graphics.setColor(Color.YELLOW);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 40));
            graphics.drawString("life", 1210, 60);


            Point cellLocation;

            if (snake != null) {
                for (int i = 0; i < snake.getBody().size(); i++) {
                    //head
                    if (i == 0) {
                        graphics.setColor(new Color(255, 255, 51, 250));
                        cellLocation = grid.getCellPosition(snake.getBody().get(i));
                        if (snake.getDirection() == Direction.LEFT) {
                            graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 210, 320);
                        } else if (snake.getDirection() == Direction.RIGHT) {
                            graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 20, 320);
                        } else if (snake.getDirection() == Direction.UP) {
                            graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 110, 320);
                        } else {
                            graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 290, 320);
                        }
                        //graphics.fillRect(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());
                    } else {
                        //body
                        graphics.setColor(new Color(255, 255, 51, 250));
                        cellLocation = grid.getCellPosition(snake.getBody().get(i));
                        graphics.fillOval(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());
                        //pattern
                        graphics.setColor(new Color(250, 250, 100, 200));
                        graphics.fillRect(cellLocation.x + grid.getCellWidth() / 4, cellLocation.y + grid.getCellHeight() / 4, grid.getCellWidth() / 2, grid.getCellHeight() / 2);
                    }

                }
            }
        }
        
        graphics.setColor(Color.YELLOW);
//        graphics.setColor(new Color(200, 200, 250));
        graphics.setFont(new Font("Calibri", Font.ITALIC, 60));
        graphics.drawString("Score: " + this.getScore(), 50, 70);

        graphics.setFont(new Font("Calibri", Font.ITALIC, 20));
        graphics.drawString(this.instruction, 480, 70);

        
        if (gameState == GameState.STARTING) {
            graphics.setColor(new Color(200, 200, 250));
            graphics.fillRect(0, 0, 2000, 1000);
            graphics.drawImage(starting, 20,20 , this);
            
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 200));
            graphics.drawString("Snake Game", 200, 300);
            
            graphics.setFont(new Font("Calibri", Font.ITALIC, 70));
            graphics.drawString("Click Enter to Start", 500, 500);
            
        }
        
        if (gameState == GameState.ENDED) {
            graphics.setColor(new Color(200, 200, 250,150));
            graphics.fillRect(0, 0, 2000, 1000);
            graphics.drawImage(ended, 35,170 , this);
            
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 200));
            graphics.drawString("Game Over", 250, 150);
            
            graphics.setFont(new Font("Calibri", Font.ITALIC, 70));
            graphics.drawString("Final Score: " + this.getScore(),  470, 350);
            
            graphics.setFont(new Font("Calibri", Font.ITALIC, 70));
            graphics.drawString("Click Enter to Restart ", 400, 450);
            
        }
        
        if (gameState == GameState.PAUSED) {
            graphics.setColor(new Color(200, 200, 250,150));
            graphics.fillRect(0, 0, 2000, 1000);
            
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 200));
            graphics.drawString("Paused", 400, 300);
            
            graphics.setFont(new Font("Calibri", Font.ITALIC, 70));
            graphics.drawString("Current Score: " + this.getScore(), 450, 500);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 50));
            graphics.drawString("Click space bar to continue" , 400, 600);
            
        }
        if (gameState == GameState.LEVELUP) {
            graphics.setColor(new Color(200, 200, 250,150));
            graphics.fillRect(0, 0, 2000, 1000);
            
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Calibri", Font.ITALIC, 200));
            graphics.drawString("Level Up!", 300, 300);
            
            graphics.setFont(new Font("Calibri", Font.ITALIC, 50));
            graphics.drawString("Click space bar to continue" , 400, 600);
            
        }
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        if ((this.score < LEVELTHREE) && (score >= LEVELTHREE)) {
//            System.out.println(score + " "  + this.score);
            this.setBackground(ResourceTools.loadImageFromResource("resources/background_sea.jpg"));
            this.gameState = GameState.LEVELUP;
        } else if ((this.score < LEVELTWO) && (score >= LEVELTWO)) {
            this.setBackground(ResourceTools.loadImageFromResource("resources/background_ocean.jpg"));
            this.gameState = GameState.LEVELUP;
        } else if ((this.score < SPEEDLEVELTHREE) && (score >= SPEEDLEVELTHREE)) {
            AudioPlayer.play("/resources/burp.wav");
            this.delay = 1;
        } else if ((this.score < SPEEDLEVELTWO) && (score >= SPEEDLEVELTWO)) {
            AudioPlayer.play("/resources/burp.wav");
            this.delay = 2;
        } else if ((this.score < SPEEDLEVELONE) && (score >= SPEEDLEVELONE)) {
            AudioPlayer.play("/resources/burp.wav");
            this.delay = 3;
        }

        this.score = score;

    }

    /**
     * @return the life
     */
    public int getLife() {
        return life;
    }

    /**
     * @param life the life to set
     */
    public void setLife(int life) {
        if ((this.life > 0) && (life <= 0)) {
//            System.out.println("death");
            gameState = GameState.ENDED;
        }
        this.life = life;
    }

}
