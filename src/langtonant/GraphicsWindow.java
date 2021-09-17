
package langtonant;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphicsWindow extends JFrame{
    JPanel mainPanel;
    XYSeries data_black_cells;
    XYSeriesCollection black_cell_series;
    JFreeChart black_cells_graph;
    ChartPanel bc_chart;
    
    GraphicsWindow(){
        super();
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Langton's ant graphics");
        this.setResizable(false);
        this.setLayout(null);
        init_components();
    }
    private void init_components(){
        //mainPanel = new JPanel();
        //mainPanel.setBounds(0,0, 700, 700);
        //mainPanel.setBackground(Color.gray);
        //mainPanel.setLayout(null);
        //add(mainPanel);
        
        data_black_cells = new XYSeries("Celdas negras");
        data_black_cells.add(0, 0);
        /*data_black_cells.add(2, 7);
        data_black_cells.add(3, 1);
        data_black_cells.add(4, 5);
        */
        black_cell_series = new XYSeriesCollection();
        black_cell_series.addSeries(data_black_cells);
        
        black_cells_graph = ChartFactory.createXYLineChart("Celdas negras ocupadas","Generacion","Celdas",black_cell_series,PlotOrientation.VERTICAL,true,false,false);
        
        bc_chart = new ChartPanel(black_cells_graph);
        bc_chart.setBounds(0, 0, 400, 300);
        add(bc_chart);
    }
    public void updateBlackGraph(int generation, int num_black){
        data_black_cells.add(generation, num_black);
    }
}
