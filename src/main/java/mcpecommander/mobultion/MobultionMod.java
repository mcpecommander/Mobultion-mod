package mcpecommander.mobultion;

import org.apache.logging.log4j.Logger;

import mcpecommander.mobultion.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:craftstudioapi;"
		+ "after:forge@[14.23.3.2650,);", acceptedMinecraftVersions = Reference.ACCEPTED_MINECRAFT_VERSIONS, certificateFingerprint = "4fe096bf3ddb2da90a69bb82abd57398549af8a5", updateJSON = Reference.UPDATE_CHECKER)

public class MobultionMod {

	@Mod.Instance
	public static MobultionMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS, modId = Reference.MOD_ID)
	public static CommonProxy proxy;

	public static final CreativeTabs MOBULTION_TAB = new MobultionModTab();

	public static Logger logger;

	public static final int GUI_CONFIG = 1;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		logger = e.getModLog();
		proxy.preInit(e);

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);

	}

}
