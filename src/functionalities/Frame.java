package functionalities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Frame {

	private JFrame frame = new JFrame("SID");

	/* TextAreas do Paho (mensagens recebidas), MongoHT (mensagens enviadas para a coleção HumidadeTemperatura),
	MongoB (mensagens que foram enviadas para o sybase e que foram colocadas na coleção BackUp)
	e Sybase (Base de dados HumidadeTemperatura) */
	
	private JTextArea textAreaPaho;
	private JTextArea textAreaMongoHT;
	private JTextArea textAreaMongoB;
	private JTextArea textAreaSybase;
	
	// ScrollPane de todas as textAreas para maior facilidade de visualização
	
	private JScrollPane scrollPanePaho;
	private JScrollPane scrollPaneMongoHT;
	private JScrollPane scrollPaneMongoB;
	private JScrollPane scrollPaneSybase;
	
	// Elementos para utilizar Paho, textField para escrever topic pretendido e botão para confirmar e subscrever o topico
	
	private JTextField topicText;
	private JButton subscribeButton;
	
	/* Elementos para utilizar Sybase, textField para escrever ip do servidor Sybase e botão para confirmar,
	textField para inserir frequencia pretendida (em segundos) e o botão para confirmar */
	 	
	private JTextField ipText;
	private JButton ipButton;
	private JTextField frequencyTextSybase;
	private JButton frequencyButtonSybase;
	
	/* Elementos para utilizar MongoDB, textField para escrever o host do servidor MongoDB e botão para confirmar,
	textField para inserir frequencia pretendida (em segundos) e o botão para confirmar */
	
	private JTextField hostText;
	private JButton hostButton;
	private JTextField frequencyTextMongo;
	private JButton frequencyButtonMongo;
	
	// Tempos em milisegundos para a migração
	
	private int timerSybase = 20000;
	private int timerMongo = 0000;
	
	// Metodo que vai iniciar a frame
	
	public void init(){
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		frame.add(panel);
		
		panelPaho(panel);
		panelMongo(panel);
		panelSybase(panel);	
		
		frame.setVisible(true);
		frame.setSize(908, 685);
	}
	
	// Painel Paho

	private void panelPaho(JPanel panel) {
		
		JPanel panelPaho = new JPanel();
		panel.add(panelPaho, BorderLayout.WEST);
		
		JPanel panelSecun = new JPanel();
		panelSecun.setLayout(new BorderLayout());
		panelPaho.add(panelSecun);
		JPanel panelSecunCenter = new JPanel();
		panelSecun.add(panelSecunCenter, BorderLayout.CENTER);
		
		JLabel paho = new JLabel("Paho");
		
		JLabel topic = new JLabel("Topic: ");
		topicText = new JTextField("foo");
		topicText.setPreferredSize(new Dimension(200, 28));
		subscribeButton = new JButton("Subscribe");
		
		textAreaPaho = new JTextArea();
		textAreaPaho.setEditable(false);
		scrollPanePaho = new JScrollPane(textAreaPaho);
		scrollPanePaho.setPreferredSize(new Dimension(480, 175));
		
		panelSecun.add(paho, BorderLayout.NORTH);
		panelSecun.add(scrollPanePaho, BorderLayout.SOUTH);
		panelSecunCenter.add(topic);
		panelSecunCenter.add(topicText);
		panelSecunCenter.add(subscribeButton);
	}
	
	// Painel MongoDB
	
	private void panelMongo(JPanel panel) {

		JPanel panelMongo = new JPanel();
		panel.add(panelMongo, BorderLayout.SOUTH);
		
		JPanel panelSecun = new JPanel();
		panelSecun.setLayout(new BorderLayout());
		panelMongo.add(panelSecun);
		JPanel panelSecunCenter = new JPanel();
		panelSecun.add(panelSecunCenter, BorderLayout.CENTER);
		
		JLabel mongo = new JLabel("Mongo");
		
		JLabel host = new JLabel("Host: ");
		hostText = new JTextField("localhost");
		hostText.setPreferredSize(new Dimension(125, 28));
		hostButton = new JButton("Connect");
		JLabel frequency = new JLabel("Frequency: ");
		frequencyTextMongo = new JTextField(String.valueOf(timerMongo/1000));
		frequencyTextMongo.setPreferredSize(new Dimension(50, 28));
		frequencyButtonMongo = new JButton("Set");
		
		frequencyButtonMongo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timerMongo = Integer.parseInt(frequencyTextMongo.getText()) * 1000;
				
			}
		});
		
		textAreaMongoHT = new JTextArea();
		textAreaMongoHT.setEditable(false);
		textAreaMongoB = new JTextArea();
		textAreaMongoB.setEditable(false);
		scrollPaneMongoHT = new JScrollPane(textAreaMongoHT);
		scrollPaneMongoHT.setPreferredSize(new Dimension(883, 175));
		scrollPaneMongoB = new JScrollPane(textAreaMongoB);
		scrollPaneMongoB.setPreferredSize(new Dimension(883, 175));
		
		panelSecun.add(mongo, BorderLayout.NORTH);
		panelSecun.add(scrollPaneMongoHT, BorderLayout.SOUTH);
		
		JPanel textArea = new JPanel();
		textArea.setLayout(new BorderLayout());
		panelSecun.add(textArea, BorderLayout.SOUTH);
		textArea.add(scrollPaneMongoHT, BorderLayout.CENTER);
		textArea.add(scrollPaneMongoB, BorderLayout.SOUTH);
		panelSecunCenter.add(host);
		panelSecunCenter.add(hostText);
		panelSecunCenter.add(hostButton);
		panelSecunCenter.add(frequency);
		panelSecunCenter.add(frequencyTextMongo);
		panelSecunCenter.add(frequencyButtonMongo);
	}

	// Painel Sybase
	
	private void panelSybase(JPanel panel) {
		
		JPanel panelSybase = new JPanel();
		panel.add(panelSybase, BorderLayout.CENTER);
		
		JPanel panelSecun = new JPanel();
		panelSecun.setLayout(new BorderLayout());
		panelSybase.add(panelSecun);
		JPanel panelSecunCenter = new JPanel();
		panelSecun.add(panelSecunCenter, BorderLayout.CENTER);
		
		JLabel sybase = new JLabel("Sybase");
		
		JLabel ipLabel = new JLabel("IP: ");
		ipText = new JTextField("localhost");
		ipText.setPreferredSize(new Dimension(125, 28));
		ipButton = new JButton("OK");
		JLabel frequency = new JLabel("Frequency: ");
		frequencyTextSybase = new JTextField(String.valueOf(timerSybase/1000));
		frequencyTextSybase.setPreferredSize(new Dimension(50, 28));
		frequencyButtonSybase = new JButton("Set");
		
		frequencyButtonSybase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timerSybase = Integer.parseInt(frequencyTextSybase.getText()) * 1000;
			}
		});
	
		textAreaSybase = new JTextArea();
		textAreaSybase.setEditable(false);
		scrollPaneSybase = new JScrollPane(textAreaSybase);
		scrollPaneSybase.setPreferredSize(new Dimension(395, 175));
		panelSybase.add(scrollPaneSybase);
		
		panelSecun.add(sybase, BorderLayout.NORTH);
		panelSecun.add(scrollPaneSybase, BorderLayout.SOUTH);
		panelSecunCenter.add(ipLabel);
		panelSecunCenter.add(ipText);
		panelSecunCenter.add(ipButton);
		panelSecunCenter.add(frequency);
		panelSecunCenter.add(frequencyTextSybase);
		panelSecunCenter.add(frequencyButtonSybase);
	}
	
	public JScrollPane getScrollPanePaho(){
		return scrollPanePaho;
	}
	
	public JScrollPane getScrollPaneMongoHT(){
		return scrollPaneMongoHT;
	}
	
	public JScrollPane getScrollPaneMongoB(){
		return scrollPaneMongoB;
	}
	
	public JScrollPane getScrollPaneSybase(){
		return scrollPaneSybase;
	}
	
	public JTextField getTopicText(){
		return topicText;
	}
	
	public JButton getSubscribeButton(){
		return subscribeButton;
	}
	
	public JTextField getIPText(){
		return ipText;
	}
	
	public JButton getIPButtonSybase(){
		return ipButton;
	}

	public JTextField getFrequencyTextSybase() {
		return frequencyTextSybase;
	}

	public JButton getFrequencyButtonSybase() {
		return frequencyButtonSybase;
	}

	public JTextField getHostText() {
		return hostText;
	}

	public JButton getHostButton() {
		return hostButton;
	}

	public JTextField getFrequencyTextMongo() {
		return frequencyTextMongo;
	}

	public JButton getFrequencyButtonMongo() {
		return frequencyButtonMongo;
	}	
	
	public int getTimerSybase(){
		return timerSybase;
	}
	
	public int getTimerMongo(){
		return timerMongo;
	}
}