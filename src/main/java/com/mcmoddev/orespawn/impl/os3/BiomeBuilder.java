package com.mcmoddev.orespawn.impl.os3;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import com.mcmoddev.orespawn.api.BiomeLocation;
import com.mcmoddev.orespawn.impl.location.BiomeLocationAcceptAny;
import com.mcmoddev.orespawn.impl.location.BiomeLocationComposition;
import com.mcmoddev.orespawn.impl.location.BiomeLocationEmpty;
import com.mcmoddev.orespawn.impl.location.BiomeLocationList;
import com.mcmoddev.orespawn.impl.location.BiomeLocationSingle;
import com.mcmoddev.orespawn.api.os3.IBiomeBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeBuilder implements IBiomeBuilder {
	private final List<Biome> whitelist = new LinkedList<>();
	private final List<Biome> blacklist = new LinkedList<>();
	
	private boolean acceptAll = false;
	
	public BiomeBuilder() {
		
	}
	
	@Override
	public IBiomeBuilder addWhitelistEntry(Biome biome) {
		this.whitelist.add(biome);
		return this;
	}

	@Override
	public IBiomeBuilder addWhitelistEntry(String biomeName) {
		return this.addWhitelistEntry(new ResourceLocation(biomeName));
	}

	@Override
	public IBiomeBuilder addWhitelistEntry(ResourceLocation biomeResourceLocation) {
		return this.addWhitelistEntry(ForgeRegistries.BIOMES.getValue(biomeResourceLocation));
	}

	@Override
	public IBiomeBuilder addBlacklistEntry(Biome biome) {
		this.blacklist.add(biome);
		return this;
	}

	@Override
	public IBiomeBuilder addBlacklistEntry(String biomeName) {
		return this.addBlacklistEntry(new ResourceLocation(biomeName));
	}

	@Override
	public IBiomeBuilder addBlacklistEntry(ResourceLocation biomeResourceLocation) {
		return this.addBlacklistEntry(ForgeRegistries.BIOMES.getValue(biomeResourceLocation));
	}

	@Override
	public IBiomeBuilder setAcceptAll() {
		this.acceptAll = true;
		return this;
	}

	@Override
	public BiomeLocation create() {
		if (this.acceptAll) {
			return new BiomeLocationAcceptAny();
		}

		BiomeLocation whitelistI;
		BiomeLocation blacklistI;
		if (this.whitelist.size() == 0) {
			if(this.blacklist.size() > 0) {
				whitelistI = new BiomeLocationAcceptAny();
			} else {
				whitelistI = new BiomeLocationEmpty();
			}
		} else {
			whitelistI = new BiomeLocationList(					
					ImmutableSet.<BiomeLocation>copyOf(
							this.whitelist.stream()
							.map( biome -> new BiomeLocationSingle(biome) )
							.collect(Collectors.toList())) );
		}

		if (this.blacklist.size() == 0) {
			blacklistI = new BiomeLocationEmpty();
		} else {
			blacklistI = new BiomeLocationList(
					ImmutableSet.<BiomeLocation>copyOf(
							this.blacklist.stream()
							.map( biome -> new BiomeLocationSingle(biome) )
							.collect(Collectors.toList())));
		}

		
		BiomeLocation res = new BiomeLocationComposition(whitelistI, blacklistI);
		return res;
	}

}
