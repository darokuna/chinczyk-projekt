package com.ludo.app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.ludo.app.control.HumanPlayer;
import com.ludo.app.control.Player;
import com.ludo.app.model.Pawn;
import com.ludo.app.model.location.camp.BlueCamp;
import com.ludo.app.model.location.camp.GreenCamp;
import com.ludo.app.model.location.camp.RedCamp;
import com.ludo.app.model.location.camp.YellowCamp;
import com.ludo.app.model.location.house.BlueHouse;
import com.ludo.app.model.location.house.GreenHouse;
import com.ludo.app.model.location.house.RedHouse;
import com.ludo.app.model.location.house.YellowHouse;
import com.ludo.app.observer.PawnObserver;
import com.ludo.app.view.box.BlueBox;
import com.ludo.app.view.box.Box;
import com.ludo.app.view.box.GreenBox;
import com.ludo.app.view.box.RedBox;
import com.ludo.app.view.box.StartBlueBox;
import com.ludo.app.view.box.StartGreenBox;
import com.ludo.app.view.box.StartRedBox;
import com.ludo.app.view.box.StartYellowBox;
import com.ludo.app.view.box.WhiteBox;
import com.ludo.app.view.box.YellowBox;
import com.ludo.lan.client.ServerHandler;
import com.ludo.lan.observer.ServerObserver;

public class BoardGame implements ActionListener,PawnObserver, ServerObserver{
	
	private JButton joinYellow;
	private JButton joinRed;
	private JButton joinGreen;
	private JButton joinBlue;
	private JButton cubeRoll;
	private JButton sendMsg;
	private JFrame view;
	private MouseAdapter pawnListener;
	private JTextArea infoArea;
	private ServerHandler handler;
	private JTextField msgField;
	private HashMap<Integer, Player> playerList = new HashMap<Integer, Player>();
	private Box[] box = new Box[96];
	private ArrayList<Pawn> pawns;
	private Player player;
	public BoardGame(String name){
		pawns = new ArrayList<Pawn>();
		player = new HumanPlayer(name);
		pawnListener = new PawnMoveListener();
		System.out.println(playerList.size());
	}
	public void createView(){
		view = new JFrame("Plansza");
		view.setPreferredSize(new Dimension(800, 600));
		view.setMinimumSize(new Dimension(800, 600));
		view.setLayout(new BorderLayout());
		Dimension dialogSize = 	view.getSize();		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
		if(dialogSize.height > screenSize.height) 
			dialogSize.height = screenSize.height;
		if(dialogSize.width > screenSize.width)
			dialogSize.height = screenSize.width;
		view.setLocation((screenSize.width-dialogSize.width)/2,   
						(screenSize.height-dialogSize.height)/2);
		view.setResizable(false);
		createElements();
		view.add(panel(), BorderLayout.CENTER);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setVisible(true);
	}
	private void createElements(){
		joinYellow = new JButton("Dołącz");
		joinYellow.addActionListener(this);
		joinRed = new JButton("Dołącz");
		joinRed.addActionListener(this);
		joinGreen = new JButton("Dołącz");
		joinGreen.addActionListener(this);
		joinBlue = new JButton("Dołącz");
		joinBlue.addActionListener(this);
		cubeRoll = new JButton("Rzuć kostką");
		cubeRoll.addActionListener(this);
		cubeRoll.setEnabled(false);
		
		infoArea = new JTextArea(10,10);
		infoArea.setLineWrap(true);
	 	infoArea.setEditable(false);
	 	
	 	msgField = new JTextField(10);
	 	sendMsg = new JButton("Wyślij");
	 	sendMsg.addActionListener(this);
	}
	private JPanel panel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.decode("0xf1f1f0"));
		panel.add(panelBoard(), BorderLayout.CENTER);
		panel.add(rightPanel(), BorderLayout.EAST);
		return panel;
	}
	private JPanel buttonPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(180, 250));
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(
                blackline, "Wybór gracza");
		title.setTitleJustification(TitledBorder.CENTER);
		panel.setBorder(title);
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		JLabel yellow = new JLabel("Żółty: ");
		yellow.setForeground(Color.decode("0xd6d00f"));
		JLabel red = new JLabel("Czerwony: ");
		red.setForeground(Color.decode("0xcc0607"));
		JLabel green = new JLabel("Zielony: ");
		green.setForeground(Color.decode("0x0acf00"));
		JLabel blue = new JLabel("Niebieski: ");
		blue.setForeground(Color.decode("0x0975db"));
		panel.add(yellow, c);
		c.insets = new Insets(20, 0, 0, 0);
		c.gridy = 1;
		panel.add(red, c);
		c.gridy = 2;
		panel.add(green, c);
		c.gridy = 3;
		panel.add(blue, c);
		c.gridy = 0;
		c.gridx = 1;
		c.insets = new Insets(0, 10, 0, 0);
		panel.add(joinYellow, c);
		c.gridy = 1;
		c.insets = new Insets(20, 10, 0, 0);
		panel.add(joinRed, c);
		c.gridy = 2;
		panel.add(joinGreen, c);
		c.gridy = 3;
		panel.add(joinBlue, c);
		return panel;
	}
	private void initBox(){
		for(int i = 0; i < box.length; i++){
			if(i == 4){
				box[i] = new StartYellowBox();
				box[i].addMouseListener(pawnListener);
			}
				
			else if(i == 20){
				box[i] = new StartRedBox();
				box[i].addMouseListener(pawnListener);
			}
				
			else if(i == 36){
				box[i] = new StartGreenBox();
				box[i].addMouseListener(pawnListener);
			}
				
			else if(i == 52){
				box[i] = new StartBlueBox();
				box[i].addMouseListener(pawnListener);
			}	
			else if(i == 64 || i == 65 || i == 66 || i == 67){
				box[i] = new YellowBox();
				box[i].addMouseListener(pawnListener);
			}
			else if(i == 80 || i == 81 || i == 82 || i == 83 )
				box[i] = new YellowBox();
			else if(i == 68 || i == 69 || i == 70 || i == 71){
				box[i] = new RedBox();
				box[i].addMouseListener(pawnListener);
			}
			else if(i == 84 || i == 85 || i == 86 || i == 87)
				box[i] = new RedBox();
			else if(i == 72 || i == 73 || i == 74 || i == 75){
				box[i] = new GreenBox();
				box[i].addMouseListener(pawnListener);
			}
			else if(i == 88 || i == 89 || i == 90 || i == 91)
				box[i] = new GreenBox();
			
			else if(i == 76 || i == 77 || i == 78 || i == 79){
				box[i] = new BlueBox();
				box[i].addMouseListener(pawnListener);
			}
			else if(i == 92 || i == 93 || i == 94 || i == 95)
				box[i] = new BlueBox();
				
			else{
				box[i] = new WhiteBox();
				box[i].addMouseListener(pawnListener);
			}
				
		}
	}
	
	private void addHousePosition(SpringLayout lay, int start,int boxi, Position position){
		switch(position){
		case LEFT:
			lay.putConstraint(SpringLayout.WEST, box[start], -60, SpringLayout.WEST,
					box[boxi]);
			lay.putConstraint(SpringLayout.NORTH, box[start], 0, SpringLayout.NORTH,
					box[boxi]);
			lay.putConstraint(SpringLayout.WEST, box[start+1], -60, SpringLayout.WEST,
					box[start]);
			lay.putConstraint(SpringLayout.NORTH, box[start+1], 0, SpringLayout.NORTH,
					box[start]);
			lay.putConstraint(SpringLayout.WEST, box[start+2], 0, SpringLayout.WEST,
					box[start]);
			lay.putConstraint(SpringLayout.NORTH, box[start+2], 60, SpringLayout.NORTH,
					box[start]);
			lay.putConstraint(SpringLayout.WEST, box[start+3], 0, SpringLayout.WEST,
					box[start+1]);
			lay.putConstraint(SpringLayout.NORTH, box[start+3], 60, SpringLayout.NORTH,
					box[start+1]);
			break;
		case RIGHT:
			lay.putConstraint(SpringLayout.WEST, box[start], 60, SpringLayout.WEST,
					box[boxi]);
			lay.putConstraint(SpringLayout.NORTH, box[start], 0, SpringLayout.NORTH,
					box[boxi]);
			lay.putConstraint(SpringLayout.WEST, box[start+1], 60, SpringLayout.WEST,
					box[start]);
			lay.putConstraint(SpringLayout.NORTH, box[start+1], 0, SpringLayout.NORTH,
					box[start]);
			lay.putConstraint(SpringLayout.WEST, box[start+2], 0, SpringLayout.WEST,
					box[start]);
			lay.putConstraint(SpringLayout.NORTH, box[start+2], 60, SpringLayout.NORTH,
					box[start]);
			lay.putConstraint(SpringLayout.WEST, box[start+3], 0, SpringLayout.WEST,
					box[start+1]);
			lay.putConstraint(SpringLayout.NORTH, box[start+3], 60, SpringLayout.NORTH,
					box[start+1]);
			break;
		}
				
	}

	private void setBoxPosition(SpringLayout lay, int start, int stop, Position position){
		switch(position){
		case DOWN:
			for(int i = start; i <= stop; i++){
				lay.putConstraint(SpringLayout.NORTH, box[i], 30, SpringLayout.NORTH,
						box[i-1]);
				lay.putConstraint(SpringLayout.WEST, box[i], 0, SpringLayout.WEST,
						box[start-1]);
			}
			break;
		case RIGHT:
			for(int i = start; i <= stop; i++ ){
				lay.putConstraint(SpringLayout.NORTH, box[i], 0, SpringLayout.NORTH,
						box[start-1]);
				lay.putConstraint(SpringLayout.WEST, box[i], 30, SpringLayout.WEST,
						box[i-1]);
			}
			break;
		case LEFT:
			for(int i = start; i <= stop; i++ ){
				lay.putConstraint(SpringLayout.NORTH, box[i], 0, SpringLayout.NORTH,
						box[start-1]);
				lay.putConstraint(SpringLayout.WEST, box[i], -30, SpringLayout.WEST,
						box[i-1]);
			}
			break;
		case UP:
			for(int i = start; i <= stop; i++){
				lay.putConstraint(SpringLayout.NORTH, box[i], -30, SpringLayout.NORTH,
						box[i-1]);
				lay.putConstraint(SpringLayout.WEST, box[i], 0, SpringLayout.WEST,
						box[start-1]);
			}
		}
	}
	
	public void addPawn(Pawn p){
		pawns.add(p);
	}
	@Override
	public void changeBoxPawn() {
		// TODO Auto-generated method stub
		for(int i = 0; i < pawns.size(); i++){
			Pawn p = pawns.get(i);
			System.out.println("Aktualna pozycja: " + p.getActualyPosition());
			box[p.getActualyPosition()].setImage(p.getPath());
			box[p.getLastPosition()].removePawn();
			Pawn pa = box[p.getActualyPosition()].addPawn(p);
			/*if(pa != p && pa != null){
				pa.setActualyPosition(pa.getCampId());
			}*/

			System.out.println();
		}
	}
	private void setDisabledJoinButton(){
		joinRed.setEnabled(false);
		joinBlue.setEnabled(false);
		joinYellow.setEnabled(false);
		joinGreen.setEnabled(false);
	}
	private void setEnabledJoinButton(){
		joinRed.setEnabled(true);
		joinBlue.setEnabled(true);
		joinYellow.setEnabled(true);
		joinGreen.setEnabled(true);
		joinRed.setText("Dołącz");
		joinBlue.setText("Dołącz");
		joinYellow.setText("Dołącz");
		joinGreen.setText("Dołącz");
	}
	private void createYellowPlayer(Player player){
		player.setCamp(new YellowCamp());
		player.setHouse(new YellowHouse());
	}
	private void createRedPlayer(Player player){
		player.setCamp(new RedCamp());
		player.setHouse(new RedHouse());
	}
	private void createGreenPlayer(Player player){
		player.setCamp(new GreenCamp());
		player.setHouse(new GreenHouse());
	}
	private void createBluePlayer(Player player){
		player.setCamp(new BlueCamp());
		player.setHouse(new BlueHouse());
	}
	
	private JPanel rightPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(buttonPanel(), BorderLayout.NORTH);
		panel.add(centerPanel());
		return panel;
	}
	private JPanel centerPanel(){
		JPanel panel = new JPanel();
		JPanel panelArea = new JPanel();
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		panelSouth.add(msgField, c);
		c.weightx = 0.5;
		c.gridx = 1;
		panelSouth.add(sendMsg, c);
		Border blackLine = BorderFactory.createLineBorder(Color.gray);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(blackLine,
			"Informacje");
		titledBorder.setTitleJustification(TitledBorder.CENTER);	
		panelArea.setBorder(titledBorder);
		panelArea.setLayout(new BorderLayout());
		panelArea.add(new JScrollPane(infoArea),BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		panel.add(cubeRoll, BorderLayout.NORTH);
		panel.add(panelArea, BorderLayout.CENTER);
		panel.add(panelSouth, BorderLayout.SOUTH);
		
		return panel;
	}
	public void setHandler(ServerHandler handler){
		this.handler = handler;
		this.handler.registerObserver(this);
	}
	private enum Position{
		UP, RIGHT, DOWN, LEFT
	}
	@Override
	public void updatePlayerList(int color, String name) {
		Player player = new HumanPlayer(name);
		switch(color){
		case 1: 
			createYellowPlayer(player);
			joinYellow.setEnabled(false);
			joinYellow.setText(player.getName());
			break;
		case 2: 
			createRedPlayer(player);
			joinRed.setEnabled(false);
			joinRed.setText(player.getName());
			break;
		case 3: 
			createGreenPlayer(player);
			joinGreen.setEnabled(false);
			joinGreen.setText(player.getName());
			break;
		case 4: 
			createBluePlayer(player);
			joinBlue.setEnabled(false);
			joinBlue.setText(player.getName());
			break;
		}
		playerList.put(color, player);
		for(Player player1 : playerList.values()){
			if(player1.getColor() == this.player.getColor()){
				this.player.setPawns(player1.getPawns());
			}
			for(Pawn p : player1.getPawns()){
				p.registerObserver(this);
				addPawn(p);
				box[p.getActualyPosition()].setImage(p.getPath());
				p.notifyObserver();
				}
		System.out.println("Lista: " + playerList.size());
		handler.sendPlayerListSize(playerList.size());
		}
	}
	private void notifyPawns(Player player){
		for(Pawn p : player.getPawns()){
			p.notifyObserver();
			}
	}
	@Override
	public void updatePawn(int player, int p0, int p1, int p2, int p3, 
			int l0, int l1, int l2, int l3) {
				playerList.get(player).setPawnPosition(0, p0);
				playerList.get(player).setPawnLastPosition(0, l0);
				playerList.get(player).setPawnPosition(1, p1);
				playerList.get(player).setPawnLastPosition(1, l1);
				playerList.get(player).setPawnPosition(2, p2);
				playerList.get(player).setPawnLastPosition(2, l2);
				playerList.get(player).setPawnPosition(3, p3);
				playerList.get(player).setPawnLastPosition(3, l3);
		notifyPawns(playerList.get(player));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == joinYellow){
			createYellowPlayer(this.player);
			setDisabledJoinButton();
			handler.sendPlayer(player);
			cubeRoll.setEnabled(true);
		}
		if(e.getSource() == joinRed){
			createRedPlayer(this.player);
			setDisabledJoinButton();
			handler.sendPlayer(player);

		}
		if(e.getSource() == joinGreen){
			createGreenPlayer(this.player);
			setDisabledJoinButton();
			handler.sendPlayer(player);
		}
		if(e.getSource() == joinBlue){
			createBluePlayer(this.player);
			setDisabledJoinButton();
			handler.sendPlayer(player);
		}
		if(e.getSource() == cubeRoll){
			player.rollDice();
			cubeRoll.setEnabled(false);
			Pawn[] pawns = player.getPawns();
			int campid0 = pawns[0].getCampId();
			int campid1 = pawns[1].getCampId();
			int campid2 = pawns[2].getCampId();
			int campid3 = pawns[3].getCampId();
			if(player.getRoll() != 6 && 
					(
					box[campid0].getBoxId() == pawns[0].getActualyPosition() &&
					box[campid1].getBoxId() == pawns[1].getActualyPosition() &&
					box[campid2].getBoxId() == pawns[2].getActualyPosition() &&
					box[campid3].getBoxId() == pawns[3].getActualyPosition()
					)){
						handler.sendCurrentRound(player.getColor());
						}
			String msg = player.getName() + " wyrzucił: " + player.getRoll();
			handler.sendMsg(msg);
			
		}
		if(e.getSource() == sendMsg){
			String msg = player.getName() + ": " + msgField.getText();
			msgField.setText("");
			handler.sendMsg(msg);
		}
	}
	private JPanel panelBoard(){
		initBox();
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		SpringLayout lay = new SpringLayout();
		
		panel.setLayout(lay);
		int w = view.getWidth();
		for(int i = 0; i < box.length; i++){
			panel.add(box[i]);
		}


		lay.putConstraint(SpringLayout.WEST, box[4], w/3 + 60, SpringLayout.WEST,
				panel);
		lay.putConstraint(SpringLayout.NORTH, box[4], 25, SpringLayout.NORTH,
				panel);
		// START RYSOWANIE PLANSZY
		setBoxPosition(lay, 5, 11, Position.DOWN);
		setBoxPosition(lay, 12, 18, Position.RIGHT);
		setBoxPosition(lay, 19, 20, Position.DOWN);
		setBoxPosition(lay, 21, 27, Position.LEFT);
		setBoxPosition(lay, 28, 34, Position.DOWN);
		setBoxPosition(lay, 35, 36, Position.LEFT);
		setBoxPosition(lay, 37, 43, Position.UP);
		setBoxPosition(lay, 44, 50, Position.LEFT);
		setBoxPosition(lay, 51, 52, Position.UP);
		setBoxPosition(lay, 53, 59, Position.RIGHT);
		setBoxPosition(lay, 60, 63, Position.UP);
		lay.putConstraint(SpringLayout.NORTH, box[0], -30, SpringLayout.NORTH,
				box[63]);
		lay.putConstraint(SpringLayout.WEST, box[0], 0, SpringLayout.WEST,
				box[59]);
		setBoxPosition(lay, 1, 2, Position.UP);
		lay.putConstraint(SpringLayout.NORTH, box[3], 0, SpringLayout.NORTH,
				box[2]);
		lay.putConstraint(SpringLayout.WEST, box[3], 30, SpringLayout.WEST,
				box[2]);
		// KONIEC RYSOWANIE PLANSZY
		
		//START RYSOWANIE KONCA KOLORU ZOLTEGO
		lay.putConstraint(SpringLayout.NORTH, box[80], 30, SpringLayout.NORTH,
				box[3]);
		lay.putConstraint(SpringLayout.WEST, box[80], 0, SpringLayout.WEST,
				box[3]);
		setBoxPosition(lay, 81, 83, Position.DOWN);
		//KONIEC RYSOWANIE KONCA KOLORU ZOLTEGO
		
		//START RYSOWANIE KONCA KOLORU CZERWONEGO
		lay.putConstraint(SpringLayout.NORTH, box[84], 0, SpringLayout.NORTH,
				box[19]);
		lay.putConstraint(SpringLayout.WEST, box[84], -30, SpringLayout.WEST,
				box[19]);
		setBoxPosition(lay, 85, 87, Position.LEFT);
		//KONIEC RYSOWANIE KONCA KOLORU CZERWONEGO
		
		//START RYSOWANIE KONCA KOLORU ZIELONEGO
		lay.putConstraint(SpringLayout.NORTH, box[88], -30, SpringLayout.NORTH,
				box[35]);
		lay.putConstraint(SpringLayout.WEST, box[88], 0, SpringLayout.WEST,
				box[35]);
		
		setBoxPosition(lay, 89, 91, Position.UP);
		//KONIEC RYSOWANIE KONCA KOLORU ZIELONEGO
		
		//START RYSOWANIE KONCA KOLORU NIEBIESKIEGO
		lay.putConstraint(SpringLayout.NORTH, box[92], 0, SpringLayout.NORTH,
				box[51]);
		lay.putConstraint(SpringLayout.WEST, box[92], 30, SpringLayout.WEST,
				box[51]);
		setBoxPosition(lay, 93, 95, Position.RIGHT);
		//KONIEC RYSOWANIE KONCA KOLORU NIEBIESKIEGO
		
		
		addHousePosition(lay, 64, 5, Position.RIGHT);
		addHousePosition(lay, 68, 31, Position.RIGHT);
		addHousePosition(lay, 72, 39, Position.LEFT);
		addHousePosition(lay, 76, 1, Position.LEFT);
		return panel;
	}
	private boolean isEnd(){
		int f = player.getHouse()[0];
		int s = player.getHouse()[1];
		int t = player.getHouse()[2];
		int f1 = player.getHouse()[3];
		if(!(box[f].isFree() && box[s].isFree() && box[t].isFree() && box[f1].isFree()))
			return true;
		else
			return false;
	}
	@Override
	public void currentRound(int i) {
		// TODO Auto-generated method stub
		if(i == player.getColor())
			cubeRoll.setEnabled(true);
		else
			cubeRoll.setEnabled(false);
		
	}
	@Override
	public void updateMsg(String msg) {
		// TODO Auto-generated method stub
		infoArea.append(msg + "\n");
		infoArea.setCaretPosition(infoArea.getDocument().getLength());
	}
	private void changePawn(int p, Box b){
		 if(b.getBoxId() == player.getPawn(p).getCampId() && player.getRoll() == 6){
			 player.movePawnBase(p);
			 handler.sendPawn(player);
			 handler.sendCurrentRound(player.getColor());
		 }
		 else if(b.getBoxId() != player.getPawn(p).getCampId()){
			 player.movePawn(p);
			 handler.sendPawn(player);
			 if(isEnd())
				 handler.sendEnd();
			 handler.sendCurrentRound(player.getColor());
		 }
	}
	private class PawnMoveListener extends MouseAdapter{
		
		 public void mouseReleased(MouseEvent me) { 
			 Box b = (Box) me.getComponent();
			 if(b.getPawn() != null && b.getPawn() == player.getPawn(0) && player.getRoll() != 0){
				changePawn(0, b);

			 }
			 if(b.getPawn() != null && b.getPawn() == player.getPawn(1) && player.getRoll() != 0){
				 changePawn(1, b);
			 }
			 if(b.getPawn() != null && b.getPawn() == player.getPawn(2) && player.getRoll() != 0){
				 changePawn(2, b);
			 }
			 if(b.getPawn() != null && b.getPawn() == player.getPawn(3) && player.getRoll() != 0){
				 changePawn(3, b);
			 }
			
		 }
	}
	@Override
	public void updateGame() {
		// TODO Auto-generated method stub
		handler.sendMsg("GRA ZAKONCZONA!!");
		setEnabledJoinButton();
		for(int i = 0; i < box.length; i++)
			box[i].removePawn();
		pawns.clear();
		playerList.clear();

	}
}


