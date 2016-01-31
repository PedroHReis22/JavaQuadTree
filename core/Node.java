package quadtree.core;

import java.awt.Color;
import java.io.Serializable;

/**
 * <p>Representa um nó da QuadTree. Cada nó representa uma região da imagem com
 * granularidade mínima de um pixel.</p>
 * 
 * <p>Armazena o ponto (x, y) inicial da região, a altura e largura da região, 
 * a cor do pixel e os filhos do nó.</p>
 * 
 * <p>Se o nó é uma folha ele armazena a cor da região que representa, caso 
 * contrário armazena a média das cores de seus filhos</p>
 * 
 * @author pedro
 *
 */
public class Node implements Serializable {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -3699702034980747558L;
	
	/**
	 * Posição X inicial da região representada pelo nó
	 */
	private int x;
	
	/**
	 * Posição Y inicial da região representada pelo nó
	 */
	private int y;
	
	/**
	 * Altura da região representada pelo nó
	 */
	private int height;
	
	/**
	 * Largura da região representada pelo nó
	 */
	private int width;
	
	/**
	 * Cor da região
	 */
	private Color color;
	
	/**
	 * Ponteiros para os filhos do nó
	 */
	private Node q[];
	
	/**
	 * 
	 * Cria a instância de um nó através dos valores x e y inciais e dimensões
	 * da região
	 * 
	 * @param x Posição X inicial da região representada pelo nó
	 * @param y Posição Y inicial da região representada pelo nó
	 * @param height Altura da região
	 * @param width Largura da região
	 */
	public Node(int x, int y, int height, int width) {
		
		this.x = x;
		this.y = y;
		
		this.height = height;
		this.width = width;
		
		q = new Node[4];
	}
	
	/**
	 * Obtém a média de cor dos filhos do nó. A cor resultante é composta pela
	 * média de cada uma das componentes (R, G e B e alpha)
	 * 
	 * @return A cor média dos filhos
	 */
	public Color averageChildren() {
		
		float r = 0.0f;
		float g = 0.0f;
		float b = 0.0f;
		float a = 0.0f;
		
		int div = 0;
		
		for(int i = 0; i < q.length; ++i) {
			
			if(q[i] != null) {
								
				r = r + (q[i].getColor().getRed() / 255.0f);
				g = g + (q[i].getColor().getGreen() / 255.0f);
				b = b + (q[i].getColor().getBlue() / 255.0f);
				a = a + (q[i].getColor().getAlpha() / 255.0f);
				
				++div;
			}	
						
		}
		
		r = r / div;
		g = g / div;
		b = b / div;
		a = a / div;
			
		return new Color(r, g, b, a);
		
	}
	
	/**
	 * Verifica se o nó é uma folha (não tem nenhum filho)
	 * 
	 * @return true se o nó é folha
	 * 		   false caso contrário
	 */
	public boolean isLeaf() {
		return q[0] == null && q[1] == null && q[2] == null && q[3] == null;
	}
	
	/**
	 * Retorna a posição X inicial representada pelo nó
	 * 
	 * @return A posição X inicial representada pelo nó
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retorna a posição Y inicial representada pelo nó
	 * 
	 * @return A posição Y inicial representada pelo nó
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Retorna a cor do nó
	 * 
	 * @return A cor do nó
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Define a cor do nó
	 * 
	 * @param color A cor que será atribuída ao nó
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Retorna o vetor contendo os filhos do nó
	 * 
	 * @return O vetor contendo os filhos do nó
	 */
	public Node[] getQ() {
		return q;
	}
	
	/**
	 * Retorna a altura da região representada pelo nó
	 * 
	 * @return A altura da região representada pelo nó
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Retorna a largura da região representada pelo nó
	 * 
	 * @return A largura da região representada pelo nó
	 */
	public int getWidth() {
		return width;
	}

}
