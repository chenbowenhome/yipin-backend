package com.yipin.basic.controller.admin;

import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.dao.CommentRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/index")
    public String indexPage(Model model) {
        return "index";
    }

    @GetMapping("/user-list")
    public String userListPage(@PageableDefault(size = 10) Pageable pageable,Model model) {
        model.addAttribute("page",userRepository.findUserByOrderByRegdateDesc(pageable));
        return "user-list";
    }

    @GetMapping("/production-list")
    public String productionListPage(@PageableDefault(size = 10) Pageable pageable,Model model) {
        Page<Production> production = productionRepository.findProductionByOrderByCreateTimeDesc(pageable);
        model.addAttribute("page",production);
        return "production-list";
    }

    @RequestMapping("/comment-list")
    public String commentListPage(@RequestParam(value = "id") Integer id,@PageableDefault(size = 10) Pageable pageable, Model model){
        model.addAttribute("production",productionRepository.findProductionById(id));
        model.addAttribute("page",commentRepository.findCommentByProductionIdOrderByCreateTimeDesc(id,pageable));
        return "comment-list";
    }

}
