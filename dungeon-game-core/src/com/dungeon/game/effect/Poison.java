package com.dungeon.game.effect;

import com.dungeon.game.entity.Character;

public class Poison extends Effect {
	private static float TICKLENGTH = 30;
    private float dmg;
    private float rate;
    private float tickTimer;
      
      //make tick rate slower
	
	    public Poison(float rate, float dmg) {
		        super("Poison",0);
		        this.rate = rate;
		        this.dmg = dmg;
		        tickTimer = TICKLENGTH;
	    }
	    
	    public void update(Character character){
		        calc(character);
		        if(dmg==0)killMe = true;
	}
	
    	public void calc(Character character){
    			if(tickTimer == 0){
    	        	character.damage(Math.round(Math.max(dmg*rate,1)),null);
    	        	System.out.println(dmg + " still to take.");
    	        	dmg -= Math.round(Math.max(dmg*rate,1));
    	        	tickTimer = TICKLENGTH;
    			}else tickTimer--;
	    }
}
