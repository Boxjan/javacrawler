package model;

import io.github.biezhi.anima.Anima;
import model.map.BilibiliUserInfoMap;

import static io.github.biezhi.anima.Anima.select;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 17:06
 */
public class BilibiliUserInfo {

    private static BilibiliUserInfo instance =  null;

    private BilibiliUserInfo() {

        Anima anima = Model.getModel();
    }

    public static BilibiliUserInfo getInstance() {
        if (instance == null) {
            synchronized(BilibiliUserInfo.class) {
                instance = new BilibiliUserInfo();
            }
        }
        return instance;
    }

    public void add(int id) {
        Anima.save(new BilibiliUserInfoMap(id));
    }

    public void updateById(int id, String key, Object value) {
        new BilibiliUserInfoMap().set(key, value).updateById(id);
    }

    public BilibiliUserInfoMap getById(int id) {
        return select().from(BilibiliUserInfoMap.class).byId(id);
    }

    public boolean isExsit(int id) {
        long count = select().from(BilibiliUserInfoMap.class).where("id", id).count();
        if (count > 0) return true;
        else return false;
    }

}
