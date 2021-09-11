
package langtonant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LangtonAnt {
    
    private static final int DIM_VENTANA=700;
    private static final int DIM_TOOLS=200;
    
    private static final int NUM_CELDAS=1000;
    private static final int DIM_CELDA = 5;
    
    private static final int DIM_SIMUL_IMG=DIM_CELDA*NUM_CELDAS+NUM_CELDAS-1;
    
    private static final int RGB_RED=0xFF0000;
    private static final int RGB_WHITE=0xFFFFFF;
    private static final int RGB_BLACK=0x000000;
    private static final int X=0;
    private static final int Y=1;
    private static final int POS_WHITE=0;
    private static final int POS_BLACK=1;
    

    public static void main(String[] args) {
        /****  GUI  INITIALIZATION ****/
        WindowSimul win = new WindowSimul(DIM_VENTANA,DIM_TOOLS);
        win.initWindowComp();
        win.setVisible(true);
        win.initWorld();
        win.startSimulation();
        
        /*AQUI INICIA TODA LA LOGICA DEL PROGRAMA*/
        /*
        Ant ant = new Ant(NUM_CELDAS,NUM_CELDAS);
        int ant_x;
        int ant_y;
        int img_ant_x;
        int img_ant_y;
        byte[][] mundo = new byte[NUM_CELDAS][NUM_CELDAS];
        
                            while(!win.ant_placed){
                                win.sim_view.muestraMundo(buffImg);
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if(win.tool.hormiga_orientacion.getSelectedItem()=="Derecha")
                                ant.setPosOri(win.temp_ant_x, win.temp_ant_y, 'R');
                            else if(win.tool.hormiga_orientacion.getSelectedItem()=="Izquierda")
                                ant.setPosOri(win.temp_ant_x, win.temp_ant_y, 'L');
                            else if(win.tool.hormiga_orientacion.getSelectedItem()=="Arriba")
                                ant.setPosOri(win.temp_ant_x, win.temp_ant_y, 'U');
                            else
                                ant.setPosOri(win.temp_ant_x, win.temp_ant_y, 'D');
        
        ant_x = ant.pos_actual[X];
        ant_y = ant.pos_actual[Y];
        img_ant_x = ant_x*DIM_CELDA+ant_x;
        img_ant_y = ant_y*DIM_CELDA+ant_y;
        //Pinta la hormiga
        imgDraw.setColor(Color.RED);
        imgDraw.fillRect(img_ant_x, img_ant_y, 4,4);
        
        
        int i=0;
        while(true){
            //Obten posicion actual de la hormiga
            ant_x = ant.pos_actual[X];
            ant_y = ant.pos_actual[Y];
            img_ant_x = ant_x*DIM_CELDA+ant_x;
            img_ant_y = ant_y*DIM_CELDA+ant_y;
            //Pinta la hormiga
            imgDraw.setColor(Color.RED);
            imgDraw.fillRect(img_ant_x, img_ant_y, 4,4);
            
            //EN PAUSA
            while(!win.running){
                win.sim_view.muestraMundo(buffImg);
                try {
                    Thread.sleep(17);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                            //ACTUALIZA EL MUNDO (ESTRUCTURA)
                            ant.gira(mundo[ant_x][ant_y]);
                            ant.avanza();
                            if(mundo[ant_x][ant_y]==POS_WHITE)
                                mundo[ant_x][ant_y] = POS_BLACK;//Negro
                            else
                                mundo[ant_x][ant_y] = POS_WHITE;//BLANCO
        
        
            //Muestra la simulacion
                    win.sim_view.muestraMundo(buffImg);
                    win.tool.actualizaDatos(i,1,1);
                    try {
                        Thread.sleep(17);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
                    }
            //Pinta donde estuvo CON LAS POSICIONES ANTERIORES
            if(mundo[ant_x][ant_y]==POS_WHITE)
                imgDraw.setColor(Color.BLACK);
            else
                imgDraw.setColor(Color.WHITE);
            imgDraw.fillRect(img_ant_x, img_ant_y,4,4);
            
            i++;
        }
        */
    }
}