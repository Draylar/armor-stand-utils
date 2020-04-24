package com.github.draylar.armorstand.mixin;

import com.github.draylar.armorstand.ArmorToolSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandInteractionMixin {
    
    @Shadow
    public abstract ItemStack getEquippedStack(final EquipmentSlot equipmentSlot);

    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    private void interactAt(final PlayerEntity player, final Vec3d vec3d, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        ItemStack heldStack = player.getMainHandStack();
        Item heldStackItem = heldStack.getItem();

        if (player.isSneaking() && hand == Hand.OFF_HAND) {
            info.setReturnValue(ActionResult.FAIL);
        }

        // toggling arms and base
        else if (player.isSneaking() && hand == Hand.MAIN_HAND && heldStackItem == Items.STICK || heldStackItem == Items.STONE_SLAB) {
            if (heldStackItem == Items.STICK) {
                ((ArmorStandAccessor) this).asu_setShowArms(!((ArmorStandAccessor) this).asu_shouldShowArms());
            } else {
                ((ArmorStandAccessor) this).asu_setHideBasePlate(!((ArmorStandAccessor) this).asu_shouldHideBasePlate());
            }

            info.setReturnValue(ActionResult.FAIL);
        }

        // swap inventory
        else if (player.isSneaking() && hand == Hand.MAIN_HAND) {
            ArmorToolSet armorStandSet = new ArmorToolSet(
                    getEquippedStack(EquipmentSlot.HEAD),
                    getEquippedStack(EquipmentSlot.CHEST),
                    getEquippedStack(EquipmentSlot.LEGS),
                    getEquippedStack(EquipmentSlot.FEET),
                    getEquippedStack(EquipmentSlot.MAINHAND),
                    getEquippedStack(EquipmentSlot.OFFHAND)
            );

            ArmorToolSet playerSet = new ArmorToolSet(
                    player.getEquippedStack(EquipmentSlot.HEAD),
                    player.getEquippedStack(EquipmentSlot.CHEST),
                    player.getEquippedStack(EquipmentSlot.LEGS),
                    player.getEquippedStack(EquipmentSlot.FEET),
                    player.getEquippedStack(EquipmentSlot.MAINHAND),
                    player.getEquippedStack(EquipmentSlot.OFFHAND)
            );

            setArmorAndTools(armorStandSet, player);
            setArmorAndTools(playerSet, (ArmorStandEntity) (Object) this);

            info.setReturnValue(ActionResult.FAIL);
        }
    }

    @Unique
    private void setArmorAndTools(ArmorToolSet set, Entity entity) {
        entity.equipStack(EquipmentSlot.HEAD, set.getHelmet());
        entity.equipStack(EquipmentSlot.CHEST, set.getChestplate());
        entity.equipStack(EquipmentSlot.LEGS, set.getLeggings());
        entity.equipStack(EquipmentSlot.FEET, set.getBoots());
        entity.equipStack(EquipmentSlot.MAINHAND, set.getMainHand());
        entity.equipStack(EquipmentSlot.OFFHAND, set.getOffHand());
    }
}