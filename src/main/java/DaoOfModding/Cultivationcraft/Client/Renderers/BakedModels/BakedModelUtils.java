package DaoOfModding.Cultivationcraft.Client.Renderers.BakedModels;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

public class BakedModelUtils
{
    public static BakedQuad retextureQuad(BakedQuad Quad, TextureAtlasSprite texture)
    {
        Direction face = FaceBakery.getFacingFromVertexData(Quad.getVertexData());

        Vector3f[] vertices = getVertices(Quad);

        return buildQuad(vertices, texture, face);
    }

    private static BakedQuad buildQuad(Vector3f[] vertices, TextureAtlasSprite texture, Direction face)
    {
        BakedQuadBuilder build = new BakedQuadBuilder();
        build.setTexture(texture);
        build.setQuadOrientation(face);

        Vector3f normal = vertices[2].copy();
        normal.sub(vertices[1]);
        Vector3f normal2 = vertices[0].copy();
        normal2.sub(vertices[1]);
        normal.cross(normal2);
        normal.normalize();

        putVertex(build, normal, vertices[0], 0, 0, texture, 1f, 1f, 1f, 1f);
        putVertex(build, normal, vertices[1], 0, 16, texture, 1f, 1f, 1f, 1f);
        putVertex(build, normal, vertices[2], 16, 16, texture, 1f, 1f, 1f, 1f);
        putVertex(build, normal, vertices[3], 16, 0, texture, 1f, 1f, 1f, 1f);

        return build.build();
    }

    private static void putVertex(BakedQuadBuilder builder, Vector3f normal,
                           Vector3f pos, float u, float v, TextureAtlasSprite sprite, float r, float g, float b, float a) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0 ; j < elements.size() ; j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, pos.getX(), pos.getY(), pos.getZ(), 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, a);
                    break;
                case UV:
                    switch (e.getIndex()) {
                        case 0:
                            float iu = sprite.getInterpolatedU(u);
                            float iv = sprite.getInterpolatedV(v);
                            builder.put(j, iu, iv);
                            break;
                        case 2:
                            builder.put(j, 0f, 1f);
                            break;
                        default:
                            builder.put(j);
                            break;
                    }
                    break;
                case NORMAL:
                    builder.put(j, normal.getX(), normal.getY(), normal.getZ());
                    break;
                default:
                    builder.put(j);
                    break;
            }
        }
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

            vertex[i] = new Vector3f(x, y, z);
        }

        return vertex;
    }
}
