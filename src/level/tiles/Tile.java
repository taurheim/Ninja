package level.tiles;

import gfx.Colors;
import gfx.Screen;
import level.Level;

/**
 *
 * @author 732897
 */
public abstract class Tile {
    public static Tile[] tiles = new Tile[256];
    
    public static final Tile VOID = new BasicSolidTile(0,0,0,Colors.get(000, -1, -1, -1),0xFF000000);
    public static final Tile STONE = new BasicSolidTile(1,1,0,Colors.get(-1,333,-1,-1),0xFF555555);
    public static final Tile GRASS = new BasicTile(2,1,0,Colors.get(-1,003,550,-1),0xFF00FF00);
    public static final Tile WATER = new AnimatedTile(3,new int[][]{{0,5},{1,5},{2,5},{1,5}},Colors.get(-1,400,511,-1),0xFF0000FF,1000);
    
    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    private int levelColor;
    
    public Tile(int id, boolean isSolid,boolean isEmitter, int levelColor){
        this.id=(byte) id;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tile id on"+id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColor = levelColor;
        tiles[id] = this;
    }
    
    //So that other classes can view the protected variables
    public byte getId(){
        return id;
    }
    
    public boolean isSolid(){
        return solid;
    }
    public boolean isEmitter(){
        return emitter;
    }
    public int getLevelColor(){
        return levelColor;
    }
    public abstract void tick();
    public abstract void render(Screen screen,Level level, int x,int y);
}
