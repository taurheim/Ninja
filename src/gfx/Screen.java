
package gfx;

public class Screen {
    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;
    
    public int[] pixels;
    
    public static final byte BIT_MIRROR_X = 0x01;
    public static final byte BIT_MIRROR_Y = 0x02;
    
    public int xOffset = 0;
    public int yOffset = 0;
    
    public int width;
    public int height;
    
    public SpriteSheet sheet;
    
    public Screen(int width, int height, SpriteSheet sheet){
        this.width = width;
        this.height = height;
        this.sheet = sheet;
        
        //Set what colors on sheet correspond to what colors
        pixels = new int[width*height];
    }
    public void setOffset(int xOffset, int yOffset) { this.xOffset = xOffset; this.yOffset = yOffset; }
    public void render(int xPos, int yPos, int tile, int color, int mirrorDir, int scale){
        xPos -= xOffset;
        yPos -= yOffset;
        
        //1&0 = 0, 1&1 = 1
        boolean mirrorX= (mirrorDir & BIT_MIRROR_X)>0;
        boolean mirrorY= (mirrorDir & BIT_MIRROR_Y)>0;
        
        int scaleMap = scale-1;
        //tile is which tile on the sheet it is (tile index)
        int xTile = tile % 32;
        int yTile = tile / 32; 
        //each tile is 8 pixels (2^3)
        int tileOffset = (xTile <<3) + (yTile <<3) * sheet.width;
        for(int y=0;y<8;y++){
            int ySheet = y;
            if(mirrorY)ySheet=7-y; //Instead of going from 0 to 7, it will go from 7 to 0
            int yPixel = y+yPos + (y*scaleMap)-((scaleMap<<3)/2);
            for(int x=0;x<8;x++){
                int xSheet = x;
                if(mirrorX) xSheet=7-x;
                
                int xPixel = x + xPos + (x*scaleMap)-((scaleMap<<3)/2);
                
                //Gives color data for where we are
                int col = (color >> (sheet.pixels[xSheet+ySheet*sheet.width + tileOffset]*8))&255;
                
                if(col<255){
                    for(int yScale = 0;yScale<scale;yScale++){
                        if(yPixel+yScale <0 || yPixel+yScale >= height) continue; //check to make sure its in bounds
                        for (int xScale = 0;xScale<scale;xScale++){
                            if(xPixel+xScale<0 || xPixel+xScale >= width) continue; //check to make sure its in bounds
                            pixels[(xPixel+xScale)+(yPixel+yScale)*width] = col;
                        }
                    }
                }
            }
        }
    }
}
