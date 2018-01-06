package mcpecommander.luggagemod.tileEntity;

import mcpecommander.luggagemod.init.ModItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityJar extends TileEntity {

	private int crackerCount = 0;

	public boolean addCracker() {

		if (!getWorld().isRemote) {
			if (crackerCount < 8) {
				crackerCount++;
				return true;
			}
		}
		return false;
	}
	
	public void removeCracker(){
		if (!getWorld().isRemote){
			if(crackerCount > 0){
				getWorld().spawnEntity(new EntityItem(getWorld(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(ModItems.cracker)));
				crackerCount--;
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("Cracker Count : ", crackerCount);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.crackerCount = compound.getInteger("Cracker Count");
	}
	
}
