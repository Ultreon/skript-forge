/**
 *   This file is part of Skript.
 * <p>
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * <p>
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * <p>
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.classes.data;

import ch.njol.skript.classes.Converter;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.entity.XpOrbData;
import ch.njol.skript.registrations.Converters;
import ch.njol.skript.util.BlockUtils;
import ch.njol.skript.util.Direction;
import ch.njol.skript.util.EnchantmentType;
import ch.njol.skript.util.Experience;
import ch.njol.skript.util.slot.Slot;
import com.github.ultreon.portutils.BlockInstance;
import com.github.ultreon.portutils.Forge;
import com.github.ultreon.portutils.Location;
import com.github.ultreon.portutils.Material;
import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSourceStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Peter Güttinger
 */
@SuppressWarnings("rawtypes")
public class DefaultConverters {
	
	public DefaultConverters() {}
	
	static {
		// Integer - Long
		Converters.registerConverter(Integer.class, Long.class, Integer::longValue);

		// OfflinePlayer - Inventory
		Converters.registerConverter(GameProfile.class, Inventory.class, new Converter<GameProfile, Inventory>() {
			@Override
			@Nullable
			public Inventory convert(final GameProfile p) {
				ServerPlayer player = Forge.getPlayer(p);
				if (player == null)
					return null;
				return player.getInventory();
			}
		}, Converter.NO_COMMAND_ARGUMENTS);
		// OfflinePlayer - Player
		Converters.registerConverter(GameProfile.class, ServerPlayer.class, new Converter<OfflinePlayer, ServerPlayer>() {
			@Override
			@Nullable
			public ServerPlayer convert(final GameProfile p) {
				return Forge.getPlayer(p);
			}
		}, Converter.NO_COMMAND_ARGUMENTS);
		
		// TODO improve handling of interfaces
		// CommandSourceStack - Player
		Converters.registerConverter(CommandSourceStack.class, ServerPlayer.class, new Converter<CommandSourceStack, ServerPlayer>() {
			@Override
			@Nullable
			public ServerPlayer convert(final CommandSourceStack s) {
				return s.getPlayer();
			}
		});
//		// BlockCommandSourceStack - Block
//		Converters.registerConverter(BlockCommandSourceStack.class, BlockInstance.class, new Converter<BlockCommandSourceStack, BlockInstance>() {
//			@Override
//			@Nullable
//			public BlockInstance convert(final BlockCommandSourceStack s) {
//				return s.getBlock();
//			}
//		});
		// Entity - Player
		Converters.registerConverter(Entity.class, ServerPlayer.class, new Converter<Entity, ServerPlayer>() {
			@Override
			@Nullable
			public ServerPlayer convert(final Entity e) {
				if (e instanceof ServerPlayer player)
					return player;
				return null;
			}
		});
		// Entity - LivingEntity // Entity->Player is used if this doesn't exist
		Converters.registerConverter(Entity.class, LivingEntity.class, new Converter<Entity, LivingEntity>() {
			@Override
			@Nullable
			public LivingEntity convert(final Entity e) {
				if (e instanceof LivingEntity)
					return (LivingEntity) e;
				return null;
			}
		});
		
		// Block - AbstractContainerMenu
		Converters.registerConverter(BlockInstance.class, AbstractContainerMenu.class, new Converter<BlockInstance, AbstractContainerMenu>() {
			@Override
			@Nullable
			public AbstractContainerMenu convert(final BlockInstance b) {
//				if (b.getState().getBlock() instanceof EntityBlock)
//					return ((EntityBlock) b.getState()).getInventory();
//				return null;
				return null;
			}
		}, Converter.NO_COMMAND_ARGUMENTS);
		
//		// Entity - AbstractContainerMenu
//		Converters.registerConverter(Entity.class, AbstractContainerMenu.class, new Converter<Entity, AbstractContainerMenu>() {
//			@Override
//			@Nullable
//			public AbstractContainerMenu convert(final Entity e) {
//				if (e instanceof InventoryHolder)
//					return ((InventoryHolder) e).getInventory();
//				return null;
//			}
//		}, Converter.NO_COMMAND_ARGUMENTS);
		
		// Block - ItemType
		Converters.registerConverter(BlockInstance.class, Item.class, new Converter<BlockInstance, Item>() {
			@Override
			public Item convert(final BlockInstance b) {
				return b.getType().asItem();
			}
		}, Converter.NO_LEFT_CHAINING | Converter.NO_COMMAND_ARGUMENTS);
		
		// Location - Block
//		Converters.registerConverter(Location.class, Block.class, new Converter<Location, Block>() {
//			@Override
//			public Block convert(final Location l) {
//				return l.getBlock();
//			}
//		});
		Converters.registerConverter(BlockInstance.class, Location.class, new Converter<BlockInstance, Location>() {
			@Override
			@Nullable
			public Location convert(final BlockInstance b) {
				return BlockUtils.getLocation(b);
			}
		}, Converter.NO_COMMAND_ARGUMENTS);
		
		// Entity - Location
		Converters.registerConverter(Entity.class, Location.class, new Converter<Entity, Location>() {
			@Override
			@Nullable
			public Location convert(final Entity e) {
				return e.getLocation();
			}
		}, Converter.NO_COMMAND_ARGUMENTS);
		// Entity - EntityData
		Converters.registerConverter(Entity.class, EntityData.class, new Converter<Entity, EntityData>() {
			@Override
			public EntityData convert(final Entity e) {
				return EntityData.fromEntity(e);
			}
		}, Converter.NO_COMMAND_ARGUMENTS | Converter.NO_RIGHT_CHAINING);
		// EntityData - EntityType
		Converters.registerConverter(EntityData.class, EntityType.class, new Converter<EntityData, EntityType>() {
			@Override
			public EntityType convert(final EntityData data) {
				return new EntityType(data, -1);
			}
		});
		
		// Location - World
//		Skript.registerConverter(Location.class, World.class, new Converter<Location, World>() {
//			private final static long serialVersionUID = 3270661123492313649L;
//
//			@Override
//			public World convert(final Location l) {
//				if (l == null)
//					return null;
//				return l.getWorld();
//			}
//		});
		
		// ItemType - ItemStack
		Converters.registerConverter(ItemType.class, ItemStack.class, new Converter<ItemType, ItemStack>() {
			@Override
			@Nullable
			public ItemStack convert(final ItemType i) {
				return i.getRandom();
			}
		});
		Converters.registerConverter(ItemStack.class, ItemType.class, new Converter<ItemStack, ItemType>() {
			@Override
			public ItemType convert(final ItemStack i) {
				return new ItemType(i);
			}
		});
		
		// Experience - XpOrbData
		Converters.registerConverter(Experience.class, XpOrbData.class, new Converter<Experience, XpOrbData>() {
			@Override
			public XpOrbData convert(final Experience e) {
				return new XpOrbData(e.getXP());
			}
		});
		Converters.registerConverter(XpOrbData.class, Experience.class, new Converter<XpOrbData, Experience>() {
			@Override
			public Experience convert(final XpOrbData e) {
				return new Experience(e.getExperience());
			}
		});
		
//		// Item - ItemStack
//		Converters.registerConverter(Item.class, ItemStack.class, new Converter<Item, ItemStack>() {
//			@Override
//			public ItemStack convert(final Item i) {
//				return i.getItemStack();
//			}
//		});
		
		// Slot - ItemType
		Converters.registerConverter(Slot.class, ItemType.class, new Converter<Slot, ItemType>() {
			@Override
			public ItemType convert(final Slot s) {
				final ItemStack i = s.getItem();
				return new ItemType(i != null ? i : new ItemStack(Material.AIR, 1));
			}
		});
//		// Slot - AbstractContainerMenu
//		Skript.addConverter(Slot.class, AbstractContainerMenu.class, new Converter<Slot, AbstractContainerMenu>() {
//			@Override
//			public AbstractContainerMenu convert(final Slot s) {
//				if (s == null)
//					return null;
//				return s.getInventory();
//			}
//		});
		
		// Block - InventoryHolder
		Converters.registerConverter(BlockInstance.class, InventoryHolder.class, new Converter<BlockInstance, InventoryHolder>() {
			@Override
			@Nullable
			public InventoryHolder convert(final BlockInstance b) {
				final BlockState s = b.getState();
				if (s instanceof InventoryHolder)
					return (InventoryHolder) s;
				return null;
			}
		}, Converter.NO_RIGHT_CHAINING | Converter.NO_COMMAND_ARGUMENTS);
		
//		Converters.registerConverter(InventoryHolder.class, BlockInstance.class, new Converter<InventoryHolder, BlockInstance>() {
//			@Override
//			@Nullable
//			public BlockInstance convert(final InventoryHolder holder) {
//				if (holder instanceof BlockState)
//					return new BlockInventoryHolder((BlockState) holder);
//				if (holder instanceof DoubleChest)
//					return holder.getInventory().getLocation().getBlock();
//				return null;
//			}
//		});
		
//		Converters.registerConverter(InventoryHolder.class, Entity.class, new Converter<InventoryHolder, Entity>() {
//			@Override
//			@Nullable
//			public Entity convert(InventoryHolder holder) {
//				if (holder instanceof Entity)
//					return (Entity) holder;
//				return null;
//			}
//		});
		
//		// World - Time
//		Skript.registerConverter(World.class, Time.class, new Converter<World, Time>() {
//			@Override
//			public Time convert(final World w) {
//				if (w == null)
//					return null;
//				return new Time((int) w.getTime());
//			}
//		});
		
		// Enchantment - EnchantmentType
		Converters.registerConverter(Enchantment.class, EnchantmentType.class, new Converter<Enchantment, EnchantmentType>() {
			@Override
			public EnchantmentType convert(final Enchantment e) {
				return new EnchantmentType(e, -1);
			}
		});
		
//		// Entity - String (UUID) // Very slow, thus disabled for now
//		Converters.registerConverter(String.class, Entity.class, new Converter<String, Entity>() {
//
//			@Override
//			@Nullable
//			public Entity convert(String f) {
//				Collection<? extends Player> players = PlayerUtils.getOnlinePlayers();
//				for (Player p : players) {
//					if (p.getName().equals(f) || p.getUniqueId().toString().equals(f))
//						return p;
//				}
//				
//				return null;
//			}
//			
//		});
		
		// Number - Vec3; DISABLED due to performance problems
//		Converters.registerConverter(Number.class, Vec3.class, new Converter<Number, Vec3>() {
//			@Override
//			@Nullable
//			public Vec3 convert(Number number) {
//				return new Vec3(number.doubleValue(), number.doubleValue(), number.doubleValue());
//			}
//		});

		// Vec3 - Direction
		Converters.registerConverter(Vec3.class, Direction.class, new Converter<Vec3, Direction>() {
			@Override
			@Nullable
			public Direction convert(Vec3 vector) {
				return new Direction(vector);
			}
		});
		
//		// EnchantmentOffer Converters
//		// EnchantmentOffer - EnchantmentType
//		Converters.registerConverter(EnchantmentOffer.class, EnchantmentType.class, new Converter<EnchantmentOffer, EnchantmentType>() {
//			@Nullable
//			@Override
//			public EnchantmentType convert(EnchantmentOffer eo) {
//				return new EnchantmentType(eo.getEnchantment(), eo.getEnchantmentLevel());
//			}
//		});

		Converters.registerConverter(String.class, ServerLevel.class, Forge::getLevel);
	}
}
