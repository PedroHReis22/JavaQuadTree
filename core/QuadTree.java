package quadtree.core;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import quadtree.util.LoadImage;

/**
 * <p>Implementa uma QuadTree para representar uma imagem.</p>
 * 
 * <p>A qualidade da imagem e a tolerância a discrepância entre os pixesl da
 * mesma região dependem do fator de precisão</p>
 * 
 * @author pedro
 *
 */
public class QuadTree implements Serializable {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 5855898554454654068L;
	
	/**
	 * Altura da imagem representada pela árvore
	 */
	private int height;
	
	/**
	 * Largura da imagem representada pela árvore
	 */
	private int width;
	
	/**
	 * Raiz da árvore
	 */
	private Node root;
	
	/**
	 * Precisão utilizada para a divisão recursiva da imagem em quadrantes.
	 * Quanto maior a precisão, mais divisões são realizadas na imagem 
	 */
	private double accuracy;
	
	/**
	 * Cria uma instância de uma QuadTree para representar uma imagem
	 * 
	 * @param file O arquivo que armazena a imagem a ser convertida à QuadTree
	 * @param accuracy A precisão utilizada no processo de divisão da imagem
	 */
	public QuadTree(File file, double accuracy) {
		
		//transforma a imagem representada pelo arquivo em uma matriz de pixels
		LoadImage loadImage = new LoadImage(file);
		Color image[][] = loadImage.getPixels();
		
		height = loadImage.getHeight();
		width = loadImage.getWidth();
		
		this.accuracy = accuracy;
		
		//armazena a matriz de pixels na árvore realizando sucessivas divisões
		//na imagem por meio de quadrantes
		root = compress(image, 0, 0, height, width);
		
	}
	
	/**
	 * Transforma uma matriz de pixels em uma QuadTree. Função recursiva que 
	 * realiza subdivisões em uma região específica da imagem
	 * 
	 * @param image A matriz de pixels da imagem a ser dividida
	 * @param i Coordenada X inicial da região
	 * @param j Coordenada Y inicial da reigão
	 * @param h Altura da região
	 * @param w Largura da região
	 * 
	 * @return O nó que representa a região
	 */
	private Node compress(Color image[][], int i, int j, int h, int w) {
					
		//cria o nó que representará a região
		Node node = new Node(j, i, h, w);
		
		Color c;
				
		//caso base, menor elemento (pixel)
		if(h == 1 && w == 1) {			
			node.setColor(image[i][j]);
		}
		//caso especial quando a divisão não é exata.
		//apenas a altura ou largura é igual a 1
		else if(h == 1 || w == 1) {
			
			if(h == 1) {
				
				if(w <= 4) {
					
					for(int k = 0; k < w; ++k) {
						node.getQ()[k] = compress(image, i, j + k, h, 1);
					}		
										
				}
				else {
					
					int w_ = w / 2;
					
					node.getQ()[0] = compress(image, i, j, 1, w_);
					node.getQ()[1] = compress(image, i, j + w_, 1, w - w_);
					
				}
				
			}
			else {
				
				if(h <= 4) {
					
					for(int k = 0; k < h; ++k) {
						node.getQ()[k] = compress(image, i + k, j, 1, w);
					}			
					
				}
				else {
									
					int h_ = h / 2;
					
					node.getQ()[0] = compress(image, i, j, h_, 1);
					node.getQ()[1] = compress(image, i + h_, j, h - h_, 1);
					
				}
				 
			}
			
			//define a cor do nó de acordo com a méida das cores dos filhos
			node.setColor(node.averageChildren());
			
			
		}
		//compressão com nível de qualidade desejado pelo usuário
		else if((c = getNodeColor(image, i, j, h, w)) != null) {
			node.setColor(c);
		}
		//divisão e chamadas recursivas
		else {
			
			int h_ = h / 2;
			int w_ = w / 2;
				
			node.getQ()[0] = compress(image, i, j + w_, h_, w - w_);
			node.getQ()[1] = compress(image, i, j, h_, w_);
			node.getQ()[2] = compress(image, i + h_, j, h - h_, w_);
			node.getQ()[3] = compress(image, i + h_, j + w_, h - h_, w - w_);
			
			node.setColor(node.averageChildren());
	
		}
				
		return node;
								
	}
	
	/**
	 * Retorna a cor da região, se a mesma possui a precisão mínima.
	 * Se a precisão mínima não é respeitada retorna um ponteiro nulo
	 * 
	 * @param image A matriz de pixels da imagem
	 * @param i Coordenada X inicial da região
	 * @param j Coordenada Y inicial da região
	 * @param h Altura da região
	 * @param w largura da região
	 * 
	 * @return A média da reigão se a região respeita a precisão mínima
	 *         null caso contrário
	 */
	private Color getNodeColor(Color image[][], int i, int j, int h, int w) {
		
		Map<Color, Integer> colors = new HashMap<>();
		
		float r = 0.0f;
		float g = 0.0f;
		float b = 0.0f;
		float a = 0.0f;
		
		for(int k = 0; k < h; ++k) {
			
			for(int l = 0; l < w; ++l) {
				
				Color c = image[k + i][l + j];
				
				r = r + (c.getRed() / 255.0f);
				g = g + (c.getGreen() / 255.0f);
				b = b + (c.getBlue() / 255.0f);
				a = a + (c.getAlpha() / 255.0f);
				
				//cria um hashmap contendo todas as cores da região e q a 
				//frequência de ocorrência
				if(!colors.containsKey(c)) {
					colors.put(c, 1);
				}
				else {
					int count = colors.get(c) + 1;
					colors.put(c, count);
				}
				
			}	
						
		}
		
		int size = h * w;
		
		r = r / size;
		g = g / size;
		b = b / size;
		a = a / size;
		
		//verifica a cor de maior ocorrência na região
		int max = 0;
		for (Color c : colors.keySet()) {  
			
			int count = colors.get(c);
			max = Math.max(max, count); 
			
		}
		
		//precisão da região em relação a cor com maior número de ocorrências
		double currentAccuracy = (double) max / size;
		
		return (currentAccuracy >= accuracy) ? new Color(r, g, b, a) : null;
		
	}
		
	/**
	 * Retorna a altura da árvore (número de níveis)
	 * 
	 * @return A altura da árvore (número de níveis)
	 */
	public int height() {
		return height(root);
	}
	
	/**
	 * Método recursivo para realizar o cálculo da altura da árvore a partir
	 * de um nó inicial
	 * 
	 * @param node Nó sobre o qual deseja-se calcular a altura da árvore
	 * 
	 * @return A altura da árvore sobre aquele nó
	 */
	private int height(Node node) {
		
		if(node == null) return 0;
		
		int h1 = height(node.getQ()[0]);
		int h2 = height(node.getQ()[1]);
		int h3 = height(node.getQ()[2]);
		int h4 = height(node.getQ()[3]);
				
		int max = (h1 > h2) ? h1 : (h2 > h3) ? h2 : (h3 > h4) ? h3 : h4;
		
		return max + 1;
		
	}
	
	/**
	 * Retorna a raiz da árvore
	 * 
	 * @return A raiz da árvore
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * Retorna a altura da imagem representada pela árvore
	 * 
	 * @return A altura da imagem representada pela árvore
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Retorna a largura da imagem representada pela árvore
	 * 
	 * @return A largura da imagem representada pela árvore
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retorna a precisão utilizada na construção da árvore e realizar divisões
	 * na imagem 
	 * 
	 * @return A precisão utilizada na construção da árvore
	 */
	public double getAccuracy() {
		return accuracy;
	}

}
