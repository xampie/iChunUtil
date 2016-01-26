package me.ichun.mods.ichunutil.common.core;

import me.ichun.mods.ichunutil.client.keybind.KeyBind;
import me.ichun.mods.ichunutil.common.block.BlockCompactPorkchop;
import me.ichun.mods.ichunutil.common.core.config.ConfigBase;
import me.ichun.mods.ichunutil.common.core.config.ConfigHandler;
import me.ichun.mods.ichunutil.common.core.event.EventHandlerServer;
import me.ichun.mods.ichunutil.common.core.network.PacketChannel;
import me.ichun.mods.ichunutil.common.core.util.EntityHelper;
import me.ichun.mods.ichunutil.common.core.util.EventCalendar;
import me.ichun.mods.ichunutil.common.entity.EntityBlock;
import me.ichun.mods.ichunutil.common.iChunUtil;
import me.ichun.mods.ichunutil.common.packet.mod.*;
import me.ichun.mods.ichunutil.common.thread.ThreadGetResources;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ProxyCommon
{
    public void preInit()
    {
        EventCalendar.checkDate();

        iChunUtil.eventHandlerServer = new EventHandlerServer();
        MinecraftForge.EVENT_BUS.register(iChunUtil.eventHandlerServer);

        iChunUtil.blockCompactPorkchop = GameRegistry.registerBlock((new BlockCompactPorkchop()).setCreativeTab(CreativeTabs.tabBlock).setHardness(0.8F).setUnlocalizedName("ichunutil.block.compactporkchop"), "compactPorkchop");

        EntityRegistry.registerModEntity(EntityBlock.class, "EntityBlock", 500, iChunUtil.instance, 160, 20, true);

        iChunUtil.channel = new PacketChannel(iChunUtil.MOD_NAME, PacketSession.class, PacketPatronInfo.class, PacketPatrons.class, PacketUserShouldShowUpdates.class, PacketBlockEntityData.class, PacketNewGrabbedEntityId.class, PacketRequestBlockEntityData.class);
    }

    public void init()
    {
        OreDictionary.registerOre("blockCompactRawPorkchop", iChunUtil.blockCompactPorkchop);

        (new ThreadGetResources(FMLCommonHandler.instance().getSide())).start();
    }

    public void postInit()
    {
        iChunUtil.oreDictBlockCompactRawPorkchop = OreDictionary.getOres("blockCompactRawPorkchop");

        for(ConfigBase cfg : ConfigHandler.configs)
        {
            cfg.setup();
        }

        if(FMLCommonHandler.instance().getSide().isServer() && !iChunUtil.config.eulaAcknowledged.equalsIgnoreCase("true"))
        {
            iChunUtil.LOGGER.info("=============================================================");
            iChunUtil.LOGGER.info(StatCollector.translateToLocal("ichunutil.eula.message"));
            iChunUtil.LOGGER.info(StatCollector.translateToLocal("ichunutil.eula.messageServer"));
            iChunUtil.LOGGER.info("=============================================================");
        }
    }

    public String getPlayerId()
    {
        return EntityHelper.uuidExample.toString().replaceAll("-", "");
    }

    public void nudgeHand(float mag)
    {
    }

    @SideOnly(Side.CLIENT)
    public KeyBind registerKeyBind(KeyBind bind, KeyBind replacing) { return bind; }

    /**
     * Please note that this keybind will trigger without checking for SHIFT/CTRL/ALT being held down. That checking has to be done on your end.
     * @param bind Minecraft Keybind
     */
    @SideOnly(Side.CLIENT)
    public void registerMinecraftKeyBind(KeyBinding bind) {}
}