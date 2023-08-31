package thorny.grasscutters.BuildSwitcher.utils;

import com.google.gson.annotations.SerializedName;

//import emu.grasscutter.game.inventory.String;

public class Builds {

    @SerializedName("name")
    private String name;

    @SerializedName("bracer")
    private ArtifactData bracer;

    @SerializedName("dress")
    private ArtifactData dress;

    @SerializedName("necklace")
    private ArtifactData necklace;

    @SerializedName("ring")
    private ArtifactData ring;

    @SerializedName("shoes")
    private ArtifactData shoes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArtifactData getBracer() {
        return bracer;
    }

    public void setBracer(ArtifactData bracer) {
        this.bracer = bracer;
    }

    public ArtifactData getDress() {
        return dress;
    }

    public void setDress(ArtifactData dress) {
        this.dress = dress;
    }

    public ArtifactData getNecklace() {
        return necklace;
    }

    public void setNecklace(ArtifactData necklace) {
        this.necklace = necklace;
    }

    public ArtifactData getRing() {
        return ring;
    }

    public void setRing(ArtifactData ring) {
        this.ring = ring;
    }

    public ArtifactData getShoes() {
        return shoes;
    }

    public void setShoes(ArtifactData shoes) {
        this.shoes = shoes;
    }
}