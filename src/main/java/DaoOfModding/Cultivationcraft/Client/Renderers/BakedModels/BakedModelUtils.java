package DaoOfModding.Cultivationcraft.Client.Renderers.BakedModels;

import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.VertexTransformer;

public class BakedModelUtils
{
/*
    private static BakedQuad buildQuad(Vector3f[] vertices, TextureAtlasSprite texture, Direction face)
    {
        BakedQuadBuilder build = new BakedQuadBuilder(texture);
        build.setQuadOrientation(face);

        Vector3f normal = vertices[2].copy();
        normal.sub(vertices[1]);
        Vector3f normal2 = vertices[0].copy();
        normal2.sub(vertices[1]);
        normal.cross(normal2);
        normal.normalize();

        Vector4f color = new Vector4f(1f, 1f, 1f, 1f);

        makeVertex(build, normal2, vertices[0], 0, 0, texture, color);
        makeVertex(build, normal2, vertices[1], 0, 16, texture, color);
        makeVertex(build, normal2, vertices[2], 16, 16, texture, color);
        makeVertex(build, normal2, vertices[3], 16, 0, texture, color);

        return build.build();
    }

    private static void makeVertex(BakedQuadBuilder build, Vector3f normal, Vector3f pos, float u, float v, TextureAtlasSprite texture, Vector4f color)
    {
        u = texture.getInterpolatedU(u);
        v = texture.getInterpolatedV(v);

        build.put(0, pos.getX(), pos.getY(), pos.getZ());
        build.put(1, color.getX(), color.getY(), color.getZ(), color.getW());
        build.put(2, u, v);
        build.put(3, 0f, 1f);
        build.put(4, normal.getX(), normal.getY(), normal.getZ());

        // What the hell is PADDING? I don't know...
        build.put(5, 0f);
    }

    private static Vector3f[] getVertices(BakedQuad Quad)
    {
        int[] vertexData = Quad.getVertexData();

        Vector3f[] vertex = new Vector3f[4];

        for(int i = 0; i < 4; i++)
        {
            int pos = (vertexData.length / 4) * i;
            float x = Float.intBitsToFloat(vertexData[pos + 0]);
            float y = Float.intBitsToFloat(vertexData[pos + 1]);
            float z = Float.intBitsToFloat(vertexData[pos + 2]);

            //float color? = Float.intBitsToFloat(vertexData[pos + 3]);

            float u = Float.intBitsToFloat(vertexData[pos + 4]);
            float v = Float.intBitsToFloat(vertexData[pos + 5]);

            float test1 = Float.intBitsToFloat(vertexData[pos + 6]);
            float test2 = Float.intBitsToFloat(vertexData[pos + 7]);


            //System.out.println(x + ", " + y + ", " + z + ", " + u + ", " + v + ", " + test1 + ", " + test2);

            vertex[i] = new Vector3f(x, y, z);
        }

        return vertex;
    }*/


    // Retexture an existing BakedQuad
    public static BakedQuad retextureQuad(BakedQuad quad, TextureAtlasSprite texture)
    {
        BakedQuadBuilder build = new BakedQuadBuilder(texture);
        TextureAtlasSprite originalTexture = quad.getSprite();

        final IVertexConsumer transformer = new VertexTransformer(build)
        {

            @Override
            public void put(int element, float... data)
            {
                if (element == 2)
                {
                    // UnInterpolate the texture coordinates and apply them to the new texture
                    float u = getUnInterpolatedU(originalTexture, data[0]);
                    float v = getUnInterpolatedV(originalTexture, data[1]);

                    parent.put(2, texture.getU(u), texture.getV(v));
                }
                else
                    parent.put(element, data);
            }
        };

        // Pipe the new texture transformer through to the BakedQuad and return the result of the new build
        quad.pipe(transformer);
        return build.build();
    }

    // Functions to UnInterpolate texture values from TextureAtlasSprites
    private static int getUnInterpolatedV(TextureAtlasSprite texture, float v)
    {
        float f = texture.getV1() - texture.getV0();
        float result = (((v - texture.getV0()) * 16) / f);

        // The + 0.1 fixes rounding errors
        return (int)(result + 0.1);
    }

    private static int getUnInterpolatedU(TextureAtlasSprite texture, float u)
    {
        float f = texture.getU1() - texture.getU0();
        float result = (((u - texture.getU0()) * 16) / f);

        return (int)(result + 0.1);
    }
}
