package quadtree.ui.filechooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 * <p>Implementa um <i>preview</i> de imagens em um JFileChooser de acordo com
 * que arquivos de imagens são selecionados.</p>
 * 
 * <p>Implementa PropertyChangeListener de modo que a imagem seja atualizada
 * toda vez que um novo arquivo é selecionado.</p>
 * 
 * @author pedro
 *
 */
public class ImagePreview extends JComponent 
	implements PropertyChangeListener {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -4776024011417063438L;

	/**
	 * Largura do preview da imagem
	 */
	private final static int WIDTH = 150;
	
	/**
	 * altura do preview da imagem
	 */
	private final static int HEIGHT = 100;

	/**
	 * Referencia o icone que exibe a imagem. Se tem como valor null, nada é 
	 * exibido pelo preview
	 */
	private ImageIcon icon;

	/**
	 * Instancia o preview da imagem
	 * 
	 * @param fileChooser O JFileChooser que receberá o preview da imagem como
	 * 						acessório
	 */
	public ImagePreview(JFileChooser fileChooser) {
		
		//adiciona ao filechooser o evento de mudança de seleção de arquivo por
		//meio do filechooser
		fileChooser.addPropertyChangeListener(this);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		//existe um icone criado (o arquivo de uma imagem está selecionado
		if (icon != null) {
			
			Graphics2D g2d = (Graphics2D) g;
			Rectangle bounds = new Rectangle(0, 0, icon.getIconWidth(), 
					icon.getIconHeight());
			
			g.setColor(Color.white);
			g2d.fill(bounds);

			icon.paintIcon(this, g, 0, 0);
		}
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
		String propName = e.getPropertyName();

		//apaga o preview em exibição              
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(propName)) {
			icon = null;
			repaint();
			return;
		}                  

		//exibe a imagem selecionado pelo preview
		if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propName)) {
			
			//arquivo selecionado
			File file = (File) e.getNewValue();

			//diretório selecionado
			if (file == null) {
	            icon = null;
	            repaint();
	            return;
			}

			//cria o ícone através da imagem
			icon = new ImageIcon(file.getPath());

			if (icon.getIconWidth() == -1) {
				icon.getImage().flush();
				icon = new ImageIcon(file.getPath());
			}

			//escala
			if (icon.getIconWidth() > WIDTH) {
				icon = new ImageIcon(icon.getImage().getScaledInstance (WIDTH, 
						-1, Image.SCALE_DEFAULT));
			}
				
			repaint();
		}
		
	}
	
}