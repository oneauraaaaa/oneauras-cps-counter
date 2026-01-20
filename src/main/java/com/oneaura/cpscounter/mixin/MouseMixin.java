package com.oneaura.cpscounter.mixin;

import com.oneaura.cpscounter.CPSManager;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput; // Yeni sınıfı import ettik
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    // İmza değişti: (long, int, int, int) yerine (long, MouseInput, int) oldu.
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        // Sadece "basma" eylemlerini (1) dikkate alıyoruz.
        if (action == 1) {
            // Buton bilgisini artık 'input' nesnesinden alıyoruz.
            int button = input.button();
            
            if (button == 0) { // Sol Tık
                CPSManager.recordLeftClick();
            } else if (button == 1) { // Sağ Tık
                CPSManager.recordRightClick();
            }
        }
    }
}
