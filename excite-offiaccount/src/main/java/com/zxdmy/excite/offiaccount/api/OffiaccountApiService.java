package com.zxdmy.excite.offiaccount.api;

import com.zxdmy.excite.offiaccount.vo.OffiaccountMenuVo;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpSelfMenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/6/27 16:19
 */
@Component
@AllArgsConstructor
public class OffiaccountApiService {

    private final WxMpService wxService;

    final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取菜单
     *
     * @return 菜单列表
     */
    public List<OffiaccountMenuVo> getMenu() {
        try {
            List<OffiaccountMenuVo> menuList = new ArrayList<>();
            // 获取当前的菜单
            List<WxMpSelfMenuInfo.WxMpSelfMenuButton> menuButtons = wxService.getMenuService().getSelfMenuInfo().getSelfMenuInfo().getButtons();
            // 将获取的信息，转换为符合要求的格式
            for (WxMpSelfMenuInfo.WxMpSelfMenuButton menuButton : menuButtons) {
                // 将当前菜单添加至列表
                OffiaccountMenuVo menu = new OffiaccountMenuVo();
                menu.setName(menuButton.getName())
                        .setType(menuButton.getType())
                        .setKey(menuButton.getKey())
                        .setUrl(menuButton.getUrl())
                        .setAppId(menuButton.getAppId())
                        .setPagePath(menuButton.getPagePath());
                // 如果当前 一级菜单 有 二级菜单，则遍历 二级菜单，继续添加
                if (menuButton.getSubButtons().getSubButtons() != null) {
                    // 创建放子菜单的列表
                    List<OffiaccountMenuVo> subMenuList = new ArrayList<>();
                    // 遍历子菜单，添加至列表中
                    for (WxMpSelfMenuInfo.WxMpSelfMenuButton subButton : menuButton.getSubButtons().getSubButtons()) {
                        OffiaccountMenuVo subMenu = new OffiaccountMenuVo();
                        subMenu.setName(subButton.getName())
                                .setType(subButton.getType())
                                .setKey(subButton.getKey())
                                .setUrl(subButton.getUrl())
                                .setAppId(subButton.getAppId())
                                .setPagePath(subButton.getPagePath());
                        subMenuList.add(subMenu);
                    }
                    // 将子菜单列表添加至一级菜单中
                    menu.setSubButtons(subMenuList);
                }
                // 将当前一级菜单添加至列表
                menuList.add(menu);
            }
            return menuList;
        } catch (WxErrorException e) {
            this.log.info("\n获取菜单失败，错误信息：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 设置菜单
     *
     * @param menuList 菜单列表
     * @return 菜单配置结果
     */
    public Boolean createMenu(List<OffiaccountMenuVo> menuList) {
        WxMenu wxMenu = new WxMenu();
        // 遍历菜单实体，构造菜单
        for (OffiaccountMenuVo menu : menuList) {
            WxMenuButton button = new WxMenuButton();
            button.setName(menu.getName());
            button.setType(menu.getType());
            button.setKey(menu.getKey());
            button.setUrl(menu.getUrl());
            button.setAppId(menu.getAppId());
            button.setPagePath(menu.getPagePath());
            // 如果该菜单还有子菜单，则继续追加子菜单
            if (menu.getSubButtons().size() > 0) {
                for (OffiaccountMenuVo subMenu : menu.getSubButtons()) {
                    WxMenuButton subButton = new WxMenuButton();
                    subButton.setName(subMenu.getName());
                    subButton.setType(subMenu.getType());
                    subButton.setKey(subMenu.getKey());
                    subButton.setUrl(subMenu.getUrl());
                    subButton.setAppId(subMenu.getAppId());
                    subButton.setPagePath(subMenu.getPagePath());
                    button.getSubButtons().add(subButton);
                }
            }
            wxMenu.getButtons().add(button);
        }
        try {
            wxService.getMenuService().menuCreate(wxMenu);
            return true;
        } catch (WxErrorException e) {
            this.log.info("\n配置菜单失败，错误信息：{}", e.getMessage());
        }
        return false;
    }
}
