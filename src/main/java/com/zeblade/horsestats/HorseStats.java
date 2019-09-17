package com.zeblade.horsestats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("horsestats")
public class HorseStats {

    public HorseStats() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        // Only process on client side
        if (!event.getWorld().isRemote) {
            return;
        }

        // Only process on horses
        Entity target = event.getTarget();
        if (!(target instanceof HorseEntity)) {
            return;
        }

        HorseEntity horse = (HorseEntity) target;
        int speedPercentage = Math.round(((float) horse.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() - 0.1125F) / 0.225F * 100.0F);
        TextFormatting speedColor = GetTextFormattingColor(speedPercentage);
        int jumpPercentage = Math.round(((float) horse.getHorseJumpStrength() - 0.4F) / 0.6F * 100.0F);
        TextFormatting jumpColor = GetTextFormattingColor(jumpPercentage);
        int healthPercentage = Math.round((horse.getMaxHealth() - 15.0F) / 15.0F * 100.0F);
        TextFormatting healthColor = GetTextFormattingColor(healthPercentage);

        TextComponent tc = GetHorseStatsText(TextFormatting.WHITE, speedColor, speedPercentage, jumpColor, jumpPercentage, healthColor, healthPercentage);
        event.getPlayer().sendMessage(tc);
    }

    private static TextFormatting GetTextFormattingColor(int percentage) {
        if (percentage >= 100) {
            return TextFormatting.LIGHT_PURPLE;
        }
        else if (percentage > 83) {
            return TextFormatting.GREEN;
        }
        else if (percentage <= 83 && percentage > 67) {
            return TextFormatting.DARK_GREEN;
        }
        else if (percentage <= 67 && percentage > 50) {
            return TextFormatting.YELLOW;
        }
        else if (percentage <= 50 && percentage > 33) {
            return TextFormatting.GOLD;
        }
        else if (percentage <= 33 && percentage > 17) {
            return TextFormatting.RED;
        }
        else {
            return TextFormatting.DARK_RED;
        }
    }

    private static TextComponent GetHorseStatsText(TextFormatting defaultColor, TextFormatting speedColor, int speedPercentage, TextFormatting jumpColor, int jumpPercentage, TextFormatting healthColor, int healthPercentage) {
        return new TextComponent() {
            @Override
            public String getUnformattedComponentText() {
                return String.format("%1$sSpeed: %2$s%3$d%%%1$s, Jump: %4$s%5$d%%%1$s, Health: %6$s%7$d%%", defaultColor, speedColor, speedPercentage, jumpColor, jumpPercentage, healthColor, healthPercentage);
            }

            @Override
            public ITextComponent shallowCopy() {
                return this;
            }
        };
    }
}