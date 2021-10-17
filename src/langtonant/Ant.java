
package langtonant;

import java.awt.Color;

public class Ant {
    private static final byte POS_WHITE=0;
    private static final byte POS_BLACK=1;
    
    public int[] pos_actual = new int[2];
    public int[] pos_anterior = new int[2];
    public byte color_siguiente,color_actual;
    public char orientacion;
    private static int limit_x;
    private static int limit_y;
    public Color neg_90_der,bla_90_izq;
    
    Ant(){
        pos_actual[0]=10;//x
        pos_actual[1]=10;//y
        pos_anterior[0]=10;
        pos_anterior[1]=10;
        orientacion = 'U';//L:left, R:right, U:up, D:down
        color_actual = 0;
        color_siguiente = 1;
        limit_x = 1000;
        limit_y = 1000;
        neg_90_der = Color.BLACK;
        bla_90_izq = Color.WHITE;
    }
    Ant(int lim_x, int lim_y){
        pos_actual[0]=0;//x
        pos_actual[1]=0;//y
        pos_anterior[0]=0;
        pos_anterior[1]=0;
        orientacion = 'U';//L:left, R:right, U:up, D:down
        color_actual = 0;
        color_siguiente = 1;
        limit_x = lim_x;
        limit_y = lim_y;
    }
    
    Ant(int x,int y,char ori, int lim_x, int lim_y,byte color){
        pos_actual[0]=x;//x
        pos_actual[1]=y;//y
        pos_anterior[0]=x;
        pos_anterior[1]=y;
        orientacion = ori;//L:left, R:right, U:up, D:down
        color_actual = color;
        color_siguiente = color==POS_WHITE?POS_BLACK:POS_WHITE;
        limit_x = lim_x;
        limit_y = lim_y;
    }
    
    public void gira(int c){
        int color= this.color_actual;
        if(color==POS_WHITE){//Blanco
            if(orientacion=='U'){
                orientacion='R';
            }else if(orientacion=='R'){
                orientacion = 'D';
            }else if(orientacion=='D'){
                orientacion = 'L';
            }else{
                orientacion = 'U';
            }
        }else{
            if(orientacion=='U'){
                orientacion='L';
            }else if(orientacion=='L'){
                orientacion = 'D';
            }else if(orientacion=='D'){
                orientacion = 'R';
            }else{
                orientacion = 'U';
            }
        }
    }
    public void avanza(){
        pos_anterior[0]=pos_actual[0];
        pos_anterior[1]=pos_actual[1];
        if(orientacion=='U'){//Y
            pos_actual[1]--;
            //pos_actual[1] = pos_actual[1]>=0? pos_actual[1]:limit_y-1;
        }else if(orientacion=='L'){//X
            pos_actual[0]--;
            //pos_actual[0] = pos_actual[0]>=0? pos_actual[0]:limit_x-1;
        }else if(orientacion=='D'){//Y
            pos_actual[1]++;
            //pos_actual[1] = pos_actual[1]<limit_y? pos_actual[1]:0;
        }else{
            pos_actual[0]++;//X
            //pos_actual[0] = pos_actual[0]<limit_x? pos_actual[0]:0;
        }
    }
    public void posInToroidal(){
        pos_actual[1] = pos_actual[1]>=0? pos_actual[1]:limit_y-1;
        pos_actual[1] = pos_actual[1]<limit_y? pos_actual[1]:0;
        pos_actual[0] = pos_actual[0]>=0? pos_actual[0]:limit_x-1;
        pos_actual[0] = pos_actual[0]<limit_x? pos_actual[0]:0;
    }
    public void setPos(int x,int y){
        pos_actual[0] = x;
        pos_actual[1] = y;
    }
    public void setOri(char ori){
        orientacion = ori;
    }
    public void setPosOri(int x,int y,char ori){
        pos_actual[0] = x;
        pos_actual[1] = y;
        orientacion = ori;
    }
    public void setColorActual(byte color){
        color_actual = color;
        color_siguiente = color==POS_WHITE?POS_BLACK:POS_WHITE;
    }
    public void setColorSiguiente(byte color){
        color_siguiente = color;
        color_actual = color==POS_WHITE?POS_BLACK:POS_WHITE;
    }
    public int getX(){
        return pos_actual[0];
    }
    public int getY(){
        return pos_actual[1];
    }
    public int getX_antes(){
        return pos_anterior[0];
    }
    public int getY_antes(){
        return pos_anterior[1];
    }
    public byte getColorSiguiente(){
        return color_siguiente;
    }
    public boolean isOutOfLimits(){
        return pos_actual[0]>= limit_x || pos_actual[1]>=limit_y||pos_actual[0]<0||pos_actual[1]<0;
    }
    public void setNeg90der(Color neg90){
        neg_90_der = neg90;
    }
    public void setBla90izq(Color bla90){
        bla_90_izq = bla90;
    }
    public int getNeg90_RGB(){
        return neg_90_der.getRGB();
    }
    public int getBla90_RGB(){
        return bla_90_izq.getRGB();
    }
}
