package com.oneaura.cpscounter.mixin;

import com.oneaura.cpscounter.CPSManager;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        // Sadece "basma" eylemlerini (1) dikkate alıyoruz.
        if (action == 1) {
            if (button == 0) { // Sol Tık
                CPSManager.recordLeftClick();
            } else if (button == 1) { // Sağ Tık
                CPSManager.recordRightClick();
            }
        }
    }
}

