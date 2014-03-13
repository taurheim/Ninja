/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dstealth.entities;

import gfx.Colors;
import gfx.Screen;
import level.Level;


public class Portal extends Entity{
    
    private int animationframe = 0;
    private int animationtimer = 0;
    private int color = Colors.get(-1,0,222,555);
    protected int scale = 1;
    
    public Portal(Level level,String name,int x, int y, int scale){
        super(level);
        this.x = x;
        this.y = y;
        this.scale = scale;
    }
    public void tick() {
        animationtimer++;
        if(animationtimer%10==0){
            animationframe+=2;
        }
        if(animationframe==6){
             animationframe=0;
        }
        if(animationtimer>=200){
            this.exists=false;
        }
    }

    public void render(Screen screen) {
        int xOffset = screen.xOffset;
        int yOffset = screen.yOffset;
        int swordcolor = Colors.get(-1,0,315,315);
                screen.render(x,y,animationframe+3*32,swordcolor,0x00,scale);//topleft
                screen.render(x+8*scale,y,animationframe+1+3*32,swordcolor,0x00,scale); //topright
                screen.render(x,y+8*scale,animationframe+4*32,swordcolor,0x00,scale);//btmleft
                screen.render(x+8*scale,y+8*scale,animationframe+1+4*32,swordcolor,0x00,scale);//btmright
    }

    public void flicker() {
        //Nothing
    }

    @Override
    public void die() {
        this.exists=false;
    }
    
}
