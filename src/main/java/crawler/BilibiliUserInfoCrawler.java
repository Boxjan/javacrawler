package crawler;

import httpclient.HttpClientInfo;
import httpclient.SimpleHttpRequest;
import model.BilibiliUserInfo;
import com.google.gson.Gson;
import org.sqlite.SQLiteException;

import java.util.HashMap;
import java.util.Map;

import static tool.Reg.regFind;


/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 27, 2018 11:52
 */

public class BilibiliUserInfoCrawler extends Crawler {

    private final int USER_INFO = 1;
    private final int USER_CREATE_NUM = 2;
    private final int USER_VIEW_NUM = 3;
    private final int USER_FRIEND = 4;

    @Override
    public void processResponse(HttpClientInfo info) {
        int type = getType(info.getRequest().getUrl());

        switch (type) {
            case USER_INFO: {
                getUserInfo(info);
                break;
            }
            case USER_CREATE_NUM: {
                getCreateNum(info);
                break;
            }
            case USER_VIEW_NUM: {
                getViewNum(info);
                break;
            }
            case USER_FRIEND:{
                getFriendNum(info);
                break;
            }
        }

    }
    public static void get(int id) {
        BilibiliUserInfoCrawler crawler =  new BilibiliUserInfoCrawler();
        Map<String, String> formData = new HashMap<String, String>();
        formData.put("mid", String.valueOf(id));
        Map<String, String> header = new HashMap<String, String>();
        header.put("referer", "https://space.bilibili.com/");

        SimpleHttpRequest request = SimpleHttpRequest.build("https://space.bilibili.com/ajax/member/GetInfo",
                header,"POST", formData);
        try {
            HttpClientInfo clientInfo = HttpClientInfo.build(request);
            crawler.addToClient(clientInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getType(String url) {
       if (url.contains("GetInfo")) return USER_INFO;
       if (url.contains("navnum")) return USER_CREATE_NUM;
       if (url.contains("upstat")) return USER_VIEW_NUM;
       if (url.contains("relation/stat")) return USER_FRIEND;
       return 0;
    }

    private void getUserInfo(HttpClientInfo info) {
        int mid;
        try {
             mid = Integer.parseInt(info.getRequest().getFormData().get("mid"));
            if (!BilibiliUserInfo.getInstance().isExsit(mid))
                BilibiliUserInfo.getInstance().add(mid);
        } catch (NumberFormatException e) {
            System.err.println("can not get mid");
            return;
        }

        GetInfo getInfo;
        try {
            getInfo = new Gson().fromJson(info.getResponse().getBody(), GetInfo.class);
        } catch (Exception e) {
            System.out.println("User id: " + String.valueOf(mid) + " got some error");
            System.out.println("User id: " + mid + " Status is false");
            return;
        }

        if (getInfo.status.equals("false")) {
            System.out.println("User id: " + String.valueOf(mid) + " got some error");
            System.out.println("User id: " + mid + " Status is false");
            return;
        }

        BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "username", getInfo.data.name);

        if (getInfo.data.sex.equals("男"))
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "sex", 1);
        else if (getInfo.data.sex.equals("女"))
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "sex", 2);
        else
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "sex", 0);

        BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "reg_time", getInfo.data.regtime);
        BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "level", getInfo.data.level_info.current_level);
        BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "birthday", getInfo.data.birthday);
        if (getInfo.data.official_verify.type == 1) {
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "is_official", 1);
        } else {
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "is_official", 0);
        }

        if (getInfo.data.vip.vipStatus == 1) {
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "is_vip", 1);
        } else {
            BilibiliUserInfo.getInstance().updateById(getInfo.data.mid, "is_vip", 0);
        }

        Map<String, String> formData = new HashMap<String, String>();
        Map<String, String> formData_s = new HashMap<String, String>();
        formData.put("mid", String.valueOf(getInfo.data.mid));
        formData_s.put("vmid", String.valueOf(getInfo.data.mid));
        Map<String, String> header = new HashMap<String, String>();
        header.put("referer", "https://space.bilibili.com");

        SimpleHttpRequest request_navnum = SimpleHttpRequest.build("https://api.bilibili.com/x/space/navnum",
                "GET", formData);
        SimpleHttpRequest request_upstat = SimpleHttpRequest.build("https://api.bilibili.com/x/space/upstat",
                "GET", formData);
        SimpleHttpRequest request_stat = SimpleHttpRequest.build("https://api.bilibili.com/x/relation/stat",
                header,"GET", formData_s);
        try {
            HttpClientInfo clientInfo = HttpClientInfo.build(request_navnum);
            addToClient(clientInfo);
            clientInfo = HttpClientInfo.build(request_upstat);
            addToClient(clientInfo);
            clientInfo = HttpClientInfo.build(request_stat);
            addToClient(clientInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("User id: " + String.valueOf(mid) + " get user info finish");

    }

    private void getCreateNum(HttpClientInfo info) {
        int mid;
        try {
            mid = Integer.parseInt(info.getRequest().getFormData().get("mid"));
        } catch (NumberFormatException e) {
            System.err.println("can not get mid");
            return;
        }
        NavNum navNum = new Gson().fromJson(info.getResponse().getBody(), NavNum.class);
        try {
            BilibiliUserInfo.getInstance().updateById(mid, "article_num", navNum.data.article);
            BilibiliUserInfo.getInstance().updateById(mid, "archive_num", navNum.data.video);
        } catch ( Exception e) {
            System.out.println("User id: " + String.valueOf(mid) + " got some error");
        }
        System.out.println("User id: " + String.valueOf(mid) + " get create info finish");

    }

    private void getViewNum(HttpClientInfo info) {
        int mid;
        try {
            mid = Integer.parseInt(info.getRequest().getFormData().get("mid"));
        } catch (NumberFormatException e) {
            System.err.println("can not get mid");
            return;
        }

        UpStat upStat = new Gson().fromJson(info.getResponse().getBody(), UpStat.class);
        try {
            BilibiliUserInfo.getInstance().updateById(mid, "article_num", upStat.data.article.view);
            BilibiliUserInfo.getInstance().updateById(mid, "archive_num", upStat.data.archive.view);
        } catch (Exception e) {
            System.out.println("User id: " + String.valueOf(mid) + "got some error");
        }
        System.out.println("User id: " + String.valueOf(mid) + " get view info finish");


    }

    private void getFriendNum(HttpClientInfo info) {
        int mid;
        try {
            mid = Integer.parseInt(info.getRequest().getFormData().get("vmid"));
        } catch (NumberFormatException e) {
            System.err.println("can not get mid");
            return;
        }

        Stat stat = new Gson().fromJson(info.getResponse().getBody(), Stat.class);
        try {
            BilibiliUserInfo.getInstance().updateById(mid, "fans", stat.data.follower);
            BilibiliUserInfo.getInstance().updateById(mid, "following", stat.data.following);
        } catch (Exception e) {
            System.out.println("User id: " + String.valueOf(mid) + "got some error");
        }
        System.out.println("User id: " + String.valueOf(mid) + " get friend info finish");

    }

}


class GetInfo {
     DataInfo data;
    public class DataInfo {
         String birthday;
         levelInfo level_info;
        class levelInfo {
            int current_level;
        }
         int mid;
         String name;
         officialVerify official_verify;
        class officialVerify {
             int type;

        }
         int regtime;
         String sex;

        class vipInfo {
             int vipStatus;
        }
         vipInfo vip;
    }
     String status;
}

class NavNum {
     DataInfo data;
    class DataInfo {
         int video;
         int article;
    }
}

class UpStat {
     DataInfo data;
    class DataInfo {
         archiveInfo archive;
        class archiveInfo {
             int view;
        }
         articleInfo article;
        class articleInfo {
             int view;
        }
    }
}

class Stat {
     DataInfo data;
    class DataInfo {
         int following;
         int follower;
    }
}