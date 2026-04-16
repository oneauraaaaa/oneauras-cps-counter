package com.oneaura.cpscounter.mixin;

import com.oneaura.cpscounter.CPSManager;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V", at = @At("HEAD"))
    private void onButton(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
        if (action == 1) { // GLFW_PRESS
            int button = buttonInfo.button();
            if (button == 0) {
                CPSManager.recordLeftClick();
            } else if (button == 1) {
                CPSManager.recordRightClick();
            }
        }
    }
}
