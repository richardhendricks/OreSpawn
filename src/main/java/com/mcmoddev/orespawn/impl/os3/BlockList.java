package com.mcmoddev.orespawn.impl.os3;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.mcmoddev.orespawn.OreSpawn;
import com.mcmoddev.orespawn.api.IBlockList;
import com.mcmoddev.orespawn.api.os3.IBlockDefinition;

import net.minecraft.block.state.IBlockState;

public class BlockList implements IBlockList {
	private final List<IBlockDefinition> myBlocks;
	private List<IBlockState> workingList;
	
	public BlockList() {
		this.myBlocks = new LinkedList<>();
		this.workingList = new LinkedList<>();
	}
	
	@Override
	public void addBlock(IBlockDefinition block) {
		this.myBlocks.add(block);
	}

	@Override
	public IBlockState getRandomBlock(Random rand) {
		if (this.workingList.size() == 0) {
			this.startNewSpawn();
		}
		
		int spot = rand.nextInt(this.workingList.size());
		IBlockState rv = this.workingList.get(spot);
		this.workingList.remove(spot);
		return rv;
	}

	@Override
	public void startNewSpawn() {
		this.workingList.clear();
		
		this.myBlocks.stream()
		.filter(b -> b.isValid())
		.forEach(b -> {
			for(int i = 0; i < b.getChance(); i++) {
				this.workingList.add(b.getBlock());
			}
		});
	}

	@Override
	public void dump() {
		this.myBlocks.stream()
		.map(bd -> bd.getBlock())
		.forEach(bs -> OreSpawn.LOGGER.debug("Block %s (with state: %s)", bs.getBlock(), bs));
	}

	@Override
	public int count() {
		return this.myBlocks.size();
	}

}
