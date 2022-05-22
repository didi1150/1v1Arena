package me.didi.characters.champions;

import me.didi.ability.Ability;
import me.didi.characters.Champion;

public abstract class ArcherChampion extends Champion{

	public ArcherChampion(Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist) {
		super(abilities, baseHealth, baseDefense, baseMagicResist);
	}
}
