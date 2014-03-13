
package pkg2dstealth.entities;

import gfx.Colors;
import gfx.Screen;
import level.Level;
import level.tiles.Tile;
import pkg2dstealth.InputHandler;

public class Player extends Mob{
    
    public int hp=10;
    private int hptimer =60;
    
    public boolean animating=true;
    public int animation=1;
    private int animationframe=0;
    private int animationtimer=0;
    public int weapon =1;
    public int ammo=100;
    
    private InputHandler input;
    private int color = Colors.get(-1,0,222,555);
    private int scale = 2;
    public boolean faceright = true;
    private boolean crouching = false;
    private int jumpcount = 0;
    private boolean gliding = false;
    private double glidegravity = 0;
    private int maxjumps = 2;
    private boolean jumpreset= false;
    private boolean damage = false;
    int flickertime =0;
    
    public Player(Level level,int x, int y, InputHandler input){
        super(level,"Player",x,y,1);
        this.input = input;
    }
    public boolean isCrouching(){
        if(crouching){
            return true;
        } else{
            return false;
        }
    }
    public void animate(int anim){
        animating = true;
        animation = anim;
    }
    public void runanimation(Screen screen,int flip, int xOffset, int yOffset, int modifier){
        switch(animation){
            case 0:
                break;
            case 1: //Shuriken
                //screen.render(xOffset + modifier-(modifier*flip),yOffset+modifier,1+27*32,color,flip,scale);
                if(!air){
                    screen.render(xOffset+(modifier*flip),yOffset+modifier,animationframe+27*32,color,flip,scale);//btmleft
                    screen.render(xOffset + modifier-(modifier*flip),yOffset+modifier,animationframe+1+27*32,color,flip,scale);//btmright
                }else{
                    screen.render(xOffset+(modifier*flip),yOffset,animationframe+26*32,color,flip,scale);//topleft
                    screen.render(xOffset+ modifier-(modifier*flip),yOffset,animationframe+1+26*32,color,flip,scale); //topright
                }
                    animationtimer++;
                if(animationtimer%3==0){
                animationframe+=2;
                }
                if(animationframe==8){
                    animationframe=0;
                    animation=0;
                    animating = false;
                    animationtimer=0;
                }
                break;
            case 2: //Sword
                int swordcolor = Colors.get(-1,0,053,555);
                screen.render(xOffset+(modifier*flip),yOffset,animationframe+8+26*32,swordcolor,flip,scale);//topleft
                screen.render(xOffset+ modifier-(modifier*flip),yOffset,animationframe+8+1+26*32,swordcolor,flip,scale); //topright
                screen.render(xOffset+(modifier*flip),yOffset+modifier,animationframe+8+27*32,swordcolor,flip,scale);//btmleft
                screen.render(xOffset + modifier-(modifier*flip),yOffset+modifier,animationframe+1+8+27*32,swordcolor,flip,scale);//btmright
                animationtimer++;
                if(animationtimer%2==0){
                animationframe+=2;
                }
                if(animationframe==10){
                    animationframe=0;
                    animation=0;
                    animating = false;
                    animationtimer=0;
                }
                break;
        }
    }
    
    public boolean tileUnder () {
    
        //Is there a tile that we're standing on?
    Tile tile1 = level.getTile((this.x+7)>>3,(this.y+3+(8*scale))>>3);
    Tile tile2 = level.getTile((this.x+7)>>3,(this.y+4+(8*scale))>>3);
    Tile tile3 = level.getTile((this.x+0)>>3,(this.y+3+(8*scale))>>3);
    Tile tile4 = level.getTile((this.x+0)>>3,(this.y+4+(8*scale))>>3);
    if(!tile1.equals(tile2) && tile2.isSolid()){
        jumpcount=0;
        return true;
    }
    if(!tile3.equals(tile4) && tile4.isSolid()){
        jumpcount=0;
        return true;
    }
    if(tile1.isSolid() || tile3.isSolid()){
        jumpcount=0;
        return true;
    }
    return false;
};
    public int currentTile(){
        Tile bodyTile = level.getTile((this.x+5)>>3,(this.y+13)>>3);
        return bodyTile.getId();
    }
    public boolean tileAbove() {
        int out = 3;
        int in = 11+(8*(scale-1));
        if(crouching){
            out = 5;
            in = 11+(8*(scale-1));
        }
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
    public boolean hasCollided(int xa, int ya) {
        
        //Collision box bounds
        int xMin =0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 12+(8*(scale-1));
        if(crouching){
            xMin =0;
            xMax = 7;
            yMin = 6;
            yMax = 11+(8*(scale-1));
        }
        
        for(int y=yMin;y<yMax;y++){
            if(isSolidTile(xa,ya,xMin,y)) return true; //far left side
        }
        for(int y=yMin;y<yMax;y++){
            if(isSolidTile(xa,ya,xMax,y)) return true; //far right side
        }
        return false;
    }
    public void tick() {
        hptimer++;
        if(currentTile()==3){
            takeDamage();
        }
        
        int xa = 0;
        int ya = 0;
        if(input.num1.isPressed()) weapon=0;
        if(input.num2.isPressed()) weapon=1;
        if(input.num3.isPressed()) weapon=2;
        if(input.left.isPressed()){xa-=2;faceright=false;}
        if(input.right.isPressed()){xa+=2;faceright=true;}
        if(air && !input.up.isPressed() && jumpcount==0){
            jumpcount=1;
        }
        if(input.up.isPressed()){
            if(tileUnder() && !crouching && !gliding){
                setVelocity(-3);
                jumpreset=false;
            } else if(this.vvelocity>-1.5 && jumpcount<maxjumps && jumpreset && !gliding){
                setVelocity(-3);
                jumpcount++;
            }
        } else {
            jumpreset=true;
        }
        if(input.space.isPressed()){
            if(air && this.vvelocity>0 && this.gravity!=0 && currentTile() !=3){
            gliding = true;
            this.gravity=glidegravity;
            this.vvelocity=1;
            }
        } else {
            this.gravity=0.1;
            gliding = false;
        }
        //if(input.down.isPressed()){ya++;}
        
        crouching = false;
        if(input.down.isPressed()){
            if(tileUnder()){
                crouching = true;
            }
        }
        if(tileAbove()){
            crouching = true;
        }
        if(tileUnder()){
            gliding =false;
            this.gravity=0.1;
        }
        move(xa,ya);
    }

    public void render(Screen screen) {
        //Working here, currently 18:00 of ep.9 i think
        int xTile = 0;
        int yTile = 28;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1; //Number between 0 and 1 for where player is facing
        int flipBottom = (numSteps >> walkingSpeed) & 1;
        
        //Walking animation. Uses movingDir to decide what to flip (numSteps to provide animation)
        if(tileUnder()){
                xTile+=4 + ((numSteps >> walkingSpeed)&1)*2;
            if(faceright){
                flipTop = 0x00;
                flipBottom = 0x00;
            } else {
                flipTop = 0x01;
                flipBottom=0x01;
            }
        } else if(air){
            flipTop = faceright? 0:1;
            flipBottom = faceright? 0:1;
        }
        
        int modifier = 8* scale;
        int xOffset = x - modifier/2;
        int yOffset = y - modifier/2;
        if(tileUnder() && !isMoving){
            xTile=4;
            flipTop = faceright? 0:1;
            flipBottom = faceright? 0:1;
        }
        if(crouching && tileUnder()){
            xTile+=5;
            screen.render(xOffset+(modifier*flipTop), yOffset, 8 + yTile*32, color, flipTop, scale);
            screen.render(xOffset+ modifier-(modifier*flipTop),yOffset,9 + yTile*32,color,flipTop,scale); //topright
            screen.render(xOffset+(modifier*flipBottom),yOffset+modifier,8 + (yTile+1)*32,color,flipBottom,scale);//btmleft
            screen.render(xOffset + modifier-(modifier*flipBottom),yOffset+modifier,9 + (yTile+1)*32,color,flipBottom,scale);//btmright
            //screen.render(x, y, yTile, color, movingDir, scale);
        }else {
            crouching = false;
        //Have to render all 4 tiles that the character appears on
        screen.render(xOffset+(modifier*flipTop),yOffset,xTile + yTile*32,color,flipTop,scale);//topleft
        screen.render(xOffset+ modifier-(modifier*flipTop),yOffset,(xTile+1) + yTile*32,color,flipTop,scale); //topright
        screen.render(xOffset+(modifier*flipBottom),yOffset+modifier,xTile + (yTile+1)*32,color,flipBottom,scale);//btmleft
        screen.render(xOffset + modifier-(modifier*flipBottom),yOffset+modifier,(xTile+1) + (yTile+1)*32,color,flipBottom,scale);//btmright
        }
        if(gliding){
            
            int glidecolor = Colors.get(-1,444,555,0);
            screen.render(xOffset+(modifier*flipTop), yOffset, 10+ 28*32, glidecolor, flipTop, scale);
            screen.render(xOffset+ modifier-(modifier*flipTop),yOffset,11+28*32,glidecolor,flipTop,scale);
        }
        if(animating){
            runanimation(screen,flipTop,xOffset,yOffset,modifier);
        }
        if(damage){
            flicker();
        }
        if(weapon==2){
            screen.render(xOffset-10*flipBottom+14, yOffset+12, 23*32, Colors.get(-1,0,500,542), flipTop, scale);
        }
    }
    
    public void flicker() {
        if(flickertime<=3){
            this.color = Colors.get(-1,500,500,500);
        } else{
            this.color = Colors.get(-1,0,222,555);
            flickertime=0;
            damage=false;
        }
        flickertime++;
    }

    public void die() {
    }
    public void takeDamage(){
        damage=true;
        if(hptimer>=40){
            hp--;
            hptimer=0;
        }
    }
}
