package splat2ink.schedules;

import com.google.gson.annotations.*;
import java.util.List;

public class rootObject {
    //Regular battle list
    public List<regular> regular;
    //Ranked battle list
    public List<gachi> gachi;
    //League battle list
    public List<league> league;
}
