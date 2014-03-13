
package pkg2dstealth.entities;

import gfx.Screen;
import java.awt.Rectangle;
import level.Level;

public abstract class Entity {
    
    public int x,y;
    protected Level level;
    public boolean exists = true;
    public boolean dying = false;
    
    public Entity(Level level){
        init(level);
    }
    
    public final void init(Level level){
        this.level =level;
    }
    
    public abstract void tick();
    
    public abstract void render(Screen screen);
    public abstract void flicker();
    public abstract void die();
}
