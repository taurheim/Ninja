/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import java.util.logging.Logger;
import level.tiles.Tile;
import gfx.Screen;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import pkg2dstealth.entities.Enemy;
import pkg2dstealth.entities.Entity;
import pkg2dstealth.entities.Player;
import pkg2dstealth.entities.Portal;
import pkg2dstealth.entities.Projectile;

/**
 *
 * @author 732897
 */
public class Level {
    public boolean playertakingdamage = false;
    private byte[] tiles;
    public int width;
    public int height;
    public boolean exists = true;
    public List<Entity> entities = new ArrayList<Entity>();
    private String imagePath;
    private BufferedImage image;
    int xo =0;
    int yo =0;
    boolean spawn = false;
    public int points = 0;
    
    public int enemies = 0;
    
    public List<Projectile> projectiles = new ArrayList<Projectile>();
    
    
    public Level(String imagePath){
        if(imagePath != null){
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            tiles = new byte[width*height];
            this.width = 64;
            this.height = 64;
            this.generateLevel();
        }
    }
    private void loadLevelFromFile(){
        try {
            //Take data from the image file and put it into the image variable
            this.image = ImageIO.read(Level.class.getResource(this.imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            tiles = new byte[width*height];
            this.loadTiles();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void loadTiles(){
        //Need to figure out what Tile IDs respond with each color located in the image
        //Translate all of the buffered image data into an int based on where it is.
        int[] tileColors = this.image.getRGB(0,0,width,height,null,0,width);
        //Parse each color int to an ID
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                //Loops through every tile in the tiles variable and lets us access it with variable "t"
                for(Tile t : Tile.tiles){
                    tileCheck: if(t!=null && t.getLevelColor() == tileColors[x+y*width]){
                        this.tiles[x+y*width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }
    //So we can change data on the fly
    private void saveLevelToFile(){
        try{
            //Writes image to file
            ImageIO.write(image,"png",new File(Level.class.getResource(this.imagePath).getFile()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void alterTile(int x, int y,Tile newTile){
        this.tiles[x+y*width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }
    
    public void generateLevel(){
        for(int x=0;x<width;x++){
            tiles[x+20*width] = Tile.STONE.getId();
        }
            for(int xx=0;xx<width;xx++){
                tiles[xx] = Tile.GRASS.getId();
                tiles[xx+width] = Tile.GRASS.getId();
                tiles[xx+2*width] = Tile.GRASS.getId();
                tiles[xx+3*width] = Tile.GRASS.getId();
                tiles[xx+4*width] = Tile.GRASS.getId();
                tiles[xx+5*width] = Tile.GRASS.getId();
                tiles[xx+6*width] = Tile.GRASS.getId();
                tiles[xx+7*width] = Tile.GRASS.getId();
                tiles[xx+8*width] = Tile.GRASS.getId();
                tiles[xx+9*width] = Tile.GRASS.getId();
                tiles[xx+10*width] = Tile.GRASS.getId();
                tiles[xx+11*width] = Tile.GRASS.getId();
                tiles[xx+12*width] = Tile.GRASS.getId();
                tiles[xx+13*width] = Tile.GRASS.getId();
                tiles[xx+14*width] = Tile.GRASS.getId();
                tiles[xx+15*width] = Tile.GRASS.getId();
                tiles[xx+16*width] = Tile.GRASS.getId();
                tiles[xx+17*width] = Tile.GRASS.getId();
                tiles[xx+18*width] = Tile.GRASS.getId();
                tiles[xx+19*width] = Tile.GRASS.getId();
            }
            tiles[12+2*width] = Tile.STONE.getId();
            tiles[13+2*width] = Tile.STONE.getId();
            /*
        for(int y=0;y<height;y++){
            for (int x=0;x<width;x++){
                if(x*y%10 < 7){
                    tiles[x+y*width] = Tile.GRASS.getId();
                } else {
                    tiles[x+y*width] = Tile.STONE.getId();
                }
                
            }
        }*/
    }
    public void renderTiles(Screen screen,int xOffset,int yOffset){
        //Camera
        if(xOffset<0) xOffset =0;
        if(xOffset>((width<<3)-screen.width)) xOffset= (width<<3)-screen.width;
        if(yOffset<0) yOffset =0;
        if(yOffset>((height<<3)-screen.height)) yOffset= (height<<3)-screen.height;
        
        screen.setOffset(xOffset,yOffset);
        xo = xOffset;
        yo = yOffset;
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                getTile(x,y).render(screen,this,x<<3,y<<3);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if(0>x || x>=width || 0>y || y >= height) return Tile.VOID;
        return Tile.tiles[tiles[x+y*width]];
    }
    public void tick(){
            enemies=0;
           for(Entity e: entities){
                if(e.getClass().getSimpleName().equals("Enemy")){
                    enemies++;
                }
           }
        ArrayList toRemove = new ArrayList();
        
        //Tick all entities
        for(Entity e : entities){
            if(e.exists){
                e.tick();
            } else {
                toRemove.add(e);
            }
        }
        
        //Tick all projectiles
        for (Projectile p : projectiles){
            if(p.exists){
                p.tick();
            } else {
                toRemove.add(p);
            }
        }
        
        //Remove non-existing entities
        for( Object e : toRemove){
            entities.remove(e);
            projectiles.remove(e);
        }
        
        //Tick all tiles
        for(Tile t : Tile.tiles){
            if(t==null){
                break;
            }
            t.tick();
        }
        checkLevelCollisions();
    }
    
    public void addEntity(Entity entity){
        this.entities.add(entity);
    }
    public void addProjectile(Projectile e){
        this.projectiles.add(e);
    }

    
   public void renderEntities(Screen screen){
       //Render these entities to a specific location
       for (Entity e : entities){
           if(e.exists){
           e.render(screen);
           }
       }
       for (Projectile p: projectiles){
           if(p.exists){
               p.render(screen);
           }
       }
   }
      public void checkLevelCollisions(){
       //System.out.println("Number of entities:"+entities.size()+", Number of projectiles: "+projectiles.size());
          playertakingdamage = false;
       Rectangle swordhit = new Rectangle(0,0,0,0);
       Rectangle playerrect = new Rectangle(0,0,0,0);
       for(Entity e: entities){
           if(e.getClass().getSimpleName().equals("Player")){
                if(((Player)e).animation==2){
                   swordhit = new Rectangle(e.x*2-xo*2,e.y*2-yo*2,25,20);
                }
                playerrect = new Rectangle(e.x*2-xo*2,e.y*2-yo*2-20,16*2,16*3);
           }
       }
       for(Entity e: entities){
           if(!e.getClass().getSimpleName().equals("Player")){
           Rectangle r1 = new Rectangle(e.x*2-xo*2-16,e.y*2-yo*2-20,16*3,16*3+10);
           //System.out.println("mob:"+r1.x+","+r1.y+" sword:"+swordhit.x+","+swordhit.y);
           //Don't remember how your old code looked, but I'm pretty sure it was a scope error here.
           //I believe the data from swordhit was being thrown away because the loop was broken.
           if(r1.intersects(swordhit) && !e.dying){
                e.die();
                points++;
                spawn=true;
                break;
           }
           if(r1.intersects(playerrect)&& !e.dying && !playertakingdamage){
               playertakingdamage = true;
           }
           for(Projectile p : projectiles){
               Rectangle r2 = new Rectangle(p.x*2-xo*2 + 4, p.y*2-yo*2+4, 8, 8);
               if(r1.intersects(r2) && !e.dying){
                   //System.out.println(r1.x+","+r1.y+"--"+r2.x+","+r2.y);
                   e.die();
                   points++;
                   if(p.type==1){
                   p.exists=false;
                   }
                   spawn = true;
               }
           }
           }
       }
   }
   public void spawn(){
       if(spawn){
           if(enemies<=10){
           int xspawn = (int)(Math.random()*width*8);
           int numspawn = (int)(Math.random()*4);
           if(numspawn==0) numspawn++;
           int c1 = (((int) (Math.random()*5))*100);
           int c2 = (((int) (Math.random()*5))*10);
           int c3 = (int) (Math.random()*5);
           int col = c1+c2+c3;
           System.out.println(col);
           for(int i=0;i<numspawn;i++){
            entities.add(new Enemy(this,xspawn,10,col));
           }
            entities.add(new Portal(this,"portal",xspawn,10,2));
            System.out.println("Portal created at: "+xspawn+",10. Level:"+this.width);
           spawn=false;
           }
       }
   }
}