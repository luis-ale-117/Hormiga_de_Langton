
package langtonant;

public class Ant {
    public int[] pos_actual = new int[2];
    public int[] pos_anterior = new int[2];
    public byte color_siguiente;
    public char orientacion;
    private int limit_x;
    private int limit_y;
    
    Ant(){
        pos_actual[0]=10;//x
        pos_actual[1]=10;//y
        pos_anterior[0]=10;
        pos_anterior[1]=10;
        orientacion = 'U';//L:left, R:right, U:up, D:down
        color_siguiente = 1;
        limit_x = 1000;
        limit_y = 1000;
    }
    Ant(int lim_x, int lim_y){
        pos_actual[0]=0;//x
        pos_actual[1]=0;//y
        pos_anterior[0]=0;
        pos_anterior[1]=0;
        orientacion = 'U';//L:left, R:right, U:up, D:down
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
        color_siguiente = color;
        limit_x = lim_x;
        limit_y = lim_y;
    }
    
    public void gira(int color){
        if(color==0){//Blanco
            if(orientacion=='U'){
                orientacion='L';
            }else if(orientacion=='L'){
                orientacion = 'D';
            }else if(orientacion=='D'){
                orientacion = 'R';
            }else{
                orientacion = 'U';
            }
        }else{
            if(orientacion=='U'){
                orientacion='R';
            }else if(orientacion=='R'){
                orientacion = 'D';
            }else if(orientacion=='D'){
                orientacion = 'L';
            }else{
                orientacion = 'U';
            }
        }
    }
    public void avanza(){
        pos_anterior[0]=pos_actual[0];
        pos_anterior[1]=pos_actual[0];
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
    public void setColorSiguiente(byte color){
        color_siguiente = color;
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
}
