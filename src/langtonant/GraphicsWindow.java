
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
    XYSeries data_black_cells,log_data_black_cells;
    XYSeriesCollection black_cell_series,log_black_cell_series;
    JFreeChart black_cells_graph,log_black_cells_graph;
    ChartPanel bc_chart,log_bc_chart;
    
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
        
        data_black_cells = new XYSeries("Celdas negras");
        black_cell_series = new XYSeriesCollection();
        black_cell_series.addSeries(data_black_cells);
        black_cells_graph = ChartFactory.createXYLineChart("Celdas negras ocupadas","Generacion","Celdas",black_cell_series,PlotOrientation.VERTICAL,true,false,false);
        bc_chart = new ChartPanel(black_cells_graph);
        bc_chart.setBounds(0, 0, 400, 300);
        add(bc_chart);
        
        log_data_black_cells = new XYSeries("Celdas negras en log");
        log_black_cell_series = new XYSeriesCollection();
        log_black_cell_series.addSeries(log_data_black_cells);
        log_black_cells_graph = ChartFactory.createXYLineChart("Logaritmo celdas negras","Generacion","Log celdas",log_black_cell_series,PlotOrientation.VERTICAL,true,false,false);
        log_bc_chart = new ChartPanel(log_black_cells_graph);
        log_bc_chart.setBounds(0, 305, 400, 300);
        add(log_bc_chart);
    }
    public void updateGraphs(int generation, int num_black){
        data_black_cells.add(generation, num_black);
        if(Math.log10(num_black)<0)
            log_data_black_cells.add(generation,0.1);
        else log_data_black_cells.add(generation,Math.log10(num_black));
    }
}
