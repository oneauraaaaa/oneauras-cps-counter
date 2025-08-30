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
        // Sadece basma eylemleriyle ilgileniyoruz (1), bırakmalarla değil (0).
        // Sol fare tuşunun kodu 0'dır.
        if (button == 0 && action == 1) {
            // Tıklamayı doğrudan sınıf üzerinden kaydediyoruz.
            CPSManager.recordClick();
        }
    }
}

