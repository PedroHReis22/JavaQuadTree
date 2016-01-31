package quadtree.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import quadtree.core.Node;
import quadtree.core.QuadTree;

/**
 * <p>Painel para a representação de uma imagem representada por meio de uma 
 * QuadTree.</p>
 * 
 * <p>Permite aplicar zoom sobre a imagem por meio de um JSlider e visualizar 
 * a imagem com menor nível de detalhes ao utilizar um nível menor da 
 * árvore.</p>
 * 
 * @author pedro
 *
 */
public class QuadTreeImageView extends ImageView {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -4780788625883802149L;
	
	/**
	 * Representa os níveis de detalhes da imagem
	 */
	private JSlider levels;
	
	/**
	 * Representa o zoom sobre a imagem
	 */
	private JSlider zoom;
	
	/**
	 * A QuadTree que representa a imagem do painel 
	 */
	private QuadTree quadTree;

	/**
	 * A altura da árvore de detalhes que deseja-se
	 */
	private int level;
	
	/**
	 * Indica se as divisões da QuadTree devem ou não ser mostradas na imagem
	 */
	private boolean showDivisions;

	/**
	 * Cria uma instância do painel de representação de uma QuadTree
	 * 
	 * @param fileName O nome do arquivo representado pela QuadTree
	 * @param filePath O caminho absoluto do arquivo representado pela QuadTree
	 * @param quadTree A quadTree que representa a imagem
	 */
	public QuadTreeImageView(String fileName, String filePath, 
			QuadTree quadTree) {
		
		super(fileName, filePath);
		
		showDivisions = false;
		
		this.quadTree = quadTree;
		
		//inicializa os componentes gráficos da imagem
		initComponents();
		
		//define os eventos da interface
		setListeners();
		
	}
	
	/**
	 * Inicializa os componentes gráficos do painel de visualização de uma
	 * imagem representada pela quadTree
	 */
	private void initComponents() {
		
		setLayout(new BorderLayout(0, 0));
		
		int height = quadTree.height();
		
		level = height - 1;
		
		levels = new JSlider(1, height, height);
		levels.setMinorTickSpacing(1);
		levels.setMajorTickSpacing(1);
		levels.setPaintTicks(true);
		levels.setPaintLabels(true);
		levels.setLabelTable(levels.createStandardLabels(1));
		
		add(levels, BorderLayout.SOUTH);
		
		zoom = new JSlider(JSlider.VERTICAL, 25, 300, 100);
		zoom.setMinorTickSpacing(25);
		zoom.setMajorTickSpacing(100);
		zoom.setPaintTicks(true);
		zoom.setPaintLabels(true);
		
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		for(int i = 25; i <= 300; i += 25) {
			labels.put(new Integer(i), new JLabel(i + "%"));
		}

		zoom.setLabelTable(labels);
		add(basePanel, BorderLayout.CENTER);
		
		lblImage.setVerticalAlignment(SwingConstants.TOP);
		lblImage.setHorizontalAlignment(SwingConstants.LEFT);
		
		add(zoom, BorderLayout.EAST);
		basePanel.setViewportView(lblImage);		
		
		createImage();
	}
	
	/**
	 * Cria a imagem utilizando a quadTree em um buffer e define o buffer como
	 * ícone do JLabel
	 */
	private void createImage() {
		
		int w = quadTree.getWidth();
		int h = quadTree.getHeight();
		
		lblImage.setPreferredSize(new Dimension(w, h));
		lblImage.setSize(new Dimension(w, h));
		
		BufferedImage buffer = new BufferedImage(w, h, 
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = buffer.createGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		paintImage(g2d, quadTree.getRoot(), level);
		
		if(showDivisions) {
			
			g2d.setColor(Color.black);
			
			g2d.drawRect(0, 0, w - 1 , h - 1);
		}
		
		ImageIcon icon = new ImageIcon(buffer);
		
		lblImage.setIcon(icon);
		
		zoom();
		
	}
	
	/**
	 * Desenha a imagem sobre um Graphics2D de acordo com o nível de 
	 * detalhamento desejado. A imagem pode ser desenhada sobre um JPanel ou um
	 * buffer, por exemplo.
	 * 
	 * Chamadas recursivas são realizadas para representar todas as regiões da 
	 * imagem.
	 * 
	 * @param g2d O Gráfico sobre o qual a imagem será representada
	 * @param node O nó atual que será representado (região da imagem)
	 * @param level O nível de detalhamento desejado. A cada iteração o nóvel
	 * 				é reduzido em uma unidade. Quando o nível de detalhamento
	 * 				for igual a 0 o nó é desenhado
	 */
	private void paintImage(Graphics2D g2d, Node node, int level) {
		
		//ou está no nivel de detalhamento desejado (level = 0) ou o nó é uma 
		//folha e não existe nível inferior
		if(level == 0 || node.isLeaf()) {
			
			Color c = node.getColor();
			
			int x = node.getX();
			int y = node.getY();
			
			int w = node.getWidth();
			int h = node.getHeight();
			
			g2d.setColor(c);
			
			g2d.fillRect(x, y, w, h);
			
			if(showDivisions) {
				g2d.setColor(Color.black);
				g2d.drawRect(x, y, w, h);
			}
			
			return;
											
		}		 
		
		for(int i = 0; i < node.getQ().length; ++i) {
			
			if(node.getQ()[i] != null) {
				paintImage(g2d, node.getQ()[i], level - 1);	
			}			
			
		}
				
	}

	/**
	 * Define as ações da interface gráfica.
	 */
	private void setListeners() {
		
		ChangeListener sppinerListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				if(levels.getValueIsAdjusting()) return;
				
				if(e.getSource().equals(levels)) {
					level = levels.getValue() - 1;
					update();
				}
				else if(e.getSource().equals(zoom)) {
					update();
				}
				
			}
			
		};
		
		levels.addChangeListener(sppinerListener);
		zoom.addChangeListener(sppinerListener);
		
		lblImage.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int d = -e.getWheelRotation();				
				zoom.setValue(zoom.getValue() + d);
			}
		});
		
	}
		
	/**
	 * Aplica zoom sobre a imagem de acordo com o slider de zoom
	 */
	private void zoom() {
		
		//cria a imagem em tamangho real
		//createImage();
		
		//dimensões da imagem em tamanho real
		int h = quadTree.getHeight();
		int w = quadTree.getWidth();
		
		//cálculo o fator de zoom
		double scale = zoom.getValue() / 100.0;
		
		//novas dimensões sobre o zoom
		h = (int) ((h * scale) + 0.5);
		w = (int) ((w * scale) + 0.5);
		
		lblImage.setPreferredSize(new Dimension(w, h));
		lblImage.setSize(new Dimension(w, h));
		
		ImageIcon imageIcon = (ImageIcon) lblImage.getIcon();
		 
		Image image = imageIcon.getImage().getScaledInstance(w, h, 
				Image.SCALE_DEFAULT);
		lblImage.setIcon(new ImageIcon(image));
		
	}
	
	/**
	 * Retorna true se a imagem está exibindo as divisões da quadTree e false
	 * caso contrário
	 * 
	 * @return true se a imagem está exibindo as divisões da quadTree 
	 * 		   false caso contrário
	 */
	public boolean showDivisions() {
		return showDivisions;
	}
	
	/**
	 * Define se a imagem deve exibir as divisões da quadTree
	 * 
	 * @param showDivisions true se as divisões devem ser apresentadas
	 *                      false caso contrário
	 */
	public void setShowDivisions(boolean showDivisions) {
		this.showDivisions = showDivisions;
	}
	
	/**
	 * Atualiza a imagem, redesenhando-a e aplicando o zoom
	 */
	public void update() {
		createImage();
	}
	
	/**
	 * Retorna a QuadTree representada pelo painel
	 * 
	 * @return A QuadTree representada pelo painel
	 */
	public QuadTree getQuadTree() {
		return quadTree;
	}
	
}
