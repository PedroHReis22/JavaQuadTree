package quadtree.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;

/**
 * <p><i>Frame</i> para configurar a exportação de uma imagem para uma 
 * QuadTree.</p>
 * 
 * <p>Apresenta a precisão desejada e a opção de cancelar a exportação.</p>
 * 
 * @author pedro
 *
 */
public class ExportFrame extends JDialog {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -3943394636921260626L;
	
	/**
	 * Botão para cancelar a exportação
	 */
	private JButton btnCancel;
	
	/**
	 * Botão para confirmar a exportação
	 */
	private JButton btnExport;
	
	/**
	 * Apresenta a precisão desejada para a exportação da imagem para uma 
	 * QuadTree 
	 */
	private JSpinner spinnerAccuracy;
	
	/**
	 * Armazena se a exportação foi confirmada ou cancelada
	 */
	private boolean export;

	/**
	 * Cria a instância de um frame para a exportação da imagem para uma 
	 * QuadTree
	 */
	public ExportFrame() {
		
		export = false;
		
		//inicializa os componentes gráficos
		initComponents();
		
		//define os listeners
		setListeners();
		
	}
	
	/**
	 * Inicializa os componentes gráficos do frame
	 */
	private void initComponents() {
		
		setTitle("Exportar para QuadTree");
		setMinimumSize(new Dimension(300, 120));
		setResizable(false);
		setModal(true);
		
		JPanel buttonsPanel = new JPanel();
		
		FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancelar");
		btnCancel.setIcon(new ImageIcon(ExportFrame.class.getResource("/quadtree/resources/cancel.png")));
		buttonsPanel.add(btnCancel);
		
		btnExport = new JButton("Exportar");
		btnExport.setIcon(new ImageIcon(ExportFrame.class.getResource("/quadtree/resources/ok.png")));
		buttonsPanel.add(btnExport);
		
		JPanel spinnerPanel = new JPanel();
		getContentPane().add(spinnerPanel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Precisão");
		
		spinnerAccuracy = new JSpinner();
		SpinnerNumberModel model = new SpinnerNumberModel(99.5, 0.0, 100.0, 0.5);
		spinnerAccuracy.setModel(model);		
		
		GroupLayout layout = new GroupLayout(spinnerPanel);
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addGap(28)
					.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(spinnerAccuracy, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
					.addGap(35))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
						.addComponent(spinnerAccuracy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31))
		);
		
		spinnerPanel.setLayout(layout);
		
	}
	
	/**
	 * Faz a ligação entre listeners e os elementos gráficos do frame
	 */
	private void setListeners() {
		
		ActionListener btnListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(btnCancel)) export = false;
				else if(e.getSource().equals(btnExport)) export = true;
				dispose();
				
			}
			
		};
		
		btnCancel.addActionListener(btnListener);
		btnExport.addActionListener(btnListener);
		
		//permite que ao apertar o botão enter a exportação se confirmada e
		//que com o esc a exportação seja cancelada
		Component editor = spinnerAccuracy.getEditor().getComponent(0);
		editor.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnExport.doClick();
				}
				else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					btnCancel.doClick();
				}
				
				try { spinnerAccuracy.commitEdit(); }
				catch (ParseException e1) { e1.printStackTrace(); }
				
			}
			
		});
		
	}
	
	/**
	 * Retorna se a exportação foi confirmada ou cancelada
	 * 
	 * @return true se a exportação foi confirmada
	 *         false caso contrário
	 */
	public boolean exported() {
		return export;
	}
	
	/**
	 * Retorna a precisão selecionada pelo usuário
	 * 
	 * @return A precisão selecionada pelo usuário
	 */
	public double getAccuracy() {
		
		double accuracy = (double) spinnerAccuracy.getValue();
		
		accuracy = accuracy < 0 ? 0 : accuracy;
		accuracy = accuracy > 100 ? 100 : accuracy;
		
		return accuracy;
	}
}
