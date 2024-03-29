package com.ludo.app.view.box;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayDeque;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.ludo.app.model.Pawn;


public abstract class Box extends JPanel{

	private int id;
	private static int idStatic = 0;
	private Image image;
	private Pawn pawnLast;
	private Image pawnImage = null;
	private ArrayDeque<Pawn> arrayPawn = new ArrayDeque<Pawn>();
	private boolean isFree = true;
	public Box(String path){
		try{
			image = ImageIO.read(new File(path));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		id = idStatic;
		idStatic++;
		this.setLayout(new FlowLayout());
		this.setMinimumSize(getBoxSize());
		this.setMaximumSize(getBoxSize());
		this.setPreferredSize(getBoxSize());
	}
	
	public Pawn addPawn(Pawn pawn){
		pawnLast = arrayPawn.pollLast();
		arrayPawn.addFirst(pawn);
		pawn.setActualyPosition(id);
		//pawnLast = pawn;
		isFree=false;
		repaint();
		return pawnLast;
	}
	public Pawn getPawn(){
		return pawnLast;
	}
	public void removePawn(){
		pawnLast = null;
		pawnImage = null;
		isFree =true;
		repaint();
	}
	public void setImage(String path){
		try{
			pawnImage = ImageIO.read(new File(path));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	public int getBoxId(){
		return id;
	}
	public boolean isFree(){
		return isFree;
	}
	private Dimension getBoxSize(){
		return new Dimension(30,30);
	}
	@Override
	protected void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, null);
		if(!arrayPawn.isEmpty())
			paintPawn(g);
	}
	
	protected void paintPawn(Graphics g){
		g.drawImage(pawnImage, 0, 0, null);
	}
	
	
	
}
