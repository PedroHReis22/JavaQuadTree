package quadtree.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * <p>Carrega uma arquivo de imagem em uma matriz de pixels, armazenando a
 * altura e largura da imagem.</p>
 *  
 * @author pedro
 *
 */
public class LoadImage {
	
	/**
	 * Altura da imagem
	 */
	private int height;
	
	/**
	 * Largura da imagem
	 */
	private int width;
	
	/**
	 * Matriz de pixels extraidos
	 */
	private Color pixels[][]; 
	
	/**
	 * Cria o objeto para carregar a matriz de pixels de um arquivo de imagem
	 * 
	 * @param file O arquivo de imagem para ser transformado na matriz de
	 * 				pixels
	 */
	public LoadImage(File file) {
		pixels = convertImageToArray(file);
	}
		
	/**
	 * Converte o arquivo da imagem em uma matriz de pixels
	 * 
	 * @param file O arquivo da imagem
	 * 
	 * @return A matriz de pixels da imagem
	 */
	private Color[][] convertImageToArray(File file) {
				
		try {
			
			//cria um buffer com o conteúdo da imagem
			BufferedImage buffer = ImageIO.read(file);
			
			//dimensões da imagem
			height = buffer.getHeight();
			width = buffer.getWidth();
			
			pixels = new Color[height][width];
			
			for(int i = 0; i < height; ++i) {
				for(int j = 0; j < width; ++j) {
					pixels[i][j] = new Color(buffer.getRGB(j, i), true);
				}
			}
			
		} catch (IOException e) { e.printStackTrace(); }
		
		return pixels;
		
	}
	
	/**
	 * Retorna a altura da imagem
	 * 
	 * @return A altura da imagem
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Retorna a largura da imagem
	 * 
	 * @return A largura da imagem
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retorna a matriz de pixels obtida do arquivo de imagem
	 * 
	 * @return A matriz de pixels do arquivo de imagem
	 */
	public Color[][] getPixels() {
		return pixels;
	}

}
