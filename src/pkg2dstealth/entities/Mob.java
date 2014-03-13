
package pkg2dstealth.entities;

import level.Level;
import level.tiles.Tile;

public abstract class Mob extends Entity {
    
    
    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int scale = 1;
    
    
    //Jumping mechanics
    public double vvelocity=1;
    public boolean air = true;
    public double gravity = 0.1;
    public int frame =0;
    public Mob(Level level,String name,int x, int y, int speed){
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.scale = scale;
    }
    public void setVelocity(double v){
        y+=v;
        vvelocity=v;
        air=true;
    }
    //How many we're moving in what direction (xa = 2 means moving 2 right)
    public void move(int xv,int yv){
        if(tileUnder()){
            air=false;
            numSteps++;
            vvelocity=0;
        } else {
            if(!air){
            air= true;
            vvelocity=1;
            }
        }
        while(tileAbove()){
            //If you hit something above you, push down until head doesn't hit
            vvelocity=1;
            y+=1;
        }
        if(!hasCollided(xv,yv)){
            //left/right collisions
            x+=xv;
            isMoving = true;
            if(xv==0){
                isMoving = false;
            }
        }
        if(air){
                //Every frame
                if(currentTile() == 3){
                    y+=vvelocity;
                    vvelocity+=gravity/2;
                } else {
                    y+= vvelocity;
                while(tileUnder()){
                    y--;
                    if(!tileUnder()){
                        y++;
                        break;
                    }
                }
                vvelocity+=gravity;
                }
        }
    }
        public abstract boolean hasCollided(int xa, int ya);
        public abstract boolean tileUnder();
        public abstract boolean tileAbove();
        public abstract int currentTile();
        public abstract void die();
        
        protected boolean isSolidTile(int xa, int ya, int x, int y){
            if(level == null){return false;}
            Tile lastTile = level.getTile((this.x + x)>>3, (this.y+y)>>3);
            Tile newTile = level.getTile((this.x+x+xa)>>3,(this.y+y+ya)>>3);
            
            //If the tiles aren't the same, and the new tile is a solid, then collide
            if(!lastTile.equals(newTile) && newTile.isSolid()){
                return true;
            }
            
            return false;
        }
        
        public String getName(){
            return name;
        }
}
