package com.github.igotyou.FactoryMod.structures;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a pipe with a dropper at each end, which are directly connected
 * through blocks of one specific type which make up the actual pipe
 *
 */
public class PipeStructure extends MultiBlockStructure {
	private Block start;
	private Block furnace;
	private Block end;
	private int length;
	private List<Block> glassPipe;
	private static Material pipeMaterial = Material.STAINED_GLASS;
	private static int maximumLength = 32;

	public PipeStructure(Block startBlock) {
		if (startBlock.getType() != Material.DROPPER) {
			return;
		}
		this.start = startBlock;
		for (Block b : MultiBlockStructure.searchForBlockOnSides(startBlock,
				Material.FURNACE)) {
			furnace = b;
			break;
		}
		if (furnace == null) {
			return;
		}
		glassPipe = new LinkedList<Block>();
		Block currentBlock = startBlock.getRelative(dataBlockFaceConversion
				.get((int) (startBlock.getState().getRawData())));
		Block previousBlock = null;
		if (currentBlock.getType() != pipeMaterial) {
			return;
		}
		glassPipe.add(currentBlock);
		int length = 1;
		while (length <= maximumLength) {
			List<Block> blocks = MultiBlockStructure
					.getAdjacentBlocks(currentBlock);
			boolean foundEnd = false;
			boolean foundPipeBlock = false;
			for (Block b : blocks) {
				if (b.getState() instanceof InventoryHolder) {
					end = b;
					this.length = length;
					foundEnd = true;
					break;
				} else if (b.getType() == pipeMaterial
						&& !b.equals(previousBlock)) {
					glassPipe.add(b);
					previousBlock = currentBlock;
					currentBlock = b;
					length++;
					foundPipeBlock = true;
					break;
				}
			}
			if (foundEnd || !foundPipeBlock) {
				break;
			}
		}
	}

	public PipeStructure(List<Block> blocks) {
		this.start = blocks.get(0);
		this.furnace = blocks.get(1);
		this.end = blocks.get(blocks.size() - 1);
		List <Block> glass = new LinkedList<Block>();
		for(Block b:blocks) {
			if (b.getType() == pipeMaterial) {
				glass.add(b);
			}
		}
		this.glassPipe = glass;
		length = glassPipe.size();
	}

	public Location getCenter() {
		return start.getLocation();
	}

	public List<Block> getAllBlocks() {
		List<Block> res = new LinkedList<Block>();
		res.add(start);
		res.add(furnace);
		res.addAll(glassPipe);
		res.add(end);
		return res;
	}

	public boolean isComplete() {
		if (start == null
				|| furnace == null
				|| end == null
				|| start.getType() != Material.DROPPER
				|| (furnace.getType() != Material.FURNACE && furnace.getType() != Material.BURNING_FURNACE)
				|| !(end.getState() instanceof InventoryHolder)) {
			return false;
		}
		for (Block b : glassPipe) {
			if (b.getType() != pipeMaterial) {
				return false;
			}
		}
		return true;
	}

	public int getLength() {
		return length;
	}

	public Block getStart() {
		return start;
	}

	public Block getEnd() {
		return end;
	}

	public Block getFurnace() {
		return furnace;
	}
}
