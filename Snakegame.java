import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Snakegame extends JPanel implements ActionListener , KeyListener{
    private class Tile{
        int x,y;
         
        Tile(int x , int y)
        {
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random rand;

    //game logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

   Snakegame(int boardWidth , int boardHeight){
    this.boardWidth = boardWidth;
    this.boardHeight = boardHeight;
    setPreferredSize(new Dimension(this.boardWidth , this.boardHeight));
    setBackground(Color.BLACK);
    addKeyListener(this);
    setFocusable(true);

    snakeHead= new Tile(5,5);
    snakeBody = new ArrayList<>();

    food= new Tile(10,10);
    rand = new Random();
    placeFood();

    velocityX=0;
    velocityY=0;

    gameloop = new Timer(100,this );
    gameloop.start();
   }



    @Override
    public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);
}

   public void draw(Graphics g){
    //grid
    // for (int i=0; i < boardWidth/tileSize ; i++ )
    // {
    //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
    //     g.drawLine(0,i*tileSize,boardWidth, i*tileSize );
    // }


    //food
    g.setColor(Color.red);
    //g.fillRect(food.x * tileSize,food.y * tileSize,tileSize , tileSize );
    g.fill3DRect(food.x * tileSize,food.y * tileSize,tileSize , tileSize, true );


    //snake head
    g.setColor(Color.GREEN);
    //g.fillRect(snakeHead.x*tileSize , snakeHead.y*tileSize , tileSize, tileSize);
    g.fill3DRect(snakeHead.x*tileSize , snakeHead.y*tileSize , tileSize, tileSize, true);
   

    //snake body
    for (int i =0; i < snakeBody.size();i++)
    {
        Tile snakePart = snakeBody.get(i);
        //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize,tileSize, tileSize);
        g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize,tileSize, tileSize, true);
    }

    //score
    g.setFont(new Font("Ariel", Font.PLAIN, 16));
    if(gameOver) {
        g.setColor(Color.red);
        g.drawString("Game Over : "+String.valueOf(snakeBody.size()),tileSize-16,tileSize );
    }
    else{
        g.drawString("Score : "+String.valueOf(snakeBody.size()),tileSize-16,tileSize );
    }
}


   public final void placeFood()
   {
    food.x = rand.nextInt(boardWidth/tileSize);
    food.y =rand.nextInt(boardHeight/tileSize);
   }



   public boolean collision (Tile t1, Tile t2)
    {
    return t1.x == t2.x && t1.y == t2.y;
    }

public void move()
{
      //eat food
      if(collision(snakeHead,food )){
        snakeBody.add(new Tile(food.x , food.y));
        placeFood();
    }

    //snake body
    for (int i =snakeBody.size()-1; i>=0; i--)
    {
        Tile snakePart = snakeBody.get(i);
        if( i==0){
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
        }
        else{
            Tile prevSnakePart = snakeBody.get(i-1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
        }
    }



    //snakehead
    snakeHead.x += velocityX;
    snakeHead.y += velocityY;


    //game over conditon
    for (int i=0; i<snakeBody.size();i++){
        Tile snakePart = snakeBody.get(i);

        //collide with the snake head
        if(collision(snakeHead,snakePart)){
            gameOver = true;
        }
    }
    if (snakeHead.x*tileSize<0 || snakeHead.x*tileSize > boardHeight||
        snakeHead.y*tileSize < 0|| snakeHead.y*tileSize > boardHeight){
            gameOver=true;
        }

}



@Override
public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gameOver){
        gameloop.stop();
    }
}

@Override
public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1 ){
        velocityX= 0;
        velocityY=-1;
    }
    if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
        velocityX=0;
        velocityY=1;
    }
    if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX!=1){
        velocityX= -1; 
        velocityY=0;
    }
    if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX !=-1){
        velocityX=1;
        velocityY=0;
    }


}


//not needed

@Override
public void keyTyped(KeyEvent e) {
}

@Override
public void keyReleased(KeyEvent e) {
}

}