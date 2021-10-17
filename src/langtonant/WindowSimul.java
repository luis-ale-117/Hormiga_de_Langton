
package langtonant;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class WindowSimul extends JFrame{
    
    private static final int DIM_VENTANA=700;
    private static final int DIM_TOOLS=200;
    
    private static final int NUM_CELDAS=1000;
    private static final int DIM_CELDA = 5;
    private static final int DEFAULT_DS=DIM_CELDA*NUM_CELDAS+NUM_CELDAS-1;
    private static final int DIM_ZOOM_1N=DEFAULT_DS/2;
    private static final int DIM_ZOOM_2N=DEFAULT_DS/4;
    private static final int DIM_ZOOM_3N=645;
    private static final int DIM_ZOOM_1P=DEFAULT_DS*3;
    private static final int DIM_ZOOM_2P=DEFAULT_DS*4;
    private static final int DIM_ZOOM_3P=DEFAULT_DS*5;
    private static final int DEFAULT_SPEED=100;
    private static final int SPEED_1N=500;
    private static final int SPEED_2N=1000;
    private static final int SPEED_3N=2000;
    private static final int SPEED_1P=25;
    private static final int SPEED_2P=1;
    private static final int SPEED_3P=0;
    
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
    private static int SPEED=DEFAULT_SPEED;
    
    private JScrollPane scrollpanel;
    public SimulPanel sim_view;
    public ToolsPanel tool;
    public GraphicsWindow gr;
    
    public JMenuBar barra_menu;
    public JMenu archivo;
    public JMenuItem abrir_arch,save_arch;
    
    /*FLAGS*/
    public boolean running,place_ant,graphs_updating;
    public Ant temp_ant;
    public int temp_ant_x,temp_ant_y;
    public byte temp_ant_color;
    public char temp_ant_ori;
    public String ori_new_ant;
    public Color temp_ant_neg90;
    public Color temp_ant_bla90;
    
    public World world;
    public BufferedImage worldImg;
    public Graphics worldDraw;
    
    private int generation,gen_calculos;
    
    public WindowSimul(int dimension,int barraTools){
        this.setSize(dimension+barraTools,dimension+13);
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
                changeCell(e);
            }
        });
        scrollpanel = new JScrollPane(sim_view);
        scrollpanel.setViewportView(sim_view);
        scrollpanel.setBounds(0,0,DIM_VENTANA-50,DIM_VENTANA-50);
        this.add(scrollpanel);
        tool = new ToolsPanel(DIM_VENTANA-50,DIM_TOOLS+50);
        this.add(tool);
        abrir_arch = new JMenuItem();
        save_arch = new JMenuItem();
        archivo =  new JMenu();
        barra_menu = new JMenuBar();
        
        archivo.setText("Archivo");
        abrir_arch.setText("Abrir archivo");
        save_arch.setText("Guardar");
        archivo.add(abrir_arch);
        archivo.add(save_arch);
        barra_menu.add(archivo);
        this.setJMenuBar(barra_menu);
        
        gr =  new GraphicsWindow();
        
        setButtonsActions();
        
        /*INICIA LAS BANDERAS*/
        running = false;
        place_ant = false;
        graphs_updating = false;
        
        /*HORMIGA TEMPORAL*/
        temp_ant = null;
        temp_ant_x = 0;
        temp_ant_y = 0;
        temp_ant_ori = 'U';
        temp_ant_color = POS_WHITE;
        
        /*MUNDO DE LA SIMULACION*/
        world = new World(NUM_CELDAS);
        worldImg = new BufferedImage(DIM_SIMUL_IMG,DIM_SIMUL_IMG,BufferedImage.TYPE_INT_RGB);
        worldDraw = worldImg.createGraphics();
        
        generation = 0;
        gen_calculos=0;
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
            gr.updateGraphs(gen_calculos, world.getNumBlack(),graphs_updating);
            /*EN PAUSA SI LO ESTA*/
            ifSimPaused();
            world.updateAntsPos();
            sim_view.muestraMundo();
            tool.actualizaDatos(generation,world.getNumBlack(),world.getNumAnts());
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException ex) {
                Logger.getLogger(LangtonAnt.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*PINTA LAS CELDAS DONDE ESTUVIERON LAS HORMIGAS*/
            paintCellsAndLines();
            ifSimEnded();
            generation++;
            if(graphs_updating){
                gen_calculos++;
            }
        }
    }
    
    private void paintCellsAndLines(){
        for(Ant a: world.ants){
            if(world.world[a.getY_antes()][a.getX_antes()]==POS_WHITE)
                //worldDraw.setColor(Color.WHITE);
                worldDraw.setColor(a.bla_90_izq);
            else
                //worldDraw.setColor(Color.BLACK);
                worldDraw.setColor(a.neg_90_der);
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
            //paintLinesIfNeeded();
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
    
    private void setButtonsActions(){
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
        tool.edit_cell.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                editCellValue();
            }
        });
        tool.zoom_sld.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                zoomSimulation();
            }
        });
        tool.vel_sld.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                speedSimulation();
            }
        });
        save_arch.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveFile();
            }
        });
        abrir_arch.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openFile();
            }
        });
    }
    
    private void start_button(){
        running = !running;
        if(running){
            tool.start_sim.setText("Pausa");
            tool.new_ant.setEnabled(false);
            tool.edit_cell.setEnabled(false);
        }
        else{
            tool.start_sim.setText("Sigue");
            tool.new_ant.setEnabled(true);
            tool.edit_cell.setEnabled(true);
        }
    }
    private void placeAnt(MouseEvent e){
        if(place_ant){
            temp_ant_x = ((int)e.getX())/(DIM_CELDA+1);
            temp_ant_y = ((int)e.getY())/(DIM_CELDA+1);
            temp_ant_color = world.getPosColor(temp_ant_x, temp_ant_y);
            temp_ant.setPos(temp_ant_x, temp_ant_y);
            temp_ant.setColorActual(temp_ant_color);
            temp_ant.setNeg90der(temp_ant_neg90);
            temp_ant.setBla90izq(temp_ant_bla90);
            
            worldDraw.setColor(Color.RED);
            worldDraw.fillRect(temp_ant_x*(DIM_CELDA+1),temp_ant_y*(DIM_CELDA+1), 4,4);//Dibuja la hormiga
            world.addAnt(temp_ant);//Agregamos la hormiga al mundo
            place_ant = false;
            tool.start_sim.setEnabled(true);
        }
    }
    private void zoomSimulation(){
        switch (tool.zoom_sld.getValue()){
            case 0->{
                sim_view.changeSize(DEFAULT_DS,DEFAULT_DS);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorWhite() && !running){
                    sim_view.paintLinesGray();
                }
            }
            case -1->{
                sim_view.changeSize(DIM_ZOOM_1N,DIM_ZOOM_1N);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorGray() && !running){
                    sim_view.paintLinesWhite();
                }
            }
            case -2->{
                sim_view.changeSize(DIM_ZOOM_2N,DIM_ZOOM_2N);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorGray() && !running){
                    sim_view.paintLinesWhite();
                }
            }
            case -3->{
                sim_view.changeSize(DIM_ZOOM_3N,DIM_ZOOM_3N);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorGray() && !running){
                    sim_view.paintLinesWhite();
                }
            }
            case 1->{
                sim_view.changeSize(DIM_ZOOM_1P,DIM_ZOOM_1P);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorWhite() && !running){
                    sim_view.paintLinesGray();
                }
            }
            case 2->{
                sim_view.changeSize(DIM_ZOOM_2P,DIM_ZOOM_2P);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorWhite() && !running){
                    sim_view.paintLinesGray();
                }
            }
            case 3->{
                sim_view.changeSize(DIM_ZOOM_3P,DIM_ZOOM_3P);
                scrollpanel.setViewportView(sim_view);
                if(sim_view.linesColorWhite() && !running){
                    sim_view.paintLinesGray();
                }
            }
        }
    }
    private void speedSimulation(){
        boolean aux=running;
        switch (tool.vel_sld.getValue()){
            case 0->{
                running = false;
                SPEED=DEFAULT_SPEED;
                running=aux;
            }
            case -1->{
                running = false;
                SPEED=SPEED_1N;
                running=aux;
            }
            case -2->{
                running = false;
                SPEED=SPEED_2N;
                running=aux;
            }
            case -3->{
                running = false;
                SPEED=SPEED_3N;
                running=aux;
            }
            case 1->{
                running = false;
                SPEED=SPEED_1P;
                running=aux;
            }
            case 2->{
                running = false;
                SPEED=SPEED_2P;
                running=aux;
            }
            case 3->{
                running = false;
                SPEED=SPEED_3P;
                running=aux;
            }
        }
    }
    private void createAnt(){
        ori_new_ant = (String)JOptionPane.showInputDialog(this, "Agrega una orientacion para la nueva hormiga.\nPosteormente da click en su posición",
                "Nueva hormiga", JOptionPane.QUESTION_MESSAGE, null, ORIENTACIONES, ORIENTACIONES[0]);
        JColorChooser neg90 = new JColorChooser(Color.BLACK);//Black by default
        int option1 = JOptionPane.showOptionDialog(null, neg90, "Color de celda si es Negra, (Negro default)", JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option1 == JOptionPane.CLOSED_OPTION){
            return;
        }
        JColorChooser bla90 = new JColorChooser(Color.WHITE);//White by default
        int option2 = JOptionPane.showOptionDialog(null, bla90, "Color de celda si es Blanca, (Blanca default)", JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option2 == JOptionPane.CLOSED_OPTION){
            return;
        }
        
        SpinnerNumberModel sModel1 = new SpinnerNumberModel(0, 0, 999, 1);
        SpinnerNumberModel sModel2 = new SpinnerNumberModel(0, 0, 999, 1);
        JSpinner spinnerX = new JSpinner(sModel1);
        JSpinner spinnerY = new JSpinner(sModel2);
        Object[] ob= new Object[5];
        ob[0] = "¿Desea ingresar una posicion sin mouse?";
        ob[1] = "Posicion en X";
        ob[2] = spinnerX;
        ob[3] = "Posicion en Y";
        ob[4] = spinnerY;
        int op = JOptionPane.showOptionDialog(null, ob, "Posicion de la Hormiga", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        
        if(ori_new_ant != null){
            //Deten la simulacion
            place_ant = true;
            running = false;
            tool.start_sim.setText("Sigue");
            tool.start_sim.setEnabled(false);
            //Valores temporales de la hormiga
            temp_ant = new Ant(NUM_CELDAS,NUM_CELDAS);
            if(ori_new_ant=="Derecha")
                temp_ant.setOri('R');
            else if(ori_new_ant=="Izquierda")
                temp_ant.setOri('L');
            else if(ori_new_ant=="Arriba")
                temp_ant.setOri('U');
            else
                temp_ant.setOri('D');
            temp_ant_neg90 = neg90.getColor();
            temp_ant_bla90 = bla90.getColor();
            if (op == JOptionPane.OK_OPTION){
                place_ant = false;
                tool.start_sim.setEnabled(true);
                
                temp_ant_x = (int)spinnerX.getValue();
                temp_ant_y = (int)spinnerY.getValue();
                temp_ant_color = world.getPosColor(temp_ant_x, temp_ant_y);
                temp_ant.setPos(temp_ant_x, temp_ant_y);
                temp_ant.setColorActual(temp_ant_color);
                temp_ant.setNeg90der(temp_ant_neg90);
                temp_ant.setBla90izq(temp_ant_bla90);

                worldDraw.setColor(Color.RED);
                worldDraw.fillRect(temp_ant_x*(DIM_CELDA+1),temp_ant_y*(DIM_CELDA+1), 4,4);//Dibuja la hormiga
                world.addAnt(temp_ant);//Agregamos la hormiga al mundo
            }else{
                tool.zoom_sld.setValue(0);
            }
        }
    }
    private void resetSim(){
        running = false;
        world.resetWorld();
        sim_view.setWorldCells(world);
        generation = 0;
        tool.actualizaDatos(0,0,0);
        gr.clearGraphics();
        tool.start_sim.setEnabled(false);
        tool.new_ant.setEnabled(true);
        chooseKindOfWorld();
//        if(tool.zoom_sld.getValue()<0){
//            tool.zoom_sld.setValue(0);
//        }
    }
    private void randomSim(){
        SpinnerNumberModel sModel1 = new SpinnerNumberModel(0, 0, 100, 1);
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
    private void editCellValue(){
        if(tool.edit_cell.isSelected()){
            JOptionPane.showMessageDialog(this,
                "Con clic izquierdo y derecho cambie los estados como sigue:"
                        + "\n1.- Clic izquierdo: Hormiga gira 90° a la derecha (Negro por default)"
                        + "\n2.- Clic derecho: Hormiga gira 90° a la izquierda (Blanco por default)",
                "Configuración de colores",
                JOptionPane.WARNING_MESSAGE);
            running = false;
            tool.start_sim.setText("Sigue");
            tool.start_sim.setEnabled(false);
            tool.new_ant.setEnabled(false);
        }else{
            tool.new_ant.setEnabled(true);
            tool.start_sim.setEnabled(true);
        }
        
    }
    private void changeCell(MouseEvent e){
        if(tool.edit_cell.isSelected()){
            int pos_x,pos_y;
            pos_x = ((int)e.getX())/(DIM_CELDA+1);
            pos_y = ((int)e.getY())/(DIM_CELDA+1);
            if(e.getButton()==MouseEvent.BUTTON1){//Izquierdo
                world.setPosColor(pos_x,pos_y,POS_BLACK);
                worldDraw.setColor(Color.BLACK);
                worldDraw.fillRect(pos_x*(DIM_CELDA+1),pos_y*(DIM_CELDA+1), 4,4);
            }else if(e.getButton()==MouseEvent.BUTTON3){
                world.setPosColor(pos_x,pos_y,POS_WHITE);
                worldDraw.setColor(Color.WHITE);
                worldDraw.fillRect(pos_x*(DIM_CELDA+1),pos_y*(DIM_CELDA+1), 4,4);
            }else{
            }
        }else{
            
        }
        
    }
    private void saveFile(){
        if(running) return;
        JFileChooser selectorArch = new JFileChooser();
        selectorArch.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de texto","txt");
        selectorArch.setFileFilter(filter);
        int respuesta = selectorArch.showSaveDialog(this);

        if(respuesta == JFileChooser.APPROVE_OPTION){
            File arch = selectorArch.getSelectedFile();
            FileWriter arch_w = null;
            PrintWriter arch_pr = null;
            try {
                arch_w = new FileWriter(arch);
                arch_pr = new PrintWriter(arch_w);
                char[] cs = new char[1000];
                int i = 0;
                for(byte[] fila: world.world){
                    i=0;
                    for(byte b: fila){
                        cs[i] = b==0?'.':'*';
                        i++;
                    }
                    arch_w.write(cs);
                    arch_w.write('\n');
                }
                arch_pr.println(generation-1);
                arch_pr.println(world.ants.size());
                for(Ant a: world.ants){
                    arch_pr.println(a.getX());
                    arch_pr.println(a.getY());
                    arch_pr.println(a.orientacion);
                    arch_pr.println(a.color_actual);
                    arch_pr.println(a.getNeg90_RGB());
                    arch_pr.println(a.getBla90_RGB());
                    arch_pr.println();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                "Error al intentar abrir el archvio\nError: "+ex,
                "Error en Archivo",
                JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    arch_pr.close();
                    arch_w.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                    "Error al intentar cerrar el archvio\nError: "+ex,
                    "Error en Archivo",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void openFile(){
        if(running) return;
        JFileChooser selectorArch = new JFileChooser();
        selectorArch.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de texto","txt");
        selectorArch.setFileFilter(filter);
        int respuesta = selectorArch.showOpenDialog(this);

        if(respuesta == JFileChooser.APPROVE_OPTION){
            String aux="";
            byte[][] new_world = world.world;
            world.delAnts();
            int gen=0,numOfAnts=0;
            int aX,aY,neg90,bla90;
            Color n90,b90;
            char ori;
            byte col_actual;
            
            try {
                File arch = selectorArch.getSelectedFile();
                FileReader arch_r = new FileReader(arch);
                BufferedReader arch_bf = new BufferedReader(arch_r);
                try {
                    for(int y=0;y<1000;y++){
                        for(int x=0;x<1000;x++){
                            new_world[y][x] = arch_bf.read()=='.'?0:(byte)1;
                        }
                        arch_bf.read();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WindowSimul.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    aux = arch_bf.readLine();
                    gen = Integer.parseInt(aux);
                    aux = arch_bf.readLine();
                    numOfAnts = Integer.parseInt(aux);
                    for(int i=0;i<numOfAnts;i++){
                        aux = arch_bf.readLine();
                        aX=Integer.parseInt(aux);
                        aux = arch_bf.readLine();
                        aY=Integer.parseInt(aux);
                        aux = arch_bf.readLine();
                        ori = aux.charAt(0);
                        aux = arch_bf.readLine();
                        col_actual = (byte)Integer.parseInt(aux);
                        aux = arch_bf.readLine();
                        neg90 = Integer.parseInt(aux);
                        aux = arch_bf.readLine();
                        bla90 = Integer.parseInt(aux);
                        n90=new Color((neg90>>16)&0xFF,(neg90>>8)&0xFF,(neg90>>0)&0xFF);
                        b90=new Color((bla90>>16)&0xFF,(bla90>>8)&0xFF,(bla90>>0)&0xFF);
                        world.addAnt(aX, aY, ori, col_actual, n90, b90);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WindowSimul.class.getName()).log(Level.SEVERE, null, ex);
                }
                generation = gen;
                world.recalculateNumBlack();
                sim_view.setWorldCells(world);
                tool.actualizaDatos(gen,world.getNumBlack(),world.getNumAnts());
                try {
                    arch_bf.close();
                    arch_r.close();
                } catch (IOException ex) {
                    Logger.getLogger(WindowSimul.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                "Error al intentar abrir el archvio\nError: "+ex,
                "Error en Archivo",
                JOptionPane.ERROR_MESSAGE);
            }
            tool.start_sim.setEnabled(true);
        }
    }
}