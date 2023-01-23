package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;

public class Element
{
    protected final ResourceLocation name;
    public final Color color;
    public final double density;

    protected double effectTickChance;

    protected ArrayList<ElementVariant> variant = new ArrayList<>();

    protected ArrayList<ElementRelationship> relationships = new ArrayList<ElementRelationship>();

    public Element (ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        name = resourcelocation;
        color = elementColor;
        density = newDensity;
        effectTickChance = 0;
    }

    // Adds an attack modifier of the specified value for the specified element
    public void setAttackModifier(ResourceLocation Element, double value)
    {
        relationships.add(new ElementRelationship(Element, value));
    }

    // Return the attack modifier against the specified element
    public double getAttackModifier(ResourceLocation Element)
    {
        for (ElementRelationship relationship : relationships)
            if (relationship.defendingElement == Element)
                return relationship.modifier;

        // If there is no modifier for the specified element in the relationship list return a modifier of 1
        return 1;
    }

    public String getName()
    {
        return Component.translatable(name.getPath()).getString();
    }

    public ResourceLocation getResourceLocation()
    {
        return name;
    }

    public boolean shouldDoBlockEffect()
    {
        if (Math.random() < effectTickChance)
            return true;

        return false;
    }

    public void effectBlock(Level level, BlockPos pos)
    {
    }

    public void addVariant(ElementVariant element)
    {
        variant.add(element);
    }

    public Element getMutation()
    {
        for (ElementVariant variant : variant)
            if (variant.tryMutate())
                return variant;

        return this;
    }
}
