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
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.github.ultreon.portutils.Forge;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.Block;
//import org.eclipse.jdt.annotation.Nullable;
//
//import com.google.gson.Gson;
//
//import ch.njol.skript.bukkitutil.BukkitUnsafe;
//import ch.njol.skript.bukkitutil.ItemUtils;
//import ch.njol.skript.bukkitutil.block.BlockCompat;
//import ch.njol.skript.bukkitutil.block.BlockValues;
//import ch.njol.skript.entity.EntityData;
//
///**
// * Provides aliases on Bukkit/Spigot platform.
// */
//public class AliasesProvider {
//	
//	/**
//	 * When an alias is not found, it will requested from this provider.
//	 * Null when this is global aliases provider.
//	 */
//	@Nullable
//	private final AliasesProvider parent;
//	
//	/**
//	 * All aliases that are currently loaded by this provider.
//	 */
//	private final Map<String, ItemType> itemAliases;
//	private final Map<String, BlockType> blockAliases;
//
//	/**
//	 * All materials that are currently loaded by this provider.
//	 */
//	private final List<Item> items;
//	private final List<Block> blocks;
//	
//	/**
//	 * Tags are in JSON format. We may need GSON when merging tags
//	 * (which might be done if variations are used).
//	 */
//	private final Gson gson;
//	
//	/**
//	 * Represents a variation of material. It could, for example, define one
//	 * more tag or change base id, but keep tag intact.
//	 */
//	public static class Variation {
//		
//		@Nullable
//		private final String id;
//		private final int insertPoint;
//		
//		private final Map<String, Object> tags;
//		private final Map<String, String> states;
//		
//		public Variation(@Nullable String id, int insertPoint, Map<String, Object> tags, Map<String, String> states) {
//			this.id = id;
//			this.insertPoint = insertPoint;
//			this.tags = tags;
//			this.states = states;
//		}
//		
//		@Nullable
//		public String getId() {
//			return id;
//		}
//		
//		public int getInsertPoint() {
//			return insertPoint;
//		}
//		
//		@Nullable
//		public String insertId(@Nullable String inserted) {
//			if (inserted == null) // Nothing to insert
//				return id;
//			inserted = inserted.substring(0, inserted.length() - 1); // Strip out -
//			if (id == null) // Inserting to nothing
//				return inserted;
//			
//			String id = this.id;
//			assert id != null;
//			if (insertPoint == -1) // No place where to insert
//				return inserted;
//			
//			// Insert given string to in middle of our id
//			String before = id.substring(0, insertPoint);
//			String after = id.substring(insertPoint + 1);
//			return before + inserted + after;
//		}
//		
//		public Map<String, Object> getTags() {
//			return tags;
//		}
//
//
//		public Map<String,String> getBlockStates() {
//			return states;
//		}
//
//
//		public Variation merge(Variation other) {
//			// Merge tags and block states
//			Map<String, Object> mergedTags = new HashMap<>(other.tags);
//			mergedTags.putAll(tags);
//			Map<String, String> mergedStates = new HashMap<>(other.states);
//			mergedStates.putAll(states);
//			
//			// Potentially merge ids
//			String id = insertId(other.id);
//			
//			return new Variation(id, -1, mergedTags, mergedStates);
//		}
//	}
//	
//	public static class VariationGroup {
//		
//		public final List<String> keys;
//		
//		public final List<Variation> values;
//		
//		public VariationGroup() {
//			this.keys = new ArrayList<>();
//			this.values = new ArrayList<>();
//		}
//		
//		public void put(String key, Variation value) {
//			keys.add(key);
//			values.add(value);
//		}
//	}
//	
//	/**
//	 * Contains all variations.
//	 */
//	private final Map<String, VariationGroup> variations;
//	
//	/**
//	 * Allows looking up aliases based on item datas created runtime.
//	 */
//	private final AliasesMap aliasesMap;
//	
//	/**
//	 * Constructs a new aliases provider with no data.
//	 */
//	public AliasesProvider(int expectedCount, @Nullable AliasesProvider parent) {
//		this.parent = parent;
//		this.itemAliases = new HashMap<>(expectedCount);
//		this.blockAliases = new HashMap<>(expectedCount);
//		this.variations = new HashMap<>(expectedCount / 20);
//		this.aliasesMap = new AliasesMap();
//		this.items = new ArrayList<>();
//		this.blocks = new ArrayList<>();
//		
//		this.gson = new Gson();
//	}
//	
//	/**
//	 * Uses GSON to parse Mojang's JSON format to a map.
//	 * @param raw Raw JSON.
//	 * @return String,Object map.
//	 */
//	@SuppressWarnings({"null", "unchecked"})
//	public Map<String, Object> parseMojangson(String raw) {
//		return (Map<String, Object>) gson.fromJson(raw, Object.class);
//	}
//	
//	/**
//	 * Applies given tags to an item stack.
//	 * @param stack Item stack.
//	 * @param tags Tags.
//	 * @return Additional flags for the item.
//	 */
//	public int applyTags(ItemStack stack, Map<String, Object> tags) {
//		// Hack damage tag into item
//		Object damage = tags.get("Damage");
//		int flags = 0;
//		if (damage instanceof Number) { // Use helper for version compatibility
//			ItemUtils.setDamage(stack, ((Number) damage).shortValue());
//			tags.remove("Damage");
//			flags |= ItemFlags.CHANGED_DURABILITY;
//		}
//		
//		if (tags.isEmpty()) // No real tags to apply
//			return flags;
//		
//		// Apply random tags using JSON
//		String json = gson.toJson(tags);
//		assert json != null;
//		BukkitUnsafe.modifyItemStack(stack, json);
//		flags |= ItemFlags.CHANGED_TAGS;
//		
//		return flags;
//	}
//	
//	/**
//	 * Name of an alias used by {@link #addBlockAlias(AliasName, String, Map, Map)} or {@link #addItemAlias(AliasName, String, Map, Map)}
//	 * for registration.
//	 */
//	public static class AliasName {
//		
//		/**
//		 * Singular for of alias name.
//		 */
//		public final String singular;
//		
//		/**
//		 * Plural form of alias name.
//		 */
//		public final String plural;
//		
//		/**
//		 * Gender of alias name.
//		 */
//		public final int gender;
//
//		public AliasName(String singular, String plural, int gender) {
//			super();
//			this.singular = singular;
//			this.plural = plural;
//			this.gender = gender;
//		}
//		
//	}
//	
//	/**
//	 * Adds an alias to this provider.
//	 * @param name Name of alias without any patterns or variation blocks.
//	 * @param id Id of material.
//	 * @param tags Tags for material.
//	 * @param blockStates Block states.
//	 */
//	public void addItemAlias(AliasName name, String id, @Nullable Map<String, Object> tags, Map<String, String> blockStates) {
//		// First, try to find if aliases already has a type with this id
//		// (so that aliases can refer to each other)
//		ItemType typeOfId = getItemAlias(id);
//		EntityData<?> related = null;
//		List<ItemData> datas;
//		if (typeOfId != null) { // If it exists, use datas from it
//			datas = typeOfId.getTypes();
//		} else { // ... but quite often, we just got Vanilla id
//			// Prepare and modify ItemStack (using somewhat Unsafe methods)
//			Item material = Forge.getItemFromMinecraftId(id);
//			if (material == null) { // If server doesn't recognize id, do not proceed
//				throw new InvalidMinecraftIdException(id);
//			}
//			if (!items.contains(material))
//				items.add(material);
//			
//			// Hacky: get related entity from block states
//			String entityName = blockStates.remove("relatedEntity");
//			if (entityName != null) {
//				related = EntityData.parse(entityName);
//			}
//			
//			// Apply (NBT) tags to item stack
//			ItemStack stack = new ItemStack(material);
//			int itemFlags = 0;
//			if (tags != null) {
//				itemFlags = applyTags(stack, new HashMap<>(tags));
//			}
//
//			ItemData data = new ItemData(stack, null);
//			data.isAlias = true;
//			data.itemFlags = itemFlags;
//			
//			// Deduplicate item data if this has been loaded before
//			AliasesMap.Match canonical = aliasesMap.exactMatch(data);
//			if (canonical.getQuality().isAtLeast(MatchQuality.EXACT)) {
//				AliasesMap.AliasData aliasData = canonical.getData();
//				assert aliasData != null; // Match quality guarantees this
//				data = aliasData.getItem();
//			}
//			
//			datas = Collections.singletonList(data);
//		}
//		
//		// If this is first time we're defining an item, store additional data about it
//		if (typeOfId == null) {
//			ItemData data = datas.get(0);
//			// Most accurately named alias for this item SHOULD be defined first
//			ItemName materialName = new ItemName(data.type, name.singular, name.plural, name.gender);
//			aliasesMap.addAlias(new AliasesMap.AliasData(data, materialName, id, related));
//		}
//
//		// Check if there is item type with this name already, create otherwise
//		ItemType type = itemAliases.get(name.singular);
//		if (type == null)
//			type = itemAliases.get(name.plural);
//		if (type == null) {
//			type = new ItemType();
//			itemAliases.put(name.singular, type); // Singular form
//			itemAliases.put(name.plural, type); // Plural form
//			type.addAll(datas);
//		} else { // There is already an item type with this name, we need to *only* add new data
//			newDataLoop: for (ItemData newData : datas) {
//				for (ItemData existingData : type.getTypes()) {
//					if (newData == existingData || newData.matchAlias(existingData).isAtLeast(MatchQuality.EXACT)) // Don't add this data, the item type already contains it!
//						continue newDataLoop;
//				}
//				type.add(newData);
//			}
//		}
//	}
//	
//	/**
//	 * Adds an alias to this provider.
//	 * @param name Name of alias without any patterns or variation blocks.
//	 * @param id Id of material.
//	 * @param tags Tags for material.
//	 * @param blockStates Block states.
//	 */
//	public void addBlockAlias(AliasName name, String id, @Nullable Map<String, Object> tags, Map<String, String> blockStates) {
//		// First, try to find if aliases already has a type with this id
//		// (so that aliases can refer to each other)
//		@Nullable BlockType typeOfId = getBlockAlias(id);
//		EntityData<?> related = null;
//		List<BlockData> datas;
//		if (typeOfId != null) { // If it exists, use datas from it
//			datas = typeOfId.getTypes();
//		} else { // ... but quite often, we just got Vanilla id
//			// Prepare and modify ItemStack (using somewhat Unsafe methods)
//			Block material = Forge.getBlockFromMinecraftId(id);
//			if (material == null) { // If server doesn't recognize id, do not proceed
//				throw new InvalidMinecraftIdException(id);
//			}
//			if (!blocks.contains(material))
//				blocks.add(material);
//			
//			// Hacky: get related entity from block states
//			String entityName = blockStates.remove("relatedEntity");
//			if (entityName != null) {
//				related = EntityData.parse(entityName);
//			}
//			
//			// Apply (NBT) tags to item stack
//			ItemStack stack = new ItemStack(material);
//			int itemFlags = 0;
//			if (tags != null) {
//				itemFlags = applyTags(stack, new HashMap<>(tags));
//			}
//			
//			// Parse block state to block values
//			BlockValues blockValues = BlockCompat.INSTANCE.createBlockValues(material, blockStates, stack, itemFlags);
//			
//			BlockData data = new BlockData(stack, blockValues);
//			data.isAlias = true;
//			data.itemFlags = itemFlags;
//			
//			// Deduplicate item data if this has been loaded before
//			AliasesMap.Match canonical = aliasesMap.exactMatch(data);
//			if (canonical.getQuality().isAtLeast(MatchQuality.EXACT)) {
//				AliasesMap.AliasData aliasData = canonical.getData();
//				assert aliasData != null; // Match quality guarantees this
//				data = aliasData.getBlock();
//			}
//			
//			datas = Collections.singletonList(data);
//		}
//		
//		// If this is first time we're defining an item, store additional data about it
//		if (typeOfId == null) {
//			BlockData data = datas.get(0);
//			// Most accurately named alias for this item SHOULD be defined first
//			BlockName materialName = new BlockName(data.type, name.singular, name.plural, name.gender);
//			aliasesMap.addAlias(new AliasesMap.AliasData(data, materialName, id, related));
//		}
//
//		// Check if there is item type with this name already, create otherwise
//		BlockType type = blockAliases.get(name.singular);
//		if (type == null)
//			type = blockAliases.get(name.plural);
//		if (type == null) {
//			type = new BlockType();
//			blockAliases.put(name.singular, type); // Singular form
//			blockAliases.put(name.plural, type); // Plural form
//			type.addAll(datas);
//		} else { // There is already an item type with this name, we need to *only* add new data
//			newDataLoop: for (BlockData newData : datas) {
//				for (BlockData existingData : type.getTypes()) {
//					if (newData == existingData || newData.matchAlias(existingData).isAtLeast(MatchQuality.EXACT)) // Don't add this data, the item type already contains it!
//						continue newDataLoop;
//				}
//				type.add(newData);
//			}
//		}
//	}
//	
//	public void addVariationGroup(String name, VariationGroup group) {
//		variations.put(name, group);
//	}
//	
//	@Nullable
//	public VariationGroup getVariationGroup(String name) {
//		return variations.get(name);
//	}
//
//	@Nullable
//	public ItemType getItemAlias(String alias) {
//		ItemType item = itemAliases.get(alias);
//		if (item == null && parent != null) {
//			return parent.getItemAlias(alias);
//		}
//		return item;
//	}
//
//	@Nullable
//	public BlockType getBlockAlias(String alias) {
//		BlockType item = blockAliases.get(alias);
//		if (item == null && parent != null) {
//			return parent.getBlockAlias(alias);
//		}
//		return item;
//	}
//
//	public AliasesMap.@Nullable AliasData getAliasData(ItemData item) {
//		AliasesMap.AliasData data = aliasesMap.matchAlias(item).getData();
//		if (data == null && parent != null) {
//			return parent.getAliasData(item);
//		}
//		return data;
//	}
//
//	public AliasesMap.@Nullable AliasData getAliasData(BlockData item) {
//		AliasesMap.AliasData data = aliasesMap.matchAlias(item).getData();
//		if (data == null && parent != null) {
//			return parent.getAliasData(item);
//		}
//		return data;
//	}
//
//	@Nullable
//	public String getMinecraftId(ItemData item) {
//		AliasesMap.AliasData data = getAliasData(item);
//		if (data != null) {
//			return data.getMinecraftId();
//		}
//		return null;
//	}
//	
//	@Nullable
//	public ItemName getItemName(ItemData item) {
//		AliasesMap.AliasData data = getAliasData(item);
//		if (data != null) {
//			return data.getItemName();
//		}
//		return null;
//	}
//	
//	@Nullable
//	public BlockName getBlockName(BlockData item) {
//		AliasesMap.AliasData data = getAliasData(item);
//		if (data != null) {
//			return data.getBlockName();
//		}
//		return null;
//	}
//	
//	@Nullable
//	public EntityData<?> getRelatedEntity(ItemData item) {
//		AliasesMap.AliasData data = getAliasData(item);
//		if (data != null) {
//			return data.getRelatedEntity();
//		}
//		return null;
//	}
//
//	public void clearAliases() {
//		itemAliases.clear();
//		blockAliases.clear();
//		variations.clear();
//		aliasesMap.clear();
//	}
//
//	public int getAliasCount() {
//		return itemAliases.size() + blockAliases.size();
//	}
//
//	/**
//	 * Check if this provider has an alias for the given material.
//	 * @param material Material to check alias for
//	 * @return True if this material has an alias
//	 */
//	public boolean hasAliasForItem(Item material) {
//		return items.contains(material);
//	}
//
//	/**
//	 * Check if this provider has an alias for the given material.
//	 * @param material Material to check alias for
//	 * @return True if this material has an alias
//	 */
//	public boolean hasAliasForBlock(Block material) {
//		return blocks.contains(material);
//	}
//
//}
