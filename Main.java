package quadtree;

import java.awt.EventQueue;

import quadtree.ui.MainFrame;

/**
 * <p>Inicializa a aplicação, apresentando a interface gráfica.</p>
 * 
 * @author pedro
 *
 */
public class Main {

	/**
	 * Método principal invocado no inicializar da aplicação
	 * 
	 * @param args Argumentos passados por linha de comando. Esses argumentos
	 * 				são ignorados
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
	
			@Override
			public void run() {
				
				try { new MainFrame().setVisible(true); }
				catch (Exception e) { e.printStackTrace(); }
				
			}
			
		});
		
		
		
	}

}
