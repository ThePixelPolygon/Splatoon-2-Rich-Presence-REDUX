package splat2ink.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class regular {
    @SerializedName("stage_a")
    @Expose
    public List<regStageA> regStageA;
    @SerializedName("stage_b")
    @Expose
    public List<regStageB> regStageB;
}
