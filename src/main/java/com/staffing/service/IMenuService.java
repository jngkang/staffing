package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.entity.Menu;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
public interface IMenuService extends IService<Menu> {

    /**
     * @return java.util.List<com.staffing.entity.Menu>
     * @author JngKang
     * @description 查询菜单
     */
    public List<Menu> findMenus();

}
