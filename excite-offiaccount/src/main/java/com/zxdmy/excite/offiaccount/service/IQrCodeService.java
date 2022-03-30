package com.zxdmy.excite.offiaccount.service;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 20:24
 */
@Service
@AllArgsConstructor
public class IQrCodeService {

    private WxMpService wxMpService;

    public String get() throws WxErrorException {
        return wxMpService.getQrcodeService().qrCodePictureUrl(wxMpService.getJsapiTicket());
    }
}
