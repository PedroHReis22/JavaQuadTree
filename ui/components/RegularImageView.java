package quadtree.ui.components;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * <p>Panel para representa uma imagem tradicional em um painel.</p>
 * 
 * <p>A imagem é representada por meio de um ícone sobre o JLabel.</p> 
 * 
 * @author pedro
 *
 */
public class RegularImageView extends ImageView {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -5835483457245975802L;

	/**
	 * Cria a instância do painel para representar uma imagem regular 
	 * 
	 * @param file O arquivo que representa a imagem
	 * @param icon O ícone da imagem para ser definido sobre o JLabel
	 */
	public RegularImageView(File file, Icon icon) {
		
		super(file);
		
		setLayout(new BorderLayout(0, 0));
		
		add(basePanel, BorderLayout.CENTER);
				
		//define o ícone sobre a imagem
		lblImage.setIcon(icon);
		
		//define a imagem sobre o canto esquerdo superior do JLabel
		lblImage.setHorizontalAlignment(SwingConstants.LEFT);
		lblImage.setVerticalAlignment(SwingConstants.TOP);
		
		basePanel.setViewportView(lblImage);
		
	}

}
