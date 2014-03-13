package level.tiles;

public class BasicSolidTile extends BasicTile{
    public BasicSolidTile(int id,int x, int y, int tileColour, int levelColor){
        super(id,x,y,tileColour,levelColor);
        this.solid=true;
    }
}
