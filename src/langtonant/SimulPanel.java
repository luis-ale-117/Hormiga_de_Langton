
package langtonant;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SimulPanel extends JPanel{
    
    private int simulDimension_x;
    private int simulDimension_y;
    private Color linesColor;
    
    //private int cell_size,x_dim,y_dim,num_celdas;
    
    SimulPanel(int dimension){
        super();
        simulDimension_x = dimension;
        simulDimension_y = dimension;
        linesColor = new Color(255,255,255);
        this.setPreferredSize(new Dimension(simulDimension_x,simulDimension_y));
    }
    public void muestraMundo(BufferedImage buffImg){
        Graphics g = this.getGraphics();
        g.drawImage(buffImg, 0, 0,simulDimension_x,simulDimension_y,this);
    }
    public void inicializaMundo(Graphics g,int cell_size,int x_dim,int y_dim, int num_celdas){
        //this.cell_size=cell_size;
        //this.x_dim= x_dim;
        //this.y_dim =y_dim;
        //this.num_celdas = num_celdas;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x_dim-1, y_dim-1);
        g.setColor(linesColor);
        for(int i=1;i<num_celdas;i++){
            g.drawLine(cell_size*i+i-1, 0, cell_size*i+i-1,y_dim-1);//Vertical
        }
        for(int i=1;i<num_celdas;i++){
            g.drawLine(0, cell_size*i+i-1, x_dim-1,cell_size*i+i-1);//Horizontal
        }
    }
    public void changeSize(int x, int y){
        simulDimension_x = x;
        simulDimension_y = y;
        this.setPreferredSize(new Dimension(x,y));
    }
    /*private void paintLines(Graphics g){
        g.setColor(linesColor);
        for(int i=1;i<num_celdas;i++){
            g.drawLine(cell_size*i+i-1, 0, cell_size*i+i-1,y_dim-1);//Vertical
        }
        for(int i=1;i<num_celdas;i++){
            g.drawLine(0, cell_size*i+i-1, x_dim-1,cell_size*i+i-1);//Horizontal
        }
    }*/
}
