package me.didi.ability;

import java.util.concurrent.Callable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OneTimeAbility extends Ability {

	private Callable<Player> callback;
	
	public OneTimeAbility(String name, ItemStack icon) {
		super(name, icon);
	}

	public void setCallback(Callable<Player> callback) {
		this.callback = callback;
	}
	
	@Override
	public void cast() {
		try {
			this.callback.call();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		
	}

}
