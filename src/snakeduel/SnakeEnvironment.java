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
    
    
    private int life = 5;
    
    //instructions
    private int instructionCounter =100;
    private String instruction = "Using arrow keys or w, a, s, d to control the direction. ";
    
    
    private Image letter;
    
    public SnakeEnvironment() {
        
    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(ResourceTools.loadImageFromResource("resources/background_snow.jpg"));
        
        this.letter = ResourceTools.loadImageFromResource("resources/diamond.jpg");
        
        AudioPlayer.play("/resources/Luv_Letter.wav", AudioPlayer.LOOP_INFINITE);
        
        
        
        this.grid = new Grid();
        this.grid.setPosition(new Point(50,100));
        this.grid.setColor(new Color(150,150,250));
        this.grid.setCellHeight(22);
        this.grid.setCellWidth(22);
        this.grid.setColumns(57);
        this.grid.setRows(26);
        
        this.apples = new ArrayList<Point>();
        this.apples.add(getRadomGridLocation());
        this.apples.add(getRadomGridLocation());
        this.apples.add(getRadomGridLocation());
        this.apples.add(getRadomGridLocation());
        
        this.poisonBottles = new ArrayList<Point>();
        this.poisonBottles.add(getRadomGridLocation());
        this.poisonBottles.add(getRadomGridLocation());
        
        this.letters = new Point(-1000,-1000);

        
        
        
        this.snake = new Snake();
        this.snake.getBody().add(new Point(5,5));
        this.snake.getBody().add(new Point(5,4));
        this.snake.getBody().add(new Point(5,3));
        this.snake.getBody().add(new Point(4,3));
    }
    
    private Point getRadomGridLocation(){
        return new Point((int)(Math.random() * this.grid.getColumns()),(int)(Math.random() * this.grid.getRows()));
}

    private void checkAppleHeadIntersect() {
        for (int i = 0; i < this.apples.size(); i++) {
            if (this.snake.getHead().equals( this.apples.get(i))) {
//                System.out.println("HITTTTTTTTTTTTTTTTTTTTTTTTT");
                //sound
                AudioPlayer.play("/resources/eat_apple_sound.wav");
                //snake grow
                snake.setGrowthCounter(1);
                //add points
                this.setScore(this.getScore() + 10);
                //set new apples
                this.apples.get(i).x = getRadomGridLocation().x;
                this.apples.get(i).y = getRadomGridLocation().y;
//                this.apples.remove(i);
//                this.apples.add(new Point (((int)(Math.random() * this.grid.getColumns())),((int)(Math.random() * this.grid.getRows()))));
                this.poisonBottles.add(getRadomGridLocation());
            }
        }
    }
    
    private void checkBottlesHeadIntersect() {
        for (int i = 0; i < this.poisonBottles.size(); i++) {
            if (this.snake.getHead().equals(this.poisonBottles.get(i))) {
                //sound
                AudioPlayer.play("/resources/glass_break_sound.wav");
                this.setScore(this.getScore() - 10);
                this.setLife(this.getLife()-1);
                this.poisonBottles.get(i).x = getRadomGridLocation().x;
                this.poisonBottles.get(i).y = getRadomGridLocation().y;
                
                
            }
        }
    }
    private void checkLettersHeadIntersect() {
        
            if (this.snake.getHead().equals(this.letters)) {
                this.setScore(this.getScore() + 100);
                this.letters.move(-1000, -1000);
                objectCounterAppear = appear;
                AudioPlayer.play("/resources/rocket_sound.wav");
                for (int j = 0; j < this.poisonBottles.size()-5; j++) {
                    this.poisonBottles.get(j).x = -1000;
                    this.poisonBottles.get(j).y = -1000;
                }  
            }
        
    }

    @Override
    public void timerTaskHandler() {
        if (snake != null){
            if (moveCounter <= 0){
                snake.move(); 
                moveCounter = delay;
                checkAppleHeadIntersect();
                checkBottlesHeadIntersect();
                checkLettersHeadIntersect();
            } else{
               moveCounter --;
            }
        }
        
        if(letters != null){
            if (this.letters.equals(new Point(-1000,-1000))) {
                objectCounterDisappear --;
            }else{
                objectCounterAppear --;
            }
            
            if(objectCounterDisappear <= 0){
                this.letters.move(getRadomGridLocation().x, getRadomGridLocation().y);
                AudioPlayer.play("/resources/bell_sound.wav");
                objectCounterDisappear = disappear;
            } 
            
            if (objectCounterAppear <=0) {
                this.letters.move(-1000, -1000);
                objectCounterAppear = appear;
            } 
        }
        
        if (instructionCounter == 0) {
            this.instruction = "Collect apples and avoid poison bottles.";
            instructionCounter = 201;
        } else if(instructionCounter == 101){
            this.instruction = "The snake will speed up as the game progress.";
            instructionCounter = 302;
        }else if(instructionCounter == 202){
            this.instruction = "Catch the diamond to reduce the number of poison bottles.";
            instructionCounter = 403;
        }else if(instructionCounter == 303){
            this.instruction = "Good luck <3";
            instructionCounter = 504;
        }else if (instructionCounter == 404) {
            this.instruction = "";
            instructionCounter = -1;
        }else if (instructionCounter == -1) {
            this.instruction = "";
        }else{
            instructionCounter --;
        }
        
                
        if (snake.getHead().x< 0){
            snake.getHead().x = grid.getColumns() - 1;
        }else if (snake.getHead().y< 0){
            snake.getHead().y = grid.getRows() -1;
        }else if (snake.getHead().x> grid.getColumns()-1){
            snake.getHead().x = 0;
        }else if (snake.getHead().y> grid.getRows()-1){
            snake.getHead().y = 0;
        }
        
            
    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if
//           (e.getKeyCode() == KeyEvent.VK_SPACE){
//            this.score += 58;
//        } 
//        else if(e.getKeyCode() == KeyEvent.VK_M){
//            snake.move();
//        } else if
                (e.getKeyCode() == KeyEvent.VK_LEFT){
            snake.setDirection(Direction.LEFT);
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            snake.setDirection(Direction.RIGHT);
        } else if(e.getKeyCode() == KeyEvent.VK_UP){
            snake.setDirection(Direction.UP);
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            snake.setDirection(Direction.DOWN);
        }else if(e.getKeyCode() == KeyEvent.VK_A){
            snake.setDirection(Direction.LEFT);
        } else if(e.getKeyCode() == KeyEvent.VK_D){
            snake.setDirection(Direction.RIGHT);
        } else if(e.getKeyCode() == KeyEvent.VK_W){
            snake.setDirection(Direction.UP);
        } else if(e.getKeyCode() == KeyEvent.VK_S){
            snake.setDirection(Direction.DOWN);
        }
//        else if(e.getKeyCode() == KeyEvent.VK_G){
//            snake.setGrowthCounter(2);
//        }
    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {
        
    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
        
    }

    @Override
    public void paintEnvironment(Graphics graphics) {  
        if(this.grid != null){
           this.grid.paintComponent(graphics);
           
           
           if (this.letters != null){
               graphics.drawImage(letter, this.grid.getCellPosition(this.letters).x, this.grid.getCellPosition(this.letters).y, this.grid.getCellWidth(), this.grid.getCellHeight(), this);
               
               }
           
           
           if (this.apples != null){
               for (int i = 0; i < this.apples.size(); i++) {
//                   System.out.printf("apple at position %d has location x = %d y= %d\n", i,  this.apples.get(i).x, this.apples.get(i).y );
                   GraphicsPalette.drawApple(graphics, this.grid.getCellPosition(this.apples.get(i)), this.grid.getCellSize());
               }
           }
           
           if (this.poisonBottles != null){
               for (int i = 0; i < this.poisonBottles.size(); i++) {
                   GraphicsPalette.drawPoisonBottle(graphics, this.grid.getCellPosition(this.poisonBottles.get(i)), this.grid.getCellSize(),Color.green);
               }
           }
           
           
           
           
           Point cellLocation;
           
           if(snake != null){
               for (int i = 0; i < snake.getBody().size(); i++) {
                   //head
                   if(i == 0) {
                       graphics.setColor(new Color(255,255,51,250));
                       cellLocation = grid.getCellPosition(snake.getBody().get(i));
                       if(snake.getDirection() == Direction.LEFT){
                           graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 210, 320);
                       }else if(snake.getDirection() == Direction.RIGHT){
                           graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 20, 320);
                       }else if(snake.getDirection() == Direction.UP){
                           graphics.fillArc(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight(), 110, 320);
                       }else{
                           graphics.fillArc(cellLocation.x, cellLocation.y , grid.getCellWidth(), grid.getCellHeight(), 290, 320);
                       }
                       //graphics.fillRect(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());
                   }else{
                       //body
                       graphics.setColor(new Color(255,255,51,250));
                       cellLocation = grid.getCellPosition(snake.getBody().get(i));
                       graphics.fillOval(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());
                       //pattern
                       graphics.setColor(new Color(250,250,100,200));
                       graphics.fillRect(cellLocation.x + grid.getCellWidth()/4, cellLocation.y + grid.getCellHeight()/4, grid.getCellWidth() / 2, grid.getCellHeight() / 2);
                   }
                   
                   
               }
           }
        }
        graphics.setColor(new Color(200,200,250));
        graphics.setFont(new Font("Calibri", Font.ITALIC,60));
        graphics.drawString("Score: " + this.getScore(), 50, 70);
        
//        graphics.drawString("Life: " + this.getLife(), 300,70);
        
        graphics.setFont(new Font("Calibri", Font.ITALIC,20));
        graphics.drawString(this.instruction ,500,70);
        
        

        
        
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
        if ((this.score < LEVELTHREE) && (score >= LEVELTHREE)){
//            System.out.println(score + " "  + this.score);
            this.setBackground(ResourceTools.loadImageFromResource("resources/background_sky.jpg")); 
        }else if ((this.score < LEVELTWO) && (score >= LEVELTWO)) {
            this.setBackground(ResourceTools.loadImageFromResource("resources/background_ocean.jpg"));
        }else if ((this.score < SPEEDLEVELTHREE) && (score >= SPEEDLEVELTHREE)) {
            AudioPlayer.play("/resources/burp.wav");
            this.delay = 1;
        }else if ((this.score < SPEEDLEVELTWO) && (score >= SPEEDLEVELTWO)) {
            AudioPlayer.play("/resources/burp.wav");
            this.delay = 2;
        }else if ((this.score < SPEEDLEVELONE) && (score >= SPEEDLEVELONE)) {
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
        if ((this.life >0) && (life <= 0)){
            System.out.println("death");
        }
        this.life = life;
    }

    
}
