package DaoOfModding.Cultivationcraft.Common;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    public static class Server
    {
        protected static final ForgeConfigSpec.ConfigValue<Boolean> qiSourceElementalEffects;

        public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec spec;

        static
        {
            builder.push("First Person Rendering");
            qiSourceElementalEffects = builder.comment("Enable Qi Sources to apply their elemental effects to the world around them").define("Enable Qi Source Elemental Effects", true);
            builder.pop();

            spec = builder.build();
        }

        public static boolean qiSourceElementalEffectsOn()
        {
            if (qiSourceElementalEffects.get())
                return true;

            return false;
        }
    }
}
