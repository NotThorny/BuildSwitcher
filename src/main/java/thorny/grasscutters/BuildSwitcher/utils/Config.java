package thorny.grasscutters.BuildSwitcher.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("builds")
    private List<Builds> builds;

    public List<Builds> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Builds> builds) {
        this.builds = builds;
    }

    public void setDefault() {
        builds = new ArrayList<>();
    }
}