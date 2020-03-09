package com.example.workoutapp.data.model;

public class Level {
    public int curr_xp, max_xp, lvl;
    public float multiplier = 1.0001f;

    public Level() {
    }

    public Level(int curr_xp) {
        this.curr_xp = curr_xp;
        this.max_xp = 50;
        this.lvl = 1;
        if (curr_xp >= max_xp) this.levelUp();
    }

    // constructor to start at lvl 1
    public Level(int lvl, int curr_xp, int max_xp) {
        this.curr_xp = 0;
        this.lvl = 1;
        this.max_xp = 50;
    }

    public int getCurr_xp() {
        return curr_xp;
    }

    public void setCurr_xp(int curr_xp) {
        this.curr_xp = curr_xp;
        if (this.curr_xp >= this.max_xp) {
            // update lvl in the database
            this.levelUp();
        }
    }

    public int getMax_xp() {
        return max_xp;
    }

    public int getLvl() {
        return lvl;
    }

    public void levelUp() {
        this.lvl += 1;
        this.updateMaxXP();
    }

    public void updateMaxXP() {
        this.max_xp = (this.max_xp + ((int)(10 * this.multiplier)));
        // update multiplier
        this.multiplier = this.multiplier * this.multiplier;
        // lvl up again if the current lvl is not high enough
        if (this.max_xp <= this.curr_xp) {
            this.curr_xp = this.curr_xp - this.max_xp;
            this.levelUp();
        }
    }
}
