package com.redblueflame.herbocraft.ui;


import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.blocks.SterilizerBlockContainer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.http.client.utils.Idn;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public class SterilizerInterface extends BaseContainerScreen<SterilizerBlockContainer> {
    private WVerticalLoadingBar img;
    public SterilizerInterface(SterilizerBlockContainer container) {
        super(container.name, container, container.player);
        initInterface(container);
    }
    private void initInterface(SterilizerBlockContainer container) {
        WInterface mainInterface = getInterface();
        WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.of(0, 0, 0), Size.of(9 * 18 + 8, 4 * 18 + 108)).setParent(mainInterface);
        mainPanel.setLabel(container.name);
        mainPanel.setOnAlign(WAbstractWidget::center);
        mainPanel.center();
        mainInterface.add(mainPanel);

        WSlot.addPlayerInventory(Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - (int) (18 * 4.5f), 4 * 18 + 24, 1), Size.of(18, 18), mainInterface);
        Collection<WSlot> items = WSlot.addArray(Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 9, 55, 1), Size.of(18, 18), mainInterface, 0, SterilizerBlockContainer.STERILIZER_INVENTORY, 1, 1);
        WStaticImage base = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 7, 55-6-14*2.5F, 1), Size.of(14, 14*2.5F)).setParent(mainInterface);
        base.setTexture(new Identifier(HerboCraft.name, "textures/ui/base.png"));

        img = mainPanel.createChild(WVerticalLoadingBar::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 7, 55-6-14*2.5F, 2), Size.of(14, 14*2.5F)).setParent(mainInterface);
        img.setTexture(new Identifier(HerboCraft.name, "textures/ui/filled.png"));
        img.setState(container.entity.progression);
        mainPanel.add(base);
    }

    @Override
    public void tick() {
        super.tick();
        img.setState(getContainer().entity.progression);
    }
}
