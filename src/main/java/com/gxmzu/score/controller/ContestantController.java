package com.gxmzu.score.controller;

import com.gxmzu.score.domain.AjaxResult;
import com.gxmzu.score.domain.entity.Contestant;
import com.gxmzu.score.domain.entity.User;
import com.gxmzu.score.service.ContestantService;
import com.gxmzu.score.service.TokenService;
import com.gxmzu.score.utils.Constants;
import com.gxmzu.score.utils.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: https://github.com/gxmzu
 * @Date: 2022/11/10/9:58
 * @Description: 参赛者接口
 */
@RestController
@RequestMapping("/contestant")
public class ContestantController extends BaseController {

    @Resource
    private ContestantService contestantService;

    @Resource
    private TokenService tokenService;

    /**
     * 获取参赛者列表
     *
     * @param contestant 参赛者查询信息
     * @return 参赛者列表
     */
    @GetMapping("/list")
    public AjaxResult getContestantList(Contestant contestant) {
        startPage(contestant);
        List<Contestant> list = contestantService.getContestantList(contestant);
        return getDataTable(list);
    }

    /**
     * 随机排序参赛者的比赛顺序
     *
     * @param matchId 比赛id
     * @return 操作结果
     */
    @PutMapping("/orderContestant")
    public AjaxResult orderContestant(HttpServletRequest request, Long matchId) {
        if(matchId == null) {
            return AjaxResult.error(HttpStatus.UNAUTHORIZED, "缺少请求参数");
        }
        User user = tokenService.getUser(request);
        if (!Constants.PRINCIPAL.equals(user.getUserType())) {
            return AjaxResult.error(HttpStatus.UNAUTHORIZED, "未授权的请求");
        }
        List<Contestant> contestantList = contestantService.orderContestant(matchId);
        if (contestantList.size() == 0) {
            return AjaxResult.error(HttpStatus.ERROR, "排序失败，请联系管理员");
        }
        return AjaxResult.success();
    }
}
