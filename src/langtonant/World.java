
package langtonant;

import java.util.ArrayList;

public class World {
    
    private static final byte POS_WHITE=0;
    private static final byte POS_BLACK=1;
    
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
        ants = new ArrayList<Ant>();
    }
    public void addAnt(){
        ants.add(new Ant(lim_x,lim_y));
    }
    public void addAnt(int x, int y,char ori,byte color){
        ants.add(new Ant(x,y,ori,lim_x,lim_y,color));
    }
    public void addAnt(Ant a){
        ants.add(a);
    }
    public boolean isToroidal(){
        return toroidal;
    }
    public void turnAnts(){
        for(Ant ant: ants)
            ant.gira(world[ant.getX()][ant.getY()]);
    }
    public void moveAnts(){
        for(Ant ant: ants){
            ant.avanza();
            anyAntOutOfBounds = anyAntOutOfBounds || ant.isOutOfLimits();
        }
    }
    public void updateAntsPos(){
        for(Ant ant: ants){
            ant.gira(world[ant.getX()][ant.getY()]);
            ant.avanza();
            anyAntOutOfBounds = anyAntOutOfBounds || ant.isOutOfLimits();
            if(ant.color_siguiente != world[ant.getX_antes()][ant.getY_antes()]){
                world[ant.getX_antes()][ant.getY_antes()] = ant.color_siguiente;
            }
            if(world[ant.getX()][ant.getY()]==POS_WHITE){
                ant.setColorSiguiente(POS_BLACK);   
            }else{
                ant.setColorSiguiente(POS_WHITE);
            }
        }
    }
    public void changeAntsCellsState(){
        for(Ant ant: ants){
            if(ant.color_siguiente != world[ant.getX_antes()][ant.getY_antes()]){
                world[ant.getX_antes()][ant.getY_antes()] = ant.color_siguiente;
            }
        }
    }
    public byte getPosColor(int x, int y){
        return world[x][y];
    }
}
