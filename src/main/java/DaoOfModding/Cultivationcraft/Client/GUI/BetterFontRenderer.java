package DaoOfModding.Cultivationcraft.Client.GUI;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

import java.util.ArrayList;

public class BetterFontRenderer
{
    // Makes "\n" work, cuz apparently that's too complicated for base Minecraft!?
    public static void draw(Font font, PoseStack stack, String string, float x, float y, int color)
    {
        int line = 0;

        for (String newString : getNewLines(string))
        {
            font.draw(stack, newString, x, y + line, color);
            line += font.lineHeight;
        }
    }

    private static ArrayList<String> getNewLines(String string)
    {
        ArrayList<String> newLined = new ArrayList<>();

        if (string.contains("\n"))
            for (String newString : string.split("\n"))
                newLined.add(newString);
        else
            newLined.add(string);

        return newLined;
    }

    // Wrap the text to fit the specified width
    // Ensures whole words per line
    public static void wordwrap(Font font, PoseStack stack, String string, float x, float y, int color, int width)
    {
        for (String newString : getNewLines(string))
            y += wordwrapSingleLine(font, stack, newString, x, y, color, width);
    }

    private static float wordwrapSingleLine(Font font, PoseStack stack, String string, float x, float y, int color, int width)
    {
        int line = 0;

        while(string.length() > 0)
        {
            String lineString = font.plainSubstrByWidth(string, width);

            // Ensure the line string ends with a whole word
            // Don't bother adjusting the lineString if it contains everything string does or string starts with a space
            // Also don't try to adjust the lineString if it contains no spaces
            if (lineString.length() < string.length() && !string.substring(lineString.length()).startsWith(" ") && lineString.lastIndexOf(" ") > -1)
                lineString = lineString.substring(0, lineString.lastIndexOf(" "));

            font.draw(stack, lineString, x, y + line, color);

            // Remove the lineString from the string and increase the line position
            string = string.substring(lineString.length());
            string = string.trim();

            line += font.lineHeight;
        }

        return line;
    }

    // Wrap the text to fit the specified width
    public static void wrap(Font font, PoseStack stack, String string, float x, float y, int color, int width)
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
