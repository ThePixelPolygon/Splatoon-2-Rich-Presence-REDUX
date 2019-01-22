package splat2ink.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class league {
    public long id;

    @SerializedName("rule")
    @Expose
    public leagueRule rule;

    @SerializedName("stage_a")
    @Expose
    public leagueStageA stage_a;

    @SerializedName("stage_b")
    @Expose
    public leagueStageB stage_b;
}
