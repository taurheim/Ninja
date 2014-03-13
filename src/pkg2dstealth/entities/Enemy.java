/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dstealth.entities;

import gfx.Colors;
import gfx.Screen;
import java.awt.Rectangle;
import level.Level;
import level.tiles.Tile;

public class Enemy extends Mob{
    private int color = Colors.get(-1,0,222,555);
    public Enemy(Level level,int x, int y,int color){
        super(level,"Enemy",x,y,1);
        this.color = Colors.get(-1,0,color,555);
    }
    public Rectangle rectbound = new Rectangle(this.x,this.y,16,16);
    
    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int scale = 2;
    protected boolean faceright=true;
    
    private int move=0;
    double nextdirchange=Math.random()*800;
    double currentframe=0D;
    
    //Animation
    public boolean animating=false;
    private int animationframe=0;
    private int animationtimer=0;
    
    public boolean hasCollided(int xa, int ya) {
        
        //Collision box bounds
        int xMin =0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 12+(8*(scale-1));
        
        for(int y=yMin;y<yMax;y++){
            if(isSolidTile(xa,ya,xMin,y)) return true; //far left side
        }
        for(int y=yMin;y<yMax;y++){
            if(isSolidTile(xa,ya,xMax,y)) return true; //far right side
        }
        return false;
    }

    public boolean tileUnder() {
        Tile tile1 = level.getTile((this.x+7)>>3,(this.y+3+(8*scale))>>3);
        Tile tile2 = level.getTile((this.x+7)>>3,(this.y+4+(8*scale))>>3);
        Tile tile3 = level.getTile((this.x+0)>>3,(this.y+3+(8*scale))>>3);
        Tile tile4 = level.getTile((this.x+0)>>3,(this.y+4+(8*scale))>>3);
    if(!tile1.equals(tile2) && tile2.isSolid()){
        return true;
    }
    if(!tile3.equals(tile4) && tile4.isSolid()){
        return true;
    }
    if(tile1.isSolid() || tile3.isSolid()){
        return true;
    }
    return false;
    }

    public boolean tileAbove() {
                int out = 3;
        int in = 11+(8*(scale-1));
        
        Tile tile1 = level.getTile((this.x+7)>>3,(this.y+in)>>3);
        Tile tile2 = level.getTile((this.x+7)>>3,(this.y+out)>>3);
        Tile tile3 = level.getTile((this.x+0)>>3,(this.y+in)>>3);
        Tile tile4 = level.getTile((this.x+0)>>3,(this.y+out)>>3);
    if(!tile1.equals(tile2) && tile2.isSolid()){
        return true;
    }
    if(!tile3.equals(tile4) && tile4.isSolid()){
        return true;
    }
    return false;
    }

    public int currentTile() {
        Tile bodyTile = level.getTile((this.x+5)>>3,(this.y+13)>>3);
        return bodyTile.getId();
    }

    public void tick() {
        //Check for collision with shuriken
        rectbound = new Rectangle(this.x,this.y,16,16);
        
        //Move at half speed
        int facing = faceright? 1:-1;
        if(move==1){
        move(facing,0);
        move=0;
        } else {
            move++;
        }
        currentframe++;
        if(currentframe>=nextdirchange){
            nextdirchange = Math.random()*800;
            currentframe=0;
            faceright = !faceright;
        }
        
        //Jump
        Tile newTile = level.getTile((this.x+4+6*facing)>>3,(this.y+15)>>3);
        if(newTile.isSolid() && tileUnder()){
            setVelocity(-3);
        }
    }

    public void render(Screen screen) {
        int yTile = 21;
        int xTile = 0;
        
        int modifier = 8* scale;
        int xOffset = x - modifier/2;
        int yOffset = y - modifier/2;
        int flipTop=0;
        int flipBottom=0;
        if(faceright){
                flipTop = 0x00;
                flipBottom = 0x00;
            } else {
                flipTop = 0x01;
                flipBottom=0x01;
            }
        if(animating){
            runanimation(screen,flipTop,xOffset,yOffset,modifier);
        } else{
        screen.render(xOffset+(modifier*flipTop),yOffset,xTile + yTile*32,color,flipTop,scale);//topleft
        screen.render(xOffset+ modifier-(modifier*flipTop),yOffset,(xTile+1) + yTile*32,color,flipTop,scale); //topright
        screen.render(xOffset+(modifier*flipBottom),yOffset+modifier,xTile + (yTile+1)*32,color,flipBottom,scale);//btmleft
        screen.render(xOffset + modifier-(modifier*flipBottom),yOffset+modifier,(xTile+1) + (yTile+1)*32,color,flipBottom,scale);//btmright}
        }
}
    
    public boolean projectileCollision(Rectangle projectile){
        if(rectbound.intersects(projectile)){
            System.out.println("collision");
            return true;
        } else{
            return false;
        }
    }

    public void flicker() {
        color = Colors.get(-1,0,222,500);
    }
        public void runanimation(Screen screen,int flip, int xOffset, int yOffset, int modifier){
                screen.render(xOffset+(modifier*flip),yOffset,animationframe+2+21*32,color,flip,scale);//topleft
                screen.render(xOffset+ modifier-(modifier*flip),yOffset,animationframe+2+1+21*32,color,flip,scale); //topright
                screen.render(xOffset+(modifier*flip),yOffset+modifier,animationframe+2+22*32,color,flip,scale);//btmleft
                screen.render(xOffset + modifier-(modifier*flip),yOffset+modifier,animationframe+1+2+22*32,color,flip,scale);//btmright
                animationtimer++;
                if(animationtimer%8==0){
                animationframe+=2;
                }
                if(animationframe==8){
                    animationframe=0;
                    animating = false;
                    animationtimer=0;
                    this.exists=false;
                }
        }
    public void die() {
        animating=true;
        this.dying=true;
    }
}