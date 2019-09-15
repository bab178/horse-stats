package com.zeblade.horsestats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
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
    public void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();

        if (!(target instanceof HorseEntity)) {
            return;
        }

        HorseEntity horse = (HorseEntity)target;
        int healthComparison = Math.round((horse.getMaxHealth() - 15.0F) / 15.0F * 100.0F);
        int jumpComparison = Math.round(((float) horse.getHorseJumpStrength() - 0.4F) / 0.6F * 100.0F);
        int speedComparison = Math.round(((float) horse.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() - 0.1125F) / 0.225F * 100.0F);

        TextComponent tc = new TextComponent() {
            @Override
            public String getUnformattedComponentText() {
                return "Speed: " + speedComparison + "%, Jump: " + jumpComparison + "%, Health: " + healthComparison + "%";
            }

            @Override
            public ITextComponent shallowCopy() {
                return this;
            }
        };

        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendMessage(tc);
        }
    }
}