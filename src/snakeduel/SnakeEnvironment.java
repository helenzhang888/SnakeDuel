/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeduel;

import environment.Environment;
import environment.Grid;
import image.ResourceTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author zhanglianghui
 */
class SnakeEnvironment extends Environment {
    private Grid grid;
    private int score = 0;
    private Snake snake;
    
    private int delay = 4;
    private int moveCounter = delay;
    
    public SnakeEnvironment() {
        
    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(ResourceTools.loadImageFromResource("resources/background_snow.jpg"));
        
        this.grid = new Grid();
        this.grid.setPosition(new Point(50,100));
        this.grid.setColor(new Color(150,150,250));
        this.grid.setCellHeight(22);
        this.grid.setCellWidth(22);
        this.grid.setColumns(36);
        this.grid.setRows(19);
        
        this.snake = new Snake();
        this.snake.getBody().add(new Point(5,5));
        this.snake.getBody().add(new Point(5,4));
        this.snake.getBody().add(new Point(5,3));
        this.snake.getBody().add(new Point(4,3));
    }

    @Override
    public void timerTaskHandler() {
        if (snake != null){
            if (moveCounter <= 0){
                snake.move(); 
                moveCounter = delay;
            } else{
               moveCounter --;
            }
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
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            this.score += 58;
        } else if(e.getKeyCode() == KeyEvent.VK_M){
            snake.move();
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT){
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
        } else if(e.getKeyCode() == KeyEvent.VK_G){
            snake.setGrowthCounter(2);
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
        if(this.grid != null){
           this.grid.paintComponent(graphics);
           
           Point cellLocation;
           
           if(snake != null){
               for (int i = 0; i < snake.getBody().size(); i++) {
                   //head
                   if(i == 0) {
                       graphics.setColor(new Color(255,255,0,200));
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
                       graphics.setColor(new Color(255,255,0,200));
                       cellLocation = grid.getCellPosition(snake.getBody().get(i));
                       graphics.fillOval(cellLocation.x, cellLocation.y, grid.getCellWidth(), grid.getCellHeight());
                       //pattern
                       graphics.setColor(new Color(250,250,120,200));
                       graphics.fillRect(cellLocation.x + grid.getCellWidth()/2, cellLocation.y + grid.getCellHeight()/4, grid.getCellWidth() / 2, grid.getCellHeight() / 2);
                   }
                   
                   
               }
           }
        }
        graphics.setColor(new Color(200,200,250));
        graphics.setFont(new Font("Calibri", Font.ITALIC,60));
        graphics.drawString("Score: " + this.score, 50, 70);
        
        graphics.setFont(new Font("Calibri", Font.ITALIC,20));
        graphics.drawString("Using arrow keys or w, a, s, d to control the direction" ,400,70);
        
        
        
    }
    
}
