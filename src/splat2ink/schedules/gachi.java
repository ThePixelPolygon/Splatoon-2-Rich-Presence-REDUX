package splat2ink.schedules;

import com.google.gson.annotations.*;

public class gachi {
    public long id;
    @SerializedName("rule")
    @Expose
    public gachiRule rule;

    @SerializedName("stage_a")
    @Expose
    public gachiStageA stage_a;
}
