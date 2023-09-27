package com.github.ultreon.portutils;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class Material {
	private final Block block;
	private final Item item;

	public Material(Block block, Item item) {
		if (block == null && item == null) throw new NullPointerException("Block and item are both null.");

		this.block = block;
		this.item = item;
	}

	public static Material of(Item item) {
		Block block = null;

		if (item instanceof BlockItem blockItem) {
			block = blockItem.getBlock();
		}

		return new Material(block, item);
	}

	public static Material of(Block block) {
		return new Material(block, block.asItem() != Items.AIR ? block.asItem() : null);
	}

	public Block getBlock() {
		return block;
	}

	public Item getItem() {
		return item;
	}

	public boolean isBlock() {
		return block != null;
	}

	public boolean isItem() {
		return item != null;
	}

	@Deprecated
	public int getMaxDurability() {
		if (isItem()) {
			return item.getMaxDamage();
		}
		return 0;
	}

	public int getMaxDurability(ItemStack stack) {
		if (isItem()) {
			return item.getMaxDamage(stack);
		}
		return 0;
	}
}
