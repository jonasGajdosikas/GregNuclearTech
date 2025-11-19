package com.jogaj.gnt.api.pattern;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;

import com.jogaj.gnt.api.GNTAPI;
import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

public class Predicates {

    // mostly copied from the code for coils
    public static TraceabilityPredicate moderators() {
        return new TraceabilityPredicate(multiblockState -> {
            var blockState = multiblockState.getBlockState();
            for (Map.Entry<IModeratorType, Supplier<ModeratorBlock>> entry : GNTAPI.MODERATORS.entrySet()) {
                if (blockState.is(entry.getValue().get())) {
                    var stats = entry.getKey();
                    Object currentModerator = multiblockState.getMatchContext().getOrPut("ModeratorType", stats);
                    if (!currentModerator.equals(stats)) {
                        multiblockState.setError(new PatternStringError("gnt.multiblock.pattern.error.moderator"));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }, () -> GNTAPI.MODERATORS.entrySet().stream()
                .sorted(Comparator.comparingDouble(value -> value.getKey().getFastNeutronConversion()))
                .map(moderator -> BlockInfo.fromBlockState(moderator.getValue().get().defaultBlockState()))
                .toArray(BlockInfo[]::new))
                .addTooltips(Component.translatable("gnt.multiblock.pattern.error.moderator"));
    }

    public static TraceabilityPredicate rotorHolderPredicate() {
        return new TraceabilityPredicate(new SimplePredicate(state -> MetaMachine.getMachine(state.getWorld(),
                state.getPos()) instanceof IRotorHolderMachine rotorHolder &&
                state.getWorld()
                        .getBlockState(state.getPos()
                                .relative(rotorHolder.self().getFrontFacing()))
                        .isAir(),
                () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream()
                        .map(BlockInfo::fromBlock).toArray(BlockInfo[]::new)));
    }
}
