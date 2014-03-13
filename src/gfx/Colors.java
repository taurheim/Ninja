/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gfx;

/**
 *
 * @author 732897
 */
public class Colors {
    //Convert color into a super long int.
    public static int get(int color1, int color2, int color3, int color4){
        //Return one long number with all 4 different colours we're parsing (2^8 bits each)
        return (get(color4)<<24) + (get(color3)<<16) + (get(color2)<<8) + get(color1);
    }
    private static int get(int color){
        //Explanation @ 9:30 of ep.5 Colour & Rendering Optimisation
        if(color<0) return 255;
        int r= color/100%10;
        int g = color/10 %10;
        int b = color%10;
        return r*36 + g*6 + b;
    }
}
