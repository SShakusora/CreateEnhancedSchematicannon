package com.sshakusora.create_enhanced_schematicannon.mixin;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionalMixinPlugin implements IMixinConfigPlugin {
    private static final Set<String> LOADED_MODS = new HashSet<>();

    @Override
    public void onLoad(String mixinPackage) {
        for (ModInfo mod : FMLLoader.getLoadingModList().getMods()) {LOADED_MODS.add(mod.getModId());}
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains(".ae2.")) {
            return LOADED_MODS.contains("ae2");
        }

        if (mixinClassName.contains(".supplementaries.")) {
            return LOADED_MODS.contains("supplementaries");
        }

        if (mixinClassName.contains(".moonlight.")) {
            return LOADED_MODS.contains("moonlight");
        }

        if (mixinClassName.contains(".amendments.")) {
            return LOADED_MODS.contains("amendments");
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
