package com.bhushan.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] coinMan;
	Texture dizzy;

	int coinManState;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0f;
	int manY=0;
	int score;
	Rectangle manRectangle;
	BitmapFont font ;
	int gameState = 0 ;

	Random random ;

	ArrayList<Integer> coinX = new ArrayList<Integer>();
	ArrayList<Integer> coinY = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;


	ArrayList<Integer> bombX = new ArrayList<Integer>();
	ArrayList<Integer> bombY = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
	Texture bomb;
	int bombcount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		coinMan = new Texture[4];
		coinMan[0] = new Texture("frame-1.png");
		coinMan[1] = new Texture("frame-2.png");
		coinMan[2] = new Texture("frame-3.png");
		coinMan[3] = new Texture("frame-4.png");
		dizzy = new Texture("dizzy-1.png");
		manY = Gdx.graphics.getHeight() /2 ;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");

		manRectangle = new Rectangle();
		random = new Random();


		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}




	public void  makeCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;

		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public void  makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;

		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if (gameState == 1){
			//GAME IS LIVE
			//for coins
			if (coinCount<100){
				coinCount++;
			}
			else {
				coinCount = 0;
				makeCoin();
			}

			//for drawing the coin
			coinRectangle.clear();
			for (int i = 0 ; i < coinX.size() ; i++){
				batch.draw(coin,coinX.get(i),coinY.get(i));

				//for moving the coin to right
				coinX.set(i,coinX.get(i) - 6 );

				coinRectangle.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));

			}


			//for bombs
			if (bombcount<200){
				bombcount++;
			}
			else {
				bombcount = 0;
				makeBomb();
			}

			//for drawing the bomb
			bombRectangle.clear();
			for (int i = 0 ; i < bombX.size() ; i++){
				batch.draw(bomb,bombX.get(i),bombY.get(i));

				//for moving the bomb to right
				bombX.set(i,bombX.get(i) - 8 );

				bombRectangle.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}



			if (Gdx.input.justTouched()){
				velocity = -10;
			}


			//for coin man to move in different frames
			if (pause<8) {
				pause++;
			}

			else {
				pause = 0;
				if (coinManState < 3) {
					coinManState++;
				} else {
					coinManState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			//check to see if manY i.e Y position is less than 0. If less than 0 then the object i.e coinMan will go below screen
			if (manY <=0){
				manY = 0;
			}


		}
		else if (gameState == 0){
			//WAITING TO START

			if (Gdx.input.justTouched()){
				gameState =1;
			}
		}
		else if (gameState == 2){
			//GAME OVER
			if (Gdx.input.justTouched()){
				gameState =1;
				manY = Gdx.graphics.getHeight() /2 ;
				score = 0 ;
				velocity = 0 ;
				coinX.clear();
				coinY.clear();
				coinRectangle.clear();
				coinCount = 0;

				bombX.clear();
				bombY.clear();
				bombRectangle.clear();
				bombcount = 0;


			}
		}



		if (gameState == 2){
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - coinMan[coinManState].getWidth() / 2, manY);
			font.draw(batch,"Game Over",150,600);
		}
		else {
			batch.draw(coinMan[coinManState], Gdx.graphics.getWidth() / 2 - coinMan[coinManState].getWidth() / 2, manY);
		}
		manRectangle= new Rectangle(Gdx.graphics.getWidth()/2 - coinMan[coinManState].getWidth() /2,manY,coinMan[coinManState].getWidth(),coinMan[coinManState].getHeight());


		for (int i = 0; i < coinRectangle.size();i++){
			if (Intersector.overlaps(manRectangle,coinRectangle.get(i))){
				Gdx.app.log("Coin","Collision");
				score++;
				coinRectangle.remove(i);
				coinX.remove(i);
				coinY.remove(i);
				break;
			}
		}


		for (int i = 0; i < bombRectangle.size();i++){
			if (Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				Gdx.app.log("bomb","Collision");

				gameState = 2;
			}
		}


		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
