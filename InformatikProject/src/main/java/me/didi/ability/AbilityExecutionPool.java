package me.didi.ability;

import java.util.ArrayList;
import java.util.List;

public class AbilityExecutionPool {

	private static List<AbilitySet> cancellableAbilities = new ArrayList<AbilitySet>();

	public static void addAbility(AbilitySet abilitySet) {
		cancellableAbilities.add(abilitySet);
	}

	public static void removeAbility(AbilitySet abilitySet) {
		cancellableAbilities.remove(abilitySet);
	}

	public static AbilitySet getAbilitySetByOwner(String owner) {
		for (AbilitySet abilitySet : cancellableAbilities) {
			if (abilitySet.getName().equalsIgnoreCase(owner)) {
				return abilitySet;
			}
		}
		return null;
	}
	
	public static class AbilitySet {
		int id;
		String owner;
		int type;

		public AbilitySet(int id, String name, int type) {
			this.id = id;
			this.owner = name;
			this.type = type;
		}

		public String getName() {
			return owner;
		}

		public int getType() {
			return type;
		}

		public int getId() {
			return id;
		}

	}
}
