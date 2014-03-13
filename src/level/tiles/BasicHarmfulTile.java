package level.tiles;

public class BasicHarmfulTile extends BasicTile{
    public BasicHarmfulTile(int id,int x, int y, int tileColour, int levelColor){
        super(id,x,y,tileColour,levelColor);
        this.solid=true;
    }
}