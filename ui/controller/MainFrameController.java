package quadtree.ui.controller;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import quadtree.core.QuadTree;
import quadtree.ui.ExportFrame;
import quadtree.ui.MainFrame;
import quadtree.ui.components.ImageView;
import quadtree.ui.components.QuadTreeImageView;
import quadtree.ui.components.RegularImageView;
import quadtree.ui.filechooser.FileChooser;

/**
 * <p>Controlador para o <i>frame</i> principal da aplicação</p>
 * 
 * <p>Controlar as operações realizadas sobre a interface</p>
 * 
 * @author pedro
 *
 */
public class MainFrameController {
	
	/**
	 * Frame principal da aplicação controlado pela instância deste controlador
	 */
	private MainFrame frame;
	
	/**
	 * Objeto para seleção de arquivo para salvar/abrir
	 */
	private FileChooser fileChooser;
	
	/**
	 * QuadTree em uso pela aba 
	 */
	private QuadTree quadTree;
	
	/**
	 * Cria a instância do controlador para o frame principal da alicação
	 * 
	 * @param frame O frame que a instância do controlador controlará
	 */
	public MainFrameController(MainFrame frame) {
		
		this.frame = frame;
		
		fileChooser = new FileChooser();
	}
	
	/**
	 * Permite ao usuário selecionar uma imagem para carregar sobre a aplicação
	 */
	public void loadImage() {
		
		//filechooser para a seleção da imagem
		int response = fileChooser.showOpenDialog(frame);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			
			frame.setTitle("QuadTree - [" + file + "]");
			
			//imagem do tipo quadtree
			if(file.getAbsolutePath().toLowerCase().endsWith(".quad")) {
				loadQuadTreeImage(file);
			}
			//imagem regular
			else {
				loadRegularImage(file);
			}	
			
		}
		
	}
	
	/**
	 * Carrega uma imagem do tipo QuadTree na aplicação, adicionando uma nova
	 * aba ao frame principal
	 * 
	 * @param file O arquivo que armazena a imagem
	 */
	private void loadQuadTreeImage(File file) {
		
		try {
			
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			//deserializa o objeto da imagem
			quadTree = (QuadTree) ois.readObject();
			
			ois.close();
			fis.close();
			
			int accuracy = (int) (quadTree.getAccuracy() * 100);
			
			String name = file.getName() + " - " + accuracy + "%";
			String path = file.getAbsolutePath();
			
			//adiciona o painel de visualização a imagem em uma nova aba
			QuadTreeImageView v = new QuadTreeImageView(name, path, quadTree);
			frame.addTab(name, v);
			
		} catch (IOException | ClassNotFoundException e) {
			
			//erro ao abrir a imagem
			
			String title = "Erro ao abrir";
			String message = "Ocorreu um erro ao abrir o arquivo " + 
					file.getAbsolutePath() + "\nPor favor, tente novamente";
			
			JOptionPane.showMessageDialog(frame, message, title, 
					JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Carrega uma imagem regular na aplicação, adicionando uma nova aba ao 
	 * frame principal
	 * 
	 * @param file O arquivo que armazena a imagem
	 */
	private void loadRegularImage(File file) {
		
		Icon icon = new ImageIcon(file.getAbsolutePath());
		
		RegularImageView imageView = new RegularImageView(file, icon);
		
		frame.addTab(file.getName(), imageView);
		
	}
	
	/**
	 * Exporta uma imagem regular para uma imagem do tipo QuadTree ou uma 
	 * Quadtree para um formato regular. 
	 * 
	 * @param component O Component atual da imagem. 
	 */
	public void export(Component component) {
		
		final ImageView imageView = (ImageView) component;
		
		if(imageView instanceof RegularImageView) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					exportToQuadTree(imageView);
				}
				
			}).run();
						
		}
		else {
			exportToImage(imageView);
		}
				
	}
	
	/**
	 * Exporta uma imagem regular para uma QuadTree
	 * 
	 * @param imageView O painel de visualização da imagem
	 */
	private void exportToQuadTree(ImageView imageView) {
		
		//cria o frame de configuração da visualização
		ExportFrame exportFrame = new ExportFrame();
		exportFrame.setLocationRelativeTo(frame);
		exportFrame.setVisible(true);		
				
		//usuário cancelou a exportação
		if(!exportFrame.exported()) return;
		
		File file = imageView.getFile();
		
		double accuracy = exportFrame.getAccuracy() / 100.0;
			
		//cria a imagem de acordo com a precisaõ
		quadTree = new QuadTree(file, accuracy);
		
		String name = imageView.getFileName() + " - " + 
						exportFrame.getAccuracy() + "%";
		
		QuadTreeImageView view = new QuadTreeImageView(name, "", quadTree);
		frame.addTab(name, view);
		
	}
	
	/**
	 * Exporta uma imagem do tipo QuadTree para uma imagem regular
	 * 
	 * @param imageView O pinel de visulização da imagem quadTree
	 */
	private void exportToImage(ImageView imageView) {
		
		//seleciona o arquivo destino da imagem
		int response = fileChooser.showSaveDialog(frame);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			
			JLabel lblImage = imageView.getLblImage();
			
			String tokens[] = path.split("\\.");
			String extension = tokens[tokens.length - 1].toLowerCase();
			
			String extensions[] = 
					new String[]{"jpg", "bmp", "gif", "png", "jpeg"};
			
			boolean flag = false;
			for(int i = 0; i < extensions.length; ++i) {
				if(extensions[i].equalsIgnoreCase(extension)) {
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				path = path + ".png";
				extension = "png";	
			}			
			
			try {
				
				int h = quadTree.getHeight();
				int w = quadTree.getWidth();
								
				BufferedImage img = new BufferedImage(w, h, 
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2d = img.createGraphics();  
				lblImage.paintAll(g2d);
				g2d.dispose();  
				
				ImageIO.write(img, extension, new File(path));  
				
			} catch (IOException e) {
				
				//erro ao salvar imagem
				
				String title = "Erro ao salvar imagem";
				String message = "Ocorreu um erro ao salvar a imagem " + 
							file.getAbsolutePath() + 
							"\nPor favor, tente novamente";
				
				JOptionPane.showMessageDialog(frame, message, title, 
						JOptionPane.ERROR_MESSAGE);
				
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * Salva a imagem em um arquivo do tipo QuadTree em um arquivo.
	 * 
	 * A imagem é salva serializando o objeto da árvore
	 * 
	 * @param component O Component atual da imagem
	 */
	public void save(Component component) {
		
		//seleção do arquivo destinatário
		int response = fileChooser.showSaveDialog(frame);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			
			//se o arquivo selecionado não é do tipo '.quad', adiciona a 
			//extensão ao arquivo
			if(!path.toLowerCase().endsWith(".quad")) {
				path = path + ".quad";
			}
			
			try {
				
				//serializa o objeto e salva em arquivo
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				oos.writeObject(quadTree);
				
				oos.flush();
				oos.close();
				
				fos.flush();
				fos.close();
				
			} catch (IOException e) {
				
				//erro ao salvar a imagem
				
				String title = "Erro ao salvar";
				String message = "Ocorreu um erro ao salvar o arquivo " + 
							file.getAbsolutePath() + 
							"\nPor favor, tente novamente";
				
				JOptionPane.showMessageDialog(frame, message, title, 
						JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();
			}
			
		}	
				
	}

	/**
	 * Realiza a operção de mudança de aba da interface pricipal.
	 * 
	 * O título do frame principal é alterado e as opções para o novo tipo de
	 * imagem são habilitadas
	 * 
	 * @param component O novo Component selecionado no frame principal
	 */
	public void changeTab(Component component) {
		
		ImageView imageView = (ImageView) component;
		
		String path = imageView.getPath();
		
		if(path != null && !path.isEmpty()) {
			frame.setTitle("QuadTree - [" + imageView.getPath() + "]");
		}
		else {
			frame.setTitle("QuadTree");
		}
		
		boolean saveMenu = imageView instanceof QuadTreeImageView;
		boolean showDivisions = imageView instanceof QuadTreeImageView;
		
		//habilita as funções para o tipo da umagem
		frame.enableSaveMenu(saveMenu);
		frame.enableShowDivisions(showDivisions);
		
		if(imageView instanceof QuadTreeImageView) {
			
			QuadTreeImageView qiv = (QuadTreeImageView) imageView;
			
			frame.checkShowDivisions(qiv.showDivisions());
			
		}
		
	}
	
	/**
	 * Atualiza uma imagem do tipo QuadTree para mostrar as divisões
	 * 
	 * @param component O Component que representa a imagem
	 * @param selected true se a imagem deve mosrtar as divisões e false caso
	 * 						contrário
	 */
	public void showDivisions(Component component, boolean selected) {
		
		QuadTreeImageView imageView = (QuadTreeImageView) component;
		
		imageView.setShowDivisions(selected);
		
		imageView.update();
		
	}
}
