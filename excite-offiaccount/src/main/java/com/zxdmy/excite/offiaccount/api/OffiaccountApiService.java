package com.zxdmy.excite.offiaccount.api;

import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.offiaccount.vo.OffiaccountMenuVo;
import com.zxdmy.excite.ums.vo.OauthUserVo;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpSelfMenuInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
                if (null != menuButton.getSubButtons() && null != menuButton.getSubButtons().getSubButtons()) {
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
            // 一级菜单
            WxMenuButton button = new WxMenuButton();
            button.setName(menu.getName());
            // 如果该菜单还有子菜单，则继续追加子菜单
            if (menu.getSubButtons().size() > 0) {
                for (OffiaccountMenuVo subMenu : menu.getSubButtons()) {
                    // 二级菜单
                    WxMenuButton subButton = new WxMenuButton();
                    subButton.setName(subMenu.getName());
                    subButton.setType(subMenu.getType());
                    subButton.setKey(subMenu.getKey());
                    subButton.setUrl(subMenu.getUrl());
                    subButton.setAppId(subMenu.getAppId());
                    subButton.setPagePath(subMenu.getPagePath());
                    // 将二级菜单追加至当前的一级菜单
                    button.getSubButtons().add(subButton);
                }
            }
            // 当前一级菜单没有子菜单，则添加该菜单的其他信息，否则不要添加，会导致子菜单不生效
            else {
                button.setType(menu.getType());
                button.setKey(menu.getKey());
                button.setUrl(menu.getUrl());
                button.setAppId(menu.getAppId());
                button.setPagePath(menu.getPagePath());
            }
            wxMenu.getButtons().add(button);
        }
        try {
            wxService.getMenuService().menuCreate(wxMenu);
            return true;
        } catch (WxErrorException e) {
            this.log.info("\n配置菜单失败，错误信息：{}", e.getMessage());
            System.out.println(e.getError().getErrorMsg());
            throw new ServiceException(e.getError().getErrorMsg());
        }
    }

    /**
     * 获取公众号所有的用户OpenID列表
     *
     * @return OpenId列表
     */
    public List<String> getAllUsersOpenId() {
        try {
            // 先获取第一批次
            WxMpUserList mpUserList = wxService.getUserService().userList(null);
            // 复制给列表
            List<String> openIds = new ArrayList<>(mpUserList.getOpenids());
            // 如果 total 大于 count
            while (mpUserList.getTotal() > mpUserList.getCount()) {
                mpUserList = wxService.getUserService().userList(mpUserList.getNextOpenid());
                // 继续追加至列表
                openIds.addAll(mpUserList.getOpenids());
            }
            // 返回列表
            return openIds;
        } catch (WxErrorException e) {
            throw new ServiceException(e.getError().getErrorMsg());
        }
    }

    /**
     * 生成带参数的二维码
     *
     * @param key    场景值
     * @param expire 过期时间
     * @return null | 二维码链接
     */
    public String getQrCodeUrlWithScene(String key, Integer expire) {
        try {
            String ticket = wxService.getQrcodeService().qrCodeCreateTmpTicket(key, expire).getTicket();
            return wxService.getQrcodeService().qrCodePictureUrl(ticket);
        } catch (WxErrorException e) {
            System.out.println(e.getError());
        }
        return null;
    }

    /**
     * 生成登录链接
     *
     * @param redirectUrl 用户登录成功后跳转的页面
     * @return 官方登录链接
     */
    public String getAuthorizationUrlUserinfo(String redirectUrl) {
        return wxService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
    }

    public String getAuthorizationUrlBase(String redirectUrl, String code) {
        return wxService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, code);
    }

    public OauthUserVo getUserBaseByAuthCode(String code) {
        OauthUserVo userVo = new OauthUserVo();
        try {
            WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            System.out.println(accessToken.toString());
            userVo.setUserid(accessToken.getOpenId());
            return userVo;
        } catch (WxErrorException e) {
            System.out.println(e.getError().getErrorMsg());
        }
        return userVo;
    }

    public OauthUserVo getUserInfoByAuthCode(String code) {
        try {
            WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, null);
            OauthUserVo userVo = new OauthUserVo();
            userVo.setUserid(userInfo.getOpenid())
                    .setNickname(userInfo.getNickname())
                    .setAvatar(userInfo.getHeadImgUrl());
            return userVo;
        } catch (WxErrorException e) {
            System.out.println(e.getError().getErrorMsg());
        }
        return null;
    }


    public WxMpUser getUserInfo(String openId) {
        try {
            return wxService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            throw new ServiceException(e.getError().getErrorMsg());
        }
    }

    public String getAccessToken(Boolean isRefresh) {
        try {
            return wxService.getAccessToken(isRefresh);
        } catch (WxErrorException e) {
            this.log.info("\n获取Access Token失败，错误信息：{}", e.getMessage());
        }
        return null;
    }
}
