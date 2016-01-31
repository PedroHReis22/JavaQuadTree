package quadtree.ui.filechooser;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * <p>Implementa um JFileChooser para arquivos de imagem aceitando imagens
 * tradicionais e do tipo QuadTree.</p> 
 * 
 * @author pedro
 *
 */
public class FileChooser extends JFileChooser {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -3994950314866981557L;
	
	/**
	 * Armazena o último arquivo selecionado pelo filechooser. Toda vez o 
	 * filechooser é inicializado com o último arquivo selecionado 
	 */
	private File lastDir;
	
	/**
	 * Cria a instância do filechooser
	 */
	public FileChooser() {
		
		//nenhum arquivo foi selecionado
		lastDir = null;
		
		//filtro para seleção apenas de imagens (regulares ou quadtree)
		setFileFilter(new ImageFilter());
		
		//não permite ao usuário selecionar qualquer tipo de ima
		setAcceptAllFileFilterUsed(false);
		
		//adiciona um preview da imagem ao filechooser
		setAccessory(new ImagePreview(this));
		
	}
	
	@Override
	public int showOpenDialog(Component parent) {
		
		if(lastDir != null) {
			setCurrentDirectory(lastDir);	
		}		
		
		int response = super.showOpenDialog(parent);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			lastDir = getSelectedFile();
		}
		
		return response;
	}
	
	@Override
	public int showSaveDialog(Component parent) {
		
		if(lastDir != null) {
			setCurrentDirectory(lastDir);	
		}
		
		int response = super.showSaveDialog(parent);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			
			if(getSelectedFile().exists()) {
				
				if(overwrites(getSelectedFile(), parent)) {
					lastDir = getSelectedFile();
					return JFileChooser.APPROVE_OPTION;
				}
				else {
					return JFileChooser.CANCEL_OPTION;
				}
				
			}
			
		}
		
		return response;
		
	}
	
	/**
	 * 
	 * Verifica se o usuário deseja sobrescrever um arquivo selecionado que já
	 * existe
	 * 
	 * @param file O arquivo selecionado
	 * @param parent O elemento sobre o qual o alerta deve ser exibido
	 * 
	 * @return true se o usuário deseja sobrescrever o arquivo
	 *         false caso contrário
	 */
	private boolean overwrites(File file, Component parent) {
		
		String title = "Sobrescrever arquivo?";
		String message = "O arquivo " + file.getName() + " já existe.\n"
				+ "Deseja sobrescrevê-lo?";
		
		int response = JOptionPane.showConfirmDialog(parent, message, title, 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		return response == JOptionPane.YES_OPTION;

	}

}

/**
 * <p>Implementa o filtro de imagens para o filechooser. Imagens regulares e do
 *  tipo quadtree são permitidas</p> 
 * 
 * @author pedro
 *
 */
class ImageFilter extends FileFilter {
	
	//extensões permitidas
	private String extensions[] = 
			new String[]{"jpg", "bmp", "gif", "png", "jpeg", "quad"};

	@Override
	public boolean accept(File pathname) {
		
		//permite a navegação por diretórios
		if(pathname.isDirectory()) return true;
		
		//identifica a extensão do arquivo selecionado
		String tokens[] = pathname.getAbsolutePath().split("\\.");
		String extension = tokens[tokens.length - 1];
				
		//verifica se a extensão é permitida
		for(String e : extensions) {
			if(e.equalsIgnoreCase(extension)) return true;
		}
		
		return false;
	}

	@Override
	public String getDescription() {
		return "Arquivos de Imagem";
	}
		
}
