
package langtonant;

public class LangtonAnt {
    
    private static final int DIM_VENTANA=700;
    private static final int DIM_TOOLS=200;

    public static void main(String[] args) {
        /****  GUI  INITIALIZATION ****/
        WindowSimul win = new WindowSimul(DIM_VENTANA,DIM_TOOLS);
        win.initWindowComp();
        win.setVisible(true);
        win.initWorld();
        win.startSimulation();
    }
}