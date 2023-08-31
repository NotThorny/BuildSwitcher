package thorny.grasscutters.BuildSwitcher.utils;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ArtifactData {

    @SerializedName("itemId")
    private int itemId;

    @SerializedName("level")
    private int level;

    @SerializedName("mainPropId")
    private int mainPropId;

    @SerializedName("appendPropIdList")
    private List<Integer> appendPropIdList;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMainPropId() {
        return mainPropId;
    }

    public void setMainPropId(int mainPropId) {
        this.mainPropId = mainPropId;
    }

    public List<Integer> getAppendPropIdList() {
        return appendPropIdList;
    }

    public void setAppendPropIdList(List<Integer> appendPropIdList) {
        this.appendPropIdList = appendPropIdList;
    }
}