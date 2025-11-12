package com.jogaj.gnt.api;

import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GNTAPI {

    public static final Map<IModeratorType, Supplier<ModeratorBlock>> MODERATORS = new HashMap<>();
}
