package com.oy.quickping.client;

import com.oy.quickping.Config;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeamRenderList {
    private static final List<BeamData> BEAM_DATA = new ArrayList<>();
    private static final int BEAM_LIFETIME = Config.BEAM_LIFETIME.get();




    public static void addBeam(BlockPos pos, float red, float green, float blue){
        BEAM_DATA.add(new BeamData(pos, red, green, blue, BEAM_LIFETIME));
    }

    public static void tick(){
        BEAM_DATA.removeIf(beamData -> beamData.lifetime() <= 0);
        BEAM_DATA.replaceAll(beamData -> new BeamData(beamData.pos(), beamData.red(), beamData.green(), beamData.blue(), beamData.lifetime() - 1));

    }
    public static List<BeamData> getActiveBeams() {
        return Collections.unmodifiableList(BEAM_DATA);
    }



    public record BeamData(BlockPos pos, float red, float green, float blue, int lifetime) {


    }
}
