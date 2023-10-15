package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.mlmanimator.Client.MultiLimbedRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;

public class QiGlowRenderer
{
    protected static HashMap<Entity, Element> glowing = new HashMap<>();

    public static void render()
    {
        //TODO ;(
    }

    public static void setQiVisible(Entity entity, Element element)
    {
        if (!glowing.containsKey(entity))
            glowing.put(entity, element);
    }
}
