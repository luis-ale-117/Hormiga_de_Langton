
package langtonant;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;


public class WindowSimul extends JFrame{
    
    private static final int DIM_VENTANA=700;
    private static final int DIM_TOOLS=200;
    
    private static final int NUM_CELDAS=1000;
    private static final int DIM_CELDA = 5;
    
    private static final int RGB_RED=0xFF0000;
    private static final int RGB_WHITE=0xFFFFFF;
    private static final int RGB_BLACK=0x000000;
    private static final int X=0;
    private static final int Y=1;
    private static final int POS_WHITE=0;
    private static final int POS_BLACK=1;
    
    private static int DIM_SIMUL_IMG=DIM_CELDA*NUM_CELDAS+NUM_CELDAS-1;
    
    private JScrollPane scrollpanel;
    public SimulPanel sim_view;
    public ToolsPanel tool;
    
    /*FLAGS*/
    public boolean running,ant_placed;
    public int temp_ant_x,temp_ant_y;
    
    public WindowSimul(int dimension,int barraTools){
        this.setSize(dimension+barraTools,dimension);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Langton's ant simulation");
        this.setResizable(false);
        this.setLayout(null);
    }
    
    public void initWindowComp(){
        sim_view = new SimulPanel(DIM_SIMUL_IMG);
        sim_view.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                placeAnt(e);
            }
        });
        scrollpanel = new JScrollPane(sim_view);
        scrollpanel.setViewportView(sim_view);
        scrollpanel.setBounds(0,0,DIM_VENTANA-50,DIM_VENTANA-50);
        //scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollpanel);
        tool = new ToolsPanel(DIM_VENTANA-50,DIM_TOOLS+50);
        this.add(tool);
        
        tool.start_sim.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start_button();
            }
        });
        
        tool.zoom_out.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
        
        tool.zoom_in.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        
        running = false;
        ant_placed = false;
        temp_ant_x = 0;
        temp_ant_y = 0;
    }
    
    private void start_button(){
        running = !running;
        if(running)
            tool.start_sim.setText("Pausa");
        else
            tool.start_sim.setText("Sigue");
    }
    private void placeAnt(MouseEvent e){
        temp_ant_x = ((int)e.getX())/(DIM_CELDA+1);
        temp_ant_y = ((int)e.getY())/(DIM_CELDA+1);
        ant_placed = true;
        tool.start_sim.setEnabled(true);
    }
    private void zoomOut(){
        if(DIM_SIMUL_IMG > 1000){
            DIM_SIMUL_IMG -= 400;
            sim_view.changeSize(DIM_SIMUL_IMG,DIM_SIMUL_IMG);
            scrollpanel.setViewportView(sim_view);
        }
        
    }
    private void zoomIn(){
        if(DIM_SIMUL_IMG < 18000){
            DIM_SIMUL_IMG += 400;
            sim_view.changeSize(DIM_SIMUL_IMG,DIM_SIMUL_IMG);
            scrollpanel.setViewportView(sim_view);
        }
    }
}
