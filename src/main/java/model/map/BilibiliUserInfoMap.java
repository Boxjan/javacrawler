package model.map;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 12:20
 */

@Table(name = "bilibili_userinfo", pk = "id")
public class BilibiliUserInfoMap extends Model {

    // POST https://space.bilibili.com/ajax/member/GetInfo "refer: https://space.bilibili.com/ajax/member/GetInfo" "from mid={userId}"
    private int id;
    private String username;
    private short sex;
    private int regTime;
    private int level;
    private String birthday;

    //额外信息
    private int isOfficial;
    private int isVip;

    //创作信息
    // GET https://api.bilibili.com/x/space/navnum?mid={userId}
    private int articleNum;
    private int archiveNum;
    // GET https://api.bilibili.com/x/space/upstat?mid={userId}
    private int articleView;
    private int archiveView;

    //社交信息 GET https://api.bilibili.com/x/relation/stat?vmid={userId}
    private int fans;
    private int following;

    public BilibiliUserInfoMap(int id, String username, short sex, int regTime,
                               int level, String birthday, int isOfficial, int isVip,
                               int articleNum, int archiveNum, int articleView,
                               int archiveView, int fans, int following) {
        this.id = id;
        this.username = username;
        this.sex = sex;
        this.regTime = regTime;
        this.level = level;
        this.birthday = birthday;
        this.isOfficial = isOfficial;
        this.isVip = isVip;
        this.articleNum = articleNum;
        this.archiveNum = archiveNum;
        this.articleView = articleView;
        this.archiveView = archiveView;
        this.fans = fans;
        this.following = following;
    }

    public BilibiliUserInfoMap(int id) {
        this.id = id;
    }

    public BilibiliUserInfoMap() { }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(short sex) {
        this.sex = sex;
    }

    public void setRegTime(int regTime) {
        this.regTime = regTime;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public void setArchiveNum(int archiveNum) {
        this.archiveNum = archiveNum;
    }

    public void setArticleView(int articleView) {
        this.articleView = articleView;
    }

    public void setArchiveView(int archiveView) {
        this.archiveView = archiveView;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public short getSex() {
        return sex;
    }

    public int getRegTime() {
        return regTime;
    }

    public int getLevel() {
        return level;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public int getIsVip() {
        return isVip;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public int getArchiveNum() {
        return archiveNum;
    }

    public int getArticleView() {
        return articleView;
    }

    public int getArchiveView() {
        return archiveView;
    }

    public int getFans() {
        return fans;
    }

    public int getFollowing() {
        return following;
    }
}
