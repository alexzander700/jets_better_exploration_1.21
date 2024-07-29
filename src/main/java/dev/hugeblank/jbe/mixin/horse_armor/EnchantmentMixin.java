package dev.hugeblank.jbe.mixin.horse_armor;

import net.minecraft.enchantment.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Unique
    private final Enchantment thiz = (Enchantment) (Object) this;

    @Unique
    private final boolean isHorseEnchantable = thiz instanceof ProtectionEnchantment || thiz instanceof FrostWalkerEnchantment || thiz instanceof DepthStriderEnchantment || thiz instanceof ThornsEnchantment || thiz instanceof RespirationEnchantment || thiz instanceof SoulSpeedEnchantment;

    @Inject(at = @At(value = "RETURN"), method = "getEquipment", locals = LocalCapture.CAPTURE_FAILHARD)
    private void jbe$spoofEquipment(LivingEntity entity, CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir, Map<EquipmentSlot, ItemStack> map) {
        if (entity instanceof HorseEntity horse) {
            ItemStack itemStack = horse.getArmorType();
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (!itemStack.isEmpty() && equipmentSlot.isArmorSlot()) {
                    map.put(equipmentSlot, itemStack);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private void jbe$modifyAcceptableTargets(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof AnimalArmorItem) cir.setReturnValue(isHorseEnchantable);
    }

}
