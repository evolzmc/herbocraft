package com.redblueflame.herbocraft.ui;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.blocks.GrowthControllerContainer;
import com.redblueflame.herbocraft.blocks.ReproducerBlockContainer;
import com.redblueflame.herbocraft.blocks.SterilizerBlockContainer;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ReproducerBlockInterface extends BaseContainerScreen<ReproducerBlockContainer> {
    private WHorizontalLoadingBar img;
    public ReproducerBlockInterface(ReproducerBlockContainer linkedContainer) {
        super(linkedContainer.name, linkedContainer, linkedContainer.player);
        initializeInterface(linkedContainer);
    }
    private void initializeInterface(ReproducerBlockContainer container) {
        WInterface mainInterface = getInterface();
        WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.of(0, 0, 0), Size.of(9 * 18 + 8, 4 * 18 + 108)).setParent(mainInterface);
        mainPanel.setLabel(container.name);
        mainPanel.setOnAlign(WAbstractWidget::center);
        mainPanel.center();
        mainInterface.add(mainPanel);
        // Player Inventory
        WSlot.addPlayerInventory(Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - (int) (18 * 4.5f), 4 * 18 + 24, 1), Size.of(18, 18), mainInterface);
        // Input Slot
        WSlot.addArray(Position.of(mainPanel, ((mainPanel.getWidth()) / 4) - 27, 55 - 9, 1), Size.of(18, 18), mainInterface, 0, SterilizerBlockContainer.STERILIZER_INVENTORY, 2, 1);
        // Progression Arrow Base
        WStaticImage base = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 21, 55 - 7, 1), Size.of(14*2F, 14)).setParent(mainInterface);
        base.setTexture(new Identifier(HerboCraft.name, "textures/ui/arrow_base.png"));
        // Output slots
        WSlot.addArray(Position.of(mainPanel, 3*((mainPanel.getWidth()) / 4) - 18 - 9, 55 - 18 - 9, 1), Size.of(18, 18), mainInterface, 2, SterilizerBlockContainer.STERILIZER_INVENTORY, 3, 3);
        // Progession Arrow Loading
        img = mainPanel.createChild(WHorizontalLoadingBar::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 21, 55 - 7, 2), Size.of(14*2F, 14)).setParent(mainInterface);
        img.setTexture(new Identifier(HerboCraft.name, "textures/ui/arrow_filled.png"));
        img.setState(container.entity.state);
        mainPanel.add(base);
    }
    @Override
    public void tick() {
        super.tick();
        img.setState(getContainer().entity.state);
    }
}
