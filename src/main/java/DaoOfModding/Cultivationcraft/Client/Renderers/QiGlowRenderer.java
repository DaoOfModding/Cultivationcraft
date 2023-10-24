package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.mlmanimator.Client.MultiLimbedRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Random;

public class QiGlowRenderer
{
    protected static HashMap<Entity, Element> glowing = new HashMap<>();

    public static void render()
    {
        //TODO ;(
    }

    public static void setQiVisible(Entity entity, Element element)
    {
        QiSourceRenderer.qisource = null;
        QiSourceRenderer.target = entity;
        QiSourceRenderer.element = element;

        Random random = new Random();

        Vec3 direction = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
        direction = direction.normalize();

        Vec3 pos = entity.getBoundingBox().getCenter();
        pos = pos.add(direction.multiply(entity.getBoundingBox().getXsize()*-1, entity.getBoundingBox().getYsize()*-1, entity.getBoundingBox().getZsize()*-1));

        Minecraft.getInstance().level.addParticle(Register.qiParticleType.get(), pos.x, pos.y, pos.z, direction.x, direction.y, direction.z);


        QiSourceRenderer.qisource = null;
        QiSourceRenderer.target = null;
        QiSourceRenderer.element = Elements.getElement(Elements.noElement);

        /*if (!glowing.containsKey(entity))
            glowing.put(entity, element);*/
    }
}
