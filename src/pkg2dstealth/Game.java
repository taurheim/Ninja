/*
 * WEB APPLET:
 * http://www.youtube.com/watch?v=l3htgKqs8kU&feature=player_detailpage#t=1071s
 */
package pkg2dstealth;

import gfx.Colors;
import gfx.Font;
import gfx.Screen;
import gfx.SpriteSheet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.logging.Logger;
import javax.swing.JFrame;
import level.Level;
import pkg2dstealth.entities.Enemy;
import pkg2dstealth.entities.Entity;
import pkg2dstealth.entities.Player;
import pkg2dstealth.entities.Projectile;

public class Game extends Canvas implements Runnable{
    //Thread
    public Boolean running = false;
    //Frame
    public static final int WIDTH = 300;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 2;
    public static Dimension DIMENSIONS = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
    public boolean debug = false;
    
    public static final String NAME = "Game";
    public JFrame frame;
    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    //All the colors
    private int[] colors = new int[6*6*6];
    
    //SpriteSheet
    private Screen screen;
    
    //Level
    public Level level;
    
    //Input Handler
    public InputHandler input;
    
    //Ticks
    public int tickCount = 0;
    
    //Player
    public Player player;
    public Enemy enemy;
    
    private Thread thread;
    
    public synchronized void start(){
        running = true;
        thread = new Thread(this,NAME + "_main");
        thread.start();
    }
    
    public synchronized void stop(){
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public Game(){
        
    }
    public void init(){
        //populate colors. Once this list is populated, we can use the colors[] array by giving it an integer and it will return us with a real color.
        int index = 0;
        for(int r=0;r<6;r++){
            for(int g=0;g<6;g++){
                for(int b=0;b<6;b++){
                    //we use 255 for an invisible color (the 256th color)
                    int rr = (r* 255/5);
                    int gg = (g* 255/5);
                    int bb = (b* 255/5);
                    
                    colors[index++] = rr <<16 | gg <<8 | bb;
                }
            }
        }
        screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/spritesheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/level2.png");
        player = new Player(level,0,0,input);
        enemy = new Enemy(level,50,50,000);
        level.addEntity(enemy);
        level.addEntity(player);
    }
    
    
    public void tick(){
        tickCount++;
        for (int i=0;i<pixels.length;i++){
        pixels[i] = i/tickCount;
        }
        
        if(input.mouse.click && !player.isCrouching()){
            input.mouse.click =false;
            if(player.weapon==0 && player.ammo>0){
            player.ammo--;
            Projectile shur = new Projectile(level,player.x+4,player.y+4,(input.mouse.x/2)+screen.xOffset,(input.mouse.y/2)+screen.yOffset,1);
            level.addProjectile(shur);
            player.animate(1);
            } else if(player.weapon==1){
                player.animate(2);
            } else if(player.weapon==2){
                Projectile lzr = new Projectile(level,player.x+4,player.y+4,screen.xOffset+(player.faceright? 500:-500),player.y+4,0);
                level.addProjectile(lzr);
            }
            
        }
        if(level.playertakingdamage){
            player.takeDamage();
        }
        level.tick();
    }
    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        int xOffset = player.x - (screen.width/2);
        int yOffset = player.y - (screen.height/2);
        level.renderTiles(screen,xOffset,yOffset);
        
        for(int x=0;x<level.width;x++){
            int color = Colors.get(-1,-1,-1,000);
            Font.render("", screen, 0+(x*8), player.y, color,1);
        
        }
        level.renderEntities(screen);
        
        if(player.hp<=0){
            Font.render("G   a   m   e    O   v   e   r   .",screen,screen.xOffset+20,screen.yOffset+screen.height/2,Colors.get(-1,-1,-1,500),5);
        }
        String msg = "Enemies:"+level.enemies;
        Font.render(msg, screen, screen.xOffset, screen.yOffset+10, Colors.get(-1, -1, -1, 555),1);
        msg = "HP:"+player.hp;
        Font.render(msg, screen, screen.xOffset+screen.width-40-(msg.length()*4), screen.yOffset+screen.height-30, Colors.get(-1, -1, -1, 555),1);
        msg = "Weapon";
        //Font.render(msg, screen, screen.width-20, screen.height-20, Colors.get(-1, -1, -1, 555),5);
        Font.render(msg, screen, screen.xOffset+screen.width-40- (msg.length()*4), screen.yOffset+screen.height-20 , Colors.get(-1, -1, -1, 555),1);
        msg = "POINTS: "+level.points;
        Font.render(msg,screen,screen.xOffset,screen.yOffset,Colors.get(-1,-1,-1,500),1);
        screen.render(screen.xOffset+screen.width-60, screen.yOffset+screen.height-10, 24*32, Colors.get(-1,0,222,-1),0,1);
        screen.render(screen.xOffset+screen.width-52,screen.yOffset+screen.height-10,25*32,Colors.get(-1,0,053,555),0,1);
        screen.render(screen.xOffset+screen.width-44,screen.yOffset+screen.height-10,23*32,Colors.get(-1,0,500,542),0,1);
        screen.render(screen.xOffset+screen.width-60+player.weapon*8,screen.yOffset+screen.height-10,17*32,Colors.get(-1, 500, -1, -1),0,1);
        
        for(int y=0;y<screen.height;y++){
            for (int x=0;x<screen.width;x++){
                int colorCode = screen.pixels[x+y*screen.width];
                if(colorCode<255){
                    pixels[x+y*WIDTH] = colors[colorCode];
                }
            }
        }
        
        Graphics g = bs.getDrawGraphics();
        //g.setColor(Color.BLACK);
        //g.fillRect(0,0,getWidth(),getHeight());
        //g.setColor(Color.RED);
        g.drawImage(image,0,0,getWidth(),getHeight(), null);
        g.setColor(Color.white);
        //Shuriken ammo
        g.drawString(player.ammo+"", screen.width*2-120, screen.height*2-12);
        /**DEBUG
        g.setColor(Color.red);
        for(Entity e : level.projectiles ){
            g.fillRect(e.x*2-screen.xOffset*2+4, e.y*2-screen.yOffset*2+4, 8, 8);
        }
        g.setColor(Color.yellow);
        for(Entity e : level.entities){
            g.fillRect(e.x*2-screen.xOffset*2-16,e.y*2-screen.yOffset*2-20,16*3,16*3);
        }
        
        g.drawString(player.x+","+player.y, 25, 55);
        g.drawString(screen.xOffset+","+screen.yOffset,25,65);
         /****/
        g.dispose();
        bs.show();
        
        
    }
    /*
     *
     */
    public void run() {
        
        long lastTime = System.nanoTime(); //Current time in nano seconds
        double nsPerTick = 1000000000D/60D; //Nanoseconds per tick: 1 second/60 frames
        
        int ticks = 0; // Current Updates
        int fps = 0; // Current fps
        
        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        
        init();
        while(running){
            //Update the current time
            long now = System.nanoTime();
            //Delta is the time in TICKS since last tick: (current ns - last ns / amount of ns in one tick)           
            delta += (now-lastTime)/nsPerTick;
            //Update lastTime to reflect this latest tick
            lastTime = now;
            
            //If shouldRender is true, draw a frame. If this starts as true it will never become false
            boolean shouldRender = false;
            
            //If it has been longer than nsPerTick milliseconds since the last tick, tick.
            while(delta>=1){
                ticks++;
                tick();
                delta-=1; //Resets delta to essentially 0 without losing a bunch of decimal places
                shouldRender = true;
            }
            
            if(shouldRender ){
            fps++;
            render();
            }
            //If a second has passed
            if((System.currentTimeMillis() - lastTimer) >= 1000){
                level.spawn();
                lastTimer += 1000;
                debug(DebugLevel.INFO,ticks+"-"+fps);
                fps = 0;
                ticks = 0;
                
        if(player.hp<=0){
            this.running=false;
        }
            }
        }
    }
    public void debug(DebugLevel level, String msg){
        switch(level){
            default:
            case INFO:
                if(debug){
                    System.out.println("["+NAME+"] " + msg);
                }
                break;
            case WARNING:
                System.out.println("["+NAME+"][WARNING] " + msg);
                break;
            case SEVERE:
                System.out.println("["+NAME+"][SEVERE] " + msg);
                stop();
                break;
        }
    }
    
    public static enum DebugLevel {
        INFO,WARNING,SEVERE;
    }
}
