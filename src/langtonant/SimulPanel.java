
package langtonant;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SimulPanel extends JPanel{
    
    private static final Color Gray = new Color(210,210,210);
    
    private int simulDimension_x;
    private int simulDimension_y;
    private Color linesColor;
    private BufferedImage simImg;
    private Graphics simDraw;
    
    private int cell_size,x_dim,y_dim,num_celdas;
    SimulPanel(int dimension){
        super();
        simulDimension_x = dimension;
        simulDimension_y = dimension;
        linesColor = Gray;
        this.setPreferredSize(new Dimension(simulDimension_x,simulDimension_y));
    }
    public void muestraMundo(){
        repaint();
    }
    public void inicializaMundo(Graphics g,int cell_size,int x_dim,int y_dim, int num_celdas){
        this.cell_size=cell_size;
        this.x_dim= x_dim;
        this.y_dim =y_dim;
        this.num_celdas = num_celdas;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x_dim-1, y_dim-1);
        g.setColor(Gray);
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
    public void setSimImg(BufferedImage img){
        simImg = img;
    }
    public void setSimImgDraw(Graphics imgGr){
        simDraw = imgGr;
    }
    @Override
    public void paint(Graphics g){
        g.drawImage(simImg, 0, 0,simulDimension_x,simulDimension_y,this);
    }
    public void paintLinesWhite(){
        linesColor = Color.WHITE;
        simDraw.setColor(Color.WHITE);
        for(int i=1;i<num_celdas;i++){
            simDraw.drawLine(cell_size*i+i-1, 0, cell_size*i+i-1,y_dim-1);//Vertical
        }
        for(int i=1;i<num_celdas;i++){
            simDraw.drawLine(0, cell_size*i+i-1, x_dim-1,cell_size*i+i-1);//Horizontal
        }
    }
    public void paintLinesGray(){
        linesColor = Gray;
        simDraw.setColor(Gray);
        for(int i=1;i<num_celdas;i++){
            simDraw.drawLine(cell_size*i+i-1, 0, cell_size*i+i-1,y_dim-1);//Vertical
        }
        for(int i=1;i<num_celdas;i++){
            simDraw.drawLine(0, cell_size*i+i-1, x_dim-1,cell_size*i+i-1);//Horizontal
        }
    }
    public boolean linesColorWhite(){
        return linesColor.equals(Color.WHITE);
    }
    public boolean linesColorGray(){
        return linesColor.equals(Gray);
    }
    public void resetWorldWin(){
        simDraw.setColor(Color.WHITE);
        simDraw.fillRect(0, 0, x_dim-1, y_dim-1);
    }
    public void setWorldCells(World w){
        for(int x=0;x<num_celdas;x++){
            for(int y=0;y<num_celdas;y++){
                if(w.world[y][x]==0){
                    simDraw.setColor(Color.WHITE);
                }
                else{
                    simDraw.setColor(Color.BLACK);
                }
                simDraw.fillRect(x*(cell_size+1),y*(cell_size+1), 4,4);
            }
        }
        simDraw.setColor(Color.RED);
        for(Ant a: w.ants){
                simDraw.fillRect(a.getX()*(cell_size+1),a.getY()*(cell_size+1), 4,4);
        }
    }
}
