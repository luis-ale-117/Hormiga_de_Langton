
package langtonant;

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
    }
}