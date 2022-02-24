package me.illusion.outcastmoderation.Moderation.Punishments;

import com.google.common.collect.Maps;
import me.illusion.outcastmoderation.Util.Time;

import java.util.Map;
import java.util.UUID;

public class Cooldown {
    private static Map<String, Long> cooldowns = Maps.newHashMap();

    private UUID pID;

    public Cooldown(UUID pID)
    {
        this.pID = pID;
    }

    public static Map<String, Long> getCooldownList()
    {
        return cooldowns;
    }

    public static String getBanTimeFormat(long time)
    {
        return Time.formatTime(time / 1000);
    }

    public static Long setBanTime(String time)
    {
        return Long.parseLong(time) * 1000L;
    }

    public static Long storeBanTime(long time)
    {
        return System.currentTimeMillis() + time;
    }

    public static Long getBanTime(long time)
    {
        return time - System.currentTimeMillis();
    }

    public static boolean hasCooldown(UUID pID)
    {
        return getCooldownList().containsKey(pID.toString());
    }

    public long cooldownTime()
    {
        if (getCooldownList().containsKey(pID.toString()))
        {
            long checkTime = getBanTime(getCooldownList().get(pID.toString()));

            if (checkTime > 0)
                return checkTime;
        }

        return -1L;
    }

    public void addCooldown(long time)
    {
        getCooldownList().put(pID.toString(), time);
    }

    public void removeCooldown()
    {
        getCooldownList().remove(pID.toString());
    }
}
