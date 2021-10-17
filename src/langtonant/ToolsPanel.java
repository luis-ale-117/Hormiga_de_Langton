
package langtonant;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class ToolsPanel extends JPanel{
    
    public int celTotales;
    public JLabel generacion,celdas_negras,hormigas,zo_label,vel_label;
    public JButton start_sim, new_ant,graph,reset,random;
    public JToggleButton edit_cell;
    public JSlider zoom_sld,vel_sld;
    
    //public JComboBox hormiga_orientacion;
    
    ToolsPanel(int dimensionSimul, int dimenTool){
        super();
        this.setBounds(dimensionSimul,0, dimenTool, dimensionSimul);
        this.setBackground(Color.gray);
        this.setLayout(null);
        initComponents();
        
        celTotales = dimensionSimul*dimensionSimul;
    }
    
    private void initComponents(){
        generacion = new JLabel("Generacion: ");
        generacion.setBounds(5, 0, 180, 30);
        this.add(generacion);
        
        celdas_negras = new JLabel("Celdas negras: ");
        celdas_negras.setBounds(5, 31, 180, 30);
        this.add(celdas_negras);
        
        hormigas = new JLabel("Hormigas: ");
        hormigas.setBounds(5, 62, 120, 30);
        this.add(hormigas);
        
        start_sim = new JButton("Inicia");
        start_sim.setBounds(5, 93, 80, 30);
        start_sim.setEnabled(false);
        this.add(start_sim);
        
        new_ant = new JButton("Agrega");
        new_ant.setBounds(90, 93, 90, 30);
        new_ant.setEnabled(true);
        this.add(new_ant);
        
        /*hormiga_orientacion = new JComboBox(new String[]{"Derecha","Izquierda","Arriba","Abajo"});
        hormiga_orientacion.setBounds(90, 93, 90, 30);
        this.add(hormiga_orientacion);*/
        
        zoom_sld = new JSlider(-3,3,0);
        zoom_sld.setMajorTickSpacing(1);
        zoom_sld.setMinorTickSpacing(1);
        zoom_sld.setPaintTicks(true);
        zoom_sld.setPaintLabels(true);
        zoom_sld.setBounds(5, 124, 160, 50);
        zoom_sld.setBackground(Color.GRAY);
        this.add(zoom_sld);
        zo_label = new JLabel("ZOOM");
        zo_label.setBounds(168, 130, 70, 30);
        this.add(zo_label);
        
        vel_sld = new JSlider(-3,3,0);
        vel_sld.setMajorTickSpacing(1);
        vel_sld.setMinorTickSpacing(1);
        vel_sld.setPaintTicks(true);
        vel_sld.setPaintLabels(true);
        vel_sld.setBounds(5, 174, 160, 50);
        vel_sld.setBackground(Color.GRAY);
        this.add(vel_sld);
        vel_label = new JLabel("SPEED");
        vel_label.setBounds(168, 180, 70, 30);
        this.add(vel_label);

        graph = new JButton("Graficas");
        graph.setBounds(5, 230, 90, 30);
        this.add(graph);
        
        reset = new JButton("Reset");
        reset.setBounds(100, 230, 80, 30);
        this.add(reset);
        
        random = new JButton("Random");
        random.setBounds(5, 265, 90, 30);
        this.add(random);
        
        edit_cell = new JToggleButton("Edit");
        edit_cell.setBounds(5, 300, 90, 30);
        this.add(edit_cell);
    }
    
    public void actualizaDatos(int gen,int cant_negras, int cant_hormigas){
        generacion.setText("Generacion: "+gen);
        celdas_negras.setText("Celdas negras: "+cant_negras);
        hormigas.setText("Hormigas: "+cant_hormigas);
    }
}