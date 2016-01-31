package quadtree.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * <p>Personaliza um JTabbedPane para que em cada aba seja exibido um botão 
 * que permita fechar a aba.</p>
 * 
 * @author pedro
 *
 */
public class ButtonTabbedPane extends JTabbedPane {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 4805006657498099197L;
	
	@Override
	public Component add(String title, Component component) {
		
		Component c = super.add(title, component);
		ImageView imageView = (ImageView) c;
		
		int index = getTabCount() - 1;
		
		setToolTipTextAt(index, imageView.getPath());
		setTabComponentAt(index, new ButtonTabComponent(this));
		
		return c;
	}
	
	/**
	 * <p>Classe que representa o JPanel que formará a aba do JTabbedPane.</p>
	 * 
	 * <p>Através desta classe que o botão é adicionado.</p>
	 * 
	 * <p>Adaptado de: http://docs.oracle.com/javase/tutorial/uiswing/
	 * examples/components/TabComponentsDemoProject/src/components/
	 * ButtonTabComponent.java</p> 
	 * 
	 * @author pedro
	 *
	 */
	private class ButtonTabComponent extends JPanel {
		
		/**
		 * Serial version
		 */
		private static final long serialVersionUID = 8270765213875737274L;
 
		/**
		 * Cria uma instância do panel que representará a aba
		 * 
		 * @param pane JPanel que representará a aba
		 */
		private ButtonTabComponent(final JTabbedPane pane) {
		
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
			if (pane == null) {
				throw new NullPointerException();
			}
			
			setOpaque(false);			
         
			//JLabel para representar o título da aba
			JLabel label = new JLabel() {
			
				/**
				 * Serial version
				 */
				private static final long serialVersionUID = 
						5122281940412751516L;

				@Override
				public String getText() {
					
					int i = pane.indexOfTabComponent(ButtonTabComponent.this);
					
					return (i != -1) ? pane.getTitleAt(i) : null; 
				
				}
			};
         
			add(label);
			
			//espaço entre o título e o botão
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			
			//adiciona o botão
			JButton button = new ButtonTab(pane, this);
			add(button);
			
			//espaço entre o topo e o componente
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
			
		}	
    
	}
	
	/**
	 * <p>Classe para representar o botão de uma aba.</p>
	 * 
	 * @author pedro
	 *
	 */
	private class ButtonTab extends JButton {
		
		/**
		 * Serial version
		 */
		private static final long serialVersionUID = -8679610640311373076L;
		
		/**
		 * O JTabbedPane que o botão faz parte. Permite deletar a aba no evento
		 * do botão
		 */
		private final JTabbedPane pane;
		
		/**
		 * Componente da aba do botão
		 */
		private final ButtonTabComponent component;
		
		/**
		 * Cria a instância do botão da aba
		 * 
		 * @param pane O JTabbedPane da aba em que o botão está inserido
		 * @param component O Component em que o botão será inserido
		 */
        private ButtonTab(final JTabbedPane pane, 
        		final ButtonTabComponent component) {
        	
        	this.pane = pane;
        	this.component = component;
        	
        	//dimensão do botão
            int size = 18;
            setPreferredSize(new Dimension(size, size));
            
            setToolTipText("Fechar essa aba");
            
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            
            setRolloverEnabled(true);
            
            //vincula evento ao botão
            setListeners();
            
        }
        
        @Override
        public void updateUI() {
        	super.updateUI();
        }
 
        @Override
        protected void paintComponent(Graphics g) {
        	
        	super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g.create();
            
            //translada o botão quando ele for precionado
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            
            if (getModel().isRollover()) {
                g2.setColor(Color.RED);
            }
            
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, 
            		getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, 
            		getHeight() - delta - 1);
            g2.dispose();
            
        }
        
        /**
         * Vincula os elementos da interface gráfica a listeners
         */
        private void setListeners() {
        	
        	ActionListener actionListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					closeTab();
				}
				
			};
			
			MouseAdapter mouseAdapter = new MouseAdapter() {
				
				@Override
				public void mouseEntered(MouseEvent e) {
					entered(e.getComponent());
		        }
		 
				@Override
		        public void mouseExited(MouseEvent e) {
					exited(e.getComponent());
		        }
				
			};
			
			addActionListener(actionListener);
			addMouseListener(mouseAdapter);
        	
        }
 
        /**
         * manipula o evento de click no botão fechando a aba atual do 
         * JTabbedPane
         */
        private void closeTab() {
        	
        	int i = pane.indexOfTabComponent(component);
        	
            if (i != -1) {
                pane.remove(i);
            }
            
        }
        
        /**
         * Evento quando o mouse entra na região do botão
         * 
         * @param component O Component que o mouse entrou na região 
         */
        private void entered(Component component) {
            
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
        
        /**
         * Evento quando o mouse sai na região do botão
         * 
         * @param component O Component que o mouse saiu na região 
         */
        private void exited(Component component) {
        	
        	if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        	
        }
        
    }
     
}