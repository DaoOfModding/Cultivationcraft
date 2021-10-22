package DaoOfModding.Cultivationcraft.Client.GUI;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class BetterFontRenderer
{
    // Makes "\n" work, cuz apparently that's too complicated for base Minecraft!?
    public static void draw(FontRenderer font, MatrixStack stack, String string, float x, float y, int color)
    {
        int line = 0;

        for (String newString : string.split("\n"))
        {
            font.draw(stack, newString, x, y + line, color);
            line += font.lineHeight;
        }
    }

    // Wrap the text to fit the specified width
    // Ensures whole words per line
    public static void wordwrap(FontRenderer font, MatrixStack stack, String string, float x, float y, int color, int width)
    {
        int line = 0;

        while(string.length() > 0)
        {
            String lineString = font.plainSubstrByWidth(string, width);

            // Ensure the line string ends with a whole word
            // Don't bother adjusting the lineString if it contains everything string does or string starts with a space
            if (lineString.length() < string.length() && !string.substring(lineString.length()).startsWith(" "))
                lineString = lineString.substring(0, lineString.lastIndexOf(" "));

            font.draw(stack, lineString, x, y + line, color);

            // Remove the lineString from the string and increase the line position
            string = string.substring(lineString.length());
            string = string.trim();

            line += font.lineHeight;
        }
    }

    // Wrap the text to fit the specified width
    public static void wrap(FontRenderer font, MatrixStack stack, String string, float x, float y, int color, int width)
    {
        int line = 0;

        while(string.length() > 0)
        {
            String lineString = font.plainSubstrByWidth(string, width);

            font.draw(stack, lineString, x, y + line, color);

            // Remove the lineString from the string and increase the line position
            string = string.substring(lineString.length());
            line += font.lineHeight;
        }
    }
}
