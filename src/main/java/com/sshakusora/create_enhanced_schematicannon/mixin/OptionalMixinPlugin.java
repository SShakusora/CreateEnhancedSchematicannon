package com.sshakusora.create_enhanced_schematicannon.mixin;

import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptionalMixinPlugin implements IMixinConfigPlugin {
    private static final Set<String> LOADED_MODS = new HashSet<>();
    private static final Map<String, String> OPTIONAL_MIXIN_GROUPS = Map.of(
            ".ae2.", "ae2",
            ".supplementaries.", "supplementaries",
            ".chimes.", "chimes",
            ".dramaticdoors.", "dramaticdoors",
            ".functionalstorage.", "functionalstorage",
            ".anvilcraft.", "anvilcraft"
    );

    @Override
    public void onLoad(String mixinPackage) {
        for (net.neoforged.fml.loading.moddiscovery.ModInfo mod : LoadingModList.get().getMods()) {
            LOADED_MODS.add(mod.getModId());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (Map.Entry<String, String> group : OPTIONAL_MIXIN_GROUPS.entrySet()) {
            if (mixinClassName.contains(group.getKey())) {
                return LOADED_MODS.contains(group.getValue());
            }
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
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
