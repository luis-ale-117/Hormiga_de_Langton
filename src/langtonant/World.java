
package langtonant;

import java.util.ArrayList;

public class World {
    public boolean toroidal;
    public boolean antOutOfBounds;
    public byte[][] world;
    public int lim_x;
    public int lim_y;
    public ArrayList<Ant> ants;
    
    World(int celdas){
        toroidal = true;
        antOutOfBounds = false;
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
    public boolean isToroidal(){
        return toroidal;
    }
    public void giraHormigas(){
        for(Ant ant: ants)
            ant.gira(world[ant.getX()][ant.getY()]);
    }
    public void mueveHormigas(){
        for(Ant ant: ants){
            ant.avanza();
            antOutOfBounds = antOutOfBounds || ant.isOutOfLimits();
        }
    }
    public void cambiaColorCeldas(){
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
