package quadtree.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import quadtree.ui.components.ButtonTabbedPane;
import quadtree.ui.controller.MainFrameController;

/**
 * <p>Cria o <i>frame</i> principal da aplicação. Por meio do frame é possível 
 * carregar, exportar e salvar imagens.</p>
 * 
 * <p>Para cada uma das imagens manipuladas pela instância da aplicação uma
 * nova aba é adicionada de modo que várias imagens possam ser visualizadas
 * simultâneamente.</p>
 * 
 * @author pedro
 *
 */
public class MainFrame extends JFrame {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4056906904534442883L;
	
	/**
	 * Menu que permite carregar uma imagem.
	 * A imagem pode ser tradicional ou uma QuadTree
	 */
	private JMenuItem mntmLoadImage;
	
	/**
	 * Menu que permite exportar uma imagem tradicional para uma QuadTree.
	 */
	private JMenuItem mntmExport;
	
	/**
	 * Menu que permite salvar a QuadTree criada a partir de uma imagem
	 */
	private JMenuItem mntmSave;
	
	/**
	 * Permite exibir ou não as divisões realizadas na imagem no processo de 
	 * criação da QuadTree
	 */
	private JCheckBoxMenuItem mntmShowDivisions;
	
	/**
	 * Representa as abas de imagens utilizadas pela aplicação
	 */
	private ButtonTabbedPane tabs; 
	
	/**
	 * Controlador da interface gráfica
	 */
	private MainFrameController controller;

	/**
	 * Cria a instância do frame principal da aplicação
	 */
	public MainFrame() {
				
		//inicializa os componentes gráficos
		initComponents();
		
		//define os listeners 
		setListeners();
		
		controller = new MainFrameController(this);
		
	}
	
	/**
	 * Inicializa os elementos gráficos da interface gráfica
	 */
	public void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(800, 600));
		setTitle("QuadTree");
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnImage = new JMenu("Imagem");
		menuBar.add(mnImage);
		
		mntmLoadImage = new JMenuItem("Abrir");
		mntmLoadImage.setIcon(new ImageIcon(getClass()
				.getResource("/quadtree/resources/open_folder.png")));
		mntmLoadImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
				InputEvent.CTRL_MASK));
		mnImage.add(mntmLoadImage);
		
		mntmSave = new JMenuItem("Salvar");
		mntmSave.setIcon(new ImageIcon(getClass()
				.getResource("/quadtree/resources/save.png")));
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
				InputEvent.CTRL_MASK));
		mnImage.add(mntmSave);
		
		mntmExport = new JMenuItem("Exportar");
		mntmExport.setIcon(new ImageIcon(getClass()
				.getResource("/quadtree/resources/export.png")));
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_MASK));
		mnImage.add(mntmExport);
		
		JMenu mnOptions = new JMenu("Opções");
		menuBar.add(mnOptions);
		
		mntmShowDivisions = new JCheckBoxMenuItem("Mostrar divisões");
		mnOptions.add(mntmShowDivisions);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabs = new ButtonTabbedPane();
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabs, BorderLayout.CENTER);
		
		mntmSave.setEnabled(false);
		mntmShowDivisions.setEnabled(false);
		
	}
	
	/**
	 * Define os listeners da interface gráfica, criando a ligação entre os
	 * elementos gráficos e as ações.
	 */
	private void setListeners() {
		
		ActionListener menuListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(mntmLoadImage)) {
					controller.loadImage();
				}
				else if(e.getSource().equals(mntmExport)) {
					controller.export(tabs.getSelectedComponent());
				}
				else if(e.getSource().equals(mntmSave)) {
					controller.save(tabs.getSelectedComponent());
				}
				else if(e.getSource().equals(mntmShowDivisions)) {
					controller.showDivisions(tabs.getSelectedComponent(), 
							mntmShowDivisions.isSelected());
				}
				
			}
			
		};
				
		tabs.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(tabs.getTabCount() > 0) {
					controller.changeTab(tabs.getSelectedComponent());
				}
			}
			
		});
		
		mntmLoadImage.addActionListener(menuListener);
		mntmExport.addActionListener(menuListener);
		mntmSave.addActionListener(menuListener);
		mntmShowDivisions.addActionListener(menuListener);
		
	}
	
	/**
	 * Adiciona uma nova aba contendo uma imagem ao frame
	 * 
	 * @param title O título da aba que deseja-se adicionar
	 * @param component O componente da aba
	 */
	public void addTab(String title, Component component) {
		tabs.add(title, component);
		tabs.setSelectedComponent(component);
	}
	
	/**
	 * Permite (des)habilitar o menu de salvar a QuadTree
	 * O menu de exportação só está disponível para QuadTrees
	 * 
	 * @param enable true se deseja habilitar o menu
	 *        false caso contrário
	 */
	public void enableSaveMenu(boolean enable) {
		mntmSave.setEnabled(enable);
	}
	
	/**
	 * Permite (des)habilitar o menu de visualizar divisões da imagem
	 * 
	 * @param enable true se deseja habilitar o menu
	 *        false caso contrário
	 */
	public void enableShowDivisions(boolean enable) {
		mntmShowDivisions.setEnabled(enable);
	}
	
	/**
	 * Permite marcar ou desmarcar o menu de visualizações da imagem
	 * 
	 * @param check true se deseja marcar o menu
	 *        false caso contrário
	 */
	public void checkShowDivisions(boolean check) {
		mntmShowDivisions.setSelected(check);
	}

}
