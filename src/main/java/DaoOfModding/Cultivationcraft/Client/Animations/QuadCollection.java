package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.Models.Quads.Quad;

import java.util.ArrayList;

public class QuadCollection
{
    ArrayList<Quad> quads = new ArrayList<Quad>();

    public QuadCollection()
    {
    }

    public void addQuad(Quad quad)
    {
        quads.add(quad);
    }

    public ArrayList<Quad> getQuads()
    {
        return quads;
    }
}
