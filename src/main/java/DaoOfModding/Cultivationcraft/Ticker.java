package DaoOfModding.Cultivationcraft;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Ticker {

    @SubscribeEvent
    public void worldTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            System.out.println("Server tick event " + event.phase);
        }
    }
}
