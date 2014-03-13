/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import gfx.Screen;
import level.Level;

public class BasicTile extends Tile{
    
    protected int tileId;
    protected int tileColor;
    
    public BasicTile(int id,int x, int y, int tileColour, int levelColor){
        super(id,false,false,levelColor);
        this.tileId = x + y*32;
        this.tileColor = tileColour;
    }
    public void tick(){}
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, tileColor,0x00,1);
    }
}
