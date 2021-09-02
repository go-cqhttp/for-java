package com.zhuangxv.bot.contact.support;


import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.core.Bot;
import lombok.Data;

@Data
public class GroupsMember {

    @JSONField(name = "group_id")
    private final long groupId;

    @JSONField(name = "user_id")
    private long userId;

    @JSONField(name = "nickname")
    private String nickname;

    @JSONField(name = "card")
    private String card;

    @JSONField(name = "sex")
    private String sex;

    @JSONField(name = "age")
    private int age;

    @JSONField(name = "area")
    private String area;

    @JSONField(name = "join_time")
    private int joinTime;

    @JSONField(name = "last_sent_time")
    private int lastSentTime;

    @JSONField(name = "level")
    private String level;

    @JSONField(name = "role")
    private String role;

    @JSONField(name = "unfriendly")
    private boolean unfriendly;

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "title_expire_time")
    private long titleExpireTime;

    @JSONField(name = "card_changeable")
    private boolean cardChangeable;

    private final Bot bot;

    public GroupsMember(long groupId, long userId, String nickname, String card, String sex, int age, String area, int joinTime, int lastSentTime, String level, String role, boolean unfriendly, String title, long titleExpireTime, boolean cardChangeable, Bot bot) {
        this.groupId = groupId;
        this.userId = userId;
        this.nickname = nickname;
        this.card = card;
        this.sex = sex;
        this.age = age;
        this.area = area;
        this.joinTime = joinTime;
        this.lastSentTime = lastSentTime;
        this.level = level;
        this.role = role;
        this.unfriendly = unfriendly;
        this.title = title;
        this.titleExpireTime = titleExpireTime;
        this.cardChangeable = cardChangeable;
        this.bot = bot;
    }
}
