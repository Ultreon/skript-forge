///**
// *   This file is part of Skript.
// * <p>
// *  Skript is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  (at your option) any later version.
// * <p>
// *  Skript is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// * <p>
// *  You should have received a copy of the GNU General Public License
// *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
// * <p>
// * Copyright Peter Güttinger, SkriptLang team and contributors
// */
//package ch.njol.skript.aliases;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.registries.ForgeRegistries;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.entity.EntityData;
//
///**
// * Stores the aliases.
// */
//public class AliasesMap {
//	
//	public static class Match {
//		
//		private final MatchQuality quality;
//		
//		@Nullable
//		private final AliasData data;
//		
//		public Match(MatchQuality quality, @Nullable AliasData data) {
//			this.quality = quality;
//			this.data = data;
//		}
//		
//		/**
//		 * Gets quality of this match.
//		 * @return Match quality.
//		 */
//		public MatchQuality getQuality() {
//			return quality;
//		}
//		
//		/**
//		 * Retrieves the alias data of this match. Provided that
//		 * {@link #getQuality()} is at least {@link MatchQuality#SAME_MATERIAL}
//		 * this will be not null; otherwise, it may or may not be null.
//		 * @return Alias data for matching item.
//		 */
//		@Nullable
//		public AliasData getData() {
//			return data;
//		}
//	}
//	
//	public static class AliasData {
//		
//		/**
//		 * The item associated with this alias.
//		 */
//		@Nullable
//		private final ItemData item;
//		@Nullable
//		private final BlockData block;
//
//		/**
//		 * Name of this alias.
//		 */
//		@Nullable
//		private final ItemName itemName;
//		@Nullable
//		private final BlockName blockName;
//
//		/**
//		 * Minecraft ID of this alias.
//		 */
//		private final String minecraftId;
//
//		/**
//		 * Entity related to this alias.
//		 */
//		@Nullable
//		private final EntityData<?> relatedEntity;
//
//		public AliasData(ItemData item,  ItemName name, String minecraftId, @Nullable EntityData<?> relatedEntity) {
//			this(item, null, name, null, minecraftId, relatedEntity);
//		}
//
//		public AliasData(BlockData block, BlockName name, String minecraftId, @Nullable EntityData<?> relatedEntity) {
//			this(null, block, null, name, minecraftId, relatedEntity);
//		}
//
//		public AliasData(@Nullable ItemData item, @Nullable BlockData block, @Nullable ItemName itemName, @Nullable BlockName blockName, String minecraftId, @Nullable EntityData<?> relatedEntity) {
//			this.item = item;
//			this.block = block;
//			this.itemName = itemName;
//			this.blockName = blockName;
//			this.minecraftId = minecraftId;
//			this.relatedEntity = relatedEntity;
//		}
//
//		public ItemData getItem() {
//			assert item != null;
//			return item;
//		}
//
//		public BlockData getBlock() {
//			assert block != null;
//			return block;
//		}
//
//		public ItemName getItemName() {
//			assert itemName != null;
//			return itemName;
//		}
//
//		public BlockName getBlockName() {
//			assert blockName != null;
//			return blockName;
//		}
//
//		public String getMinecraftId() {
//			return minecraftId;
//		}
//
//		@Nullable
//		public EntityData<?> getRelatedEntity() {
//			return relatedEntity;
//		}
//
//		public boolean isBlock() {
//			return block != null;
//		}
//
//		public boolean isItem() {
//			return item != null;
//		}
//	}
//		
//	private static class MaterialEntry {
//		
//		/**
//		 * The default alias for this material.
//		 */
//		@Nullable
//		public AliasData defaultItem;
//		
//		/**
//		 * All different aliases that share this material.
//		 */
//		public final List<AliasData> items;
//		public final List<AliasData> blocks;
//
//		public MaterialEntry() {
//			this.items = new ArrayList<>();
//			this.blocks = new ArrayList<>();
//		}
//		
//	}
//	
//	/**
//	 * One material entry per material. Ordinal of material is index of entry.
//	 */
//	private final Map<ResourceLocation, MaterialEntry> itemEntries = new HashMap<>();
//	private final Map<ResourceLocation, MaterialEntry> blockEntries = new HashMap<>();
//
//	@SuppressWarnings("null") // clear() initializes material entries
//	public AliasesMap() {
//		clear();
//	}
//	
//	private MaterialEntry getEntry(ItemData item) {
//		MaterialEntry entry = itemEntries.get(ForgeRegistries.ITEMS.getKey(item.type));
//		assert entry != null;
//		return entry;
//	}
//
//	private MaterialEntry getEntry(BlockData block) {
//		MaterialEntry entry = blockEntries.get(ForgeRegistries.BLOCKS.getKey(block.type));
//		assert entry != null;
//		return entry;
//	}
//
//	public void addAlias(AliasData data) {
//		if (data.isBlock()) {
//			MaterialEntry entry = getEntry(data.getBlock());
//		}
//		MaterialEntry entry = getEntry(data.getItem());
//		if (data.getItem().isDefault()) {
//			entry.defaultItem = data;
//		} else {
//			entry.items.add(data);
//		}
//	}
//	
//	/**
//	 * Attempts to get the closest matching alias for given item.
//	 * @param item Item to find closest alias for.
//	 * @return The match, containing the alias data and match quality.
//	 */
//	public Match matchAlias(ItemData item) {
//		MaterialEntry entry = getEntry(item);
//		
//		// Special case: no aliases available!
//		if (entry.defaultItem == null && entry.items.isEmpty()) {
//			return new Match(MatchQuality.DIFFERENT, null);
//		}
//		
//		// Try to find the best match
//		MatchQuality maxQuality = MatchQuality.DIFFERENT;
//		AliasData bestMatch = null;
//		for (AliasData data : entry.items) {
//			MatchQuality quality = item.matchAlias(data.getItem());
//			if (quality.isBetter(maxQuality)) {
//				maxQuality = quality;
//				bestMatch = data;
//			}
//		}
//		
//		// Check that we found a reasonably good match
//		// Just same material id -> default item
//		if (maxQuality.isBetter(MatchQuality.SAME_MATERIAL)) {
//			assert bestMatch != null; // Re-setting quality sets this too
//			return new Match(maxQuality, bestMatch);
//		} else { // Try default item
//			AliasData defaultItem = entry.defaultItem;
//			if (defaultItem != null) { // Just match against it
//				return new Match(item.matchAlias(defaultItem.getItem()), defaultItem);
//			} else { // No default item, no match
//				if (bestMatch != null) { // Initially ignored this, but it is best match
//					return new Match(MatchQuality.SAME_MATERIAL, bestMatch);
//				}
//			}
//		}
//		
//		throw new AssertionError(); // Shouldn't have reached here
//	}
//
//	/**
//	 * Attempts to get the closest matching alias for given block.
//	 * @param block Block to find closest alias for.
//	 * @return The match, containing the alias data and match quality.
//	 */
//	public Match matchAlias(BlockData block) {
//		MaterialEntry entry = getEntry(block);
//
//		// Special case: no aliases available!
//		if (entry.defaultItem == null && entry.blocks.isEmpty()) {
//			return new Match(MatchQuality.DIFFERENT, null);
//		}
//
//		// Try to find the best match
//		MatchQuality maxQuality = MatchQuality.DIFFERENT;
//		AliasData bestMatch = null;
//		for (AliasData data : entry.items) {
//			MatchQuality quality = block.matchAlias(data.getBlock());
//			if (quality.isBetter(maxQuality)) {
//				maxQuality = quality;
//				bestMatch = data;
//			}
//		}
//
//		// Check that we found a reasonably good match
//		// Just same material id -> default block
//		if (maxQuality.isBetter(MatchQuality.SAME_MATERIAL)) {
//			assert bestMatch != null; // Re-setting quality sets this too
//			return new Match(maxQuality, bestMatch);
//		} else { // Try default block
//			AliasData defaultItem = entry.defaultItem;
//			if (defaultItem != null) { // Just match against it
//				return new Match(block.matchAlias(defaultItem.getBlock()), defaultItem);
//			} else { // No default block, no match
//				if (bestMatch != null) { // Initially ignored this, but it is best match
//					return new Match(MatchQuality.SAME_MATERIAL, bestMatch);
//				}
//			}
//		}
//
//		throw new AssertionError(); // Shouldn't have reached here
//	}
//
//	/**
//	 * Attempts to find an alias that exactly matches the given item.
//	 * @param item Item to match.
//	 * @return An exact match, or no match.
//	 */
//	public Match exactMatch(ItemData item) {
//		MaterialEntry entry = getEntry(item);
//		
//		// Special case: no aliases available!
//		if (entry.defaultItem == null && entry.items.isEmpty()) {
//			return new Match(MatchQuality.DIFFERENT, null);
//		}
//		
//		for (AliasData data : entry.items) {
//			if (item.matchAlias(data.getItem()) == MatchQuality.EXACT) {
//				return new Match(MatchQuality.EXACT, data);
//			}
//		}
//		
//		return new Match(MatchQuality.DIFFERENT, null);
//	}
//
//	/**
//	 * Attempts to find an alias that exactly matches the given item.
//	 * @param item Item to match.
//	 * @return An exact match, or no match.
//	 */
//	public Match exactMatch(BlockData item) {
//		MaterialEntry entry = getEntry(item);
//		
//		// Special case: no aliases available!
//		if (entry.defaultItem == null && entry.items.isEmpty()) {
//			return new Match(MatchQuality.DIFFERENT, null);
//		}
//		
//		for (AliasData data : entry.items) {
//			if (item.matchAlias(data.getBlock()) == MatchQuality.EXACT) {
//				return new Match(MatchQuality.EXACT, data);
//			}
//		}
//		
//		return new Match(MatchQuality.DIFFERENT, null);
//	}
//
//	/**
//	 * Clears all data from this aliases map.
//	 */
//	public void clear() {
//		for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
//			itemEntries.put(entry.getKey().location(), new MaterialEntry());
//		}
//		for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
//			blockEntries.put(entry.getKey().location(), new MaterialEntry());
//		}
//	}
//}
