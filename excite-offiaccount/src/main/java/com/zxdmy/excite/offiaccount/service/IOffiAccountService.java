package com.zxdmy.excite.offiaccount.service;

import com.zxdmy.excite.offiaccount.bo.OffiAccount;

import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 19:31
 */
public interface IOffiAccountService {

    boolean saveOffiAccount(OffiAccount account);

    OffiAccount getOffiAccount(String appId);

    List<OffiAccount> getOffiAccountList();
}
