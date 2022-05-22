package me.didi.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RecastableAbility extends Ability {

	public RecastableAbility(String name, ItemStack icon) {
		super(name, icon);
	}

	private int counter = 0;

	List<Callable<Player>> callables = new ArrayList<>();

	/**
	 * Adds a method to the callable list
	 */
	public void addFunction(Callable<Player> callable) {
		callables.add(callable);
	}

	@Override
	public void cast() {
		if (counter >= callables.size()) {
			counter = 0;
			cancel();
		}
		try {
			callables.get(counter).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		counter++;
	}

	@Override
	public void cancel() {
	}

}
