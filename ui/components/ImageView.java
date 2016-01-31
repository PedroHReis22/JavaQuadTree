package quadtree.ui.components;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * <p>Classe abstrata que representa um painel de visualização de imagens.</p>
 * 
 * <p>A imagem é representada por meio de um JLabel definido sobre um 
 * JScrollPanel.</p>
 * 
 * @author pedro
 *
 */
public abstract class ImageView extends JPanel {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1620589535556791957L;
	
	/**
	 * Arquivo da imagem representada pelo painel
	 */
	protected File file;
	
	/**
	 * O nome do arquivo da imagem representada pelo painel
	 */
	private String fileName;
	
	/**
	 * O caminho absoluto do arquivo da imagem representada pelo painel
	 */
	private String filePath;	
	
	/**
	 * JScrollPane sobre o qual o JLabel é definido para representar a imagem
	 */
	protected JScrollPane basePanel;
	
	/**
	 * JLabel que representa a imagem
	 */
	protected JLabel lblImage;
	
	/**
	 * Ponto x utilizado durante os eventos de navegação da imagem
	 */
	private int x;
	
	/**
	 * Ponto y utilizado durante os eventos de navegação da imagem
	 */
	private int y;
	
	/**
	 * Cria a instância de um painel de visualização da imagem através de um
	 * arquivo.
	 * 
	 * @param file O arquivo da imagem representada pelo painel
	 */
	public ImageView(File file) {
		
		this.file = file;
		
		fileName = file.getName();
		filePath = file.getAbsolutePath();
		
		lblImage = new JLabel();
		basePanel = new JScrollPane();
		
		setListeners();
	}
	
	/**
	 * Cria a instância de um painel de visualização da imagem definindo o 
	 * título e caminho absoluto da imagem representada pelo painel
	 * 
	 * @param fileName O nome do arquivo que armazena a imagem representada
	 * @param filePath O caminho absoluto do arquivo que armazena a imagem 
	 * 					representada
	 */
	public ImageView(String fileName, String filePath) {
		
		file = null;
		
		this.fileName = fileName;
		this.filePath = filePath;
		
		lblImage = new JLabel();
		basePanel = new JScrollPane();
		
		setListeners();
	}
	
	/**
	 * Define os eventos sobre o JLabel que representa a imagem
	 * 
	 * Define o cursor de navegação quando o ponteiro do mouse é posicionado 
	 * sobre o JLabel
	 * 
	 * Realiza a navegação sobre o JLabel com o arrastar do mouse
	 */
	private void setListeners() {
		
		lblImage.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				basePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
				boolean flag = basePanel.getVerticalScrollBar().isVisible() ||
						basePanel.getHorizontalScrollBar().isVisible();
				
				if(flag) {				
					Cursor c = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
					basePanel.setCursor(c);
				}
				
			}
			
		});
		
		lblImage.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
				int dx = e.getX() - x;
				int dy = e.getY() - y;
				
				JScrollBar vertical = basePanel.getVerticalScrollBar();
				JScrollBar horizontal = basePanel.getHorizontalScrollBar();
				
				int h = lblImage.getHeight();
				int w = lblImage.getWidth();
				
				int dh = (int) (horizontal.getMaximum() * ((double) dx / w) / 2);
				int dv = (int) (vertical.getMaximum() * ((double) dy / h) / 2);
				 				
				horizontal.setValue(horizontal.getValue() - dh);
				vertical.setValue(vertical.getValue() - dv);
				
				x = e.getX();
				y = e.getY();
				
			}
			
		});
		
	}
	
	/**
	 * Retorna o arquivo da imagem 
	 * 
	 * @return O arquivo da imagem
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Retorna o nome do arquivo da imagem
	 * 
	 * @return O nome do arquivo da imagem
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Retorna o caminho absoluto do arquivo da imagem
	 * 
	 * @return O caminho absoluto do arquivo da imagem
	 */
	public String getPath() {
		return filePath;
	}
	
	/**
	 * Retorna o JLabel que representa a imagem
	 * 
	 * @return O JLabel que representa a imagem
	 */
	public JLabel getLblImage() {
		return lblImage;
	}

}
