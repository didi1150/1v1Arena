package me.didi.ability;

import java.util.ArrayList;
import java.util.List;

public class AbilityExecutionPool {

	private static List<Integer> cancellableAbilities = new ArrayList<Integer>();

	public static void addAbility(Integer integer) {
		cancellableAbilities.add(integer);
	}

	public static void removeAbility(Integer integer) {
		cancellableAbilities.remove(integer);
	}

}
