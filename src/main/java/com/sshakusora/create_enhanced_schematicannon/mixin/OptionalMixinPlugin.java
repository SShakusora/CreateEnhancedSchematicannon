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
    private final boolean IS_DEV = this.isDevEnvironment();

    private boolean isDevEnvironment() {
        return !net.minecraftforge.fml.loading.FMLLoader.isProduction();
    }

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
        if (mixinClassName.startsWith("com.sshakusora.create_enhanced_schematicannon.mixin.create.debug")) {
            return IS_DEV;
        }

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

        if (mixinClassName.contains(".chimes.")) {
            return LOADED_MODS.contains("chimes");
        }

        if (mixinClassName.contains(".dramaticdoors.")) {
            return LOADED_MODS.contains("dramaticdoors");
        }

        if (mixinClassName.contains(".chinjufumod.")) {
            return LOADED_MODS.contains("chinjufumod");
        }

        if (mixinClassName.contains(".quark.")) {
            return LOADED_MODS.contains("quark");
        }

        if (mixinClassName.contains(".immersive_weathering.")) {
            return LOADED_MODS.contains("immersive_weathering");
        }

        if (mixinClassName.contains(".integrateddynamics.")) {
            return LOADED_MODS.contains("integrateddynamics");
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
