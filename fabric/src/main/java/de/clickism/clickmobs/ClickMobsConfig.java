/*
 * Copyright 2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package de.clickism.clickmobs;

import de.clickism.clickmobs.mob.PickupHandler;
import de.clickism.configured.Config;
import de.clickism.configured.ConfigOption;

import java.util.List;

public interface ClickMobsConfig {
    Config CONFIG =
            Config.of("config/ClickMobs/config.yml")
                    .version(7)
                    .header("""
                            ---------------------------------------------------------
                            ClickMobs Config
                            NOTE: RELOAD/RESTART SERVER FOR CHANGES TO TAKE EFFECT
                            ---------------------------------------------------------
                            """);

    ConfigOption<Boolean> CHECK_UPDATE =
            CONFIG.option("check_update", false)
                    .description("""
                            是否在服务器启动时检查更新。
                            """);

    ConfigOption<List<String>> WHITELISTED_MOBS =
            CONFIG.option("whitelisted_mobs", List.of("cow", "pig", "sheep"))
                    .listOf(String.class)
                    .header("""
                            ---------------------------------------------------------
                            在以下部分，您可以设置生物的白名单/黑名单。
                            ---------------------------------------------------------
                            要将原版生物加入白名单/黑名单，请添加其实体名称。
                                例如: "creeper" 或 "ender_dragon"
                            要将其他模组的生物加入白名单/黑名单，请使用带命名空间的完整标识符。
                                例如: "othermod:fancy_creeper"
                            ---------------------------------------------------------
                            您还可以使用标签（谓词）来根据特定属性设置白名单/黑名单。
                            可用的标签有：
                                ?all (所有), ?hostile (敌对), ?baby (幼年), ?tamed (驯服), ?nametagged (命名), ?silent (静音), ?mob (特定生物)
                            
                            您可以（可选地）向某些标签传递参数：
                                - ?nametagged(Dinnerbone)
                            您可以组合多个标签：
                                - ?tamed ?nametagged
                            您可以使用 "not" 来否定标签：
                                - not ?hostile
                            您可以将 ?mob 标签与其他标签结合使用，以便在特定生物上使用标签。
                                - ?mob(creeper, zombie) ?nametagged(Friendly!)
                            
                            查看 wiki 以获取更多关于标签的文档：
                            https://github.com/Clickism/ClickMobs/wiki/Tags
                            ---------------------------------------------------------
                            """)
                    .description("""
                            允许被捡起的生物。
                            白名单优先于黑名单。
                            （包含在白名单中的黑名单生物仍然可以被捡起）
                            """);

    ConfigOption<List<String>> BLACKLISTED_MOBS =
            CONFIG.option("blacklisted_mobs", List.of("?hostile", "wither", "ender_dragon"))
                    .listOf(String.class)
                    .description("""
                            不允许被捡起的生物。
                            """);

    ConfigOption<Boolean> ENABLE_DISPENSERS =
            CONFIG.option("enable_dispensers", true)
                    .description("""
                            发射器是否可以发射捡起的生物。
                            """);

    ConfigOption<List<String>> BLACKLISTED_ITEMS_IN_HAND =
            CONFIG.option("blacklisted_items_in_hand", List.of("lead", "saddle"))
                    .listOf(String.class)
                    .description("""
                            手持时阻止捡起生物的物品。
                            使用物品的（完整）标识符。
                            
                            为了兼容 Happy Ghast，缰绳总是被阻止的，
                            不需要在这里添加。
                            
                            使用标签 "?all" 来阻止所有物品。这将使得玩家
                            只能用空手捡起生物。
                            
                            对于来自其他模组的物品，请在物品名称前添加命名空间。
                                例如: "othermod:otheritem"
                            """)
                    .onChange(list -> {
                        PickupHandler.BLACKLISTED_MATERIALS_IN_HAND.clear();
                        list.forEach(item -> {
                            String name = item.toLowerCase();
                            if (!item.contains(":")) {
                                // Add minecraft namespace
                                name = "minecraft:" + name;
                            }
                            PickupHandler.BLACKLISTED_MATERIALS_IN_HAND.add(name);
                        });
                    });

}
