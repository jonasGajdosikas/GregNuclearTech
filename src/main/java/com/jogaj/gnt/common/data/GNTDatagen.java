package com.jogaj.gnt.common.data;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.common.data.lang.LangHandler;
import com.jogaj.gnt.common.registry.GNTRegistration;
import com.tterrag.registrate.providers.ProviderType;

public class GNTDatagen {
    public static void init(){
//        GNT.LOGGER.info("init datagen");
        GNTRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
    }
}
