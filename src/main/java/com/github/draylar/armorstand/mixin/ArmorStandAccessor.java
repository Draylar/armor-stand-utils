package com.github.draylar.armorstand.mixin;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandAccessor {
    @Invoker("setShowArms")
    void asu_setShowArms(boolean showArms);

    @Invoker("setHideBasePlate")
    void asu_setHideBasePlate(boolean showArms);

    @Invoker("shouldShowArms")
    boolean asu_shouldShowArms();

    @Invoker("shouldHideBasePlate")
    boolean asu_shouldHideBasePlate();
}