package com.redblueflame.herbocraft.ui;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.blocks.SterilizerBlockContainer;
import com.redblueflame.herbocraft.blocks.UpgraderBlockContainer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class UpgraderBlockInterface  extends BaseContainerScreen<UpgraderBlockContainer> {
    private WVerticalLoadingBar spinner;
    private short spinner_state;
    private WVerticalLoadingBar progress;
    public UpgraderBlockInterface(UpgraderBlockContainer linkedContainer) {
        super(linkedContainer.name, linkedContainer, linkedContainer.player);
        initializeInterface(linkedContainer);
    }
    private void initializeInterface(UpgraderBlockContainer container) {
        WInterface mainInterface = getInterface();
        WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.of(0, 0, 0), Size.of(12 * 18 + 8, 4 * 18 + 113)).setParent(mainInterface);
        mainPanel.setLabel(container.name);
        mainPanel.setOnAlign(WAbstractWidget::center);
        mainPanel.center();
        mainInterface.add(mainPanel);
        // Player Inventory
        WSlot.addPlayerInventory(Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - (int) (18 * 4.5f), 4 * 18 + 29, 1), Size.of(18, 18), mainInterface);
        // Input Slot
        WSlot.addArray(Position.of(mainPanel, ((mainPanel.getWidth()) / 4) - 18 - 9 - 18, 60 - 18 - 9, 1), Size.of(18, 18), mainInterface, 0, SterilizerBlockContainer.STERILIZER_INVENTORY, 3, 3);
        // Middle Slot
        WSlot.addArray(Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 9, 60 - 9, 1), Size.of(18, 18), mainInterface, 10, SterilizerBlockContainer.STERILIZER_INVENTORY, 1, 1);
        // Output slots
        WSlot.addArray(Position.of(mainPanel, 3*((mainPanel.getWidth()) / 4) - 18 - 9 + 18, 60 - 18 - 9, 1), Size.of(18, 18), mainInterface, 11, SterilizerBlockContainer.STERILIZER_INVENTORY, 3, 3);

        // Progression Arrow Base for readability
        WStaticImage base_1 = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 4) + 9 + 9, 60 - 7, 1), Size.of(14*1.75F, 14)).setParent(mainInterface);
        base_1.setTexture(new Identifier(HerboCraft.name, "textures/ui/arrow_base.png"));
        mainPanel.add(base_1);
        WStaticImage base_2 = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) + 9 + 9, 60 - 7, 1), Size.of(14*1.75F, 14)).setParent(mainInterface);
        base_2.setTexture(new Identifier(HerboCraft.name, "textures/ui/arrow_base.png"));
        mainPanel.add(base_2);

        // Spinner for activity base
        WStaticImage spinner = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 9, 60 - 9 - 5 - 27, 1), Size.of(18, 27)).setParent(mainInterface);
        spinner.setTexture(new Identifier(HerboCraft.name, "textures/ui/upgrader_power_base.png"));
        mainPanel.add(spinner);

        // Background for progression

        WStaticImage progress_base = mainPanel.createChild(WStaticImage::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - (50*1.2F)/2, 60 + (18*1.2F)/2 + 3, 1), Size.of(50*1.2F, 18*1.2F)).setParent(mainInterface);
        progress_base.setTexture(new Identifier(HerboCraft.name, "textures/ui/upgrader_arrow_base.png"));
        mainPanel.add(progress_base);

        // Spinner update
        this.spinner = mainPanel.createChild(WVerticalLoadingBar::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - 9, 60 - 9 - 5 - 27, 2), Size.of(18, 27)).setParent(mainInterface);
        this.spinner.setTexture(new Identifier(HerboCraft.name, "textures/ui/upgrader_power_filled.png"));
        mainPanel.add(this.spinner);
        //TODO: remove filled default values
        this.spinner.setState((short) 255);

        // Progress state
        this.progress = mainPanel.createChild(WVerticalLoadingBar::new, Position.of(mainPanel, ((mainPanel.getWidth()) / 2) - (50*1.2F)/2, 60 + (18*1.2F)/2 + 3, 2), Size.of(50*1.2F, 18*1.2F)).setParent(mainInterface);
        this.progress.setTexture(new Identifier(HerboCraft.name, "textures/ui/upgrader_arrow_filled.png"));
        mainPanel.add(this.progress);
        this.progress.setState((short) 255);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
        // Add spinner state
        if (getContainer().entity.isWorking) {
            spinner_state += 2;
            if (spinner_state > 255) {
                spinner_state = 0;
            }
        }
        this.spinner.setState(spinner_state);
        this.progress.setState((short) getContainer().entity.state);
        super.render(matrices, mouseX, mouseY, tickDelta);
    }
}
