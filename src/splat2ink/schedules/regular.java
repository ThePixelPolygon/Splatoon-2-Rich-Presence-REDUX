package splat2ink.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class regular {
    public long id;
    @SerializedName("stage_a")
    @Expose
    public regStageA stage_a;
    @SerializedName("stage_b")
    @Expose
    public regStageB stage_b;
}
