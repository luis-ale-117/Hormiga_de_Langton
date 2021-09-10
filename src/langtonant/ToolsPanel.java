
package langtonant;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ToolsPanel extends JPanel{
    
    public int celTotales;
    public JLabel generacion,celdas_negras,hormigas;
    public JButton start_sim,zoom_out,zoom_in, new_ant;
    public JComboBox hormiga_orientacion;
    
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
        generacion.setBounds(5, 0, 120, 30);
        this.add(generacion);
        
        celdas_negras = new JLabel("Celdas negras: ");
        celdas_negras.setBounds(5, 31, 120, 30);
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
        
        zoom_out = new JButton("Zoom -");
        zoom_out.setBounds(5, 124, 80, 30);
        zoom_out.setEnabled(true);
        this.add(zoom_out);
        
        zoom_in = new JButton("Zoom +");
        zoom_in.setBounds(90, 124, 80, 30);
        zoom_in.setEnabled(true);
        this.add(zoom_in);
        /*
        sanas = new JLabel("Sanas: ");
        sanas.setBounds(5, 155, 120, 30);
        this.add(sanas);
        
        porcentajeSanas = new JLabel("%Sanas: ");
        porcentajeSanas.setBounds(5, 186, 120, 30);
        this.add(porcentajeSanas);
        */
    }
    
    public void actualizaDatos(int gen,int cant_negras, int cant_hormigas){
        generacion.setText("Generacion: "+gen);
        celdas_negras.setText("Celdas negras: "+cant_negras);
        hormigas.setText("Hormigas: "+cant_hormigas);
        /*infecB.setText("Tipo B: "+tipB);
        muertas.setText("Muertas: "+muert);
        sanas.setText("Sanas: "+sana);
        porcentajeSanas.setText("%Sanas: "+(sana*100.0/celTotales));
        */
    }
}