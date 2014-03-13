/*
 * http://www.youtube.com/watch?v=o7pfq0W3e4I&feature=player_detailpage#t=938s
 */
package gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author 732897
 */
public class SpriteSheet {
    public String path;
    public int width;
    public int height;
    public int[] pixels;
    
    public SpriteSheet(String path){
        BufferedImage image = null;
        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (IOException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(image == null){
            return;
        }
        
        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();
        
        pixels = image.getRGB(0,0,width,height,pixels,0,width);
        
        for (int i=0;i<pixels.length;i++){
            pixels[i] = (pixels[i] & 0xff)/(64);//Removes the alpha channel AARRGGBB
            //puts a 0,1,2,3 or 4 into the spritesheet
        }
    }
}
