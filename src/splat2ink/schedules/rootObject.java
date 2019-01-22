package splat2ink.schedules;

import com.google.gson.annotations.*;
import java.util.List;

public class rootObject {
    public long id;

    @SerializedName("stage_a")
    @Expose
    public List<regStageA> regStageA;
    @SerializedName("stage_b")
    @Expose
    public List<regStageB> regStageB;
}
