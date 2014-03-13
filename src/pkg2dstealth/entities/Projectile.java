//SUPER UGLY CODE
package pkg2dstealth.entities;

import gfx.Colors;
import gfx.Screen;
import java.awt.Rectangle;
import level.Level;
import level.tiles.Tile;

public class Projectile extends Entity {
    public int type=0;
    public int x2=0;
    public int y2=0;
    protected int scale=1;
    private int color = Colors.get(-1,0,222,555);
    private int xo= -1;
    private int yo= -1;
    private double dx=0;
    private double dy=0;
    private int frame =0;
    private double l=0;
    private double xdub=0;
    private double ydub=0;
    private int tc=0;
    private int tci=0;
    public boolean hit = false;
    
    public Projectile(Level level,int x, int y,  int x2, int y2,int type){
        super(level);
        this.x = x;
        xdub = x;
        ydub = y;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.scale = scale;
        dx=(x2)-x;
        dy=(y2)-y;
        l = Math.sqrt(dx*dx + dy*dy);
        int tempx = (int) ((10*dx)/l);
        int tempy = (int) ((10*dy)/l);
        dx = tempx/10.0;
        dy = tempy/10.0;
        this.type=type;
        if(type==0){ //Lazer
            color= Colors.get(-1,0,500,542);
        } else{ //Shuriken
            color=Colors.get(-1,0,222,555);
        }
    }

    public void tick() {
        
        tc++;
        if(!hasCollided()){
            xdub+=dx*3;
            ydub+=dy*3;
            x = (int) xdub;
            y = (int) ydub;
            if(tc%3 == 0){
                if(type==1){
                if(frame==1){
                    frame=0;
                } else {frame=1;}
            } else if(type==0){
                if(frame==3){
                    frame=0;
                } else {frame++;}
            }
            }
        } else if((tc-tci)>=60){
            this.exists = false;
            this.x=-1;
            this.y=-1;
        }
    }
    public boolean hasCollided(){
        Tile tile1 = level.getTile((this.x+3)>>3,(this.y+3)>>3);
        Tile tile2 = level.getTile((this.x+3)>>3,(this.y+4)>>3);
        Tile tile3 = level.getTile((this.x+4)>>3,(this.y+3)>>3);
        Tile tile4 = level.getTile((this.x+4)>>3,(this.y+4)>>3);
    if(tile1.isSolid() || tile2.isSolid() || tile3.isSolid() || tile4.isSolid()){
        if(tci==0){
            tci=tc;
        }
        return true;
    } else {
        return false;
    }
    }
    public void render(Screen screen) {
        if(xo==-1 && yo==-1){
            xo = screen.xOffset;
            yo = screen.yOffset;
        }
        int flip;
        if(dx>0){
            flip=0x00;
        }else{
            flip=0x01;
        }
        screen.render(x,y , frame+(23+type)*32, color,flip , scale);
    }

    public void flicker() {
        color = Colors.get(-1,0,500,500);
        
    }

    public void die() {
    }
    
}
