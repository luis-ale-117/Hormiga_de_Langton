
package langtonant;

import java.awt.Color;
import java.util.ArrayList;

public class World {
    
    private static final byte POS_WHITE=0;
    private static final byte POS_BLACK=1;
    
    public int num_black;
    
    public boolean toroidal;
    public boolean anyAntOutOfBounds;
    public byte[][] world;
    public int lim_x;
    public int lim_y;
    public ArrayList<Ant> ants;
    
    World(int celdas){
        toroidal = true;
        anyAntOutOfBounds = false;
        world = new byte[celdas][celdas];
        lim_x = celdas;
        lim_y = celdas;
        ants = new ArrayList<Ant>(100);
        num_black = 0;
    }
    public void addAnt(){
        ants.add(new Ant(lim_x,lim_y));
    }
    public void addAnt(int x, int y,char ori,byte color,Color neg,Color bla){
        ants.add(new Ant(x,y,ori,lim_x,lim_y,color));
        ants.get(ants.size()-1).setBla90izq(bla);
        ants.get(ants.size()-1).setNeg90der(neg);
    }
    public void addAnt(Ant a){
        ants.add(a);
    }
    public void setToroidalWorld(boolean tor){
        toroidal=tor;
    }
    public boolean isToroidal(){
        return toroidal;
    }
//    public void turnAnts(){
//        ants.forEach(ant -> {
//            ant.gira(world[ant.getX()][ant.getY()]);
//        });
//    }
    public void updateAntsPos(){
        for(Ant ant: ants){
            ant.gira(world[ant.getY()][ant.getX()]);
            /*Where it is the ant, update color*/
            if(world[ant.getY()][ant.getX()] == ant.color_actual){
                world[ant.getY()][ant.getX()] = ant.color_siguiente;
                if(ant.color_siguiente == POS_BLACK){
                    num_black++;
                }else{
                    num_black--;
                }
            }
            /*Ant moves*/
            ant.avanza();
            /*Ant updates its current position color*/
            try {
                ant.setColorActual(world[ant.getY()][ant.getX()]);
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                if(isToroidal()){
                    ant.posInToroidal();
                    ant.setColorActual(world[ant.getY()][ant.getX()]);
                }else{
                    anyAntOutOfBounds = true;
                }
            }
        }
    }
    public boolean antsOutBounds(){
        return anyAntOutOfBounds;
    }
    public byte getPosColor(int x, int y){
        return world[y][x];
    }
    public void setPosColor(int x, int y, byte col){
        byte color_anterior = (byte)world[y][x];
        world[y][x] = col;
        if(color_anterior!=col){
            if(col == POS_WHITE){
                num_black--;
            }else{
                num_black++;
            }
        }
    }
    public int getNumAnts(){
        return ants.size();
    }
    public int getNumBlack(){
        return num_black;
    }
    public void resetWorld(){
        for(int x = 0;x<lim_x;x++){
            for(int y = 0;y<lim_x;y++){
                world[y][x]=0;
            }
        }
        anyAntOutOfBounds = false;
        ants.clear();
        num_black = 0;
    }
    public void randomInit(double porcenBlack,int numAnts){
        //resetWorld();
        anyAntOutOfBounds = false;
        ants.clear();
        num_black = 0;
        porcenBlack *=0.01;
        for(int x = 0;x<lim_x;x++){
            for(int y = 0;y<lim_x;y++){
                if(Math.random()<=porcenBlack){
                    world[y][x]=(byte)1;
                    num_black++;
                }else{
                    world[y][x]=(byte)0;
                }
            }
        }
        for(int i = 0;i<numAnts;i++){
            createRandomAnt();
        }
    }
    private void createRandomAnt(){
        int randX = (int)(Math.random()*lim_x);
        int randY = (int)(Math.random()*lim_y);
        double aux_ori = Math.random();
        char randOri;
        if(aux_ori<0.25){
            randOri = 'U';
        }else if(aux_ori<0.5){
            randOri = 'D';
        }else if(aux_ori<0.75){
            randOri = 'R';
        }else{
            randOri = 'L';
        }
        Color col_neg = getRandColorNeg();
        Color col_bla = getRandColorBla();
        addAnt(randX,randY,randOri,world[randY][randX],col_neg,col_bla);
    }
    public void recalculateNumBlack(){
        num_black=0;
        for(byte[] fila: world){
            for(byte b: fila){
                num_black+=(int)b;
            }
        }
    }
    public void delAnts(){
        ants.clear();
    }
    private Color getRandColorBla(){//Mas claros
        int R = (int)(Math.random()*128)+127;
        int G = (int)(Math.random()*128)+127;
        int B = (int)(Math.random()*128)+127;
        return new Color(R,G,B);
    }
    private Color getRandColorNeg(){//Mas oscuros
        int R = (int)(Math.random()*128);
        int G = (int)(Math.random()*128);
        int B = (int)(Math.random()*128);
        return new Color(R,G,B);
    }
}
