package com.BlackMinecraft.h1.util;
import com.BlackMinecraft.h1.config.MessagesManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
public class HealthUtil {
    /**
     * Обновляет здоровье игрока на основе количества жизней.
     * Отправка сообщения игроку происходит, если sendMessage = true.
     *
     * @param player          игрок, которому обновляем здоровье
     * @param lives           количество жизней
     * @param messagesManager менеджер сообщений для отправки уведомления игроку
     * @param sendMessage     если true, игроку будет отправлено сообщение о жизнях
     */
    public static void updatePlayerHealth(Player player, int lives, MessagesManager messagesManager, boolean sendMessage) {
        double newMaxHealth = lives * 2.0;
        if (newMaxHealth < 2.0) {
            newMaxHealth = 2.0;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        player.setHealthScaled(true);
        player.setHealthScale(newMaxHealth);
        player.setHealth(newMaxHealth);
        if (sendMessage) {
            String heartMessage = RussianTopLanguage.formatHearts(lives);
            String message = messagesManager.getMessage("life.info").replace("%lives%", heartMessage);
            player.sendMessage(message);
        }
    }
    /**
     * Перегруженный метод, который всегда отправляет сообщение игроку.
     *
     * @param player          игрок, которому обновляем здоровье
     * @param lives           количество жизней
     * @param messagesManager менеджер сообщений
     */
    public static void updatePlayerHealth(Player player, int lives, MessagesManager messagesManager) {
        updatePlayerHealth(player, lives, messagesManager, true);
    }
}
