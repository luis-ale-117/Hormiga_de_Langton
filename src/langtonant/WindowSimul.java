
package langtonant;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


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
    private static final byte POS_WHITE=0;
    private static final byte POS_BLACK=1;
    private static final String[] ORIENTACIONES={"Derecha","Izquierda","Arriba","Abajo"};
    private static final String[] WORLD_TYPES={"Toroidal","Finito"};
    
    private static int DIM_SIMUL_IMG=DIM_CELDA*NUM_CELDAS+NUM_CELDAS-1;
    
    private JScrollPane scrollpanel;
    public SimulPanel sim_view;
    public ToolsPanel tool;
    public GraphicsWindow gr;
    
    /*FLAGS*/
    public boolean running,place_ant,graphs_updating;
    public Ant temp_ant;
    public int temp_ant_x,temp_ant_y;
    public byte temp_ant_color;
    public char temp_ant_ori;
    public String ori_new_ant;
    
    public World world;
    public BufferedImage worldImg;
    public Graphics worldDraw;
    
    private int generation;
    
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
        this.add(scrollpanel);
        tool = new ToolsPanel(DIM_VENTANA-50,DIM_TOOLS+50);
        this.add(tool);
        
        
        gr =  new GraphicsWindow();
        
        tool.start_sim.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start_button();
            }
        });
        
        tool.new_ant.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                createAnt();
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
        
        tool.graph.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                showGraphicsWin();
            }
        });
        tool.reset.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSim();
            }
        });
        tool.random.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                randomSim();
            }
        });
        
        running = false;
        place_ant = false;
        graphs_updating = false;
        
        temp_ant = null;
        temp_ant_x = 0;
        temp_ant_y = 0;
        temp_ant_ori = 'U';
        temp_ant_color = POS_WHITE;
        
        world = new World(NUM_CELDAS);
        worldImg = new BufferedImage(DIM_SIMUL_IMG,DIM_SIMUL_IMG,BufferedImage.TYPE_INT_RGB);
        worldDraw = worldImg.createGraphics();
        
        generation =0;
    }
    
    public void initWorld(){
        sim_view.inicializaMundo(worldDraw, DIM_CELDA, DIM_SIMUL_IMG, DIM_SIMUL_IMG, NUM_CELDAS);
        sim_view.setSimImg(worldImg);
        sim_view.setSimImgDraw(worldDraw);
        sim_view.muestraMundo();
    }
    
    public void startSimulation(){
        chooseKindOfWorld();
        while(true){
            /*PINTA LAS HORMIGAS*/
            paintAnts();
            gr.updateGraphs(generation, world.getNumBlack(),graphs_updating);
            /*EN PAUSA SI LO ESTA*/
            ifSimPaused();
            world.updateAntsPos();
            sim_view.muestraMundo();
            tool.actualizaDatos(generation,world.getNumBlack(),world.getNumAnts());
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*PINTA LAS CELDAS DONDE ESTUVIERON LAS HORMIGAS*/
            paintCellsAndLines();
            ifSimEnded();
            generation++;
        }
    }
    
    private void paintCellsAndLines(){
        for(Ant a: world.ants){
            if(world.world[a.getX_antes()][a.getY_antes()]==POS_WHITE)
                worldDraw.setColor(Color.WHITE);
            else
                worldDraw.setColor(Color.BLACK);
            worldDraw.fillRect(a.getX_antes()*(DIM_CELDA+1),a.getY_antes()*(DIM_CELDA+1), 4,4);
        }
        paintLinesIfNeeded();
    }
    private void paintLinesIfNeeded(){
        if(DIM_SIMUL_IMG > 5000 && sim_view.linesColorWhite()){
            sim_view.paintLinesGray();
        }
        if(DIM_SIMUL_IMG < 5000 && sim_view.linesColorGray()){
            sim_view.paintLinesWhite();
        }
    }
    private void paintAnts(){
        for(Ant a: world.ants){
            worldDraw.setColor(Color.RED);
            worldDraw.fillRect(a.getX()*(DIM_CELDA+1),a.getY()*(DIM_CELDA+1), 4,4);
        }
    }
    private void ifSimPaused(){
        while(!running){
            sim_view.muestraMundo();
            paintLinesIfNeeded();
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void ifSimEnded(){
        if(world.antsOutBounds() && !world.isToroidal()){
            JOptionPane.showMessageDialog(this,
            "Simulación finalizada porque una o más hormigas llegaron\na los límites del mundo establecido. Inicie otra.",
            "Fin de la simulación",
            JOptionPane.WARNING_MESSAGE);
            running = false;
            tool.start_sim.setText("Sigue");
            tool.start_sim.setEnabled(false);
            
            tool.new_ant.setEnabled(false);
            ifSimPaused();
        }
    }
    
    private void chooseKindOfWorld(){
        String worldKind = (String)JOptionPane.showInputDialog(this, "Escoge un tipo de mundo para la simulación",
                "Mundo", JOptionPane.QUESTION_MESSAGE, null, WORLD_TYPES, WORLD_TYPES[0]);
        if(worldKind != null){
            if(worldKind=="Toroidal")
                world.setToroidalWorld(true);
            else
                world.setToroidalWorld(false);
        }
    }
    
    private void start_button(){
        running = !running;
        if(running){
            tool.start_sim.setText("Pausa");
        }
        else{
            tool.start_sim.setText("Sigue");
        }
    }
    /*Falta borrar hormiga de la imagen si se da clic en otro lado, ie suponemos un solo clic*/
    private void placeAnt(MouseEvent e){
        if(place_ant){
            tool.start_sim.setEnabled(true);
            
            temp_ant_x = ((int)e.getX())/(DIM_CELDA+1);
            temp_ant_y = ((int)e.getY())/(DIM_CELDA+1);
            temp_ant_color = world.getPosColor(temp_ant_x, temp_ant_y);
            temp_ant.setPos(temp_ant_x, temp_ant_y);
            temp_ant.setColorActual(temp_ant_color);
            
            worldDraw.setColor(Color.RED);
            worldDraw.fillRect(temp_ant_x*(DIM_CELDA+1),temp_ant_y*(DIM_CELDA+1), 4,4);//Dibuja la hormiga
            world.addAnt(temp_ant);//Agregamos la hormiga al mundo
        }
        place_ant = false;
        tool.start_sim.setEnabled(true);
    }
    private void zoomOut(){
        if(DIM_SIMUL_IMG > 1000){
            DIM_SIMUL_IMG -= 400;
            sim_view.changeSize(DIM_SIMUL_IMG,DIM_SIMUL_IMG);
            scrollpanel.setViewportView(sim_view);
        }
        
        if(DIM_SIMUL_IMG < 5000 && sim_view.linesColorGray() && !running){
            sim_view.paintLinesWhite();
        }
    }
    private void zoomIn(){
        if(DIM_SIMUL_IMG < 18000){
            DIM_SIMUL_IMG += 400;
            sim_view.changeSize(DIM_SIMUL_IMG,DIM_SIMUL_IMG);
            scrollpanel.setViewportView(sim_view);
        }
        if(DIM_SIMUL_IMG > 5000 && sim_view.linesColorWhite() && !running){
                sim_view.paintLinesGray();
        }
    }
    private void createAnt(){
        ori_new_ant = (String)JOptionPane.showInputDialog(this, "Agrega una orientacion para la nueva hormiga.\nPosteormente da click en su posición",
                "Nueva hormiga", JOptionPane.QUESTION_MESSAGE, null, ORIENTACIONES, ORIENTACIONES[0]);
        if(ori_new_ant != null){
            place_ant = true;
            running = false;
            tool.start_sim.setText("Sigue");
            tool.start_sim.setEnabled(false);
            
            temp_ant = new Ant(NUM_CELDAS,NUM_CELDAS);
            if(ori_new_ant=="Derecha")
                temp_ant.setOri('R');
            else if(ori_new_ant=="Izquierda")
                temp_ant.setOri('L');
            else if(ori_new_ant=="Arriba")
                temp_ant.setOri('U');
            else
                temp_ant.setOri('D');
        }
    }
    private void resetSim(){
        running = false;
        world.resetWorld();
        sim_view.setWorldCells(world);
        generation = 0;
        tool.actualizaDatos(0,0,0);
        tool.start_sim.setEnabled(false);
        tool.new_ant.setEnabled(true);
    }
    private void randomSim(){
        SpinnerNumberModel sModel1 = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner spinner1 = new JSpinner(sModel1);
        int option1 = JOptionPane.showOptionDialog(this, spinner1, "Cantidad de Hormigas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option1 == JOptionPane.CANCEL_OPTION){
            return;
        }
        
        SpinnerNumberModel sModel2 = new SpinnerNumberModel(0, 0, 100, 0.1);
        JSpinner spinner2 = new JSpinner(sModel2);
        int option2 = JOptionPane.showOptionDialog(this, spinner2, "Porcentaje casillas negras", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option2 == JOptionPane.CANCEL_OPTION){
            return;
        }

        running = false;
        world.randomInit((double)spinner2.getValue(), (int)spinner1.getValue());
        sim_view.setWorldCells(world);
        generation = 0;
        tool.actualizaDatos(generation,world.getNumBlack(),world.getNumAnts());
        tool.start_sim.setEnabled(true);
        tool.new_ant.setEnabled(true);
    }
    
    private void showGraphicsWin(){
        gr.setVisible(!gr.isVisible());
        graphs_updating = gr.isVisible();
    }
}
